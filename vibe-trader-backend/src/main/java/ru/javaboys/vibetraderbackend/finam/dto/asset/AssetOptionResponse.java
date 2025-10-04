package ru.javaboys.vibetraderbackend.finam.dto.asset;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetOptionResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("options")
    private final List<AssetOption> options;

}