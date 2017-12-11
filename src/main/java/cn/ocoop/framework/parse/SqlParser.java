package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.order.OrderByItem;
import cn.ocoop.framework.parse.order.OrderByVisitor;
import cn.ocoop.framework.parse.shard.extract.*;
import cn.ocoop.framework.parse.shard.extract.value.ShardValue;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.util.JdbcUtils;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liolay on 2017/12/8.
 */
@UtilityClass
public class SqlParser {
    private static final Set<String> shardColumn = new HashSet<>();

    public static void setShardColumn(Set<String> shardColumn) {
        if (CollectionUtils.isNotEmpty(shardColumn)) {
            SqlParser.shardColumn.addAll(shardColumn);
        }
    }

    public List<OrderByItem> parseOrderBy(String sql) {
        SQLStatement statement = parseSQLStatement(sql);
        OrderByVisitor orderByVisitor = new OrderByVisitor();
        statement.accept(orderByVisitor);
        return orderByVisitor.getOrderByItems();
    }

    public SQLStatement parseSQLStatement(String sql) {
        return SQLUtils.parseStatements(sql, JdbcUtils.MYSQL).get(0);
    }


    public Map<String, ShardValue> analyzeShard(String sql) {
        SQLStatement statement = parseSQLStatement(sql);
        AbstractShardVisitor visitor = null;
        if (statement instanceof SQLInsertStatement) {
            visitor = new InsertShardVisitor(SqlParser.shardColumn);
        } else if (statement instanceof SQLDeleteStatement) {
            visitor = new DeleteShardVisitor(SqlParser.shardColumn);
        } else if (statement instanceof SQLUpdateStatement) {
            visitor = new UpdateShardVisitor(SqlParser.shardColumn);
        } else if (statement instanceof SQLSelectStatement) {
            visitor = new SelectShardVisitor(SqlParser.shardColumn);
        }
        if (visitor != null) {
            statement.accept(visitor);
            return visitor.getName_value();
        }
        return null;
    }


}
