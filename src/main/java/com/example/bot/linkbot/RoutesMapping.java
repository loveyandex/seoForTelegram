package com.example.bot.linkbot;

import com.example.bot.linkbot.model.Routes;
import okhttp3.Route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RoutesMapping {
    Routes value();
}  