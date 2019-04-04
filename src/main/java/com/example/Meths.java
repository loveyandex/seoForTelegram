package com.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * created By aMIN on 4/5/2019 4:05 AM
 */

public class Meths {


    public static String post(OkHttpClient client, String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public static void sendToBot(String text) {
        String token = "bot495402062:AAHyqLaAsQS_BeQNwDU9qTG81RVXWEvwP6s";

        String d = "https://api.telegram.org/"
                + token
                + "/sendMessage?chat_id=145464749&text=";
        try {
            post(new OkHttpClient(), d + text);
        } catch (IOException e) {
            System.out.println(e.toString());

        }
    }
}
