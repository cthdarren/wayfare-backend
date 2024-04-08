package com.wayfare.backend.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.GeoApiContext;
import com.wayfare.backend.model.Currency;
import com.wayfare.backend.repository.CurrencyRepository;
import com.wayfare.backend.security.WayfareUserDetails;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static java.net.http.HttpClient.newHttpClient;

@Service
public class helper{

    @Value("${EXCHANGE_RATE_API_KEY}")
    private String EXCHANGE_RATE_API_KEY;
    private final CurrencyRepository currencyRepo;

    public static GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyCNmU-849bB_xLG90P8LtPjvkTXmqTHJVA").build();

    public helper(CurrencyRepository currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

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

    @PostConstruct
    public void getExchangeRates(){
        HttpClient client = newHttpClient();
        String getUrl =String.format("https://v6.exchangerate-api.com/v6/%s/latest/USD/", EXCHANGE_RATE_API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200){
                JSONObject jsonresponse = new JSONObject(response.body());
                JSONObject data = jsonresponse.getJSONObject("conversion_rates");
                HashMap<String,Object> result = new ObjectMapper().readValue(data.toString(), HashMap.class);
                Optional<Currency> prev = currencyRepo.findAll().stream().findFirst();
                if (prev.isPresent()) {
                    Currency prevCurrency = prev.get();
                    prevCurrency.setData(result);
                    prevCurrency.setLastUpdate(Instant.now());
                    currencyRepo.save(prevCurrency);
                }
                else{
                    Currency newCurrency = new Currency(result);
                    newCurrency.setLastUpdate(Instant.now());
                    currencyRepo.save(newCurrency);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

