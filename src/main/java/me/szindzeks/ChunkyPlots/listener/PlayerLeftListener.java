package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeftListener implements Listener {
    @EventHandler
    public void onPlayerLeft(final PlayerQuitEvent event){
        final Player player = event.getPlayer();

        final UserManager userManager = ChunkyPlots.plugin.userManager;
        User user = userManager.getUser(player.getName());
        userManager.saveUser(user);
    }
}
