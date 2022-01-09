package github.yuanlin.netty.service;

import github.yuanlin.netty.model.Student;

/**
 * 测试接口
 *
 * @author yuanlin
 * @date 2022/01/08/14:23
 */
public interface HelloService {

    String hello(String msg);

    String hello2();

    String hello3(String msg1, Integer msg2, Student msg3);
}
