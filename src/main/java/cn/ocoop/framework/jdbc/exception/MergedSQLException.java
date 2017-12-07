package cn.ocoop.framework.jdbc.exception;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liolay on 2017/12/5.
 */
public class MergedSQLException extends SQLException {
    private List<Exception> exceptions = Lists.newArrayList();

    public void stack(Exception e) {
        exceptions.add(e);
    }

    public boolean notEmpty() {
        return CollectionUtils.isNotEmpty(exceptions);
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        List<StackTraceElement> stackTraceElements = Arrays.asList(stackTrace);
        for (Exception exception : exceptions) {
            CollectionUtils.addAll(stackTraceElements, exception.getStackTrace());
        }
        super.setStackTrace(stackTraceElements.toArray(new StackTraceElement[stackTraceElements.size()]));
    }
}
