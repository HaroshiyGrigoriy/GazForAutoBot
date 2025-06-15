package com.gaz.demo.bot.services;

import com.gaz.demo.bot.BotState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {
    private final Map<Long, BotState> userStates = new ConcurrentHashMap<>();

    public BotState getUserStates(Long userId) {
        return userStates.getOrDefault(userId, BotState.DEFAULT);
    }

    public void setUserState(Long userId, BotState state) {
        userStates.put(userId, state);
    }
}
