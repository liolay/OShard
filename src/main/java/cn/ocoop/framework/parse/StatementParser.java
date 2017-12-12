package cn.ocoop.framework.parse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcUtils;
import lombok.experimental.UtilityClass;

/**
 * Created by liolay on 2017/12/12.
 */
@UtilityClass
public class StatementParser {
    public SQLStatement parse(String sql) {
        return SQLUtils.parseStatements(sql, JdbcUtils.MYSQL).get(0);
    }
}
