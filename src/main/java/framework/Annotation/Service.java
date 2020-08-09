package framework.Annotation;

import java.lang.annotation.*;

/**
 * @ClassName Service
 * @Description
 * @Author xfl
 * @Date 2020/8/7 17:19
 * @Version V1.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
