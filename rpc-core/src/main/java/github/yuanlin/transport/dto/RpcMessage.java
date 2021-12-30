package github.yuanlin.transport.dto;

import lombok.*;

/**
 * RPC 数据包
 *
 *      4byte       4byte       4byte       1byte       1byte        1byte       4byte
 * +------------+------------+---------+-----------+-------------+-----------+------------+
 * |    魔术位   |  整体长度   |  头长度  |   协议版本  |   消息类型   |  序列化方式 |    请求ID   |
 * +-----------+------------+---------+-----------+-------------+-----------+-------------+
 * |                                     协议头扩展字段                                     |
 * +--------------------------------------------------------------------------------------+
 * |                                                                                      |
 * |                                     payload                                          |
 * |                                                                                      |
 * +--------------------------------------------------------------------------------------+
 *
 *
 * @author yuanlin
 * @date 2021/12/28/11:55
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcMessage {

    /**
     * 协议版本
     */
    private byte version;
    /**
     * 消息类型
     */
    private byte messageType;
    /**
     * 序列化方式
     */
    private byte codec;
    /**
     * 请求ID
     */
    private int requestId;
    /**
     * 携带数据
     */
    private Object payload;
}
