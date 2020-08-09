package framework.v1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName DoLoadConfig
 * @Description
 * @Author xfl
 * @Date 2020/8/9 8:55
 * @Version V1.0
 **/
public class DoLoadConfig {
    private Properties contextConfig = new Properties();
    /*
     *加载配置文件
     */
    public Properties doLoadConfig(String contextConfigLocation) {
        //将web.xml中contextConfigLocation的值读取出来
        //即application.properties
        //读取properties文件中的内容，存入流(内存)中
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);

        try {
            //因为配置文件是properties的，所以设定全局私有变量contextConfig，去解析properties文件
            contextConfig.load(fis);
            return contextConfig;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
