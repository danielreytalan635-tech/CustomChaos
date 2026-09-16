package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import me.yourname.customchaos.menu.ChaosMenu;
import org.bukkit.entity.Player;

public class MenuCommand implements SubCommand {

    private final ChaosMenu chaosMenu;

    public MenuCommand(ChaosMenu chaosMenu) {
        this.chaosMenu = chaosMenu;
    }

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "Opens the Chaos Control Menu GUI.";
    }

    @Override
    public String getSyntax() {
        return "/chaos menu";
    }

    @Override
    public void perform(Player player, String[] args) {
        chaosMenu.openMainMenu(player);
    }
}
