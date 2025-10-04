package ru.javaboys.vibetraderbackend.finam.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.AuthResponse;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsRequest;
import ru.javaboys.vibetraderbackend.finam.dto.auth.TokenDetailsResponse;

@FeignClient(
        name = "finam-sessions-api",
        url = "${finam-api.url}/v1/sessions"
)
public interface SessionsApiV1 {

    @PostMapping(value = "")
    AuthResponse auth(
            @RequestBody AuthRequest request
    );

    @PostMapping(value = "/details")
    TokenDetailsResponse tokenDetails(
            @RequestBody TokenDetailsRequest request
    );

}
