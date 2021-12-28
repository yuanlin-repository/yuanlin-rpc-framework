package github.yuanlin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC 服务扫描包（标注在启动类上）
 *
 * @author yuanlin
 * @date 2021/12/28/12:24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcScan {

    String[] basePackages();
}
