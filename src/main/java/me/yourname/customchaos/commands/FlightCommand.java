package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class FlightCommand implements SubCommand {

    @Override
    public String getName() {
        return "flight";
    }

    @Override
    public String getDescription() {
        return "Toggles the ability to fly for yourself.";
    }

    @Override
    public String getSyntax() {
        return "/chaos flight";
    }

    @Override
    public void perform(Player player, String[] args) {
        boolean newState = !player.getAllowFlight();
        player.setAllowFlight(newState);

        if (newState) {
            player.sendMessage(Component.text("Flight enabled!", NamedTextColor.AQUA));
        } else {
            player.setFlying(false);
            player.sendMessage(Component.text("Flight disabled.", NamedTextColor.GRAY));
        }
    }
}
