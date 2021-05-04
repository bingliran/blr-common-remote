package remote.test.api;

import com.blr19c.common.remote.config.load.RpcApi;

/**
 * 测试api
 */
@RpcApi
public interface TestRemoteInterface {

    String test1(String s);
}
