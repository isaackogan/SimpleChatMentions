package com.isaackogan.simplechatmentions;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Custom Chat Rendering for Messages
 */
public class MentionRenderer implements ChatRenderer {

    /**
     * The original renderer passed in from Bukkit
     */
    private final ChatRenderer baseRenderer;

    /**
     * The players that were mentioned
     */
    private final Player[] mentionedPlayers;

    /**
     * Constructor: Assign default attributes
     *
     * @param baseRenderer     Base renderer
     * @param mentionedPlayers Mentioned players array
     */
    public MentionRenderer(ChatRenderer baseRenderer, Player[] mentionedPlayers) {
        this.baseRenderer = baseRenderer;
        this.mentionedPlayers = mentionedPlayers;
    }


    /**
     * Send the mentioned message to a player
     *
     * @param recipient Player receiving
     * @param sender    Person sending
     */
    public static void sendMentionedActionbar(Player recipient, Player sender) {
        // Tell them they got mentioned
        recipient.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " mentioned you!"));
    }

    /**
     * Edit the original message content & wrap it in the received renderer
     *
     * @param source            Person who wrote the message
     * @param sourceDisplayName Display name of author
     * @param message           Message they sent
     * @param viewer            The person that is viewing the message
     * @return The edited message component to render
     */
    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        message = getModifiedMessage(source, viewer, message);
        return baseRenderer.render(source, sourceDisplayName, message, viewer);

    }

    /**
     * Get a modified message for a given player
     *
     * @param sender   Person sending the message
     * @param audience Person viewing the message
     * @param original Original Message Content
     * @return New message content
     */
    private Component getModifiedMessage(Player sender, Audience audience, Component original) {

        // Only player messages
        if (!(audience instanceof Player)) {
            return original;
        }

        // For each mentioned player
        for (Player p : this.mentionedPlayers) {

            // Format message based off the player
            original = formatText(p, (Player) audience, sender, original);
        }

        return original;
    }

    /**
     * Format message based off the relationship of the mentioned player to the receiver/sender
     *
     * @param mentioned Person being mentioned
     * @param receiver  Person receiving the message
     * @param sender    Person sending the message
     * @param original  Original message content upon receival
     * @return Edited "original" component
     */
    private Component formatText(Player mentioned, Player receiver, Player sender, Component original) {
        final Pattern pSearcher = Pattern.compile(mentioned.getName(), Pattern.CASE_INSENSITIVE);

        // Is being mentioned
        if (mentioned.getName().equals(receiver.getName())) {
            return original.replaceText(TextReplacementConfig.builder().match(pSearcher)
                    .replacement((matchResult, builder) -> Component.text(ChatColor.GOLD + builder.content() + ChatColor.RESET))
                    .build());
        }

        // Is mentioning
        else if (sender.getName().equals(receiver.getName())) {
            return original.replaceText(TextReplacementConfig.builder().match(pSearcher)
                    .replacement((matchResult, builder) -> Component.text(ChatColor.GRAY + builder.content() + ChatColor.RESET))
                    .build());
        }

        // Neither
        return original;

    }


}