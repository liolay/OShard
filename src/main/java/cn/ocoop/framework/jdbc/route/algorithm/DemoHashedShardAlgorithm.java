package cn.ocoop.framework.jdbc.route.algorithm;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by liolay on 2017/12/11.
 */
public class DemoHashedShardAlgorithm implements ShardAlgorithm {
    @Override
    public Collection<String> shard(Collection<String> dataSourceName, Map<String, Object> shardValue) {
        Integer id = (Integer) shardValue.get("id");
        if (id == null) {
            return dataSourceName;
        }

        Set<String> routedDataSource = Sets.newHashSet();
        for (String name : dataSourceName) {
            if (name.endsWith(String.valueOf(id % 2))) {
                routedDataSource.add(name);
            }
        }
        return routedDataSource;
    }
}
