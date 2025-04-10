package io.github.ttecnomaster.stormOmega.core.instances;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import io.github.ttecnomaster.stormOmega.StormOmega;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class InstanceManager {
    private final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");;
    private final Map<String, Template> templates = new HashMap<>();
    public void addTemplate(String world, Vector spawn) {
        templates.put(world, new Template(spawn));
    }

    public <T extends Instance> T createInstance(Class<T> clazz, String template) throws InstanceException {
        try {

            // Calls default constructor (Expects default constructor to have no parameters)
            Constructor<T> constructor = clazz.getConstructor();
            T instance = constructor.newInstance();

            // Create random UUID for the instance
            UUID uuid = UUID.randomUUID();

            // Sets the private field "uuid" of Instance to the newly created UUID
            Field field = Instance.class.getDeclaredField("uuid");
            field.setAccessible(true);
            field.set(instance, uuid);

            createTemporarySlimeWorld(instance, template, true ? "instance" : uuid.toString());

            return instance;

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new InstanceException("Instance class could not be instantiated!");
        } catch (IllegalAccessException e) {
            throw new InstanceException("Constructor of Instance NEEDS to be public!");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTemporarySlimeWorld(Instance instance, String templateString, String name) {

        if(!templates.containsKey(templateString)) throw new InstanceException("Template \""+templateString+"\" does not exist!");
        Template template = templates.get(templateString);

        // should be ran asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(StormOmega.PLUGIN, () -> {

            SlimeLoader loader = slimePlugin.getLoader("file");

            try {

                // Load template world and clone it to a new SlimeWorld
                SlimeWorld templateWorld = slimePlugin.loadWorld(loader, templateString, template.properties);
                SlimeWorld slimeWorld = templateWorld.clone(name);

                // generate world synchronously
                Bukkit.getScheduler().runTask(StormOmega.PLUGIN, () -> {
                    slimePlugin.generateWorld(slimeWorld);
                    finishInstanceLoading(instance, slimeWorld);
                });

            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldInUseException e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void finishInstanceLoading(Instance instance, SlimeWorld slimeWorld) {

        // Set field "slimeWorld" of Instance to the slimeWorld which will automatically mark it as loaded!

        try {
            Field field = Instance.class.getDeclaredField("slimeWorld");
            field.setAccessible(true);
            field.set(instance, slimeWorld);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Set the field "bukkitWorld" of Instance to the slimeWorld
        World bukkitWorld = Bukkit.getWorld(slimeWorld.getName());
        try {
            Field field = Instance.class.getDeclaredField("bukkitWorld");
            field.setAccessible(true);
            field.set(instance, bukkitWorld);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Call start on the Instance
        instance.start();

    }

    private static class Template {
        private final SlimeWorld.SlimeProperties properties;
        public Template(Vector spawn) {
            properties = SlimeWorld.SlimeProperties.builder().
                    spawnX(spawn.getX()).
                    spawnY(spawn.getY()).
                    spawnZ(spawn.getZ()).
                    difficulty(0).
                    allowMonsters(false).
                    allowAnimals(false).
                    readOnly(true).
                    pvp(false).
                    environment("NORMAL")
                    .build();
        }
    }
}
