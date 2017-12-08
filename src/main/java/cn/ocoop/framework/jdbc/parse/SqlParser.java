package cn.ocoop.framework.jdbc.parse;

import cn.ocoop.framework.jdbc.parse.order.OrderByVisitor;
import cn.ocoop.framework.jdbc.parse.order.OrderItem;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcUtils;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Created by liolay on 2017/12/8.
 */
@UtilityClass
public class SqlParser {
    public Map<Integer, OrderItem> parseOrderBy(String sql) {
        SQLStatement statement = parseSQLStatement(sql);
        OrderByVisitor orderByVisitor = new OrderByVisitor(statement);
        statement.accept(orderByVisitor);
        return orderByVisitor.getIndex_orderByItem();
    }

    private SQLStatement parseSQLStatement(String sql) {
        return SQLUtils.parseStatements(sql, JdbcUtils.MYSQL).get(0);
    }

    public static void main(String[] args) {
        parseOrderBy("SELECT a.id,a.name from t_user a order by a.id");
    }

}
