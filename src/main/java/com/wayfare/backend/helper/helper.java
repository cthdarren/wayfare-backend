package com.wayfare.backend.helper;

import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class helper{
    public static byte[] generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[64];
        r.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String secret,byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(secret.getBytes(StandardCharsets.UTF_8));
    }

    //TODO VALIDATE THIS EXCEPTION GLOBALLY
    public static WayfareUserDetails getCurrentUserDetails() throws UsernameNotFoundException {
        return (WayfareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

