package framework.v1;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DoScanner
 * @Description
 * @Author xfl
 * @Date 2020/8/9 9:06
 * @Version V1.0
 **/
public class DoScanner {
    private List<String> classNames = new ArrayList<String>();
    /*
     *扫描相关的类
     */
    public List<String> doScanner(String scanPackage) {
        //此处获取相关类所在的路径
        //需要将路径中的.改成/
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        //遍历该路径下的文件
        for (File file : classPath.listFiles()) {
            //判断是否为文件夹
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else {
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                classNames.add(className);
            }
        }
        return classNames;
    }
}
