package com.blr19c.common.remote.registry;

import java.util.Arrays;
import java.util.Objects;

/**
 * protocol
 *
 * @author blr
 * @since 2021.4.16
 */
public enum ProtocolEnum {
    REDIS;

    public static ProtocolEnum value(String protocolName) {
        final String protocol = Objects.requireNonNull(protocolName).toUpperCase();
        return Arrays.stream(ProtocolEnum.values())
                .filter(p -> p.toString().equals(protocol))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(protocol + " does not exist"));
    }
}
