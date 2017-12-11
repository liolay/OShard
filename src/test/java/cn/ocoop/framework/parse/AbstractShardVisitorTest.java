package cn.ocoop.framework.parse;

import com.google.common.collect.Sets;
import org.junit.BeforeClass;

/**
 * Created by liolay on 2017/12/11.
 */
public class AbstractShardVisitorTest {
    @BeforeClass
    public void setShardColumn() {
        SqlParser.setShardColumn(Sets.newHashSet("id"));
    }
}
