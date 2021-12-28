package github.yuanlin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC 配置类（用于 property 文件配置）
 *
 * @author yuanlin
 * @date 2021/12/28/15:21
 */
@Getter
@AllArgsConstructor
public enum RpcConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address"),
    ZK_REGISTERED_ROOT_PATH("/registered");

    private final String value;
}
