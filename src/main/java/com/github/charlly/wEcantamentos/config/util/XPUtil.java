package com.github.charlly.wEcantamentos.config.util;

import org.bukkit.entity.Player;

public class XPUtil {

    /**
     * Calcula o XP total REAL de um jogador (corrige o bug da 1.8)
     */
    public static int getTotalExperience(Player player) {
        int level = player.getLevel();
        float exp = player.getExp();
        int total = 0;

        if (level < 17) {
            total = (int) (Math.pow(level, 2) + 6 * level);
        } else if (level < 32) {
            total = (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else {
            total = (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        }

        total += (int) (exp * getExpToNextLevel(level));
        return total;
    }

    /**
     * Define o XP total REAL de um jogador (corrige o bug da 1.8)
     */
    public static void setTotalExperience(Player player, int amount) {
        if (amount < 0) amount = 0;

        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);

        player.giveExp(amount);
    }

    /**
     * Pega a quantidade de XP necessária para o próximo nível
     */
    private static int getExpToNextLevel(int level) {
        if (level < 16) {
            return 2 * level + 7;
        } else if (level < 31) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }
}