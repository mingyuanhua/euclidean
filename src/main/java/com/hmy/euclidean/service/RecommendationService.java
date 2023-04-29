package com.hmy.euclidean.service;

import com.hmy.euclidean.dao.FavoriteDao;
import com.hmy.euclidean.entity.db.Item;
import com.hmy.euclidean.entity.db.ItemType;
import com.hmy.euclidean.entity.response.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {
    private static final int DEFAULT_GAME_LIMIT = 3;
    private static final int DEFAULT_PER_GAME_RECOMMENDATION_LIMIT = 10;
    private static final int DEFAULT_TOTAL_RECOMMENDATION_LIMIT = 20;

    @Autowired
    private GameService gameService;

    @Autowired
    private FavoriteDao favoriteDao;

    // Return a list of Item objects for the given type. Types are one of [Stream, Video, Clip]. Add items are related to the top games provided in the argument
    private List<Item> recommendByTopGames(ItemType type, List<Game> topGames) throws RecommendationException {
        List<Item> recommendedItems = new ArrayList<>();
        for (Game game : topGames) {
            List<Item> items;
            try {
                items = gameService.searchByType(game.getId(), type, DEFAULT_PER_GAME_RECOMMENDATION_LIMIT);
            } catch (Exception e) {
                throw new RecommendationException("Failed to get recommendation result");
            }
            for (Item item : items) {
                if (recommendedItems.size() == DEFAULT_TOTAL_RECOMMENDATION_LIMIT) {
                    return recommendedItems;
                }
                recommendedItems.add(item);
            }
        }
        return recommendedItems;
    }
}
