package com.isaackogan.simplechatmentions;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;

/**
 * Listener for mentions
 */
public class MentionListener implements Listener {

    /**
     * When a chat message is received
     *
     * @param event Chat message event
     */
    @EventHandler
    public void onChat(AsyncChatEvent event) {

        // Get Mentioned Players
        final Player[] mentionedPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(c -> event.message().toString().toLowerCase(Locale.ROOT).contains(c.getName().toLowerCase(Locale.ROOT)))
                .toArray(Player[]::new);

        // No mentions
        if (mentionedPlayers.length < 1)
            return;

        // Send Action Bar & Play Sound for Mentioned Players
        for (Player player : mentionedPlayers) {
            MentionRenderer.sendMentionedActionbar(player, event.getPlayer());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.0F, 0.5F);
        }

        // Update Messages for Players
        event.renderer(new MentionRenderer(event.renderer(), mentionedPlayers));

    }


}
