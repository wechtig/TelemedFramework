package at.fh.telemed.telemedframeworkroot.controller;

import at.fh.telemed.telemedframeworkroot.dtos.MedUser;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
@RestController
public class LoginController {

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");
        modelAndView.addObject("user", new MedUser());
        return modelAndView;
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping("/unauthorizedView")
    public ModelAndView handleUnauthorizedRequest() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("unauthorizedView");
        return modelAndView;
    }
}