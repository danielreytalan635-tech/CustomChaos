package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Random;

public class CoinflipCommand implements SubCommand {

    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public String getDescription() {
        return "Flips a coin: heads or tails.";
    }

    @Override
    public String getSyntax() {
        return "/chaos coinflip";
    }

    @Override
    public void perform(Player player, String[] args) {
        String result = RANDOM.nextBoolean() ? "Heads" : "Tails";
        player.sendMessage(Component.text("The coin lands on: ", NamedTextColor.GRAY)
            .append(Component.text(result, NamedTextColor.GOLD)));
    }
}
