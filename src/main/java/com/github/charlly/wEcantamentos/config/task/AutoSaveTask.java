package com.github.charlly.wEcantamentos.config.task;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.config.conexao.Connections;
import com.github.charlly.wEcantamentos.repository.XPRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    private final XPRepository repository = new XPRepository();

    @Override
    public void run() {
        if (Connections.con == null) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                int levelAtual = player.getLevel();
                float progressAtual = player.getExp();
                repository.updatePlayerData(player.getUniqueId(), levelAtual, progressAtual);
            }
        }

        if (Main.getPlugin().getConfiguration().getBoolean("AutoSave.avisar-console")) {
            Bukkit.getLogger().info("[wEcantamentos] Níveis de XP salvos (Autosave).");
        }

    }
}