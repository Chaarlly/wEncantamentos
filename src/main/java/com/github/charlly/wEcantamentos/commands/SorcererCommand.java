package com.github.charlly.wEcantamentos.commands;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.view.SorcererMenuView; // Importe o seu menu
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SorcererCommand implements CommandExecutor {

    private final Main main;

    public SorcererCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return true;
        }

        Player p = (Player) sender;

        new SorcererMenuView(main).open(p);
        return true;
    }
}