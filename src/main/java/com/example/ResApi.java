package com.example;

import com.example.database.QDB;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class ResApi {



    @GetMapping("/s")
    public String s() {
        return "kirshodi" ;
    }


    @RequestMapping(value = "/send/{text}"
            , method = RequestMethod.GET)
    public String gett(@PathVariable String text, HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String token = "bot700687388:AAHGmzovGb0LVXKRZAkechWrHBst7BJPMjw";

        String url = "https://api.telegram.org/"
                + token
                + "/sendMessage?chat_id=145464749&text="
                + text;

        return run(url);

//        response.setHeader("Location", url);
//        response.setStatus(302);
    }


    public static String run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }



}
