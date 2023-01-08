package org.micro.validation;

import org.springframework.messaging.MessagingException;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private static Matcher matcher;

    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static void main(String[] args) {
        String email = "khoa@vnu.edu.vn";
        System.out.println(validateEmail(email));
    }
}
