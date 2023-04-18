package com.hmy.euclidean.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hmy.euclidean.entity.db.Item;

public class FavoriteRequestBody {

    @JsonProperty("favorite")
    private Item favoriteItem;

    public Item getFavoriteItem() {
        return favoriteItem;
    }
}

