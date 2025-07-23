package dev.lrxh.neptune.feature.leaderboard.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerEntry {
    private final String username;
    private final UUID uuid;
    private final int value;
}
