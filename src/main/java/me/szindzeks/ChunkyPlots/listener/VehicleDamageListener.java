package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class VehicleDamageListener implements Listener {
    @EventHandler
    public void onVehicleDamage(final VehicleDamageEvent event){
        final Entity attacker = event.getAttacker();

        final Location vehicleLocation = event.getVehicle().getLocation();
        final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(vehicleLocation.getChunk());

        if(attacker instanceof Player) {
            Player player = (Player) attacker;
            if(eventPlot != null) {
                if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.VEHICLE_DAMAGE_MEMBER, Flag.VEHICLE_DAMAGE_STRANGER)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
