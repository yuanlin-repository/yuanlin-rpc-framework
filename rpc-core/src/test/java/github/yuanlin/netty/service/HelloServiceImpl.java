package github.yuanlin.netty.service;

import github.yuanlin.netty.model.Student;

/**
 * 测试接口实现类
 *
 * @author yuanlin
 * @date 2022/01/08/14:24
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String msg) {
        return msg + " world";
    }

    @Override
    public String hello2() {
        return "你好 世界~";
    }

    @Override
    public String hello3(String msg1, Integer msg2, Student msg3) {
        return msg1 + "::" + msg2 + "::" + msg3;
    }

}
