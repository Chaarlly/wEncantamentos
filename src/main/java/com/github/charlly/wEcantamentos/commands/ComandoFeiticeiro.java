package com.github.charlly.wEcantamentos.commands;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.view.FeiticeiroMenuView; // Importe o seu menu
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoFeiticeiro implements CommandExecutor {

    private final Main main;

    public ComandoFeiticeiro(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return true;
        }

        Player p = (Player) sender;

        new FeiticeiroMenuView(main).open(p);
        return true;
    }
}