package cn.ocoop.framework.parse.order;

import cn.ocoop.framework.parse.exception.UnKnownOrderByException;
import cn.ocoop.framework.parse.select.SelectItem;
import cn.ocoop.framework.parse.select.SelectItemVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by liolay on 2017/12/8.
 */
public class OrderByVisitor extends MySqlASTVisitorAdapter {

    private Map<Integer, SelectItem> index_selectItem;
    @Getter
    private List<OrderByItem> orderByItems = Lists.newArrayList();

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        super.visit(x);

        SelectItemVisitor selectItemVisitor = new SelectItemVisitor();
        x.accept(selectItemVisitor);
        this.index_selectItem = selectItemVisitor.getIndex_selectItem();

        visitOrderBy(x);
        return false;
    }

    private void visitOrderBy(MySqlSelectQueryBlock x) {
        SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy == null) return;

        for (SQLSelectOrderByItem orderByItem : orderBy.getItems()) {
            SQLExpr expr = orderByItem.getExpr();

            OrderByItem orderItem = null;
            if (expr instanceof SQLPropertyExpr) {
                SQLPropertyExpr orderByItemExpr = (SQLPropertyExpr) expr;

                orderItem = new OrderByItem(
                        orderByItemExpr.getOwnernName(),
                        orderByItemExpr.getName(),
                        OrderByTypeEnum.value(orderByItem.getType() != null ? orderByItem.getType().name() : null)
                );
            } else if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr orderByItemExpr = (SQLIdentifierExpr) expr;
                orderItem = new OrderByItem(
                        null,
                        orderByItemExpr.getName(),
                        OrderByTypeEnum.value(orderByItem.getType() != null ? orderByItem.getType().name() : null)
                );
            }

            if (orderItem != null) {
                boolean matched = false;
                for (SelectItem selectItem : index_selectItem.values()) {
                    if (orderItem.getOwnerName() == null) {
                        if (selectItem.getOwnerName() == null) {
                            if (!StringUtils.equalsIgnoreCase(selectItem.getColumnName(), orderItem.getColumnName())) {
                                continue;
                            }
                        } else {
                            if (!StringUtils.equalsIgnoreCase(selectItem.getColumnAliasName(), orderItem.getColumnName())) {
                                continue;
                            }
                        }
                    } else {
                        if (!StringUtils.equalsIgnoreCase(selectItem.getOwnerName(), orderItem.getOwnerName())) {
                            continue;
                        }
                        if (!StringUtils.equalsIgnoreCase(selectItem.getColumnName(), orderItem.getColumnName())) {
                            continue;
                        }
                    }
                    orderItem.setIndex(selectItem.getColumnIndex());
                    orderByItems.add(orderItem);
                    matched = true;
                    break;
                }

                if (!matched) {
                    throw new UnKnownOrderByException("order-by column's name '" + orderItem.getColumnName() + "' could not be found at neither `select items` nor `select items alias`");
                }
            }
        }
    }
}
