package me.yourname.customchaos;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tracks players who are temporarily frozen in place (used by
 * "/chaos keyboarddisconnect"). Every freeze is timer-driven and always
 * expires on its own; an admin can also end one early. There is no
 * persistent or player-inescapable lock state - unfreeze always fires,
 * either via the countdown reaching zero, an explicit "stop", or the
 * player disconnecting.
 */
public class FreezeManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Location> frozenLocations = new HashMap<>();
    private final Map<UUID, BukkitTask> countdownTasks = new HashMap<>();

    public FreezeManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isFrozen(UUID playerId) {
        return frozenLocations.containsKey(playerId);
    }

    public Location getFrozenLocation(UUID playerId) {
        return frozenLocations.get(playerId);
    }

    /**
     * Freezes a player in place for the given number of seconds, showing an
     * actionbar countdown. Automatically unfreezes when the timer runs out.
     */
    public void freeze(Player target, int seconds) {
        UUID id = target.getUniqueId();

        // Cancel any existing freeze on this player first so timers don't stack.
        cancelExistingTask(id);

        frozenLocations.put(id, target.getLocation());

        int[] remaining = {seconds};

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player online = Bukkit.getPlayer(id);
            if (online == null || !online.isOnline()) {
                unfreeze(id);
                return;
            }

            if (remaining[0] <= 0) {
                unfreeze(id);
                return;
            }

            online.sendActionBar(
                Component.text("Keyboard disconnected... ", NamedTextColor.RED)
                    .append(Component.text(remaining[0] + "s", NamedTextColor.YELLOW))
            );
            remaining[0]--;
        }, 0L, 20L);

        countdownTasks.put(id, task);
    }

    /**
     * Ends a freeze - used by the admin "stop" override, on player quit,
     * and internally once the countdown reaches zero.
     */
    public void unfreeze(UUID playerId) {
        boolean wasFrozen = frozenLocations.remove(playerId) != null;
        cancelExistingTask(playerId);

        if (!wasFrozen) {
            return;
        }

        Player online = Bukkit.getPlayer(playerId);
        if (online != null && online.isOnline()) {
            online.sendActionBar(Component.text("Your keyboard reconnects!", NamedTextColor.GREEN));
        }
    }

    private void cancelExistingTask(UUID playerId) {
        BukkitTask existing = countdownTasks.remove(playerId);
        if (existing != null) {
            existing.cancel();
        }
    }
}
