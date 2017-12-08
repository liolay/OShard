package cn.ocoop.framework.jdbc.parse.select;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * Created by liolay on 2017/12/8.
 */
public class SelectItemVisitor extends MySqlASTVisitorAdapter {
    @Getter
    private Map<Integer, SelectItem> index_selectItem = Maps.newHashMap();

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        super.visit(x);
        for (int itemIndex = 0; itemIndex < x.getSelectList().size(); itemIndex++) {
            SQLSelectItem selectItem = x.getSelectList().get(itemIndex);
            if (selectItem.getExpr() instanceof SQLPropertyExpr) {
                SQLPropertyExpr itemExpr = (SQLPropertyExpr) selectItem.getExpr();
                index_selectItem.put(itemIndex, new SelectItem(itemExpr.getOwnernName(), itemIndex, itemExpr.getName(), selectItem.getAlias()));
            } else if (selectItem.getExpr() instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr itemExpr = (SQLIdentifierExpr) selectItem.getExpr();
                index_selectItem.put(itemIndex, new SelectItem(null, itemIndex, itemExpr.getName(), selectItem.getAlias()));
            }

        }
        return true;
    }
}
