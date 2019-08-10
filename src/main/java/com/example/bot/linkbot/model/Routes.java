package com.example.bot.linkbot.model;

import com.example.bot.linkbot.Vars;

/**
 * created By aMIN on 4/4/2019 12:53 PM
 */
//Joining can be conditional by admins of channels and groups
public enum Routes {
    Scientific("علمی"),//0
    Entertainment("سرگرمی"),//1
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
    GROUPSANDCHANNELSPEOPLE (Vars.GROUPSANDCHANNELSPEOPLE),//12
    MYLINKS("لینک های من"),//13
    CANCELMAKEINGLINK("بی خیال"),//14
    BACKTOSTART("back"),//15
    ADDINGLINKTO("اضافه کردن لینک"),//16
    ;

    Routes() {

    }

    public String name;

    Routes(String s) {
        name = s;
    }

}
