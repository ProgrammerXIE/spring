package framework.v1;

import framework.Annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @ClassName DoDispatch
 * @Description
 * @Author xfl
 * @Date 2020/8/9 9:21
 * @Version V1.0
 **/
public class DoDispatch {
    public void doDispatch(HttpServletRequest req, HttpServletResponse resp, List<HandlerMapping> handlerMappings) throws Exception{

        HandlerMapping handlerMapping = getHandler(req,handlerMappings);

        if(handlerMapping == null){
            resp.getWriter().write("404 Not Found!");
            return;
        }
        Class<?>[] paramTypes = handlerMapping.getParamTypes();

        Object[] paramValues = new Object[paramTypes.length];

        Map<String,String[]> params = req.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s",",");

            if(!handlerMapping.getParamIndexMapping().containsKey(param.getKey())){continue;}

            int index = handlerMapping.getParamIndexMapping().get(param.getKey());
            paramValues[index] = convert(paramTypes[index], value);
        }

        //此处两个if解决了Controller方法中不传入HttpServletRequest和HttpServletResponse，只传入参数是报错的情况
        if(handlerMapping.getParamIndexMapping().containsKey(HttpServletRequest.class.getName())){
            int reqIndex = handlerMapping.getParamIndexMapping().get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }
        if(handlerMapping.getParamIndexMapping().containsKey(HttpServletResponse.class.getName())) {
            int respIndex = handlerMapping.getParamIndexMapping().get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        handlerMapping.getMethod().invoke(handlerMapping.getController(),paramValues);
    }

    private HandlerMapping getHandler(HttpServletRequest req, List<HandlerMapping> handlerMappings) {
        if(handlerMappings.isEmpty()){return null;}
        //拿到用户请求的URL
        //是一个绝对路径
        String url = req.getRequestURI();
        //处理成相对路径
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");

        //框架初次加载，没有对应action，暂时
        //之后优化
        if("/".equals(url)){
            url = url + "demo/login";
        }

        for (HandlerMapping mapping : handlerMappings) {
            Matcher matcher = mapping.getUrl().matcher(url);
            if(!matcher.matches()){continue;}
            return mapping;

        }
        return null;
    }

    //url传过来的参数都是string类型的,HTTP是基于字符串协议
    //只需要把String转换成任意类型
    private Object convert(Class<?> type,String value){
        if(Integer.class == type){
            return Integer.valueOf(value);
        }

        //此处由策略模式实现多种类型转换
        return  value;
    }
}
