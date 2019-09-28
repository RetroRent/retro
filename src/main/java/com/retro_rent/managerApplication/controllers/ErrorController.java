//package com.retro_rent.manager.controllers;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
//    private static final String PATH = "/error";
//
//    @RequestMapping(value = PATH)
//    public ResponseEntity error(HttpServletRequest request, Model model) {
//        model.addAttribute("error_message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
//        model.addAttribute("error_status", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("error");
//        return modelAndView;
//    }
//
//
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//}
