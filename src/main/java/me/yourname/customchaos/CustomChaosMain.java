package me.yourname.customchaos;

import me.yourname.customchaos.commands.*;
import me.yourname.customchaos.menu.ChaosMenu;
import me.yourname.customchaos.menu.ChaosMenuListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomChaosMain extends JavaPlugin {

    private CommandRouter commandRouter;
    private FreezeManager freezeManager;
    private ChaosMenu chaosMenu;

    @Override
    public void onEnable() {
        commandRouter = new CommandRouter();
        freezeManager = new FreezeManager(this);
        chaosMenu = new ChaosMenu(commandRouter);

        getServer().getPluginManager().registerEvents(new FreezeListener(freezeManager), this);
        getServer().getPluginManager().registerEvents(new ChaosMenuListener(chaosMenu), this);

        registerCommands();

        PluginCommand chaosCommand = getCommand("chaos");
        if (chaosCommand != null) {
            chaosCommand.setExecutor(commandRouter);
            chaosCommand.setTabCompleter(commandRouter);
        } else {
            getLogger().severe("Failed to register /chaos command - check plugin.yml!");
        }

        getLogger().info("CustomChaos enabled with " + commandRouter.getSubCommands().size() + " subcommands.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomChaos disabled.");
    }

    /**
     * Registers every SubCommand implementation with the router.
     * This is the "skeleton dictionary" — add one line per new class
     * as you build out the rest of the 200-command list.
     */
    private void registerCommands() {
        // --- Admin utility (safe console-dispatch pattern, no OP mutation) ---
        commandRouter.register(new SudoCommand());

        // --- Category 2: Physics Distortion & Chaos ---
        commandRouter.register(new YeetCommand());
        commandRouter.register(new SlapCommand());
        commandRouter.register(new EarthquakeCommand());

        // --- Category 3: Magical Abilities & Superpowers ---
        commandRouter.register(new FirebenderCommand());
        commandRouter.register(new TimestopCommand());
        commandRouter.register(new FlightCommand());

        // --- Category 4: Entity Manipulation & Mob Spawning ---
        commandRouter.register(new SwarmCommand());

        // --- Category 6: World Editing & Environment Control ---
        commandRouter.register(new NightvisionCommand());
        commandRouter.register(new BloodmoonCommand());

        // --- Category 7: Chat, Visuals, & Cosmetics ---
        commandRouter.register(new BroadcastCommand());
        commandRouter.register(new GlowingCommand());
        commandRouter.register(new ConfettiCommand());
        commandRouter.register(new HealanimationCommand());

        // --- Category 8: Minigames & System Tools ---
        commandRouter.register(new CoinflipCommand());
        commandRouter.register(new RollCommand());
        commandRouter.register(new RpsCommand());
        commandRouter.register(new TpsCommand());
        commandRouter.register(new PingCommand());
        commandRouter.register(new RandomTeleportCommand());
        commandRouter.register(new KeyboardDisconnectCommand(freezeManager));
        commandRouter.register(new MenuCommand(chaosMenu));

        // TODO: Register the remaining ~180 subcommands here, one line each,
        // following the exact same "new XyzCommand()" pattern as above.
    }
}
