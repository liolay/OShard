package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.order.OrderByItem;
import cn.ocoop.framework.parse.order.OrderByVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Created by liolay on 2017/12/12.
 */
@UtilityClass
public class OrderByParser {
    public List<OrderByItem> parse(String sql) {
        SQLStatement statement = StatementParser.parse(sql);
        OrderByVisitor orderByVisitor = new OrderByVisitor();
        statement.accept(orderByVisitor);
        return orderByVisitor.getOrderByItems();
    }
}
