package com.blr19c.common.remote.config.load;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注入服务
 *
 * @author blr
 * @since 2021.4.23
 */
public class RpcReferenceAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, PriorityOrdered {

    private final RpcReferenceBeanInitInterface referenceBeanInitInterface;
    private final ConcurrentHashMap<Class<?>, Object> injectionMetadataCache = new ConcurrentHashMap<>(64);

    public RpcReferenceAnnotationBeanPostProcessor(RpcReferenceBeanInitInterface referenceBeanInitInterface) {
        this.referenceBeanInitInterface = referenceBeanInitInterface;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        buildRpcReferenceMetadata(bean);
        return pvs;
    }

    /**
     * RpcReference绑定
     */
    private void buildRpcReferenceMetadata(Object bean) {
        Class<?> beanType = bean.getClass();
        //beanType 如果是 java. 或者是Ordered.class
        if (!AnnotationUtils.isCandidateClass(beanType, RpcReference.class))
            return;
        for (Class<?> targetClass = beanType; targetClass != null && targetClass != Object.class; targetClass = targetClass.getSuperclass()) {
            ReflectionUtils.doWithLocalFields(targetClass, field -> {
                //跳过static和非RpcReference字段
                if (Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(RpcReference.class))
                    return;
                RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                String serviceName = StringUtils.isEmpty(rpcReference.value()) ? UUID.randomUUID().toString() : rpcReference.value();
                ReflectionUtils.makeAccessible(field);
                field.set(bean, injectionMetadataCache.computeIfAbsent(field.getType(), (v) -> referenceBeanInitInterface.init(serviceName, v)));
            });
        }
    }

    /**
     * @see AutowiredAnnotationBeanPostProcessor#getOrder()
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
