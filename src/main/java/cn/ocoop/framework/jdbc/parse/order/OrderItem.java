package cn.ocoop.framework.jdbc.parse.order;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liolay on 2017/12/8.
 */
@Getter
public class OrderItem {
    private String ownerName;
    private String columnName;
    private OrderTypeEnum orderType;
    @Setter
    private Comparable value;

    public OrderItem(String ownerName, String columnName, OrderTypeEnum orderType) {
        this.ownerName = ownerName;
        this.columnName = columnName;
        this.orderType = orderType;
    }
}
