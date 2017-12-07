package cn.ocoop.framework.jdbc.spay;

import cn.ocoop.framework.jdbc.connection.RoutingConnection;
import cn.ocoop.framework.jdbc.execute.invocation.MethodInvocation;
import cn.ocoop.framework.jdbc.statement.RoutingStatement;

import java.sql.*;

/**
 * Created by liolay on 2017/12/7.
 */
public abstract class AbstractSpayPrepareStatement extends RoutingStatement implements PreparedStatement {
    public AbstractSpayPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation) {
        super(routingConnection, methodInvocation);
    }

    public AbstractSpayPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation, int resultSetType, int resultSetConcurrency) {
        super(routingConnection, methodInvocation, resultSetType, resultSetConcurrency);
    }

    public AbstractSpayPrepareStatement(RoutingConnection routingConnection, MethodInvocation methodInvocation, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        super(routingConnection, methodInvocation, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public final ParameterMetaData getParameterMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("ParameterMetaData");
    }

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("getMetaData");
    }
}
