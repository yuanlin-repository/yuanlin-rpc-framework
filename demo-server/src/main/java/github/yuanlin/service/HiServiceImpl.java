package github.yuanlin.service;

import github.yuanlin.HiService;
import github.yuanlin.annotation.RpcService;

/**
 * HiService 接口实现类
 *
 * @author yuanlin
 * @date 2022/01/12/18:39
 */
@RpcService(group = "test", version = "01")
public class HiServiceImpl implements HiService {

    @Override
    public String hi(String msg) {
        return "hi " + msg;
    }
}
