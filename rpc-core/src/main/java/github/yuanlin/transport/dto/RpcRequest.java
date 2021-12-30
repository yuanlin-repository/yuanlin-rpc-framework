package github.yuanlin.transport.dto;

import lombok.*;

import java.io.Serializable;

/**
 * RPC 请求
 *
 * @author yuanlin
 * @date 2021/12/28/11:36
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private String requestId;
    private String interfaceName = "";
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String version = "";
    private String group = "";

    public String getServiceName() {
        return this.interfaceName + this.group + this.version;
    }
}
