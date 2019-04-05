package com.example.bot.linkbot.model;

/**
 * created By aMIN on 4/4/2019 12:53 PM
 */


//Joining can be conditional by admins of channels and groups

public enum Gune {
    Scientific("علمی"), Entertainment("سرگرمی"), Newsly("خبر"), Engineerical("فنی- مهندسی"),
    Sport("ورزشی"), Girlely("دخترونه ها"), FriendShip("دوست یابی"), Financial("مالی"), EconmicalProblems("مسائل اقتصادی"),
    Politicals("سیاسی"), Fitness("تناسب اندام");

    Gune() {

    }

    public String name;

    Gune(String s) {
        name = s;
    }

}
