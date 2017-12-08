package cn.ocoop.framework.jdbc.parse.order;

import cn.ocoop.framework.jdbc.parse.exception.UnKnownOrderByException;
import cn.ocoop.framework.jdbc.parse.select.SelectItem;
import cn.ocoop.framework.jdbc.parse.select.SelectItemVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by liolay on 2017/12/8.
 */
public class OrderByVisitor extends MySqlASTVisitorAdapter {

    private Map<Integer, SelectItem> index_selectItem = Maps.newHashMap();
    @Getter
    private Map<Integer, OrderItem> index_orderByItem = Maps.newLinkedHashMap();

    public OrderByVisitor(SQLStatement sqlStatement) {
        SelectItemVisitor selectItemVisitor = new SelectItemVisitor();
        sqlStatement.accept(selectItemVisitor);
        index_selectItem = selectItemVisitor.getIndex_selectItem();
    }

    @Override
    public boolean visit(SQLSelectOrderByItem x) {
        super.visit(x);

        SQLExpr expr = x.getExpr();

        OrderItem orderItem = null;
        if (expr instanceof SQLPropertyExpr) {
            SQLPropertyExpr orderByItemExpr = (SQLPropertyExpr) expr;

            orderItem = new OrderItem(
                    orderByItemExpr.getOwnernName(),
                    orderByItemExpr.getName(),
                    OrderTypeEnum.value(x.getType() != null ? x.getType().name() : null)
            );
        } else if (expr instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr orderByItemExpr = (SQLIdentifierExpr) expr;
            orderItem = new OrderItem(
                    null,
                    orderByItemExpr.getName(),
                    OrderTypeEnum.value(x.getType() != null ? x.getType().name() : null)
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

                index_orderByItem.put(selectItem.getColumnIndex(), orderItem);
                matched = true;
                break;
            }

            if (!matched) {
                throw new UnKnownOrderByException("order-by column's name '" + orderItem.getColumnName() + "' could not be found at neither `select items` nor `select items alias`");
            }
        }
        return true;
    }
}
