package pl.msc.demoboot.utilties;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static boolean checkEmailOrPassword(String pattern, String pStr) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(pStr);
        System.out.println(m.matches());
        return m.matches();
    }

    public static String activationCodeGenerator(){
        String randomString = "";
        String sings = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();

        for(int i=0; i<32; i++){
            int liczba = random.nextInt(sings.length());
            randomString+=sings.substring(liczba,liczba+1);
        }
        return randomString;
    }
}
