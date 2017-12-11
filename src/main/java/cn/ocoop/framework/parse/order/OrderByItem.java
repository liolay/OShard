package cn.ocoop.framework.parse.order;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liolay on 2017/12/8.
 */
@Getter
public class OrderByItem {
    @Setter
    private int index;
    private String ownerName;
    private String columnName;
    private OrderByTypeEnum orderType;

    public OrderByItem(String ownerName, String columnName, OrderByTypeEnum orderType) {
        this.ownerName = ownerName;
        this.columnName = columnName;
        this.orderType = orderType;
    }
}
