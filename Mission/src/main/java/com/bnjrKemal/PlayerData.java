package com.bnjrKemal;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {

    private Gorev currentGorev;
    private Rank currentRank;
    private Rank previousRank;
    private Map<MissionType, Integer> completedMissions;

    public PlayerData() {
        this.currentGorev = null;
        this.currentRank = null;
        this.previousRank = null;
        this.completedMissions = new HashMap<>();
    }

    public int getCurrentMissionProgress(MissionType type) {
        return completedMissions.getOrDefault(type, 0);
    }

    public boolean hasCompletedRank() {
        return currentRank != null && Mission.missionManager.areMissionsComplete(currentRank, completedMissions);
    }

    public int getCurrentMissionGoal(MissionType type) {
        Rank currentRank = getCurrentRank();
        if (currentRank != null) {
            return currentRank.getMissionGoal(type);
        }
        return 0; // or handle the case when there is no current rank
    }

    public Gorev getCurrentGorev() {
        return currentGorev;
    }

    public Rank getCurrentRank() {
        return currentRank;
    }

    public Rank getPreviousRank() {
        return previousRank;
    }

    public void incrementRank() {
        previousRank = currentRank;
        currentRank = Mission.missionManager.getNextRank(currentRank);
    }

    public void progressGorev(Player player, MissionType type, Object data) {
        if (currentGorev == null || currentGorev.getType() != type) {
            // Yeni bir görev başlat
            currentGorev = new Gorev(type, Mission.missionManager.getMissionGoal(currentRank, type));
        }

        currentGorev.incrementProgress();

        if (currentGorev.getProgress() >= currentGorev.getGoal()) {
            Mission.missionManager.progressMission(player, type, data);
            currentGorev = null; // Görev tamamlandı, null olarak ayarla
        }
    }

    // Diğer gerekli metodlar ve işlevler eklenebilir
}
