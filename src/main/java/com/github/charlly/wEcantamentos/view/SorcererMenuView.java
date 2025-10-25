package com.github.charlly.wEcantamentos.view;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.config.api.W_Config;
import com.github.charlly.wEcantamentos.config.util.ItemBuilder;
import com.github.charlly.wEcantamentos.config.util.XPUtil; // <-- 1. IMPORTE O XPUtil
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SorcererMenuView {

    private final Main main;
    private final W_Config config;
    public static String TITLE_INVENTORY = "§8Feiticeiro";

    public SorcererMenuView(Main main) {
        this.main = main;
        this.config = main.getConfiguration();
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, TITLE_INVENTORY);
        createItems(inv, player);
        player.openInventory(inv);
    }

    private void createItems(Inventory inv, Player player) {

        int custoLivroSimples = config.getInt("CustoLivro.Simples");
        int custoLivroNormal = config.getInt("CustoLivro.Normal");
        int custoLivroIntermediario = config.getInt("CustoLivro.Intermediario");
        int custoLivroAvancado = config.getInt("CustoLivro.Avancado");

        int custoFrascoSimples = config.getInt("CustoFrasco.Simples");
        int custoFrascoNormal = config.getInt("CustoFrasco.Normal");
        int custoFrascoAvancado = config.getInt("CustoFrasco.Avancado");

        ItemStack livroSimples = new ItemBuilder(Material.BOOK)
                .setName("§aLivro de encantamentos")
                .setLore(
                        "§7Tipo: §fSimples",
                        "§f",
                        "§7Clique com o botão direito para",
                        "§7ver os encantamentos simples.",
                        "§f",
                        "§7Custo: §e" + formatarNumero(custoLivroSimples) + " XP"
                ).build();

        ItemStack livroNormal = new ItemBuilder(Material.BOOK)
                .setName("§aLivro de encantamentos")
                .setLore(
                        "§7Tipo: §fNormal",
                        "§f",
                        "§7Clique com o botão direito para",
                        "§7ver os encantamentos normais.",
                        "§f",
                        "§7Custo: §e" + formatarNumero(custoLivroNormal) + " XP"
                ).build();

        ItemStack livroIntermediario = new ItemBuilder(Material.BOOK)
                .setName("§aLivro de encantamentos")
                .setLore(
                        "§7Tipo: §fIntermediário",
                        "§f",
                        "§7Clique com o botão direito para",
                        "§7ver os encantamentos intermediários.",
                        "§f",
                        "§7Custo: §e" + formatarNumero(custoLivroIntermediario) + " XP"
                ).build();

        ItemStack livroAvancado = new ItemBuilder(Material.BOOK)
                .setName("§aLivro de encantamentos")
                .setLore(
                        "§7Tipo: §fAvançado",
                        "§f",
                        "§7Clique com o botão direito para",
                        "§7ver os encantamentos avançados.",
                        "§f",
                        "§7Custo: §e" + formatarNumero(custoLivroAvancado) + " XP"
                ).build();

        ItemStack frascoSimples = new ItemBuilder(Material.EXP_BOTTLE, 1, (short) 0)
                .setName("§aFrasco de Experiência")
                .setLore(
                        "§7Custo: §e" + formatarNumero(custoFrascoSimples) + " XP",
                        "§f",
                        "§7Você receberá um frasco com",
                        "§7" + formatarNumero(custoFrascoSimples) + " de XP."
                ).build();

        ItemStack frascoNormal = new ItemBuilder(Material.EXP_BOTTLE, 1, (short) 0)
                .setName("§aFrasco de Experiência")
                .setLore(
                        "§7Custo: §e" + formatarNumero(custoFrascoNormal) + " XP",
                        "§f",
                        "§7Você receberá um frasco com",
                        "§7" + formatarNumero(custoFrascoNormal) + " de XP."
                ).build();

        ItemStack frascoAvancado = new ItemBuilder(Material.EXP_BOTTLE, 1, (short) 0)
                .setName("§aFrasco de Experiência")
                .setLore(
                        "§7Custo: §e" + formatarNumero(custoFrascoAvancado) + " XP",
                        "§f",
                        "§7Você receberá um frasco com",
                        "§7" + formatarNumero(custoFrascoAvancado) + " de XP."
                ).build();

        int totalXpDoPlayer = XPUtil.getTotalExperience(player);

        ItemStack frascoTotal = new ItemBuilder(Material.EXP_BOTTLE, 1, (short) 0)
                .setName("§aFrasco de Experiência")
                .setLore(
                        "§7Custo: §e" + formatarNumero(totalXpDoPlayer) + " XP",
                        "§f",
                        "§7Você receberá um frasco com",
                        "§7todo o seu XP atual."
                ).build();

        ItemStack youExp = new ItemBuilder(Material.MINECART)
                .setName("§eSua experiência")
                .setLore(
                        "§fXP Total: §7" + formatarNumero(totalXpDoPlayer)
                )
                .build();

        ItemStack abrirReciclador = new ItemBuilder(Material.HOPPER)
                .setName("§cReciclador")
                .setLore(
                        "§7Clique para abrir o menu",
                        "§7de reciclagem de livros."
                )
                .build();

        inv.setItem(13, youExp);
        inv.setItem(19, frascoSimples);
        inv.setItem(20, frascoNormal);
        inv.setItem(28, frascoAvancado);
        inv.setItem(29, frascoTotal);
        inv.setItem(24, livroSimples);
        inv.setItem(25, livroNormal);
        inv.setItem(33, livroIntermediario);
        inv.setItem(34, livroAvancado);
        inv.setItem(49, abrirReciclador);
    }

    private String formatarNumero(int numero) {
        return String.format("%,d", numero).replace(",", ".");
    }
}