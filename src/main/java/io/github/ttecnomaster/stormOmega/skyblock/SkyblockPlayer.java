package io.github.ttecnomaster.stormOmega.skyblock;

import org.bukkit.entity.Player;

public class SkyblockPlayer {
    private final Player bukkitPlayer;
    private boolean loaded = true;
    public SkyblockPlayer(Player player) {
        this.bukkitPlayer = player;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void unload() {
        this.loaded = false;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
