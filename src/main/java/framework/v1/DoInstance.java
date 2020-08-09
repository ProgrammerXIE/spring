package framework.v1;

import framework.Annotation.Controller;
import framework.Annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DoInstance
 * @Description
 * @Author xfl
 * @Date 2020/8/9 9:09
 * @Version V1.0
 **/
public class DoInstance {

    private Map<String,Object> ioc = new HashMap<String,Object>();

    public Map<String,Object> doInstance(List<String> classNames) {
        //为DI做准备
        if(classNames.isEmpty()){return null;}

        for (String classname:classNames) {
            try{
                Class<?> clazz = Class.forName(classname);
                //扫描带有注解的类
                if(clazz.isAnnotationPresent(Controller.class)){
                    Object instance = clazz.newInstance();
                    //spring中默认类名首字母小写
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName,instance);
                }else if(clazz.isAnnotationPresent(Service.class)){
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();
                    if("".equals(beanName)){
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName,instance);
                    for (Class<?> i : clazz.getInterfaces()) {
                        if(ioc.containsKey(i.getName())){
                            throw new Exception("The “" + i.getName() + "”is exists!!!");
                        }
                        ioc.put(i.getName(),instance);
                    }
                }else{
                    continue;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ioc;
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //通过ASCII进行大小写转换
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
