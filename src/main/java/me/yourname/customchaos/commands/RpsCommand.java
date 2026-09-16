package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class RpsCommand implements SubCommand {

    private static final List<String> CHOICES = List.of("rock", "paper", "scissors");
    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return "rps";
    }

    @Override
    public String getDescription() {
        return "Play rock, paper, scissors against the server.";
    }

    @Override
    public String getSyntax() {
        return "/chaos rps <rock|paper|scissors>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2 || !CHOICES.contains(args[1].toLowerCase())) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        String playerChoice = args[1].toLowerCase();
        String serverChoice = CHOICES.get(RANDOM.nextInt(CHOICES.size()));

        String outcome;
        if (playerChoice.equals(serverChoice)) {
            outcome = "It's a tie!";
        } else if (
            (playerChoice.equals("rock") && serverChoice.equals("scissors")) ||
            (playerChoice.equals("paper") && serverChoice.equals("rock")) ||
            (playerChoice.equals("scissors") && serverChoice.equals("paper"))
        ) {
            outcome = "You win!";
        } else {
            outcome = "You lose!";
        }

        player.sendMessage(Component.text(
            "You chose " + playerChoice + ", the server chose " + serverChoice + ". " + outcome,
            NamedTextColor.YELLOW
        ));
    }
}
