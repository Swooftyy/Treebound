package git.swofty.treebound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Treebound extends JavaPlugin implements Listener {

    // Variable for Leafs
    Material leafOak = Material.OAK_LEAVES;
    Material leafBirch = Material.BIRCH_LEAVES;
    Material leafDark = Material.DARK_OAK_LEAVES;
    Material leafAcacia = Material.ACACIA_LEAVES;
    Material leafJungle = Material.JUNGLE_LEAVES;

    // ArrayList for running players
    ArrayList<Player> runningPlayers;

    // ArrayList for chasing players
    ArrayList<Player> chasingPlayers;

    // Boolean for gameStart
    // Note: without a serialization system this will only allow one game to be running at once
    Boolean gameStarted = false;

    @Override
    public void onEnable() {
        // Code to run on plugin enable
        Bukkit.getLogger().info("Enabled Treebound - v1 by Swofty");
    }

    @Override
    public void onDisable() {
        // Code to run on plugin disable
        Bukkit.getLogger().info("Disabled Treebound - v1 by Swofty");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Define player as the sender using Player cast, will output error if command is ran by console
        Player player = (Player) sender;

        // Get which command was run
        if (command.getLabel() == "treebound") {

            // Checks if there are no arguments
            if (args.length == 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d====[ TreeBound ]===="));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound chaser add <player>"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound chaser remove <player>"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound runner add <player>"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound runner remove <player>"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound start"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound stop"));
            }
                    // Checks what first arguments are
                    switch (args[0]) {
                        case "chaser":

                            // Send chaser help if 3nd argument isn't provided or if the 3rd argument isn't add or remove
                            if (args.length <= 2 || !args[1].equalsIgnoreCase("add") || !args[1].equals("remove")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d====[ TreeBound ]===="));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound chaser add <player>"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound chaser remove <player>"));
                                return true;
                            }

                            // Checks if game has started, if so return
                            if (gameStarted.equals(true)) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fA game is already in progress, please wait for this to finish before setting roles"));
                                return true;
                            }

                            // Checks if 3rd agument is a player, if not sends error message and returns
                            if (Bukkit.getPlayer(args[2]) != null) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fCould not find player '" + args[2] + "'"));
                                return true;
                            }

                            // If the program gets to this point that means that all arguments are correct and the plugin has been able to locate the player
                            // Set 3rd argument to a player variable for ease-of-use
                            Player chaserTarget = Bukkit.getPlayer(args[2]);

                            // Checks if we're removing or adding the player


                            // Checks if player is a chaser
                            if (chasingPlayers.contains(chaserTarget)) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fThis player is already a chaser"));
                                return true;
                            }

                            // Checks if target is a runner
                            if (runningPlayers.contains(chaserTarget)) {
                                runningPlayers.remove(chaserTarget);
                            }

                            // Finally all checks are done and target can become a chaser
                            runningPlayers.add(chaserTarget);

                            // Send player and target a message of success
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fSuccessfully set '" +chaserTarget.getDisplayName() + "' to chaser"));
                            chaserTarget.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fYou have been set to chaser"));
                            break;

                        case "runner":

                            // Send runner help if 3nd argument isn't provided or if the 3rd argument isn't add or remove
                            if (args.length <= 2 || !args[1].equalsIgnoreCase("add") || !args[1].equals("remove")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d====[ TreeBound ]===="));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound runner add <player>"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "/treebound runner remove <player>"));
                                return true;
                            }

                            // Checks if game has started, if so return
                            if (gameStarted.equals(true)) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fA game is already in progress, please wait for this to finish before setting roles"));
                                return true;
                            }

                            // Checks if 3rd agument is a player, if not sends error message and returns
                            if (Bukkit.getPlayer(args[2]) != null) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fCould not find player '" + args[2] + "'"));
                                return true;
                            }

                            // If the program gets to this point that means that all arguments are correct and the plugin has been able to locate the player
                            // Set 3rd argument to a player variable for ease-of-use
                            Player runnerTarget = Bukkit.getPlayer(args[2]);

                            // Checks if we're removing or adding the player
                            if (args[1].equalsIgnoreCase("add")) {

                                // Checks if player is a chaser
                                if (runningPlayers.contains(runnerTarget)) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fThis player is already a runner"));
                                    return true;
                                }

                                // Checks if target is a runner
                                if (chasingPlayers.contains(runnerTarget)) {
                                    chasingPlayers.remove(runnerTarget);
                                }

                                // Removes previous runner as minigame only allows one runner
                                if (runningPlayers.size() == 1) {
                                    Player clearedPlayer = runningPlayers.get(1);
                                    clearedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fYou have been removed from being a runner"));
                                    runningPlayers.clear();
                                }

                                // Finally all checks are done and target can become a chaser
                                runningPlayers.add(runnerTarget);

                                // Send player and target a message of success
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fSuccessfully set '" + runnerTarget.getDisplayName() + "' to runner"));
                                runnerTarget.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fYou have been set to runner"));
                            } else {

                                // Checks if player is not a chaser
                                if (!runningPlayers.contains(runnerTarget)) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fThis player is already not a runner"));
                                    return true;
                                }


                                // Finally all checks are done and target can become a chaser
                                runningPlayers.remove(runnerTarget);

                                // Send player and target a message of success
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fSuccessfully removed '" + runnerTarget.getDisplayName() + "' from runner"));
                                runnerTarget.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fYou have been removed from runner"));
                            }
                            break;

                        case "start":

                            // Checks if game is already started
                            if(gameStarted.equals(true)) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fGame has already started"));
                                return true;
                            }

                            // Makes sure there is 1 chaser and 1 runner
                            if (!(chasingPlayers.size() >= 1) || !(runningPlayers.size() >= 1)) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fThere must be atleast 1 runner / chaser to start the game"));
                                return true;
                            }

                            // Starts game
                            gameStarted = true;

                            // Announces game start to all players of the game
                            for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                                if (runningPlayers.contains(playerOnline) || chasingPlayers.contains(playerOnline)) {
                                    playerOnline.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fGame has started, you can no longer step on anything other then leaves!"));
                                }
                            }

                            // Makes sure respawn point has leaves under to prevent death on respawn
                            Location worldSpawn = Bukkit.getWorld(String.valueOf(player.getWorld())).getSpawnLocation();
                            player.getWorld().getBlockAt(worldSpawn.getBlockX(), worldSpawn.getBlockY() - 1, worldSpawn.getBlockZ()).setType(leafOak);
                            break;

                        case "stop":

                            // Checks if game is already ended
                            if(gameStarted.equals(false)) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fGame has already ended"));
                                return true;
                            }

                            // Stops game
                            gameStarted = false;

                            // Announces game end to all players of the game
                            for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                                if (runningPlayers.contains(playerOnline) || chasingPlayers.contains(playerOnline)) {
                                    playerOnline.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[TreeBound] &fGame has ended, you can now step on whatever you want"));
                                }
                            }
                            break;



                    }

        }
        return super.onCommand(sender, command, label, args);
    }
}
