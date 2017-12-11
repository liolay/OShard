package cn.ocoop.framework.parse.order;

/**
 * Created by liolay on 2017/12/8.
 */
public enum OrderByTypeEnum {
    ASC, DESC;

    public static OrderByTypeEnum value(String orderBy) {
        if (DESC.equalsIgnoreCase(orderBy)) return DESC;
        return ASC;
    }

    public boolean equalsIgnoreCase(String orderBy) {
        return this.name().equalsIgnoreCase(orderBy);
    }
}
