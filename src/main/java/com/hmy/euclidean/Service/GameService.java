package com.hmy.euclidean.Service;

import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final String TOKEN = "Bearer jonris96d3cj0a4nlkt398bgzch5au";
    private static final String CLIENT_ID = "5l0ari2560dmkdrf5wql00ye5mk8nd";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;
}
