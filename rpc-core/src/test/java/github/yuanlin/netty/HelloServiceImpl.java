package github.yuanlin.netty;

/**
 * 测试接口实现类
 *
 * @author yuanlin
 * @date 2022/01/08/14:24
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String msg) {
        return " world";
    }
}
