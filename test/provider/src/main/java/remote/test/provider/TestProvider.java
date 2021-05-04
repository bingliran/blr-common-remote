package remote.test.provider;

import com.blr19c.common.remote.config.load.RpcService;
import com.blr19c.common.remote.config.load.UniversalLoader;
import remote.test.api.TestRemoteInterface;

@RpcService
public class TestProvider implements TestRemoteInterface {

    public TestProvider() {
        System.out.println(111);
    }


    @Override
    public String test1(String s) {
        return s + "test1";
    }
}
