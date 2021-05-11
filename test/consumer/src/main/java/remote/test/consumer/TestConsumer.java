package remote.test.consumer;

import com.blr19c.common.remote.config.load.RpcReference;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import remote.test.api.TestRemoteInterface;

@Component
public class TestConsumer implements ApplicationRunner {

    @RpcReference
    private TestRemoteInterface remoteInterface;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(remoteInterface.test1("测试测试"));
    }
}
