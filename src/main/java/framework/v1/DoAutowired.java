package framework.v1;

import framework.Annotation.AutoWired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DoAutowired
 * @Description
 * @Author xfl
 * @Date 2020/8/9 9:13
 * @Version V1.0
 **/
public class DoAutowired {
    /**
     * @Author xfl
     * @Description 自动依赖注入
     * @Date 8:11 2020/8/8
     * @Param []
     * @return void
     **/
    public Map<String,Object> doAutowired(Map<String,Object> ioc) {
        if(ioc.isEmpty()){return null;}

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //Declared所有的参数全部获取
            //包括public、private、protected
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if(!field.isAnnotationPresent(AutoWired.class)){continue;}
                AutoWired autoWired = field.getAnnotation(AutoWired.class);
                //用户如果没有自定义beanName则根据类型注入
                String beanName = autoWired.value().trim();
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                }
                //除了public方法，其他方法只要加了autowired注释都可以访问
                //反射中称为暴力访问
                field.setAccessible(true);
                try {
                    //用反射机制动态给字段赋值
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return ioc;
    }
}
