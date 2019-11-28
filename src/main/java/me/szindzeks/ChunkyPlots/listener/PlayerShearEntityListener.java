package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class PlayerShearEntityListener implements Listener {
    @EventHandler
    public void onPlayerShearEntity(final PlayerShearEntityEvent event){
        final Location entityLocation = event.getEntity().getLocation();
        final Player player = event.getPlayer();
        final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(entityLocation.getChunk());

        if(ChunkyPlots.plugin.userManager.getUser(player.getName()).isBypassingRestrictons == true) return;
        else if(ChunkyPlots.plugin.userManager.getUser(player.getName()).cooldown == true) event.setCancelled(true);
        else if(eventPlot != null) {
            if(eventPlot.getOwnerNickname().equals(player.getName())) return;
            else if(eventPlot.getFlags().get(Flag.ENTITY_SHEAR_MEMBER) == true && eventPlot.members.contains(player.getName()))
                return;
            else if(eventPlot.getFlags().get(Flag.ENTITY_SHEAR_STRANGER) == true && !eventPlot.members.contains(player.getName()))
                return;
            else {
                String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
                player.sendMessage(message);
                event.setCancelled(true);
            }
        }
    }
}
