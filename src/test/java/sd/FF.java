package sd;

/**
 * created By aMIN on 4/8/2019 2:52 PM
 */

public class FF{

    public static void main(String[] args) {

        String x = "زبان\u200Cشناسی";
        System.out.println(x);
        for (int i = 0; i <x.length() ; i++) {
            System.out.println(x.charAt(i));

        }
    }

}
