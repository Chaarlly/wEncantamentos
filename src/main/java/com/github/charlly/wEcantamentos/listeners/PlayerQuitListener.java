package com.github.charlly.wEcantamentos.listeners;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.repository.XPRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final Main main;

    public PlayerQuitListener(Main main) {
        this.main = main;
    }

    private final XPRepository repository = new XPRepository();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        final UUID uuid = p.getUniqueId();
        final int levelAtual = p.getLevel();
        final float progressAtual = p.getExp();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            repository.updatePlayerData(uuid, levelAtual, progressAtual);
        });
    }
}