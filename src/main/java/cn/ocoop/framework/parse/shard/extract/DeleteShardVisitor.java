package cn.ocoop.framework.parse.shard.extract;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;

import java.util.Set;

/**
 * Created by liolay on 2017/12/11.
 */
public class DeleteShardVisitor extends AbstractShardVisitor {

    public DeleteShardVisitor(Set<String> shardColumn) {
        super(shardColumn);
    }
    @Override
    public boolean visit(MySqlDeleteStatement x) {
        super.visit(x);
        collectVariantRefExprIfNecessary(x.getWhere());
        return false;
    }
}
