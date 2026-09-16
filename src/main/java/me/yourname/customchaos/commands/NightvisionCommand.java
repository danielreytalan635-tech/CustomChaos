package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightvisionCommand implements SubCommand {

    private static final int DURATION_TICKS = 20 * 60 * 5; // 5 minutes

    @Override
    public String getName() {
        return "nightvision";
    }

    @Override
    public String getDescription() {
        return "Grants yourself night vision for 5 minutes.";
    }

    @Override
    public String getSyntax() {
        return "/chaos nightvision";
    }

    @Override
    public void perform(Player player, String[] args) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, DURATION_TICKS, 0, false, false));
        player.sendMessage(Component.text("You can now see in the dark.", NamedTextColor.AQUA));
    }
}
