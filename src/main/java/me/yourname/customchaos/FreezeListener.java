package me.yourname.customchaos;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Enforces freezes tracked by FreezeManager. Players can still look around
 * (yaw/pitch), open their own inventory, and chat as normal - only
 * positional movement is blocked, and only for the duration set by
 * "/chaos keyboarddisconnect".
 */
public class FreezeListener implements Listener {

    private final FreezeManager freezeManager;

    public FreezeListener(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!freezeManager.isFrozen(player.getUniqueId())) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        boolean positionChanged = from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
        if (!positionChanged) {
            return;
        }

        Location anchor = freezeManager.getFrozenLocation(player.getUniqueId());
        if (anchor == null) {
            event.setCancelled(true);
            return;
        }

        event.setTo(new Location(to.getWorld(), anchor.getX(), anchor.getY(), anchor.getZ(), to.getYaw(), to.getPitch()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        freezeManager.unfreeze(event.getPlayer().getUniqueId());
    }
}
