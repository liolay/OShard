package cn.ocoop.framework.jdbc.parse.order;

/**
 * Created by liolay on 2017/12/8.
 */
public enum OrderTypeEnum {
    ASC, DESC;

    public static OrderTypeEnum value(String orderBy) {
        if (DESC.equalsIgnoreCase(orderBy)) return DESC;
        return ASC;
    }

    public boolean equalsIgnoreCase(String orderBy) {
        return this.name().equalsIgnoreCase(orderBy);
    }
}
