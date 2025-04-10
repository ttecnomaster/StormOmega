package io.github.ttecnomaster.stormOmega;

import io.github.ttecnomaster.stormOmega.core.instances.Instance;
import io.github.ttecnomaster.stormOmega.core.instances.InstanceManager;
import io.github.ttecnomaster.stormOmega.skyblock.instances.SkyblockInstance;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class StormOmega extends JavaPlugin {

    public static Plugin PLUGIN;
    public static final InstanceManager INSTANCE_MANAGER = new InstanceManager();

    @Override
    public void onEnable() {
        // Plugin startup logic

        PLUGIN = this;

        INSTANCE_MANAGER.addTemplate("skyblock_hub", new Vector(-3, 70, -69));

        Instance instance = INSTANCE_MANAGER.createInstance(SkyblockInstance.class, "skyblock_hub");

        getServer().getScheduler().scheduleSyncRepeatingTask(this, instance::tick, 1, 1);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
