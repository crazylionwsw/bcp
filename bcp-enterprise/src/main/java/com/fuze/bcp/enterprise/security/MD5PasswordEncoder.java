package com.fuze.bcp.enterprise.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  接收Pad端的为已加密密码
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword.toString().equals(encodedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
