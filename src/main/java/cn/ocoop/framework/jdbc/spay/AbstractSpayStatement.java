package cn.ocoop.framework.jdbc.spay;

import cn.ocoop.framework.jdbc.sql.WrapperImpl;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;

/**
 * Created by liolay on 2017/12/7.
 */
public abstract class AbstractSpayStatement extends WrapperImpl implements Statement {
    @Override
    public final int getFetchDirection() throws SQLException {
        throw new SQLFeatureNotSupportedException("getFetchDirection");
    }

    @Override
    public final void setFetchDirection(int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException("setFetchDirection");
    }

    @Override
    public final void addBatch(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("addBatch sql");
    }

    @Override
    public final void clearBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("clearBatch");
    }

    @Override
    public final int[] executeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("executeBatch");
    }

    @Override
    public final void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException("closeOnCompletion");
    }

    @Override
    public final boolean isCloseOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException("isCloseOnCompletion");
    }

    @Override
    public final void setCursorName(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("setCursorName");
    }
}
