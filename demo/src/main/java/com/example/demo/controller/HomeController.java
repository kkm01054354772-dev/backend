package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.Info;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Log4j2
@Controller
public class HomeController {
    
    @GetMapping("/") // http://localhost:8080/
    public String getIndex(RedirectAttributes rttr) {
        // return "index";

        // ?key=value 보내고 싶을 때
        rttr.addAttribute("bno",10);
        rttr.addAttribute("name","홍길동");
        rttr.addFlashAttribute("money",1000);
        return "redirect:/home"; 
    }
    



    // get 요청으로 들어올 때
    @GetMapping("/home")
    public void getHome(int bno, String name) {
       log.info("home 요청 {} {}", bno, name); // System.out.println();
    }
    
    @GetMapping("/add")
    public String getAdd(@RequestParam int num1, @RequestParam String op, @RequestParam int num2, Model model) {
        log.info("사칙연산 요청 {} {} {}", num1, op, num2);
        int result =0;
        switch (op) {
            case "+":
                result =num1+num2;
                break;
            case "-":
                result=num1-num2;
                break;
            case "*":
                result=num1*num2;
                break;
            case "/":
                result=num1/num2;
                break;
            default:
                break;
        }
        log.info("result = " + result);

        // Model : 컨트롤러가 뷰에 데이터를 전달하기 위해 사용하는 인터페이스
        // (key, value) : key는 중복 x, 
        model.addAttribute("num1",num1);
        model.addAttribute("op",op);
        model.addAttribute("num2",num2);
        model.addAttribute("result",result);
        return "exam3";

    }


    @GetMapping("/calc")
    public void getCalc() {
        log.info("calc get");
    }
    
    // 폼이 들어왔을 때
    @PostMapping("/calc")
    public void postCalc(@RequestParam(required = false, defaultValue = "0") int num1, @RequestParam(required = false, defaultValue = "0") int num2) {
        log.info("calc post {} {}",num1,num2);
    }


    @GetMapping("/info")
    public void getInfo() {
        log.info("info.html 호출");
    }

    // 1번째 방법 : 개별 처리
    // @PostMapping("/info")
    // public void postInfo(String username, int age, String addr, String tel) {
    //     log.info("info post");
    //     log.info("{},{},{},{}",username,age,addr,tel);
    // }
    
    // 2번째 방법 : DTO 이용
    // @PostMapping("/info")
    // public void postInfo(Info info) {
    //     log.info("info post");
    //     log.info("{}, {}, {}, {}",info.getUsername(),info.getAge(),info.getAddr(),info.getTel());
    // }

    // 3번째 방법 : HttpServletRequest 객체 사용
    // 사용자가 요청할 때 사용하는 모든 정보를 가지고 올 수 있음 ex) 브라우저 정보, 사용자 ip, 경로 추출 등
    @PostMapping("/info")
    public void postInfo(HttpServletRequest request) {
        log.info("info post");
        String username=request.getParameter("username");
        int age=Integer.parseInt(request.getParameter("age"));
        String addr=request.getParameter("addr");
        String tel=request.getParameter("tel");
        log.info("{}, {}, {}, {}",username,age,addr,tel);
    }
    
    
    
}
