package com.hmy.euclidean.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("box_art_url")
    private final String boxArtUrl;
}
