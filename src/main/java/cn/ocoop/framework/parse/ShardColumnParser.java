package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.shard.*;
import cn.ocoop.framework.parse.shard.value.ShardValue;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by liolay on 2017/12/8.
 */
@UtilityClass
public class ShardColumnParser {
    private static final Set<String> shardColumn = new HashSet<>();

    public static void init(Set<String> shardColumn) {
        if (!shardColumn.isEmpty()) {
            throw new RuntimeException("ShardColumnParser init twice!");
        }

        if (CollectionUtils.isNotEmpty(shardColumn)) {
            ShardColumnParser.shardColumn.addAll(shardColumn);
        }
    }


    public Map<String, ShardValue> parse(String sql) {
        SQLStatement statement = StatementParser.parse(sql);
        AbstractShardVisitor visitor = null;
        if (statement instanceof SQLInsertStatement) {
            visitor = new InsertShardVisitor(ShardColumnParser.shardColumn);
        } else if (statement instanceof SQLDeleteStatement) {
            visitor = new DeleteShardVisitor(ShardColumnParser.shardColumn);
        } else if (statement instanceof SQLUpdateStatement) {
            visitor = new UpdateShardVisitor(ShardColumnParser.shardColumn);
        } else if (statement instanceof SQLSelectStatement) {
            visitor = new SelectShardVisitor(ShardColumnParser.shardColumn);
        }
        if (visitor != null) {
            statement.accept(visitor);
            return visitor.getName_value();
        }
        return Maps.newHashMap();
    }


}
