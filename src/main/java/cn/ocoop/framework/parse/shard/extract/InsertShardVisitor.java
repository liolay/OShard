package cn.ocoop.framework.parse.shard.extract;

import cn.ocoop.framework.parse.shard.extract.value.DynamicShardValue;
import cn.ocoop.framework.parse.shard.extract.value.ExtractShardValue;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;

import java.util.Set;

/**
 * Created by liolay on 2017/12/11.
 */
public class InsertShardVisitor extends AbstractShardVisitor {

    public InsertShardVisitor(Set<String> shardColumn) {
        super(shardColumn);
    }

    @Override
    public boolean visit(MySqlInsertStatement x) {
        super.visit(x);
        if (x.getQuery() == null) {
            for (int columnIndex = 0; columnIndex < x.getColumns().size(); columnIndex++) {
                SQLExpr columnExpr = x.getColumns().get(columnIndex);
                if (!(columnExpr instanceof SQLIdentifierExpr)) {
                    continue;
                }
                SQLIdentifierExpr column = (SQLIdentifierExpr) columnExpr;
                if (!shardColumn.contains(column.getName())) {
                    continue;
                }

                SQLExpr valueExpr = x.getValues().getValues().get(columnIndex);
                if (valueExpr instanceof SQLVariantRefExpr) {
                    name_value.put(column.getName(), new DynamicShardValue(((SQLVariantRefExpr) valueExpr).getIndex()));
                } else if (valueExpr instanceof SQLValuableExpr) {
                    name_value.put(column.getName(), new ExtractShardValue(((SQLValuableExpr) valueExpr).getValue()));
                }
            }
            return false;
        }

        SelectShardVisitor selectShardVisitor = new SelectShardVisitor(shardColumn);
        x.getQuery().accept(selectShardVisitor);
        this.name_value = selectShardVisitor.getName_value();
        return false;
    }
}
