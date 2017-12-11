package cn.ocoop.framework.parse.shard.extract;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;

import java.util.Set;

/**
 * Created by liolay on 2017/12/11.
 */
public class SelectShardVisitor extends AbstractShardVisitor {
    public SelectShardVisitor(Set<String> shardColumn) {
        super(shardColumn);
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        super.visit(x);
        collectVariantRefExprIfNecessary(x.getWhere());
        return false;
    }
}
