package ru.javaboys.vibetraderbackend.finam.dto.auth;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDetailsResponse {

    @JsonProperty("created_at")
    private final Instant createdAt;

    @JsonProperty("expires_at")
    private final Instant expiresAt;

    @JsonProperty("md_permissions")
    private final List<MdPermission> mdPermissions;

    @JsonProperty("account_ids")
    private final List<String> accountIds;

    @JsonProperty("readonly")
    private final Boolean readonly;

}
