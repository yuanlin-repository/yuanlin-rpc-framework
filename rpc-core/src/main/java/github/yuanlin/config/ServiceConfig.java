package github.yuanlin.config;

import lombok.*;

/**
 * RPC 服务配置
 *
 * @author yuanlin
 * @date 2021/12/30/16:42
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfig {

    /**
     * 服务实例
     */
    private Object service;
    private String group = "";
    private String version = "";

    public String getServiceName() {
        return getInterfaceName() + group + version;
    }

    public String getInterfaceName() {
        return service.getClass().getInterfaces()[0].getName();
    }
}
