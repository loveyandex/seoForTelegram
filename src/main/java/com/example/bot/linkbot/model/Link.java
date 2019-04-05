package com.example.bot.linkbot.model;

import com.example.bot.linkbot.model.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Link extends UserDateAudit {

    private Long id;

    @NotBlank
    @Size(max = 500)
    private String tile;


    @Size(max = 1000)
    private String description;


    @NotBlank
    @Size(max = 1000)
    private String link;


    @Size(max = 1000)
    private String coverImg;


    private RoleLink roleLink;

    private StatusOfAdding statusOfAdding;

}
