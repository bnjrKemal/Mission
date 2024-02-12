package com.bnjrKemal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionManager {

    private File rankConfigFile;
    private FileConfiguration rankConfig;
    private Mission plugin;
    private Map<Player, PlayerData> playerDataMap;
    private List<Rank> ranks;

    public MissionManager(Mission plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
        ranks = new ArrayList<>();
        loadRankConfig();
        loadMissionsConfig();
        loadPlayers();
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player);
    }

    public void loadRankConfig() {
        // Ranks config dosyasını yükleme
        rankConfigFile = new File(plugin.getDataFolder(), "ranks.yml");
        rankConfig = YamlConfiguration.loadConfiguration(rankConfigFile);
    }

    public void saveRankConfig() {
        // Ranks config dosyasını kaydetme
        try {
            rankConfig.save(rankConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRanks() {
        if (rankConfig.contains("ranks")) {
            ConfigurationSection ranksSection = rankConfig.getConfigurationSection("ranks");

            for (String rankName : ranksSection.getKeys(false)) {
                ConfigurationSection rankSection = ranksSection.getConfigurationSection(rankName);
                Rank rank = new Rank(rankName, rankSection.getString("lore"));
                rank.loadMissions(rankSection.getConfigurationSection("missions"));

                // Rank'ı bir yerde saklamak için örneğin bir liste veya harita kullanabilirsiniz
            }
        }
    }

    public void saveRanks() {
        // Ranks config dosyasına rank'ları kaydetme
        ConfigurationSection ranksSection = rankConfig.createSection("ranks");

        for (Rank rank : ranks) {
            String rankName = rank.getName();
            ConfigurationSection rankSection = ranksSection.createSection(rankName);
            rankSection.set("lore", rank.getLore());

            // Save missions individually
            ConfigurationSection missionsSection = rankSection.createSection("missions");
            Map<MissionType, Integer> missionData = (Map<MissionType, Integer>) rank.saveMissions();
            for (Map.Entry<MissionType, Integer> entry : missionData.entrySet()) {
                missionsSection.set(entry.getKey().toString(), entry.getValue());
            }
        }

        saveRankConfig();
    }

    public void loadPlayerData(Player player) {
        // Oyuncu verilerini yükleme (config dosyasından okuma veya varsayılan değerlerle başlatma)
        PlayerData playerData = new PlayerData();
        playerDataMap.put(player, playerData);
    }

    public boolean isInMission(Player player) {
        return playerDataMap.containsKey(player);
    }

    public void progressMission(Player player, MissionType type, Object data) {
        PlayerData playerData = playerDataMap.get(player);
        playerData.progressGorev(player, type, data);

        if (playerData.hasCompletedRank()) {
            Rank currentRank = playerData.getCurrentRank();
            player.sendMessage("Congratulations! You have achieved the rank: " + currentRank.getName());

            Rank previousRank = playerData.getPreviousRank();
            playerData.incrementRank();
            Rank newRank = playerData.getCurrentRank();

            if (!previousRank.getName().equals(newRank.getName())) {
                player.sendMessage("Congratulations! You have been promoted to the rank: " + newRank.getName());
            }
        }

        // GUI güncelleme veya diğer ilgili işlemler buraya eklenebilir.
    }

    public int getMissionGoal(Rank rank, MissionType type) {
        for (Gorev mission : rank.getMissions()) {
            if (mission.getType() == type) {
                return mission.getGoal();
            }
        }
        return 0; // Belirli bir görev türü için hedef bulunamadı
    }

    public boolean areMissionsComplete(Rank rank, Map<MissionType, Integer> completedMissions) {
        return rank.areMissionsComplete(completedMissions);
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public Rank getNextRank(Rank currentRank) {
        List<Rank> ranks = getRanks();
        int currentIndex = ranks.indexOf(currentRank);

        if (currentIndex >= 0 && currentIndex < ranks.size() - 1) {
            // Return the next rank if it exists
            return ranks.get(currentIndex + 1);
        }

        // Return null if there is no next rank
        return null;
    }

    public void loadPlayers() {
        File playersFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            plugin.saveResource("players.yml", false);
        }
    }

    public void loadMissionsConfig() {
        File missionsFile = new File(plugin.getDataFolder(), "missions.yml");
        if (!missionsFile.exists()) {
            plugin.saveResource("missions.yml", false);
        }
    }

}
