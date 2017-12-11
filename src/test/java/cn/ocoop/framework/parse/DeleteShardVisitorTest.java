package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.shard.extract.value.ShardValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liolay on 2017/12/11.
 */
public class DeleteShardVisitorTest extends AbstractShardVisitorTest{
    @Test
    public void visitDelete() {
        String sql = "delete from a where id = 1 and name like '%1%'";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),1);
    }


    @Test
    public void visitDeleteDynamic() {
        String sql = "delete from a where id = ? and name like '%1%'";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),0);
    }
}
