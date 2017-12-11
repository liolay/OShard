package cn.ocoop.framework.jdbc.resultset;

import cn.ocoop.framework.parse.order.OrderByItem;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by liolay on 2017/12/7.
 */
@Slf4j
public class ResultSetWrapper extends AbstractResultSet implements InvocationHandler {
    private boolean isClosed = false;
    private List<ResultSet> rss;
    private OrderedResultSet rs;
    private Queue<OrderedResultSet> resultSetQueue = null;
    private boolean pollNew = false;

    public ResultSetWrapper(List<OrderByItem> orderByItems, List<ResultSet> resultSets) throws SQLException {
        this.rss = resultSets;
        this.resultSetQueue = new PriorityQueue<>(resultSets.size());
        for (ResultSet resultSet : this.rss) {

            this.resultSetQueue.offer(new OrderedResultSet(resultSet, orderByItems));
        }
        this.rs = this.resultSetQueue.poll();
    }

    @Override
    public boolean next() throws SQLException {
        if (pollNew) {
            this.rs = this.resultSetQueue.poll();
        }
        if (this.rs == null) return false;
        pollNew = true;
        if (this.rs.next()) {
            this.resultSetQueue.offer(this.rs);
            return true;
        }
        return next();
    }

    @Override
    public void close() throws SQLException {
        this.isClosed = true;
        for (ResultSet resultSet : rss) {
            resultSet.close();
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    public ResultSet proxy() {
        return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class[]{ResultSet.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("next".equals(method.getName()) || "close".equals(method.getName())) {
            return method.invoke(this, args);
        }

        return method.invoke(rs.getResultSet(), args);
    }


}
