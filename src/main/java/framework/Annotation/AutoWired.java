package framework.Annotation;

import java.lang.annotation.*;

/**
 * @ClassName AutoWired
 * @Description
 * @Author xfl
 * @Date 2020/8/7 17:19
 * @Version V1.0
 **/
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWired {
    boolean required() default true;
    String value() default "";
}
