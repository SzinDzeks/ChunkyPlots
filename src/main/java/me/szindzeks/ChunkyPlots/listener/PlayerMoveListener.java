package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();

        if (from != null || to != null || from.distance(to) != 0){
            final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
            final UserManager userManager = ChunkyPlots.plugin.userManager;
            final ConfigManager configManager = ChunkyPlots.plugin.configManager;

            final Player player = event.getPlayer();
            final User user = userManager.getUser(player.getName());

            final Plot previousPlot = plotManager.getPlot(from.getChunk());
            final Plot newPlot = plotManager.getPlot(to.getChunk());

            if(user != null) {
                if(newPlot != null && newPlot.blacklist.contains(user.getNickname())) event.setCancelled(true);
                if (user.hasEntered == false) {
                    if(newPlot != null) {
                        if(!newPlot.blacklist.contains(player.getName())) {
                            sendEnterMessages(player, configManager, newPlot);
                            user.hasEntered = true;
                        } else event.setCancelled(true);
                    }
                } else if(user.hasEntered == true){
                    if(previousPlot != null) {
                        if (newPlot != null) {
                            if (!newPlot.equals(previousPlot)) {
                                if (!newPlot.getOwnerNickname().equals(previousPlot.getOwnerNickname())) {
                                    sendEnterMessages(player, configManager, newPlot);
                                    user.hasEntered = true;
                                }
                            }
                        } else {
                            sendLeaveMessages(player, configManager, previousPlot);
                            user.hasEntered = false;
                        }
                    }
                }
            }
        }
    }

    private void sendEnterMessages(Player player, ConfigManager configManager, Plot newPlot){
        player.sendTitle("  ", configManager.getMessages().get(MessageType.ENTERED_PLOT).replace("%plotOwnerName%", newPlot.getOwnerNickname()), 6, 20 * 3, 6);
        player.sendMessage(configManager.getMessages().get(MessageType.ENTERED_PLOT).replace("%plotOwnerName%", newPlot.getOwnerNickname()));
    }
    private void sendLeaveMessages(Player player, ConfigManager configManager, Plot previousPlot){
        player.sendTitle("  ", configManager.getMessages().get(MessageType.LEFT_PLOT).replace("%plotOwnerName%", previousPlot.getOwnerNickname()), 6, 20 * 3, 6);
        player.sendMessage(configManager.getMessages().get(MessageType.LEFT_PLOT).replace("%plotOwnerName%", previousPlot.getOwnerNickname()));
    }
}
