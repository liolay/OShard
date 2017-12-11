package cn.ocoop.framework.parse.shard.extract;

import cn.ocoop.framework.parse.shard.extract.value.DynamicShardValue;
import cn.ocoop.framework.parse.shard.extract.value.ExtractShardValue;
import cn.ocoop.framework.parse.shard.extract.value.ShardValue;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Created by liolay on 2017/12/11.
 */
public abstract class AbstractShardVisitor extends MySqlASTVisitorAdapter {
    protected Set<String> shardColumn;
    @Getter
    protected Map<String, ShardValue> name_value = Maps.newHashMap();

    public AbstractShardVisitor(Set<String> shardColumn) {
        this.shardColumn = shardColumn;
    }

    protected void collectVariantRefExprIfNecessary(SQLExpr x) {
        if (x instanceof SQLBinaryOpExpr) {
            collectVariantRefExpr((SQLBinaryOpExpr) x);
        }
    }

    protected void collectVariantRefExpr(SQLBinaryOpExpr expr) {
        SQLExpr left = expr.getLeft();
        if (left instanceof SQLBinaryOpExpr) {
            collectVariantRefExpr((SQLBinaryOpExpr) left);
            return;
        }

        String columnName = null;
        if (left instanceof SQLPropertyExpr) {
            columnName = ((SQLPropertyExpr) left).getName();
        } else if (left instanceof SQLIdentifierExpr) {
            columnName = ((SQLIdentifierExpr) left).getName();
        }
        if (columnName != null && shardColumn.contains(columnName)) {
            SQLExpr right = expr.getRight();
            if (right instanceof SQLVariantRefExpr) {
                name_value.put(columnName, new DynamicShardValue(((SQLVariantRefExpr) right).getIndex()));
            } else if (right instanceof SQLValuableExpr) {
                name_value.put(columnName, new ExtractShardValue(((SQLValuableExpr) right).getValue()));
            }
        }
    }
}
