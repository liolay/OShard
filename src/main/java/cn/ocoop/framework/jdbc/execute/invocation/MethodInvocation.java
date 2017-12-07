package cn.ocoop.framework.jdbc.execute.invocation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liolay on 2017/12/5.
 */
@AllArgsConstructor
public class MethodInvocation {
    @Getter
    private Method method;
    @Getter
    private Object[] params;

    public Object invoke(Object target) {
        try {
            return method.invoke(target, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
