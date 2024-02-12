package com.bnjrKemal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MissionCommand implements CommandExecutor {

    MissionManager missionManager;

    public MissionCommand(MissionManager missionManager) {
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
            // /mission komutuyla GUI'yi aç
            openMissionGUI(player);
        } else {
            // Diğer komutları işleme ekle (örneğin /mission reload)
            // Örneğin: /mission reload - ranks.yml dosyasını yeniden yükle
            if (args[0].equalsIgnoreCase("reload")) {
                missionManager.loadRankConfig();
                missionManager.loadRanks();
                player.sendMessage("Ranks reloaded.");
            }
            // İhtiyaca göre diğer komutları ekleyebilirsiniz
        }

        return true;
    }

    private void openMissionGUI(Player player) {
        PlayerData playerData = missionManager.getPlayerData(player);
        if (playerData == null) {
            // Handle the case where PlayerData is not found for the player
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 9, "Mission Ranks");

        List<Rank> ranks = missionManager.getRanks();
        for (Rank rank : ranks) {
            ItemStack rankItem = new ItemStack(Material.DIAMOND); // İstediğiniz bir malzeme seçebilirsiniz
            ItemMeta meta = rankItem.getItemMeta();
            meta.setDisplayName(rank.getName());

            Gorev currentGorev = playerData.getCurrentGorev();
            if (currentGorev != null) {
                int currentProgress = currentGorev.getProgress();
                int goal = currentGorev.getGoal();

                String missionString = currentGorev.getType().toString() + ": " + currentProgress + "/" + goal;
                meta.getLore().add(missionString);
            }

            rankItem.setItemMeta(meta);

            gui.addItem(rankItem);
        }

        player.openInventory(gui);
    }
}
