package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event){
        final Location location = event.getEntity().getLocation();
        Entity attacker = event.getDamager();
        final Entity victim = event.getEntity();

        if(attacker instanceof Projectile){
            ProjectileSource ps = ((Projectile) attacker).getShooter();
            if(ps instanceof LivingEntity) attacker = (LivingEntity) ps;
        } else if(attacker instanceof Trident){

        }

        Plot plot = ChunkyPlots.plugin.plotManager.getPlotByChunk(victim.getLocation().getChunk());
        if(plot != null)
            if (attacker instanceof Player) {
                Player player = (Player) attacker;
                final User user = ChunkyPlots.plugin.userManager.getUser(player.getName());
                if(user.isBypassingRestrictons == true) return;
                else if(user.cooldown == true) event.setCancelled(true);
                else if (victim instanceof Player) {
                    if (plot.getFlags().get(Flag.PVP) == false) {
                            String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
                            player.sendMessage(message);
                            event.setCancelled(true);
                    }
                } else if (victim instanceof Monster) {
                    if (plot.getFlags().get(Flag.PVE) == false) {
                        if(!plot.members.contains(player.getName()) || !plot.getOwnerNickname().equals(player.getName())){
                            String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
                            player.sendMessage(message);
                            event.setCancelled(true);
                        }
                    }
                } else if (!plot.getOwnerNickname().equals(player.getName())) {
                    if (plot.getFlags().get(Flag.ENTITY_DAMAGE_MEMBER) == true && plot.members.contains(player.getName())) return;
                    else if (plot.getFlags().get(Flag.ENTITY_DAMAGE_STRANGER) == true && !plot.members.contains(player.getName())) return;
                    else {
                        String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
                        player.sendMessage(message);
                        event.setCancelled(true);
                    }
                }
            }
            else if(plot.getFlags().get(Flag.PVE) == false) event.setCancelled(true);
            else if(attacker instanceof TNTPrimed){
                TNTPrimed tnt = ((TNTPrimed) attacker);
                if(tnt.getSource() instanceof Player){
                    Player player = (Player)tnt.getSource();
                    if(!player.getName().equals(plot.getOwnerNickname())) {
                        if (plot.getFlags().get(Flag.ENTITY_DAMAGE_MEMBER) == true && plot.members.contains(player.getName())) return;
                        else if (plot.getFlags().get(Flag.ENTITY_DAMAGE_STRANGER) == true && !plot.members.contains(player.getName())) return;
                        else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
    }
}
