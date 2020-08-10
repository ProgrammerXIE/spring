package demo.controller;

import framework.Annotation.Controller;
import framework.Annotation.RequestMapping;
import framework.Annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName DemoController
 * @Description
 * @Author xfl
 * @Date 2020/8/9 10:30
 * @Version V1.0
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/add.*")
    public void add(HttpServletRequest req, HttpServletResponse resp,
                    @RequestParam("a") Integer a , @RequestParam("b") Integer b){
        try {
            resp.getWriter().write(a + "+" + b + "=" + ( a + b ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest req, HttpServletResponse resp){
        try {
            resp.getWriter().write("登录成功！！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
