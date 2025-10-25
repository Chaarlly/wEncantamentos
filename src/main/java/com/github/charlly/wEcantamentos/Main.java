package com.github.charlly.wEcantamentos;

import com.github.charlly.wEcantamentos.commands.SorcererCommand;
import com.github.charlly.wEcantamentos.config.api.W_Config;
import com.github.charlly.wEcantamentos.config.conexao.Connections;
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

    private W_Config configuration;
    private W_Config messages;
    private W_Config enchants;

    @Override
    public void onEnable() {
        plugin = this;

        configuration = new W_Config("configuracao.yml");
        messages = new W_Config("mensagens.yml");
        enchants = new W_Config("encantamentos.yml");

        configuration.saveDefaultConfig();
        messages.saveDefaultConfig();
        enchants.saveDefaultConfig();

        loadConexao();
        loadListeners();
        loadCommands();
        startAutoSaveTask();
    }

    @Override
    public void onDisable() {
        Connections.close();
        HandlerList.unregisterAll(this);
    }

    private void loadCommands() {
        getCommand("feiticeiro").setExecutor(new SorcererCommand(this));
    }

    private void loadListeners() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new SorcererMenuListener(this), this);
        pm.registerEvents(new XPBottleListener(this), this);
        pm.registerEvents(new PlayerDeathListener(this), this);
    }

    private void loadConexao() {
        if (configuration.getBoolean("database.mysql.enable")) {
            Connections.open();
            Connections.createTables();
        }
    }

    private void startAutoSaveTask() {
        long delay = 20L * 60 * this.getConfiguration().getInt("AutoSave.primeiro");
        long period = 20L * 60 * this.getConfiguration().getInt("AutoSave.periodico");
        new AutoSaveTask().runTaskTimerAsynchronously(this, delay, period);
    }

    public W_Config getEncantamentosConfig() {
        return enchants;
    }
}