/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.example.database.Data;
import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.ApiContextInitializer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Connection connection;

    public static void main(String[] args) throws Exception {
        //Add this line to initialize bots context
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }


    @GetMapping("/ss")
    @ResponseBody
    public String D() {
        try {
            ResultSet resultSet = connection.createStatement()
                    .executeQuery("SELECT count(*) FROM music4");
            while (resultSet.next())
                return String.valueOf(resultSet.getInt(1));

        } catch (Exception e) {
            return "error: " + e.toString();
        }

        return "kirshodi";
    }


    @GetMapping("/dburl")
    @ResponseBody

    public String dD() {
        return dbUrl;
    }

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @RequestMapping("/df")
    @ResponseBody
    List<String> dbd(Map<String, Object> model) {
        ArrayList<String> strings = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS  bia2music" +
                    "(" +
                    "  name           varchar(100) null," +
                    "  src_url        varchar(500) null," +
                    "  tags           varchar(500) null," +
                    "  artist         varchar(200) null," +
                    "  album          varchar(100) null," +
                    "  name_persian   varchar(100) null," +
                    "  artist_persian varchar(100) null" +
                    ")");

            stmt.executeUpdate("INSERT INTO bia2music VALUES ('amin','sd','sd0','sd0','hj','j','j')");


            ResultSet rs = stmt.executeQuery("SELECT * FROM bia2music");


            while (rs.next()) {
                strings.add(rs.getString(1));
            }
        } catch (SQLException e) {
            return strings;
        }
        return strings;
    }


    @RequestMapping("/db")
    String db(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
//            stmt.executeUpdate("create table if not exists usegh (" +
//                    "  id INT(39) not null primary key ," +
//                    "  firstName varchar(50) not null ," +
//                    "  isBot bool ," +
//                    "  lastName varchar(50)," +
//                    "  userName varchar(50)," +
//                    "  languageCode varchar(2)," +
//                    "  numberMsg int" +
//                    ")");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            ArrayList<String> output = new ArrayList<String>();
            while (rs.next()) {
                output.add("Read from DB: " + rs.getTimestamp("tick"));
            }

            model.put("records", output);
            return "db";
        } catch (Exception e) {
            model.put("message", e.toString());
            return "error";
        }
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }
    }

    @Bean
    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }


    @Bean
    public void sd() {
        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            while (true) {
                String token = "bot495402062:AAHyqLaAsQS_BeQNwDU9qTG81RVXWEvwP6s";

                String d = "https://api.telegram.org/"
                        + token
                        + "/sendMessage?chat_id=145464749&text=";
                String url = d + "timing";
                try {

                    String post = post(client, "https://still-waters-40834.herokuapp.com/");
                    post(client, d + post.substring(0, 164));
                    Thread.sleep(60000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    public static String post(OkHttpClient client, String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    @GetMapping("crt")
    @ResponseBody
    public String creatdedb() {
        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("CREATE TABLE IF NOT EXISTS  seek" +
                    "(" +
                    "id SERIAL PRIMARY KEY,   " +
                    "amount  INT (39)  NOT NULL" +
                    ")");

            return String.valueOf(i);
        } catch (SQLException e) {
            return new Gson().toJson(e.toString());
        }
    }


}
