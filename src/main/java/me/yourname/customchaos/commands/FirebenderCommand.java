package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FirebenderCommand implements SubCommand {

    private static final double RANGE = 15.0;

    @Override
    public String getName() {
        return "firebender";
    }

    @Override
    public String getDescription() {
        return "Shoots a stream of fire in the direction you are looking.";
    }

    @Override
    public String getSyntax() {
        return "/chaos firebender";
    }

    @Override
    public void perform(Player player, String[] args) {
        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection().normalize();

        for (double distance = 1; distance <= RANGE; distance += 1) {
            Location point = eye.clone().add(direction.clone().multiply(distance));
            point.getWorld().spawnParticle(Particle.FLAME, point, 6, 0.1, 0.1, 0.1, 0.01);
        }

        player.getWorld().playSound(eye, Sound.ITEM_FIRECHARGE_USE, 1.0f, 0.8f);

        Entity targetEntity = player.getTargetEntity((int) RANGE);
        if (targetEntity instanceof LivingEntity livingEntity) {
            livingEntity.setFireTicks(60);
            if (livingEntity instanceof Player targetPlayer) {
                targetPlayer.sendMessage(Component.text(player.getName() + " hit you with a fire blast!", NamedTextColor.RED));
            }
        }
    }
}
