package framework.v1;

import framework.Annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HandlerMapping
 * @Description
 * @Author xfl
 * @Date 2020/8/9 9:23
 * @Version V1.0
 **/
public class HandlerMapping {
    private String url;
    private Method method;
    private Object controller;
    private Class<?>[] paramTypes;

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    //形参列表
    //参数名作为key，参数位置（下标）作为value
    private Map<String,Integer> paramIndexMapping;

    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return controller;
    }

    public HandlerMapping(String url, Method method, Object controller) {
        this.url = url;
        this.method = method;
        this.controller = controller;
        paramTypes = method.getParameterTypes();
        paramIndexMapping = new HashMap<String,Integer>();
        putParamIndexMapping(paramIndexMapping);
    }

    private void putParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        Annotation[][] pa = method.getParameterAnnotations();
        for(int j = 0 ; j < pa.length ; j++){
            for (Annotation a : pa[j]) {
                //对RequestParam进行解析
                if(a instanceof RequestParam){
                    //拿到参数名称，到URL中进行匹配
                    String paramName = ((RequestParam)a).value();
                    //从req拿到参数类列表中去找对应的key
                    if(!"".equals(paramName)){
                        paramIndexMapping.put(paramName,j);
                    }
                }
            }

        }

        Class<?>[] paramsTypes = method.getParameterTypes();
        for(int i = 0 ; i < paramsTypes.length ; i++){
            Class<?> type = paramsTypes[i];
            if(type == HttpServletRequest.class ||
            type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }
    }
}
