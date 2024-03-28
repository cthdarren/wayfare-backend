package com.wayfare.backend.helper;

import com.google.maps.GeoApiContext;
import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class helper{

    public static GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyCNmU-849bB_xLG90P8LtPjvkTXmqTHJVA").build();

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

