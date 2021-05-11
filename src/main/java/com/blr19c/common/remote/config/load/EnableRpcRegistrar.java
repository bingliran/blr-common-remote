package com.blr19c.common.remote.config.load;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc启动器
 *
 * @author blr
 * @since 2021.5.11
 */
public class EnableRpcRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        MergedAnnotation<EnableRpc> annotation = importingClassMetadata.getAnnotations().get(EnableRpc.class);
        if (!annotation.isPresent())
            return;
        BeanDefinitionBuilder enableRpcObserver = BeanDefinitionBuilder
                .genericBeanDefinition(EnableRpcObserver.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgValue(annotation.synthesize());
        registry.registerBeanDefinition(EnableRpcObserver.class.getName(), enableRpcObserver.getBeanDefinition());
    }
}
