package com.bnjrKemal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rank {

    private String name;
    private String lore;
    private List<Gorev> missions;

    public Rank(String name, String lore) {
        this.name = name;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public List<Gorev> getMissions() {
        return missions;
    }

    public void loadMissions(ConfigurationSection missionsSection) {
        // Ranks config dosyasından görevleri yükleme
        missions = new ArrayList<>(); // Bu satırı ekleyin

        for (String missionType : missionsSection.getKeys(false)) {
            MissionType type = MissionType.valueOf(missionType);
            int goal = missionsSection.getInt(missionType);

            missions.add(new Gorev(type, goal));
        }
        System.out.println(missions);
    }

    public ConfigurationSection saveMissions() {
        // Ranks config dosyasına görevleri kaydetme
        ConfigurationSection missionsSection = new YamlConfiguration();
        for (Gorev mission : missions) {
            missionsSection.set(mission.getType().toString(), mission.getGoal());
        }
        return missionsSection;
    }

    public int getMissionGoal(MissionType type) {
        for (Gorev mission : missions) {
            if (mission.getType() == type) {
                return mission.getGoal();
            }
        }
        return 0; // Belirli bir görev türü için hedef bulunamadı
    }

    public boolean areMissionsComplete(Map<MissionType, Integer> completedMissions) {
        for (Gorev mission : missions) {
            int completed = completedMissions.getOrDefault(mission.getType(), 0);
            if (completed < mission.getGoal()) {
                return false;
            }
        }
        return true;
    }
}

