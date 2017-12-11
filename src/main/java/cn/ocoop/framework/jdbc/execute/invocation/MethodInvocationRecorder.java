package cn.ocoop.framework.jdbc.execute.invocation;

import cn.ocoop.framework.util.BeanUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liolay on 2017/12/6.
 */
public class MethodInvocationRecorder {
    private List<MethodInvocation> methodInvocationLine = Lists.newArrayList();

    public MethodInvocation reference(Class clazz, String methodName, Class[] paramsTypes, Object... params) {
        return new MethodInvocation(
                BeanUtils.findMethod(clazz, methodName, paramsTypes),
                params
        );

    }

    public void record(Class clazz, String methodName, Class[] paramsTypes, Object... params) {
        MethodInvocation methodInvocation = reference(clazz, methodName, paramsTypes, params);
        methodInvocationLine.add(methodInvocation);
    }


    public void replay(Object target) {
        for (MethodInvocation methodInvocation : methodInvocationLine) {
            methodInvocation.invoke(target);
        }
    }

    public void clear() {
        methodInvocationLine.clear();
    }
}
