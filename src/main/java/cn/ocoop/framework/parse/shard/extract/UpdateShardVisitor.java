package cn.ocoop.framework.parse.shard.extract;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;

import java.util.Set;

/**
 * Created by liolay on 2017/12/11.
 */
public class UpdateShardVisitor extends AbstractShardVisitor {
    public UpdateShardVisitor(Set<String> shardColumn) {
        super(shardColumn);
    }

    @Override
    public boolean visit(MySqlUpdateStatement x) {
        super.visit(x);
        collectVariantRefExprIfNecessary(x.getWhere());
        return false;
    }
}
