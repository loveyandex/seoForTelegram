package com.example.bot.linkbot.model;

/**
 * created By aMIN on 4/4/2019 12:53 PM
 */


//Joining can be conditional by admins of channels and groups

public enum Routes {
    Scientific("علمی"),
    Entertainment("سرگرمی"),
    Newsly("خبر"),
    Engineerical("فنی- مهندسی"),
    Sport("ورزشی"),
    Girlly("دخترونه ها"),
    FriendShip("دوست یابی"),
    Financial("مالی"),
    EconmicalProblems("مسائل اقتصادی"),
    Politicals("سیاسی"),
    Fitness("تناسب اندام"),
    Unknown("سایر موارد(در این لیست نیست)"),
    GROUPSANDCHANNELS("گروه ها و کانال ها"),
    ADDINGLINKTO("اضافه کردن لینک"),

    ;

    Routes() {

    }

    public String name;

    Routes(String s) {
        name = s;
    }

}
