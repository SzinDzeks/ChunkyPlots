package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
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

        if(eventPlot != null) {
            if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.ENTITY_SHEAR_MEMBER, Flag.ENTITY_SHEAR_STRANGER)) {
                event.setCancelled(true);
            }
        }
    }
}
