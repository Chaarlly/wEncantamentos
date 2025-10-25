package com.github.charlly.wEcantamentos.listeners;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.manager.EnchantManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantApplyListener implements Listener {

    private final Main main;

    public EnchantApplyListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        try {
            if (e.getInventory().getType() != InventoryType.PLAYER && e.getInventory().getType() != InventoryType.CRAFTING) {
                return;
            }

            if (!e.isRightClick()) {
                return;
            }

            ItemStack cursorItem = e.getCursor();
            ItemStack targetItem = e.getCurrentItem();

            if (cursorItem == null || cursorItem.getType() != Material.ENCHANTED_BOOK || !cursorItem.hasItemMeta() ||
                    targetItem == null || targetItem.getType() == Material.AIR) {
                return;
            }

            ItemMeta bookMeta = cursorItem.getItemMeta();
            if (bookMeta.getLore() == null || bookMeta.getLore().isEmpty()) {
                return;
            }

            String enchantID = null;
            List<String> bookLore = bookMeta.getLore();
            for (String line : bookLore) {
                if (line.startsWith(EnchantManager.ID_TAG_PREFIX)) {
                    enchantID = line.substring(EnchantManager.ID_TAG_PREFIX.length());
                    break;
                }
            }

            if (enchantID == null) {
                return;
            }

            // --- Pega informações do encantamento (CORRIGIDO COM LOOP FOR) ---
            String enchantNameLine = null; // Inicializa como null
            // Pega a lista de lore do encantamento no config
            List<String> configLore = main.getEncantamentosConfig().getStringList("Encantamentos." + enchantID + ".lore");

            // Loop para encontrar a linha "Tipo:"
            if (configLore != null) { // Garante que a lista não é nula
                for (String line : configLore) {
                    // Verifica se a linha não é nula e se contém o texto desejado
                    if (line != null && line.contains("&fTipo:")) {
                        enchantNameLine = line; // Achou a linha
                        break; // Para o loop
                    }
                }
            }

            // Fallback se não achou a linha "Tipo:" na lore do config
            if (enchantNameLine == null) {
                enchantNameLine = bookMeta.getDisplayName();
            } else {
                enchantNameLine = enchantNameLine.replace("&", "§"); // Colore se achou
            }
            // --- Fim da correção ---

            String appliesTo = main.getEncantamentosConfig().getString("Encantamentos." + enchantID + ".applies-to", "NONE");

            if (!isCompatible(targetItem.getType(), appliesTo)) {
                e.setCancelled(true);
                ((Player)e.getWhoClicked()).sendMessage("§cEste encantamento não pode ser aplicado neste tipo de item!");
                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1f, 0.8f);
                return;
            }

            ItemMeta targetMeta = targetItem.getItemMeta();
            List<String> targetLore = targetMeta.hasLore() ? targetMeta.getLore() : new ArrayList<>();

            boolean alreadyEnchanted = false;
            for(String line : targetLore){
                if(line.equals(enchantNameLine)){
                    alreadyEnchanted = true;
                    break;
                }
            }

            if(alreadyEnchanted){
                e.setCancelled(true);
                ((Player)e.getWhoClicked()).sendMessage("§cEste item já possui este encantamento!");
                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1f, 0.8f);
                return;
            }

            e.setCancelled(true);

            targetLore.add(enchantNameLine);
            targetMeta.setLore(targetLore);
            targetItem.setItemMeta(targetMeta);

            if (cursorItem.getAmount() > 1) {
                cursorItem.setAmount(cursorItem.getAmount() - 1);
                e.setCursor(cursorItem);
            } else {
                e.setCursor(null);
            }

            ((Player)e.getWhoClicked()).sendMessage("§aItem encantado com sucesso!");
            ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ANVIL_USE, 1f, 1.2f);
            ((Player)e.getWhoClicked()).updateInventory();

        } catch (Exception ex) {
            e.getWhoClicked().sendMessage("§cOcorreu um erro ao tentar encantar o item. Avise um admin.");
            ex.printStackTrace();
        }
    }

    private boolean isCompatible(Material itemMat, String category) {
        String matName = itemMat.toString();

        if (category.equalsIgnoreCase("HELMET") && matName.endsWith("_HELMET")) {
            return true;
        }
        if (category.equalsIgnoreCase("ALL")) return true;
        if (category.equalsIgnoreCase("SWORD") && matName.endsWith("_SWORD")) return true;
        if (category.equalsIgnoreCase("PICKAXE") && matName.endsWith("_PICKAXE")) return true;
        if (category.equalsIgnoreCase("AXE") && matName.endsWith("_AXE")) return true;
        if (category.equalsIgnoreCase("SHOVEL") && matName.endsWith("_SPADE")) return true;
        if (category.equalsIgnoreCase("BOW") && matName.equals("BOW")) return true;
        if (category.equalsIgnoreCase("ARMOR") && (matName.endsWith("_HELMET") || matName.endsWith("_CHESTPLATE") || matName.endsWith("_LEGGINGS") || matName.endsWith("_BOOTS"))) return true;
        if (category.equalsIgnoreCase("TOOLS") && (matName.endsWith("_PICKAXE") || matName.endsWith("_AXE") || matName.endsWith("_SPADE") || matName.endsWith("_HOE") || matName.equals("FISHING_ROD") || matName.equals("FLINT_AND_STEEL") || matName.equals("SHEARS"))) return true;

        return false;
    }
}