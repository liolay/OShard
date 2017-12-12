package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.shard.value.ShardValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liolay on 2017/12/11.
 */
public class UpdateShardVisitorTest extends AbstractShardVisitorTest {

    @Test
    public void visitUpdate() {
        String sql = "update a set name = 'liolay',age = 28 where id = 1 and aa = 'haha'";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(), 1);
    }

    @Test
    public void visitUpdateDynamic() {
        String sql = "update a set name = ?,age = ? where id = ? and aa = ?";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(), 2);
    }

    @Test
    public void visitUpdateJoin() {
        String sql = "update a join b on a.sid = b.sid set a.name = b.name,a.age = b.age where a.id = 1 and a.aa = 'haha'";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(), 1);
    }

    @Test
    public void visitUpdateJoinDynamic() {
        String sql = "update a join b on a.sid = b.sid set a.name = b.name,a.age = b.age where a.id = ? and a.aa = ?";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(), 0);
    }

}
