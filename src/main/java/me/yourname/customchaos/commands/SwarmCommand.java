package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SwarmCommand implements SubCommand {

    private static final int BEE_COUNT = 6;

    @Override
    public String getName() {
        return "swarm";
    }

    @Override
    public String getDescription() {
        return "Spawns a swarm of bees around a target player.";
    }

    @Override
    public String getSyntax() {
        return "/chaos swarm [player]";
    }

    @Override
    public void perform(Player player, String[] args) {
        Player target = player;

        if (args.length >= 2) {
            Player found = Bukkit.getPlayerExact(args[1]);
            if (found == null) {
                player.sendMessage(Component.text("Player '" + args[1] + "' is not online.", NamedTextColor.RED));
                return;
            }
            target = found;
        }

        Location center = target.getLocation();

        for (int i = 0; i < BEE_COUNT; i++) {
            double angle = (2 * Math.PI / BEE_COUNT) * i;
            double x = center.getX() + Math.cos(angle) * 2;
            double z = center.getZ() + Math.sin(angle) * 2;
            Location spawnLoc = new Location(center.getWorld(), x, center.getY() + 1, z);
            center.getWorld().spawnEntity(spawnLoc, EntityType.BEE);
        }

        target.sendMessage(Component.text("A swarm of bees appears around you!", NamedTextColor.YELLOW));
        if (!target.equals(player)) {
            player.sendMessage(Component.text("Summoned a bee swarm around " + target.getName() + ".", NamedTextColor.GREEN));
        }
    }
}
