package cn.ocoop.framework.jdbc.statement;

import cn.ocoop.framework.jdbc.AbstractSpayPrepareStatement;
import cn.ocoop.framework.jdbc.connection.ConnectionWrapper;
import cn.ocoop.framework.jdbc.connection.RoutingConnection;
import cn.ocoop.framework.jdbc.execute.MethodInvocation;
import cn.ocoop.framework.jdbc.resultset.ResultSetWrapper;
import cn.ocoop.framework.parse.OrderByParser;
import cn.ocoop.framework.parse.shard.value.DynamicShardValue;
import cn.ocoop.framework.parse.shard.value.ShardValue;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liolay on 2017/12/5.
 */
public class RoutingPrepareStatement extends AbstractSpayPrepareStatement {
    private static final Logger log = LoggerFactory.getLogger(RoutingPrepareStatement.class);
    private int autoGeneratedKeys;
    private int[] columnIndexes;
    private String[] columnNames;
    private Map<Integer, Object> parameters = new HashMap<>();

    public RoutingPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        super(routingConnection, methodInvocation, resultSetType, resultSetConcurrency, resultSetHoldability);
        this.sql = sql;
    }

    public RoutingPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation, String sql, int autoGeneratedKeys) {
        super(routingConnection, methodInvocation);
        this.sql = sql;
        this.autoGeneratedKeys = autoGeneratedKeys;
    }

    public RoutingPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation, String sql, int[] columnIndexes) {
        super(routingConnection, methodInvocation);
        this.sql = sql;
        this.columnIndexes = columnIndexes;
    }

    public RoutingPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation, String sql, String[] columnNames) {
        super(routingConnection, methodInvocation);
        this.sql = sql;
        this.columnNames = columnNames;
    }

    public RoutingPrepareStatement(RoutingConnection routingConnection, MethodInvocation prepareStatement, String sql) {
        super(routingConnection, prepareStatement);
        this.sql = sql;
    }

    public RoutingPrepareStatement(RoutingConnection routingConnection, MethodInvocation prepareStatement, String sql, int resultSetType, int resultSetConcurrency) {
        super(routingConnection, prepareStatement, resultSetType, resultSetConcurrency);
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        List<ResultSet> resultSets = Lists.newArrayList();

        List<ConnectionWrapper> connections = routingConnection.route(analyzeShardValue());
        for (ConnectionWrapper connection : connections) {
            PreparedStatement statement = (PreparedStatement) createMethodInvocation.invoke(connection);
            invocationRecorder.replay(statement);
            statements.add(statement);
            log.debug("数据源:{},执行sql:{}", connection.getDataSource().getName(), StringUtils.substringAfter(statement.toString(), ":"));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet != null) {
                resultSets.add(resultSet);
            }
        }

        if (resultSets.size() <= 0) return null;
        if (resultSets.size() == 1) return resultSets.get(0);
        return new ResultSetWrapper(OrderByParser.parse(sql), resultSets).proxy();
    }

    @Override
    public int executeUpdate() throws SQLException {
        Long result = 0L;
        List<ConnectionWrapper> connections = routingConnection.route(analyzeShardValue());
        for (ConnectionWrapper connection : connections) {
            PreparedStatement statement = (PreparedStatement) createMethodInvocation.invoke(connection);
            invocationRecorder.replay(statement);
            statements.add(statement);
            log.debug("数据源:{},执行sql:{}", connection.getDataSource().getName(), StringUtils.substringAfter(statement.toString(), ":"));
            result += statement.executeUpdate();
        }

        return result.intValue();
    }

    /**
     * @return key:shardKey,value:shardValue
     */
    protected Object resolveShardValue(ShardValue shardValue) {
        if (shardValue instanceof DynamicShardValue) {
            //noinspection SuspiciousMethodCalls
            return parameters.get((int) shardValue.getValue() + 1);
        }
        return shardValue.getValue();
    }

    @Override
    public boolean execute() throws SQLException {

        boolean isQuerySql = true;
        List<ConnectionWrapper> connections = routingConnection.route(analyzeShardValue());
        for (ConnectionWrapper connection : connections) {
            PreparedStatement statement = (PreparedStatement) createMethodInvocation.invoke(connection);
            invocationRecorder.replay(statement);
            statements.add(statement);
            log.debug("数据源:{},执行sql:{}", connection.getDataSource().getName(), StringUtils.substringAfter(statement.toString(), ":"));
            isQuerySql = statement.execute() && isQuerySql;
        }
        return isQuerySql;
    }


    @Override
    public void close() throws SQLException {
        super.close();
        parameters.clear();
    }


    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        parameters.put(parameterIndex, sqlType);
        record(new Class[]{int.class, int.class}, parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, boolean.class}, parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, byte.class}, parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, short.class}, parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, int.class}, parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, long.class}, parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, float.class}, parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, double.class}, parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, BigDecimal.class}, parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, String.class}, parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, byte[].class}, parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Date.class}, parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Time.class}, parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Timestamp.class}, parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class, int.class}, parameterIndex, x, length);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class, int.class}, parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class, int.class}, parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException {
        parameters.clear();
        record(null);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Object.class, int.class}, parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Object.class}, parameterIndex, x);
    }

    @Override
    public void addBatch() throws SQLException {
        record(null);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, int length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Reader.class, int.class}, parameterIndex, x, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Reader.class, int.class}, parameterIndex, x, x);
    }


    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Blob.class}, parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }


    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, java.util.Date.class, Calendar.class}, parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Time.class, Calendar.class}, parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Timestamp.class, Calendar.class}, parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int x, String typeName) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, int.class, String.class}, parameterIndex, x, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, URL.class}, parameterIndex, x);
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Reader.class, long.class}, parameterIndex, x, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class, long.class}, parameterIndex, x, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, SQLXML.class}, parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Object.class, int.class, int.class}, parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class, long.class}, parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class, long.class}, parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, long length) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Reader.class, long.class}, parameterIndex, x, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class}, parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class}, parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Reader.class}, parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }


    @Override
    public void setClob(int parameterIndex, Reader x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Reader.class}, parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, InputStream.class}, parameterIndex, x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x) throws SQLException {
        parameters.put(parameterIndex, x);
        record(new Class[]{int.class, Clob.class}, parameterIndex, x);
    }

    private void record(Class[] paramsType, Object... params) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        invocationRecorder.record(PreparedStatement.class, methodName, paramsType, params);
    }

}
