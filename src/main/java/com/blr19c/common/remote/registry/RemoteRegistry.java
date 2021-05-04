package com.blr19c.common.remote.registry;

import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.load.UniversalLoader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 注册器
 *
 * @author blr
 * @since 2021.4.22
 */
public interface RemoteRegistry extends ApplicationRunner {
    ScheduledExecutorService registerScheduled = Executors.newSingleThreadScheduledExecutor();

    /**
     * 注册
     */
    void register(RpcURI rpcURI);

    /**
     * 卸载注册
     */
    void unregister(RpcURI rpcURI);

    /**
     * 订阅
     */
    void subscribe(RpcURI rpcURI, NotifyListener listener);

    /**
     * 卸载订阅
     */
    void unsubscribe(RpcURI rpcURI, NotifyListener listener);


    @Override
    default void run(ApplicationArguments args) {
        init();
        registerScheduled.scheduleAtFixedRate(this::cyclicUpdate, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 循环更新
     */
    default void cyclicUpdate() {
        for (RpcURI uri : UniversalLoader.getServiceRegistrarList()) {
            register(uri);
        }
    }

    default void init() {
        cyclicUpdate();
    }
}
