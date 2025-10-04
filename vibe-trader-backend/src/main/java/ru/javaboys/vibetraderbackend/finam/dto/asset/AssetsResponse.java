package ru.javaboys.vibetraderbackend.finam.dto.asset;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetsResponse {

    @JsonProperty("assets")
    private final List<Asset> assets;

}