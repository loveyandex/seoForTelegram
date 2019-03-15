package com.example;

import com.example.database.QDB;
import jdk.nashorn.internal.objects.annotations.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class ResApi {


    @GetMapping("/div")
    public String D() {
        try {
            ResultSet resultSet = QDB.getInstance().connection.createStatement().executeQuery("select count (*) from music4;");
            while (resultSet.next())
                return String.valueOf(resultSet.getInt(1));

        } catch (SQLException e) {
           return e.getLocalizedMessage();
        }

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
