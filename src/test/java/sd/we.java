package sd;

import com.mpatric.mp3agic.*;

import java.io.IOException;

public class we {

    public static void main(String[] args) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        Mp3File mp3file = new Mp3File("Vahid Nil - In Shahr.mp3");
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
        } else {
            // mp3 does not have an ID3v2 tag, let's create one..
            id3v2Tag = new ID3v24Tag();
            mp3file.setId3v2Tag(id3v2Tag);
        }
        id3v2Tag.setTrack("5");
        id3v2Tag.setArtist("An Artist");
        id3v2Tag.setTitle("The Title");
        id3v2Tag.setAlbum("The Album");
        id3v2Tag.setYear("2001");
        id3v2Tag.setGenre(12);
        id3v2Tag.setComment("Some comment");
        id3v2Tag.setLyrics("Some lyrics");
        id3v2Tag.setComposer("The Composer");
        id3v2Tag.setPublisher("A Publisher");
        id3v2Tag.setOriginalArtist("Another Artist");
        id3v2Tag.setAlbumArtist("An Artist");
        id3v2Tag.setCopyright("Copyright");
        id3v2Tag.setUrl("http://foobar");
        id3v2Tag.setEncoder("The Encoder");
        mp3file.save("MyMp3Files.mp3");

    }
}
