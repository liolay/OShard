package cn.ocoop.framework.jdbc.resultset;

import cn.ocoop.framework.jdbc.parse.order.OrderItem;
import cn.ocoop.framework.jdbc.parse.order.OrderTypeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by liolay on 2017/12/8.
 */
@Slf4j
public class OrderResultSet implements Comparable<OrderResultSet> {
    @Getter
    private final Map<Integer, OrderItem> index_OrderItem;
    @Getter
    private ResultSet resultSet;
    private boolean nextMore;

    public OrderResultSet(ResultSet resultSet, Map<Integer, OrderItem> index_OrderItem) throws SQLException {
        this.resultSet = resultSet;
        this.index_OrderItem = index_OrderItem;
        analyzeOrderByColumnValue();
    }

    private void analyzeOrderByColumnValue() throws SQLException {
        this.nextMore = this.resultSet.next();
        if (nextMore) {
            for (Map.Entry<Integer, OrderItem> entry : index_OrderItem.entrySet()) {
                Comparable object = getObject(entry.getKey() + 1, entry.getValue().getColumnName());
                entry.getValue().setValue(object);
            }
        }
        this.resultSet.previous();

    }

    public boolean nextMore() {
        return this.nextMore;
    }

    public boolean next() throws SQLException {
        boolean next = this.resultSet.next();
        if (next) {
            analyzeOrderByColumnValue();
        }
        return next;
    }


    public Comparable getObject(int columnIndex, String name) throws SQLException {

        Object object = resultSet.getObject(columnIndex);
        if (!(object instanceof Comparable)) {
            log.error("column '" + name + "' could is not Comparable!");
            throw new RuntimeException("column '" + name + "' could is not Comparable!");
        }
        return (Comparable) resultSet.getObject(columnIndex);
    }


    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(OrderResultSet o) {

        for (Map.Entry<Integer, OrderItem> entry : index_OrderItem.entrySet()) {
            Comparable before = entry.getValue().getValue();
            Comparable after = o.getIndex_OrderItem().get(entry.getKey()).getValue();
            if (before == null) {
                if (after == null) {
                    continue;
                } else {
                    return -1;
                }
            }
            int i = before.compareTo(after);
            if (entry.getValue().getOrderType() == OrderTypeEnum.ASC && i != 0) return i;
            if (entry.getValue().getOrderType() == OrderTypeEnum.DESC && i != 0) return -i;
        }
        return 0;
    }
}
