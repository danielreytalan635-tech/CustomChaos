package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FakeLagCommand implements SubCommand {

    private final JavaPlugin plugin;

    public FakeLagCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "fakelag";
    }

    @Override
    public String getDescription() {
        return "Simulates packet loss by rubber-banding the target backwards.";
    }

    @Override
    public String getSyntax() {
        return "/chaos fakelag <player>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Player '" + targetName + "' is not online.", NamedTextColor.RED));
            return;
        }

        player.sendMessage(Component.text("Simulating lag on " + target.getName() + " for 10 seconds.", NamedTextColor.GREEN));
        target.sendMessage(Component.text("Your connection seems unstable...", NamedTextColor.YELLOW));

        // Runs every 2 seconds (40 ticks) for 10 seconds total = 5 executions.
        new BukkitRunnable() {
            int ticksElapsed = 0;
            final int intervalSeconds = 2;
            final int totalSeconds = 10;

            @Override
            public void run() {
                if (!target.isOnline()) {
                    this.cancel();
                    return;
                }

                ticksElapsed += intervalSeconds;

                Location currentLoc = target.getLocation();
                Vector direction = currentLoc.getDirection().normalize();

                // Move the target backwards 2 blocks relative to where they're facing.
                Location rubberBandLoc = currentLoc.clone().subtract(direction.multiply(2));
                rubberBandLoc.setDirection(currentLoc.getDirection());

                // Keep the destination safe: preserve original Y to avoid clipping into terrain.
                rubberBandLoc.setY(currentLoc.getY());

                target.teleport(rubberBandLoc);

                if (ticksElapsed >= totalSeconds) {
                    target.sendMessage(Component.text("Connection stabilized.", NamedTextColor.GREEN));
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 40L, 40L);
    }
}
