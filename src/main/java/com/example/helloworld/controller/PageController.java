package com.example.helloworld.controller;

import com.example.helloworld.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MVC Controller serving Thymeleaf HTML pages.
 *
 * <p>GET /        → redirects to /hello-page
 * <p>GET /hello-page → renders the Hello World HTML page with CFS logo
 * <p>GET /health-page → renders the Health status HTML page
 */
@Controller
public class PageController {

    private final HelloService helloService;

    public PageController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/hello-page";
    }

    @GetMapping("/hello-page")
    public String helloPage(Model model) {
        model.addAttribute("message", helloService.getHello().message());
        return "hello";
    }

    @GetMapping("/health-page")
    public String healthPage(Model model) {
        model.addAttribute("status", "UP");
        return "health";
    }

    @GetMapping("/hello-page-boq")
    public String helloPageBoq(Model model) {
        model.addAttribute("message", helloService.getHello().message());
        return "hello-boq";
    }
}
