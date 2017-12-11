package cn.ocoop.framework.jdbc.datasource;

import cn.ocoop.framework.jdbc.WrapperImpl;
import cn.ocoop.framework.jdbc.connection.RoutingConnection;
import cn.ocoop.framework.jdbc.execute.invocation.MethodInvocation;
import cn.ocoop.framework.jdbc.execute.invocation.MethodInvocationRecorder;
import cn.ocoop.framework.parse.SqlParser;
import cn.ocoop.framework.parse.shard.algorithm.ShardAlgorithm;
import com.google.common.collect.Lists;
import lombok.Getter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by liolay on 2017/12/5.
 */
public class RoutingDataSource extends WrapperImpl implements DataSource {
    private PrintWriter logWriter = new PrintWriter(System.out);
    @Getter
    private Map<String, DataSource> dataSources;
    private MethodInvocationRecorder invocationRecorder = new MethodInvocationRecorder();
    private int loginTimeout = 0;
    private ShardAlgorithm shardAlgorithm;

    public RoutingDataSource(Map<String, DataSource> dataSources, Set<String> shardColumn, ShardAlgorithm shardAlgorithm) {
        this.dataSources = dataSources;
        SqlParser.setShardColumn(shardColumn);
        this.shardAlgorithm = shardAlgorithm;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return new RoutingConnection(this, reference(null));
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new RoutingConnection(this, reference(new Class[]{String.class, String.class}, username, password));
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
        for (DataSource dataSource : dataSources.values()) {
            dataSource.setLogWriter(out);
        }
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }


    private MethodInvocation reference(Class[] paramsType, Object... params) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return invocationRecorder.reference(DataSource.class, methodName, paramsType, params);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.loginTimeout = seconds;
        for (DataSource dataSource : dataSources.values()) {
            dataSource.setLoginTimeout(seconds);
        }
    }

    public List<NamedDataSource> route(Map<String, Object> shardColumn_value) {
        List<NamedDataSource> routedDataSource = Lists.newArrayList();
        Collection<String> dataSourceNames = shardAlgorithm.shard(dataSources.keySet(), shardColumn_value);
        for (String dataSourceName : dataSourceNames) {
            routedDataSource.add(new NamedDataSource(dataSourceName, dataSources.get(dataSourceName)));
        }
        return routedDataSource;
    }
}
