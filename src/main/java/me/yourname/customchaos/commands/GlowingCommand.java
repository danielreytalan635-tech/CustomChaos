package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GlowingCommand implements SubCommand {

    @Override
    public String getName() {
        return "glowing";
    }

    @Override
    public String getDescription() {
        return "Toggles a glowing outline on a target player.";
    }

    @Override
    public String getSyntax() {
        return "/chaos glowing <player>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Component.text("Player '" + args[1] + "' is not online.", NamedTextColor.RED));
            return;
        }

        boolean newState = !target.isGlowing();
        target.setGlowing(newState);

        target.sendMessage(Component.text(newState ? "You are now glowing!" : "You stopped glowing.", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("Toggled glowing for " + target.getName() + ".", NamedTextColor.GREEN));
    }
}
