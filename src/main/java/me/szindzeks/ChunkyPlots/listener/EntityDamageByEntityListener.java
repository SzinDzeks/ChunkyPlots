package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {
    private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event){
        final Entity attacker = event.getDamager();
        final Entity victim = event.getEntity();

        if(!canEntityDamageEntity(attacker, victim)){
            event.setCancelled(true);
        }
    }

    public boolean canEntityDamageEntity(Entity attacker, Entity victim) {
        if(attacker instanceof Player){
            Player player = (Player) attacker;
            return canPlayerDamageEntity(player, victim);
        } else if(attacker instanceof Projectile){
            Projectile projectile = (Projectile) attacker;
            return canProjectileDamageEntity(projectile, victim);
        } else if(attacker instanceof Monster){
            Monster monster = (Monster) attacker;
            return canMonsterDamageEntity(monster, victim);
        } else if(attacker instanceof Firework){
            Firework firework = (Firework) attacker;
            return canFireworkDamageEntity(firework, victim);
        } else if(attacker instanceof TNTPrimed){
            TNTPrimed tntPrimed = (TNTPrimed) attacker;
            return canTntDamageEntity(tntPrimed, victim);
        }
        return false;
    }

    public boolean canPlayerDamageEntity(Player player, Entity victim) {
        Plot victimPlot = plotManager.getPlotByLocation(victim.getLocation());
        if(victim instanceof Player){
            return victimPlot.flags.get(Flag.PVP);
        } else if(victim instanceof Monster){
            return victimPlot.flags.get(Flag.PVE);
        }
        return PlotPermissionUtil.canPlayerAffectPlot(player, victimPlot, Flag.ENTITY_DAMAGE_MEMBER, Flag.ENTITY_DAMAGE_STRANGER);
    }

    public boolean canProjectileDamageEntity(Projectile projectile, Entity victim) {
        ProjectileSource projectileSource = projectile.getShooter();
        if(projectileSource instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) projectileSource;
            return canEntityDamageEntity(livingEntity, victim);
        } else if(projectileSource instanceof BlockProjectileSource){
            Plot victimPlot = plotManager.getPlotByLocation(victim.getLocation());
            BlockProjectileSource blockProjectileSource = (BlockProjectileSource) projectileSource;
            Block block = blockProjectileSource.getBlock();
            return PlotPermissionUtil.canBlockAffectPlot(block, victimPlot);
        }
        return false;
    }

    public boolean canMonsterDamageEntity(Monster monster, Entity victim) {
        Plot victimPlot = plotManager.getPlotByLocation(victim.getLocation());
        if(victim instanceof Player){
            return victimPlot.flags.get(Flag.PVE);
        } else {
            return victimPlot.flags.get(Flag.EVE);
        }
    }

    public boolean canFireworkDamageEntity(Firework firework, Entity victim) {
        //TODO: Handle firework damage
        return false;
    }

    public boolean canTntDamageEntity(TNTPrimed tntPrimed, Entity victim) {
        //TODO: Handle tnt damage
        return false;
    }
}
