package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpsCommand implements SubCommand {

    @Override
    public String getName() {
        return "tps";
    }

    @Override
    public String getDescription() {
        return "Shows the server's current ticks-per-second.";
    }

    @Override
    public String getSyntax() {
        return "/chaos tps";
    }

    @Override
    public void perform(Player player, String[] args) {
        double[] tps = Bukkit.getTPS();

        player.sendMessage(
            Component.text("TPS (1m, 5m, 15m): ", NamedTextColor.GRAY)
                .append(Component.text(
                    String.format("%.2f, %.2f, %.2f", tps[0], tps[1], tps[2]),
                    NamedTextColor.GREEN
                ))
        );
    }
}
