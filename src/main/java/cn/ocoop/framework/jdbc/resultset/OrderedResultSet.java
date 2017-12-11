package cn.ocoop.framework.jdbc.resultset;

import cn.ocoop.framework.parse.order.OrderByItem;
import cn.ocoop.framework.parse.order.OrderByTypeEnum;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by liolay on 2017/12/8.
 */
@Slf4j
public class OrderedResultSet implements Comparable<OrderedResultSet> {
    @Getter
    private final Map<OrderByItem,Comparable> orderItem_value;
    private List<OrderByItem> orderByItems;
    @Getter
    private ResultSet resultSet;

    public OrderedResultSet(ResultSet resultSet, List<OrderByItem> orderByItems) throws SQLException {
        this.resultSet = resultSet;
        this.orderByItems = orderByItems;
        this.orderItem_value = Maps.newHashMap();
        analyzeOrderByColumnValue();
    }

    private void analyzeOrderByColumnValue() throws SQLException {
        boolean hasMore = this.resultSet.next();
        if (hasMore) {
            for (OrderByItem orderByItem : orderByItems) {
                Comparable object = getObject(orderByItem.getIndex() + 1, orderByItem.getColumnName());
                orderItem_value.put(orderByItem,object);
            }
        }
        this.resultSet.previous();
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
    public int compareTo(OrderedResultSet o) {
        for (Map.Entry<OrderByItem, Comparable> entry : orderItem_value.entrySet()) {
            Comparable before = entry.getValue();
            Comparable after = o.getOrderItem_value().get(entry.getKey());
            if (before == null) {
                if (after == null) {
                    continue;
                }
                return -1;
            }
            int i = before.compareTo(after);
            if (entry.getKey().getOrderType() == OrderByTypeEnum.ASC && i != 0) return i;
            if (entry.getKey().getOrderType() == OrderByTypeEnum.DESC && i != 0) return -i;
        }
        return 0;
    }
}
