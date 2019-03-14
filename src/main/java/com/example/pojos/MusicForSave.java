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
public class MusicForSave {
    private String nameOfSong;
    private String nameOfSinger;
    private String persianNameOfSong;
    private String persianNameOFSinger;
    private String src_url;
    private String caption;
    private String tags;
    private String persian_tags;
    private Status status;
}
