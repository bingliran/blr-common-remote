package com.blr19c.common.remote.common;

import java.io.Serializable;

/**
 * 调用方式
 *
 * @author blr
 */
public class RpcInvocation implements Serializable {

    protected String serviceName;
    protected Class<?> type;
    protected String methodName;
    protected Class<?>[] parameterTypes;
    protected Object[] args;

    public RpcInvocation(String serviceName, Class<?> type, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.serviceName = serviceName;
        this.type = type;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
