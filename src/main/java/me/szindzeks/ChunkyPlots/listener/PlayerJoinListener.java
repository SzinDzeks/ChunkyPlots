package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event){
        final Player player = event.getPlayer();

        final UserManager userManager = ChunkyPlots.plugin.userManager;
        User user = userManager.getUser(player.getName());
        if(user == null) user = new User(event.getPlayer().getName());
        final ChunkyPlots plugin = ChunkyPlots.plugin;
        plugin.userManager.addUser(user);
    }
}
