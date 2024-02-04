package ru.gknsv.controllers;

import javafx.event.ActionEvent;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class MyAlcoholPlate extends AlcoholPlate {

    public void deleteMyAlco(ActionEvent actionEvent) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/alco/delete/" + alcoDto.getId()))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() == 200) {
            calculationSide.displayAlcoholList();
        }
    }
}
