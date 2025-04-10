package io.github.ttecnomaster.stormOmega.skyblock.instances;

import io.github.ttecnomaster.stormOmega.core.instances.Instance;
import io.github.ttecnomaster.stormOmega.skyblock.SkyblockPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SkyblockInstance extends Instance {

    private final Set<SkyblockPlayer> players = new HashSet<>();

    @Override
    public void start() {

    }

    @Override
    public void tick() {
        checkPlayerCount();
    }

    private void addPlayer(SkyblockPlayer player) {
        player.getBukkitPlayer().sendMessage("Welcome to SkyBlock!");
    }

    private void removePlayer(SkyblockPlayer player) {
        player.unload();
    }

    private void checkPlayerCount() {

        // Check if new players got added to the world
        for(Player bukkitPlayer : getBukkitWorld().getPlayers()) {
            if(!doesBukkitPlayerExist(bukkitPlayer)) {
                SkyblockPlayer skyblockPlayer = new SkyblockPlayer(bukkitPlayer);
                addPlayer(skyblockPlayer);
                players.add(skyblockPlayer);
            }
        }

        // Check if a player left the world
        Iterator<SkyblockPlayer> it = players.iterator();
        while (it.hasNext()) {
            SkyblockPlayer skyblockPlayer = it.next();
            if(!getBukkitWorld().getPlayers().contains(skyblockPlayer.getBukkitPlayer())) {
                removePlayer(skyblockPlayer);
                it.remove();
            }
        }

    }

    private boolean doesBukkitPlayerExist(Player player) {
        for(SkyblockPlayer skyblockPlayer : players) {
            if(skyblockPlayer.getBukkitPlayer().equals(player)) return true;
        }
        return false;
    }
}
