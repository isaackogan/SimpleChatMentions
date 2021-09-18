package com.isaackogan.simplechatmentions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Primary Entry Class for registering listeners
 */
public final class SimpleChatMentions extends JavaPlugin {

    // Main Listener
    MentionListener listener = new MentionListener();

    /**
     * When the plugin is enabled
     */
    @Override
    public void onEnable() {

        // Register Listener
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * When the plugin is disabled
     */
    @Override
    public void onDisable() {
    }

}
