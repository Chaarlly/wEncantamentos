package com.github.charlly.wEcantamentos.manager;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.config.api.W_Config;
import com.github.charlly.wEcantamentos.config.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player; // Importar Player
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap; // Importar HashMap
import java.util.List;
import java.util.Random;

public class EnchantManager {

    private final W_Config enchantConfig;
    private final Random random = new Random();
    public static final String TIER_TAG_PREFIX = "§0wE_TIER:";
    public static final String ID_TAG_PREFIX = "§0wE_ID:";

    public EnchantManager(Main main) {
        this.enchantConfig = main.getEncantamentosConfig();
    }

    public ItemStack getRandomEnchantBook(String tier) {
        if (enchantConfig == null) return null;

        ConfigurationSection enchantmentsSection = enchantConfig.getConfig().getConfigurationSection("Encantamentos");
        if (enchantmentsSection == null) return null;

        List<String> possibleEnchantIDs = new ArrayList<>();
        List<Double> chances = new ArrayList<>();
        double totalChanceWeight = 0;

        for (String enchantID : enchantmentsSection.getKeys(false)) {
            // Verifica se o enchant está ativo e pertence ao tier correto
            boolean active = enchantConfig.getBoolean("Encantamentos." + enchantID + ".active", false); // Pega 'active'
            if (active && tier.equalsIgnoreCase(enchantConfig.getString("Encantamentos." + enchantID + ".tier"))) {
                double chance = enchantConfig.getDouble("Encantamentos." + enchantID + ".chance");
                if (chance > 0) {
                    possibleEnchantIDs.add(enchantID);
                    chances.add(chance);
                    totalChanceWeight += chance;
                }
            }
        }

        if (possibleEnchantIDs.isEmpty()) return null;

        double randomValue = random.nextDouble() * totalChanceWeight;
        String selectedEnchantID = null;

        for (int i = 0; i < possibleEnchantIDs.size(); i++) {
            randomValue -= chances.get(i);
            if (randomValue <= 0) {
                selectedEnchantID = possibleEnchantIDs.get(i);
                break;
            }
        }

        if (selectedEnchantID == null) {
            if (!possibleEnchantIDs.isEmpty()) {
                selectedEnchantID = possibleEnchantIDs.get(0);
            } else {
                return null;
            }
        }

        String name = enchantConfig.getString("Encantamentos." + selectedEnchantID + ".name", "&cErro");
        // Pega a lore VISÍVEL (a descrição do encantamento)
        List<String> visibleLore = enchantConfig.getStringList("Encantamentos." + selectedEnchantID + ".lore");

        // Adiciona o ID SECRETO no final da lore visível
        visibleLore.add(ID_TAG_PREFIX + selectedEnchantID); // <-- ADICIONA O ID SECRETO

        return new ItemBuilder(Material.ENCHANTED_BOOK)
                .setName(name)
                .setLore(visibleLore) // Usa a lore que agora contém o ID secreto
                .build();
    }

}