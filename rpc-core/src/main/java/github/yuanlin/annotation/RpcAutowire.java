package github.yuanlin.annotation;

import java.lang.annotation.*;

/**
 * RPC 服务自动注入注解（标注在属性上）
 *
 * @author yuanlin
 * @date 2021/12/28/11:27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcAutowire {

    String version() default "";

    String group() default "";
}
