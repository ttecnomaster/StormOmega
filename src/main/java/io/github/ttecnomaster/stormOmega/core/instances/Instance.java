package io.github.ttecnomaster.stormOmega.core.instances;

import com.grinderwolf.swm.api.world.SlimeWorld;

import java.util.UUID;

public abstract class Instance {
    private UUID uuid;
    private SlimeWorld slimeWorld;

    public UUID getUUID() {
        return uuid;
    }

    public SlimeWorld getSlimeWorld() {
        return slimeWorld;
    }

    public boolean isLoaded() {
        return slimeWorld == null;
    }

    public abstract void start();
    public abstract void tick();
}
