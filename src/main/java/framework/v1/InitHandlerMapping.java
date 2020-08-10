package framework.v1;

import framework.Annotation.Controller;
import framework.Annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName InitHandlerMapping
 * @Description
 * @Author xfl
 * @Date 2020/8/9 9:16
 * @Version V1.0
 **/
public class InitHandlerMapping {
    /*
     * @Author xfl
     * @Description 初始化URL和method的一对一的关系
     * @Date 8:37 2020/8/8
     * @Param []
     * @return void
     **/
    public List<HandlerMapping> initHandlerMapping(Map<String,Object> ioc, List<HandlerMapping> handlerMappings) {
        if(ioc.isEmpty()){return null;}

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if(!clazz.isAnnotationPresent(Controller.class)){continue;}
            String baseUrl = "";
            //此处获取类上面的RequestMapping值
            if(clazz.isAnnotationPresent(RequestMapping.class)){
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }

            //获取类中所有的public方法
            for (Method method : clazz.getMethods()) {
                //没有加RequestMapping注解的直接忽略
                if(!method.isAnnotationPresent(RequestMapping.class)){continue;}
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = ("/" + baseUrl + "/" +requestMapping.value()).replaceAll("/+","/");
                Pattern pattern = Pattern.compile(url);
                handlerMappings.add(new HandlerMapping(pattern,method,entry.getValue()));
                System.out.println("Mapping:" + pattern + "," + method);
            }

        }
        return handlerMappings;
    }
}
