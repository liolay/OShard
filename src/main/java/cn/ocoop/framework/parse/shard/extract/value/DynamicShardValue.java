package cn.ocoop.framework.parse.shard.extract.value;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liolay on 2017/12/11.
 */
@Getter
@Setter
@ToString
public class DynamicShardValue implements ShardValue {
    private int index;

    public DynamicShardValue(int index) {
        this.index = index;
    }

    @Override
    public Object getValue() {
        return index;
    }
}
