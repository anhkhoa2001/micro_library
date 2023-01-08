package org.micro.validation;

import org.micro.model.Authority;
import org.micro.service.AuthorityService;
import org.micro.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private static Matcher matcher;

    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String validateLogin(String username, String password) {

        if (StringUtil.isNullOrEmpty(username)) {
            return "Tên tài khoản không được để trống";
        }

        if (StringUtil.isNullOrEmpty(password)) {
            return "Mật khẩu không được để trống";
        }

        return null;
    }

    public String validateRegister(String username, String password, Set<Integer> authorities,
                                   AuthorityService authorityService) {

        if (StringUtil.isNullOrEmpty(username)) {
            return "Tên tài khoản không được để trống";
        }

        if (StringUtil.isNullOrEmpty(password)) {
            return "Mật khẩu không được để trống";
        }
        if(!validateEmail(username)) {
            return "Email tài khoản không hợp lệ";
        }
        List<Authority> authorities1 = authorityService.findAllById(authorities);
        if(authorities == null || authorities.isEmpty()) {
            return "Quyền tài khoản không được bỏ trống";
        } else if(authorities1 == null || authorities.size() != authorities1.size()){
            return "Quyền tài khoản không đúng";
        }

        return null;
    }
}
