package github.yuanlin.transport.constants;

/**
 * RPC 常量
 *
 * @author yuanlin
 * @date 2021/12/28/11:54
 */
public class RpcConstants {

/**
 * RPC 数据包
 *
 *      4byte       4byte       2byte       1byte       1byte        1byte       4byte
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
 */

    // 最大帧长度
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

    // 头部长度（如果协议版本更新，扩展字段中有新的字段出现，则头长度需要更新）
    public static final int FULL_LENGTH_FIELD_LENGTH = 4;
    public static final int HEAD_LENGTH = 17;

    // 魔数
    public static final byte[] MAGIC_NUMBER = {(byte)'s', (byte)'t', (byte)'a', (byte)'r'};

    // 当前版本
    public static final byte VERSION = 1;

    // 长度偏移量
    public static final int FULL_LENGTH_FIELD_OFFSET = MAGIC_NUMBER.length;
    public static final int HEAD_LENGTH_FIELD_OFFSET = MAGIC_NUMBER.length + 4;

    // 消息类型
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;

    // 心跳包数据
    public static final String PING = "ping";
    public static final String PONG = "pong";

}
