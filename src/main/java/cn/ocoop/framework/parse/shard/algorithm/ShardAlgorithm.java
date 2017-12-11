package cn.ocoop.framework.parse.shard.algorithm;

import java.util.Collection;
import java.util.Map;

/**
 * Created by liolay on 2017/12/11.
 */
public interface ShardAlgorithm {
    Collection<String> shard(Collection<String> dataSourceName, Map<String, Object> shardColumn_value);
}
