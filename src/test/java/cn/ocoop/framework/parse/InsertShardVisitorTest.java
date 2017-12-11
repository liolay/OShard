package cn.ocoop.framework.parse;

import cn.ocoop.framework.parse.shard.extract.value.ShardValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liolay on 2017/12/11.
 */
public class InsertShardVisitorTest  extends AbstractShardVisitorTest{
    @Test
    public void visitInsert() {
        String sql = "insert into user(name,sex,id) values('name','F','id1')";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),"id1");
    }

    @Test
    public void visitInsertDynamic() {
        String sql = "insert into user(name,sex,id) values(?,?,?)";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),2);
    }

    @Test
    public void visitInsertSelect() {
        String sql = "insert into user(name,sex,id) select 1,2,3 from a where a.id = ?";
        Map<String, ShardValue> map = SqlParser.analyzeShard(sql);
        Assert.assertEquals(map.get("id").getValue(),0);
    }
}
