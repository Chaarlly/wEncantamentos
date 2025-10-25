package com.github.charlly.wEcantamentos.listeners;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.repository.XPRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final XPRepository repository = new XPRepository();
    private final Main main;

    public PlayerJoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!repository.hasPlayerData(p.getUniqueId())) {
            int levelInicial = main.getConfiguracao().getInt("Encantamentos.level-inicial");
            p.setLevel(levelInicial);
            p.setExp(0.0f);
            repository.createPlayerData(p.getUniqueId(), levelInicial);
        } else {
            int levelSalvo = repository.getPlayerLevel(p.getUniqueId());
            float progressSalvo = repository.getPlayerProgress(p.getUniqueId());
            p.setLevel(levelSalvo);
            p.setExp(progressSalvo);
        }
    }
}