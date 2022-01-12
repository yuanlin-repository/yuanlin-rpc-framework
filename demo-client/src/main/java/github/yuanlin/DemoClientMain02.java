package github.yuanlin;

import github.yuanlin.annotation.RpcAutowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 使用 Spring 自动注入代理对象
 *
 * @author yuanlin
 * @date 2022/01/12/20:11
 */
public class DemoClientMain02 {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Test test = applicationContext.getBean(Test.class);
        test.test();
    }
}

@Component
class Test {

    @RpcAutowire(group = "test", version = "01")
    private HiService hiService;

    public String test() {
        return hiService.hi("帅哥~");
    }
}