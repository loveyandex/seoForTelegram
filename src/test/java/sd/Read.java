package sd;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Read {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get("Vahid Nil - In Shahr.mp3");
// Load as binary:
        byte[] data = Files.readAllBytes(path);
        System.out.println(data.length);
        byte[] baseData = new byte[100];

        for (int i = 0; i < baseData.length; i++) {
            baseData[i] = data[i];
        }

        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 16; i < 127; i++) {
            byte aByte = data[ i];
            char c = (char) aByte;
            System.out.print(c);
            stringBuilder.append(c);
        }

        System.exit(0);
        System.out.println();
        String x = stringBuilder.toString();
        System.out.println(x);
        String s = x.replaceAll("\\[Nex1Music.IR]", "[melodybot]");
        byte[] newArt = s.getBytes();

        System.out.println(s);
        System.out.println(s.length());
        System.out.println(newArt.length);
        for (int g = 0; g < newArt.length; g++) {
            System.out.print(((char) newArt[g]));
        }

        FileOutputStream out = new FileOutputStream("king.mp3", true);
        out.write(baseData);
//        out.flush();
//        out.write(newArt);
        out.flush();
        out.close();
    }
}
