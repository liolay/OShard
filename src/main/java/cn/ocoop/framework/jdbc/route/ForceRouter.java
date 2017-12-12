package cn.ocoop.framework.jdbc.route;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * Created by liolay on 2017/12/12.
 */
public class ForceRouter implements AutoCloseable {
    private static final ThreadLocal<Map<String, Object>> SHARD_KEY_VALUE = InheritableThreadLocal.withInitial(HashedMap::new);
    private static final ForceRouter ROUTER = new ForceRouter();

    private ForceRouter() {

    }

    public static ForceRouter getInstance() {
        return ROUTER;
    }

    public ForceRouter set(String key, Object value) {
        SHARD_KEY_VALUE.get().put(key, value);
        return this;
    }

    public Object get(String key) {
        return SHARD_KEY_VALUE.get().get(key);
    }

    public Map<String, Object> get() {
        return SHARD_KEY_VALUE.get();
    }

    public ForceRouter clear() {
        SHARD_KEY_VALUE.remove();
        return this;
    }

    public ForceRouter remove(String key) {
        SHARD_KEY_VALUE.get().remove(key);
        return this;
    }

    @Override
    public void close() throws Exception {
        clear();
    }
}
