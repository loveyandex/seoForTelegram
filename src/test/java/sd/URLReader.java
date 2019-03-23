package sd;

import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;

public class URLReader {
    public static void main(String[] args) throws Exception {

        URL oracle = new URL("https://music96.ir/wp-content/uploads/2019/03/Macan-Band-Ki-Boodi-To1-85x85.jpg");
        InputStream inputStream = oracle.openStream();

        int available = inputStream.available();
        System.out.println(available);

        byte[] bytes = new byte[available];







    }
}