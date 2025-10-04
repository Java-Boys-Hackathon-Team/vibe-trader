package ru.javaboys.vibetraderbackend.finam.dto.asset;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import ru.javaboys.vibetraderbackend.finam.dto.BigDecimalValueWrapper;

@Getter
@Builder
public class AssetResponse {

    @JsonProperty("board")
    private final String board;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("ticker")
    private final String ticker;

    @JsonProperty("mic")
    private final String mic;

    @JsonProperty("isin")
    private final String isin;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("lot_size")
    private final BigDecimalValueWrapper lotSize;

    @JsonProperty("decimals")
    private final Integer decimals;

    @JsonProperty("min_step")
    private final String minStep;

}