package com.example.bot.bitMelBot.pojos;

import org.telegram.telegrambots.meta.api.objects.User;

public class Usegh {
    private Integer id; ///< Unique identifier for this user or bot
    private String firstName; ///< User‘s or bot’s first name
    private Boolean isBot; ///< True, if this user is a bot
    private String lastName; ///< Optional. User‘s or bot’s last name
    private String userName; ///< Optional. User‘s or bot’s username
    private String languageCode; ///< Optional. IETF language tag of the user's language


    public Usegh() {

    }

    public Usegh(Integer id, String firstName, Boolean isBot, String lastName, String userName, String languageCode) {
        this.id = id;
        this.firstName = firstName;
        this.isBot = isBot;
        this.lastName = lastName;
        this.userName = userName;
        this.languageCode = languageCode;
    }


    public Usegh(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.isBot = user.getBot();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.languageCode = user.getLanguageCode();
    }

}
