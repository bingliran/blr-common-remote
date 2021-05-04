package remote.test.consumer;

import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.load.RpcReference;
import com.blr19c.common.remote.config.load.UniversalLoader;
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
        //UniversalLoader.putRpcURI(RpcURI.create("REDIS://@127.0.0.1:8333/remote.test.aip.TestRemoteInterface"));
        System.out.println(remoteInterface.test1("你妹"));
    }
}
