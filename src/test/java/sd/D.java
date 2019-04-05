package sd;

import com.example.bot.linkbot.model.StatusOfAdding;
import com.mpatric.mp3agic.*;

import java.io.IOException;

public class D {

    public static void main(String[] args) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        System.out.println(StatusOfAdding.ADDINGNAME.name());
        System.exit(0);

        Mp3File mp3file = new Mp3File("Vahid Nil - In Shahr.mp3");
        ID3v1 id3v1Tag;
        if (mp3file.hasId3v1Tag()) {
            id3v1Tag =  mp3file.getId3v1Tag();
        } else {
            // mp3 does not have an ID3v1 tag, let's create one..
            id3v1Tag = new ID3v1Tag();
            mp3file.setId3v1Tag(id3v1Tag);
        }
        id3v1Tag.setTrack("5");
        id3v1Tag.setArtist("An Artist");
        id3v1Tag.setTitle("The Title");
        id3v1Tag.setAlbum("The Album");
        id3v1Tag.setYear("2001");
        id3v1Tag.setGenre(12);
        id3v1Tag.setComment("Some comment");
        mp3file.save("MyMp3File.mp3");
    }
}
