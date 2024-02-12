package com.bnjrKemal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    private MissionManager missionManager;

    public RankCommand(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komut sadece oyuncular tarafından kullanılabilir.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // /rank komutuyla mevcut rank'ı ve görev ilerlemelerini göster
            displayRankInfo(player);
        } else {
            // İhtiyaca göre diğer komutları ekleyebilirsiniz
        }

        return true;
    }

    private void displayRankInfo(Player player) {
        PlayerData playerData = missionManager.getPlayerData(player);
        Rank currentRank = playerData.getCurrentRank();

        if (currentRank != null) {
            player.sendMessage("Current Rank: " + currentRank.getName());

            for (MissionType type : MissionType.values()) {
                int currentProgress = playerData.getCurrentMissionProgress(type);
                int goal = playerData.getCurrentMissionGoal(type);

                player.sendMessage(type.toString() + ": " + currentProgress + "/" + goal);
            }

            player.sendMessage("Rank Missions:");

            for (MissionType type : MissionType.values()) {
                int goal = currentRank.getMissionGoal(type);
                int currentProgress = playerData.getCurrentMissionProgress(type);

                player.sendMessage(type.toString() + ": " + currentProgress + "/" + goal);
            }
        } else {
            player.sendMessage("You don't have a rank yet. Complete missions to earn ranks.");
        }
    }
}
