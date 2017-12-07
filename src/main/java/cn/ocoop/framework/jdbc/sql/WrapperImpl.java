package cn.ocoop.framework.jdbc.sql;

import java.sql.SQLException;
import java.sql.Wrapper;

/**
 * Created by liolay on 2017/12/5.
 */
public class WrapperImpl implements Wrapper {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

}
