package com.blr19c.common.remote.registry.redis;

import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.registry.NotifyListener;
import com.blr19c.common.remote.registry.RemoteRegistry;
import com.blr19c.common.remote.registry.RemoteRegistryHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import java.util.Set;
import java.util.UUID;

/**
 * redis注册器
 *
 * @author blr
 * @since 2021.4.22
 */
public class RedisRegistry extends RedisMessageListenerContainer implements RemoteRegistry {
    private final StringRedisTemplate redisTemplate;
    private final Topic redisRegisterTopic = new ChannelTopic("redisRegisterTopic");
    private final Topic redisUnregisterTopic = new ChannelTopic("redisUnregisterTopic");

    public RedisRegistry(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.setConnectionFactory(redisTemplate.getRequiredConnectionFactory());
        this.addMessageListener(new RegListener(), redisRegisterTopic);
        this.addMessageListener(new UnRegListener(), redisUnregisterTopic);
    }

    @Override
    public void register(RpcURI rpcURI) {
        String regId = rpcURI.getParamArgs("regId");
        if (regId == null) {
            regId = UUID.randomUUID().toString();
            rpcURI.setParamArgs("regId", regId);
        }
        redisTemplate.opsForSet().add(redisRegisterTopic.getTopic(), rpcURI.toString());
        redisTemplate.opsForSet().add(regId, rpcURI.toString());
        redisTemplate.convertAndSend(redisRegisterTopic.getTopic(), regId);
    }

    @Override
    public void unregister(RpcURI rpcURI) {
        String regId = rpcURI.getParamArgs("regId");
        redisTemplate.opsForSet().add(redisUnregisterTopic.getTopic(), rpcURI.toString());
        redisTemplate.opsForSet().add(regId, rpcURI.toString());
        redisTemplate.convertAndSend(redisUnregisterTopic.getTopic(), regId);
        redisTemplate.opsForSet().remove(regId, rpcURI.toString());
        redisTemplate.opsForSet().remove(redisRegisterTopic.getTopic(), rpcURI.toString());
    }

    @Override
    public void subscribe(RpcURI rpcURI, NotifyListener listener) {

    }

    @Override
    public void unsubscribe(RpcURI rpcURI, NotifyListener listener) {

    }

    @Override
    public void init() {
        cyclicUpdate();
        Set<String> urlSet = redisTemplate.opsForSet().members(redisRegisterTopic.getTopic());
        if (urlSet != null)
            for (String url : urlSet) {
                RemoteRegistryHolder.putRpcURI(RpcURI.create(url));
            }
    }


    class UnRegListener implements MessageListener {

        @Override
        public void onMessage(Message message, byte[] pattern) {
            String regId = new String(message.getBody());
            Set<String> members = redisTemplate.opsForSet().members(regId);
            if (members == null)
                return;
            for (String url : members) {
                RpcURI uri = RpcURI.create(url);
                RemoteRegistryHolder.remove(uri);
            }
        }
    }

    class RegListener implements MessageListener {

        @Override
        public void onMessage(Message message, byte[] pattern) {
            String regId = new String(message.getBody());
            Set<String> members = redisTemplate.opsForSet().members(regId);
            if (members == null)
                return;
            for (String url : members) {
                RemoteRegistryHolder.putRpcURI(RpcURI.create(url));
            }
        }
    }
}
