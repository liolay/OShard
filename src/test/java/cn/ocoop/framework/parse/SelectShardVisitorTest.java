package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.shard.value.ShardValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liolay on 2017/12/11.
 */
public class SelectShardVisitorTest  extends AbstractShardVisitorTest{

    @Test
    public void visitSelect() {
        String sql = "select 'p1' as p1, " +
                "(select 'p2' as p2, (select * from d where d.id = 'id1') from b where b.id = 'id2')," +
                "(select 'p3' as p3 from c where c.id = 'id3')" +
                "from a where a.id = 'id4' and a.name = 'name1' and exists(select null from e where e.id = 'id5')";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),"id4");
    }

    @Test
    public void visitSelectDynamic() {
        String sql = "select ? as p1, " +
                "(select ? as p2, (select * from d where d.id = ?) from b where b.id = ?)," +
                "(select ? as p3 from c where c.id = ?)" +
                "from a where a.id = ? and a.name = ? and exists(select null from e where e.id = ?)";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),6);
    }
}
