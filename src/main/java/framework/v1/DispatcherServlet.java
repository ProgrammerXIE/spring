package framework.v1;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName DispatcherServlet
 * @Description
 * @Author xfl
 * @Date 2020/8/7 15:34
 * @Version V1.0
 **/
public class DispatcherServlet extends HttpServlet {

    //保存application.properties中的内容
    private Properties contextConfig = new Properties();

    //保存扫描到的所有的类名
    private List<String> classNames = new ArrayList<String>();

    //IOC容器
    private Map<String,Object> ioc = new HashMap<String,Object>();

    //保存URL和method的对应关系
    //private Map<String,Method> handlerMapping = new HashMap<String,Method>();
    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    private DoLoadConfig doLoadConfig = new DoLoadConfig();
    private DoScanner doScanner = new DoScanner();
    private DoInstance doInstance = new DoInstance();
    private DoAutowired doAutowired = new DoAutowired();
    private InitHandlerMapping initHandlerMapping = new InitHandlerMapping();
    private DoDispatch doDispatch = new DoDispatch();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //6.调用
        try {
            doDispatch.doDispatch(req,resp,handlerMappings);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception,Detail : " + Arrays.toString(e.getStackTrace()));
        }
    }

    /*
     *初始化阶段
     */

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1.加载配置文件
        contextConfig = doLoadConfig.doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //2.扫描相关的类
        classNames = doScanner.doScanner(contextConfig.getProperty("scanPackage"));

        //3.初始化扫描到的类，并将它们放到IOC容器中
        ioc = doInstance.doInstance(classNames);
        
        //4.完成依赖注入
        ioc = doAutowired.doAutowired(ioc);

        //5.初始化HandlerMapping
        handlerMappings = initHandlerMapping.initHandlerMapping(ioc,handlerMappings);

        System.out.println("My FrameWork is init.");
    }
}
