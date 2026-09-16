package me.yourname.customchaos;

import org.bukkit.entity.Player;

/**
 * Contract for every "/chaos <name>" subcommand registered with the CommandRouter.
 */
public interface SubCommand {

    /**
     * The literal name typed after "/chaos", e.g. "yeet" for "/chaos yeet".
     */
    String getName();

    /**
     * A short human-readable description shown in "/chaos" help output.
     */
    String getDescription();

    /**
     * The usage string shown to players, e.g. "/chaos yeet [player]".
     */
    String getSyntax();

    /**
     * Executes the subcommand.
     *
     * @param player the player who ran the command
     * @param args   the full argument array, where args[0] is this subcommand's name
     */
    void perform(Player player, String[] args);
}
