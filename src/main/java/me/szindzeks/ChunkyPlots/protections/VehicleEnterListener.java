package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class VehicleEnterListener implements Listener {
    @EventHandler
    public void onVehicleEnter(final VehicleEnterEvent event) {
        final Entity entered = event.getEntered();

        final Location vehicleLocation = event.getVehicle().getLocation();
        final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(vehicleLocation.getChunk());

        if(entered instanceof Player player) {
            if(eventPlot != null) {
                if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.VEHICLE_ENTER_MEMBER, Flag.VEHICLE_ENTER_STRANGER)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
