package com.github.charlly.wEcantamentos.listeners;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.config.util.XPUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class XPBottleListener implements Listener {

    private final String NBT_PREFIX = "§0§0§0XP:";

    private final Main main;

    public XPBottleListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();

            if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            ItemStack item = p.getItemInHand();

            if (item == null || item.getType() != Material.EXP_BOTTLE || !item.hasItemMeta()) {
                return;
            }

            ItemMeta meta = item.getItemMeta();

            if (meta.getLore() == null || meta.getLore().isEmpty()) {
                return;
            }

            List<String> lore = meta.getLore();
            int xpAmount = 0;

            for (String line : lore) {
                if (line.startsWith(NBT_PREFIX)) {
                    try {
                        xpAmount = Integer.parseInt(line.substring(NBT_PREFIX.length()));
                        break;
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        return;
                    }
                }
            }

            if (xpAmount <= 0) {
                return;
            }

            e.setCancelled(true);

            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                p.setItemInHand(null);
            }

            int currentXP = XPUtil.getTotalExperience(p);

            XPUtil.setTotalExperience(p, currentXP + xpAmount);

            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1.2f);
            p.sendMessage("§aVocê resgatou " + String.format("%,d", xpAmount).replace(",", ".") + " XP!");

        } catch (Exception ex) {
            e.getPlayer().sendMessage("§cOcorreu um erro ao usar este frasco.");
            ex.printStackTrace();
        }
    }
}