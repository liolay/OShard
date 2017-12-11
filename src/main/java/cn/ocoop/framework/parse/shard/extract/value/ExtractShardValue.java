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
public class ExtractShardValue implements ShardValue {
    private Object value;

    public ExtractShardValue(Object value) {
        this.value = value;
    }
}
