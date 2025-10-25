package com.github.charlly.wEcantamentos.config.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List; // Adicionei este import para a lore

public class ItemBuilder {

    private final ItemStack item;

    /**
     * Construtor para itens simples (quantidade 1, data 0)
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    /**
     * Construtor completo com quantidade e data/durabilidade
     */
    public ItemBuilder(Material material, int amount, short durability) {
        this.item = new ItemStack(material, amount, durability);
    }

    /**
     * Define o nome do item (aceita códigos de cor &)
     */
    public ItemBuilder setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name.replace("&", "§")); // Converte & para §
        item.setItemMeta(meta);
        return this; // Permite encadear métodos (ex: .setName().setLore())
    }

    /**
     * Define a lore do item (aceita códigos de cor &)
     */
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        // Converte & para § em cada linha da lore
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replace("&", "§"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Define a lore do item usando varargs (aceita códigos de cor &)
     * Isso permite chamar: .setLore("Linha 1", "Linha 2", "Linha 3")
     */
    public ItemBuilder setLore(String... lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = Arrays.asList(lore);
        // Converte & para § em cada linha da lore
        for (int i = 0; i < loreList.size(); i++) {
            loreList.set(i, loreList.get(i).replace("&", "§"));
        }
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Retorna o ItemStack finalizado
     */
    public ItemStack build() {
        return this.item;
    }
}