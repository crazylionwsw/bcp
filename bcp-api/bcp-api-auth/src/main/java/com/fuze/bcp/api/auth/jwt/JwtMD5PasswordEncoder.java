package com.fuze.bcp.api.auth.jwt;

import com.fuze.bcp.utils.EncryUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by admin on 2017/6/7.
 */
public class JwtMD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return EncryUtil.MD5(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (EncryUtil.MD5(rawPassword.toString()).equals(encodedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
