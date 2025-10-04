package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AssetOptionResponse {

    @JsonProperty("symbol")
    private final String symbol;

    @JsonProperty("options")
    private final List<AssetOption> options;

}