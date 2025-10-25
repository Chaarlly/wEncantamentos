package com.github.charlly.wEcantamentos.listeners;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.manager.EnchantManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class BookListener implements Listener {

    private final Main main;
    private final EnchantManager enchantManager;

    public BookListener(Main main) {
        this.main = main;
        this.enchantManager = new EnchantManager(main);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            ItemStack item = p.getItemInHand();
            if (item == null || item.getType() != Material.BOOK || !item.hasItemMeta()) {
                return;
            }

            ItemMeta meta = item.getItemMeta();
            if (meta.getLore() == null || meta.getLore().isEmpty()) {
                return;
            }

            String tier = null;
            for (String line : meta.getLore()) {
                if (line.startsWith(EnchantManager.TIER_TAG_PREFIX)) {
                    tier = line.substring(EnchantManager.TIER_TAG_PREFIX.length());
                    break;
                }
            }

            if (tier == null) {
                return;
            }

            e.setCancelled(true);

            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                p.setItemInHand(null);
            }
            p.updateInventory();

            ItemStack finalBook = enchantManager.getRandomEnchantBook(tier);

            if (finalBook != null) {
                HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(finalBook);
                if (!overflow.isEmpty()) {
                    p.getWorld().dropItem(p.getLocation(), overflow.get(0));
                    p.sendMessage("§cSeu inventário está cheio! O livro foi dropado no chão.");
                }
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1.2f);
                p.sendMessage("§aVocê revelou um livro: " + (finalBook.hasItemMeta() && finalBook.getItemMeta().hasDisplayName() ? finalBook.getItemMeta().getDisplayName() : "§cLivro Inválido"));
            } else {
                p.sendMessage("§cNão foi possível sortear um encantamento para este livro. Avise um admin.");
                ItemStack originalBook = item.clone(); // Clone before potentially modifying amount
                originalBook.setAmount(1); // Ensure we give back only one
                p.getInventory().addItem(originalBook);
            }

        } catch (Exception ex) {
            e.getPlayer().sendMessage("§cOcorreu um erro ao abrir este livro. Avise um admin.");
            ex.printStackTrace();
        }
    }
}