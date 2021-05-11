package remote.test.consumer;

import com.blr19c.common.remote.config.load.EnableRpc;
import com.blr19c.common.remote.remoting.RemotingEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(remoting = RemotingEnum.NETTY)
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
