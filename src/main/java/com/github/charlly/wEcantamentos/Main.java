package com.github.charlly.wEcantamentos;

import com.github.charlly.wEcantamentos.commands.ComandoFeiticeiro;
import com.github.charlly.wEcantamentos.config.api.W_Config;
import com.github.charlly.wEcantamentos.config.conexao.Conexao;
import com.github.charlly.wEcantamentos.listeners.*;
import com.github.charlly.wEcantamentos.config.task.AutoSaveTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main plugin;

    private W_Config configuracao;
    private W_Config mensagens;
    private W_Config encantamentos;

    @Override
    public void onEnable() {
        plugin = this;

        configuracao = new W_Config("configuracao.yml");
        mensagens = new W_Config("mensagens.yml");
        encantamentos = new W_Config("encantamentos.yml");

        configuracao.saveDefaultConfig();
        mensagens.saveDefaultConfig();
        encantamentos.saveDefaultConfig();

        loadConexao();
        loadListeners();
        loadCommands();
        startAutoSaveTask();
    }

    @Override
    public void onDisable() {
        Conexao.close();
        HandlerList.unregisterAll(this);
    }

    private void loadCommands() {
        getCommand("feiticeiro").setExecutor(new ComandoFeiticeiro(this));
    }

    private void loadListeners() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new FeiticeiroMenuListener(this), this);
        pm.registerEvents(new XPBottleListener(this), this);
        pm.registerEvents(new BookListener(this), this);
        pm.registerEvents(new EnchantApplyListener(this),this);
        pm.registerEvents(new PlayerDeathListener(this), this);
    }

    private void loadConexao() {
        if (configuracao.getBoolean("database.mysql.enable")) {
            Conexao.open();
            Conexao.createTables();
        }
    }

    private void startAutoSaveTask() {
        long delay = 20L * 60 * this.getConfiguracao().getInt("AutoSave.primeiro");
        long period = 20L * 60 * this.getConfiguracao().getInt("AutoSave.periodico");
        new AutoSaveTask().runTaskTimerAsynchronously(this, delay, period);
    }

    public W_Config getEncantamentosConfig() {
        return encantamentos;
    }
}