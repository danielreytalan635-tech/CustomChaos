package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BloodmoonCommand implements SubCommand {

    @Override
    public String getName() {
        return "bloodmoon";
    }

    @Override
    public String getDescription() {
        return "Turns the current world into a stormy, ominous night.";
    }

    @Override
    public String getSyntax() {
        return "/chaos bloodmoon";
    }

    @Override
    public void perform(Player player, String[] args) {
        World world = player.getWorld();
        world.setTime(18000L);
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(6000);

        world.getPlayers().forEach(p ->
            p.sendMessage(Component.text("A blood moon rises over " + world.getName() + "...", NamedTextColor.DARK_RED))
        );
    }
}
