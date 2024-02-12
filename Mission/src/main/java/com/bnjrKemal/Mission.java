package com.bnjrKemal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Mission extends JavaPlugin implements Listener {

    public static MissionManager missionManager;

    @Override
    public void onEnable() {
        missionManager = new MissionManager(this);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("mission").setExecutor(new MissionCommand(missionManager));
        getCommand("rank").setExecutor(new RankCommand(missionManager));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        missionManager.loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (missionManager.isInMission(player)) {
            missionManager.progressMission(player, MissionType.BLOCK_BREAK, event.getBlock().getType());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (missionManager.isInMission(player)) {
            missionManager.progressMission(player, MissionType.BLOCK_PLACE, event.getBlock().getType());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            if (missionManager.isInMission(player)) {
                missionManager.progressMission(player, MissionType.MOB_KILL, event.getEntityType());
            }
        }
    }
}
