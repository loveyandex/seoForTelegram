package com.example;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ResApi {


    @GetMapping("/div")
    public String D(){
        return "king";
    }


    @RequestMapping(value = "/send/{text}"
            , method = RequestMethod.GET)
    public void gett(@PathVariable String text, HttpServletRequest request,
                     HttpServletResponse response) {
        String token = "bot700687388:AAHGmzovGb0LVXKRZAkechWrHBst7BJPMjw";

        response.setHeader("Location", "https://api.telegram.org/"
                + token
                + "/sendMessage?chat_id=145464749&text="
                + text);
        response.setStatus(302);
    }
}
