package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class VehicleDamageListener implements Listener {
    @EventHandler
    public void onVehicleDamage(final VehicleDamageEvent event){
        final Location vehicleLocation = event.getVehicle().getLocation();
        final Entity attacker = event.getAttacker();
        final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(vehicleLocation.getChunk());

        if(attacker instanceof Player) {
            Player player = (Player) attacker;
            if(ChunkyPlots.plugin.userManager.getUser(player.getName()).isBypassingRestrictions == true) return;
            else if (ChunkyPlots.plugin.userManager.getUser(player.getName()).cooldown == true) event.setCancelled(true);
            else if (eventPlot != null) {
                if (eventPlot.getOwnerNickname().equals(player.getName())) return;
                else if (eventPlot.getFlags().get(Flag.VEHICLE_DAMAGE_MEMBER) == true && eventPlot.members.contains(player.getName()))
                    return;
                else if (eventPlot.getFlags().get(Flag.VEHICLE_DAMAGE_STRANGER) == true && !eventPlot.members.contains(player.getName()))
                    return;
                else {
                    String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
                    player.sendMessage(message);
                    event.setCancelled(true);
                }
            }
        }
    }
}
