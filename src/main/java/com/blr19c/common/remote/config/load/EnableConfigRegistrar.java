package com.blr19c.common.remote.config.load;

import com.blr19c.common.remote.registry.ProtocolEnum;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

public class EnableConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        MultiValueMap<String, Object> allAnnotationAttributes
                = importingClassMetadata.getAllAnnotationAttributes(EnableRpc.class.getName());
        if (allAnnotationAttributes == null)
            return;
        ProtocolEnum protocolEnum = (ProtocolEnum) allAnnotationAttributes.getFirst("protocol");
        System.out.println(protocolEnum);
        //BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RedisRegistry.class);
        //registry.registerBeanDefinition(RedisRegistry.class.getName(), beanDefinitionBuilder.getBeanDefinition());

    }
}
