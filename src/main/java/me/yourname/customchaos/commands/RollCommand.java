package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Random;

public class RollCommand implements SubCommand {

    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getDescription() {
        return "Rolls a die. Defaults to 6 sides, or specify a custom number.";
    }

    @Override
    public String getSyntax() {
        return "/chaos roll [sides]";
    }

    @Override
    public void perform(Player player, String[] args) {
        int sides = 6;

        if (args.length >= 2) {
            try {
                sides = Integer.parseInt(args[1]);
                if (sides < 2) {
                    player.sendMessage(Component.text("Sides must be 2 or greater.", NamedTextColor.RED));
                    return;
                }
            } catch (NumberFormatException exception) {
                player.sendMessage(Component.text("'" + args[1] + "' is not a valid number.", NamedTextColor.RED));
                return;
            }
        }

        int result = RANDOM.nextInt(sides) + 1;
        player.sendMessage(Component.text("You rolled a " + result + " (out of " + sides + ").", NamedTextColor.YELLOW));
    }
}
