package cn.ocoop.framework.jdbc.parse.select;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by liolay on 2017/12/8.
 */
@Getter
@AllArgsConstructor
public class SelectItem {
    private String ownerName;
    private int columnIndex;
    private String columnName;
    private String columnAliasName;
}
