//package com.retro_rent.manager.controllers;
//
//import com.retro_rent.manager.Dao.RoleDao;
//import com.retro_rent.manager.modle.User;
//import com.retro_rent.manager.Service.UserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.validation.Valid;
//import java.util.Optional;
//
//@Controller
//public class LoginController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleDao roleDao;
//
//    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
//
//    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
//    public ModelAndView login(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/accessDenied")
//    public ModelAndView accessDenied(Model model)
//    {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Optional<User> userOptional = userService.findUserByEmail(auth.getName());
//        User user;
//        ModelAndView modelAndView = new ModelAndView();
//        if(userOptional.isPresent()) {
//            user = userOptional.get();
//            if (user.getRole().equals(roleDao.findByRole("WAITING"))) {
//                model.addAttribute("message", "user status is - waiting for approval");
//            } else {
//                model.addAttribute("message", "user access is denied for the current page");
//            }
//
//            model.addAttribute("current_user", user.getName());
//
//
//            modelAndView.setViewName("accessDenied");
//        }
//
//        return modelAndView;
//    }
//
//    @RequestMapping(value="/registration", method = RequestMethod.GET)
//    public ModelAndView registration(){
//        ModelAndView modelAndView = new ModelAndView();
//        User user = new User();
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("registration");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public ModelAndView createNewUser(@Valid User user, Model model) {
//        ModelAndView modelAndView = new ModelAndView();
//
//        Optional<User> userOptional = userService.findUserByEmail(user.getEmail());
//
//        if(userOptional.isPresent()) {
//            model.addAttribute("error_message", "There is already a user registered with the email provided");
//            modelAndView.setViewName("registration");
//        } else {
//            userService.saveUser(user);
//            modelAndView.addObject("successMessage", "User has been registered successfully");
//            modelAndView.addObject("user", new User());
//            modelAndView.setViewName("login");
//
//        }
//
//        return modelAndView;
//    }
//}
//
