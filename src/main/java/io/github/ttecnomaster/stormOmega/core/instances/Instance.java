package io.github.ttecnomaster.stormOmega.core.instances;

import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.World;

import java.util.UUID;

public abstract class Instance {
    private UUID uuid;
    private SlimeWorld slimeWorld;
    private World bukkitWorld;

    public UUID getUUID() {
        return uuid;
    }

    public SlimeWorld getSlimeWorld() {
        return slimeWorld;
    }

    public World getBukkitWorld() {
        return bukkitWorld;
    }

    public boolean isLoaded() {
        return slimeWorld == null;
    }

    public abstract void start();
    public abstract void tick();
}
