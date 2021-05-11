package com.blr19c.common.remote.config.load;

import org.springframework.core.annotation.MergedAnnotation;

/**
 * @see EnableRpcRegistrar
 */
public class EnableRpcObserver {
    private final MergedAnnotation<EnableRpc> mergedAnnotation;
    private final EnableRpc enableRpc;

    public EnableRpcObserver(MergedAnnotation<EnableRpc> mergedAnnotation, EnableRpc enableRpc) {
        this.mergedAnnotation = mergedAnnotation;
        this.enableRpc = enableRpc;
    }

    public MergedAnnotation<EnableRpc> getAnnotationMetadata() {
        return mergedAnnotation;
    }

    public EnableRpc getAnnotation() {
        return enableRpc;
    }
}
