package com.example.pojos;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * is created by aMIN on 12/22/2018 at 3:55 AM
 */
@Getter
@Setter
@Accessors(chain = true)
public class Music3 {
    private String name;
    private String src_url;
    private String tags;
    private String artist;
    private String album;
    private String name_persian;
    private String artist_persian;
    private String tags_persian;
    private String fileId;
    private String channelUrl;
    private int howmuchsent;
}
