package com.mycompany.test2.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", toBuilder = true)
@Valid
public class SubscriptionModel implements Serializable {
    @NotNull
    private UUID id;

    @NotBlank
    private String alias;

    @JsonProperty("targetUsers")
    @Size(min = 1)
    private List<@NotBlank String> targetUserList;

    @JsonProperty("topics")
    @Size(min = 1)
    private List<@Valid SubscriptionTopicModel> topicList;
}
