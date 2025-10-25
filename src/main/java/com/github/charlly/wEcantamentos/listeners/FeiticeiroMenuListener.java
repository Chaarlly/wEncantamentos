package com.github.charlly.wEcantamentos.listeners;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.config.util.ItemBuilder;
import com.github.charlly.wEcantamentos.config.util.XPUtil;
import com.github.charlly.wEcantamentos.view.FeiticeiroMenuView;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class FeiticeiroMenuListener implements Listener {

    private final Main main;

    public FeiticeiroMenuListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        try {
            if (!e.getView().getTitle().equals(FeiticeiroMenuView.TITLE_INVENTORY)) {
                return;
            }
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR || !clickedItem.hasItemMeta()) {
                return;
            }
            p.closeInventory();
            Bukkit.getScheduler().runTask(main, () -> {
                if (clickedItem.getType() == Material.BOOK) {
                    handleBookPurchase(p, clickedItem);
                }
                if (clickedItem.getType() == Material.EXP_BOTTLE) {
                    handleBottlePurchase(p, clickedItem);
                }
            });
        } catch (Exception ex) {
            e.getWhoClicked().sendMessage("§cOcorreu um erro ao processar seu clique. Avise um admin.");
            ex.printStackTrace();
        }
    }

    private void handleBookPurchase(Player p, ItemStack bookItem) {
        ItemMeta meta = bookItem.getItemMeta();
        if (meta.getLore() == null || meta.getLore().isEmpty()) {
            return;
        }

        String loreTipo = meta.getLore().get(0);
        int custo = 0;
        String tipoNomeDisplay = "";
        String tipoId = "";

        if (loreTipo.contains("Simples")) {
            custo = main.getConfiguracao().getInt("CustoLivro.Simples");
            tipoNomeDisplay = "Simples";
            tipoId = "Simples";
        } else if (loreTipo.contains("Normal")) {
            custo = main.getConfiguracao().getInt("CustoLivro.Normal");
            tipoNomeDisplay = "Normal";
            tipoId = "Normal";
        } else if (loreTipo.contains("Intermediário")) {
            custo = main.getConfiguracao().getInt("CustoLivro.Intermediario");
            tipoNomeDisplay = "Intermediário";
            tipoId = "Intermediario";
        } else if (loreTipo.contains("Avançado")) {
            custo = main.getConfiguracao().getInt("CustoLivro.Avancado");
            tipoNomeDisplay = "Avançado";
            tipoId = "Avancado";
        } else {
            return;
        }

        int playerXpTotal = XPUtil.getTotalExperience(p);

        if (playerXpTotal >= custo) {
            XPUtil.setTotalExperience(p, playerXpTotal - custo);

            ItemStack placeholderBook = new ItemBuilder(Material.BOOK)
                    .setName("§aLivro de Encantamentos " + tipoNomeDisplay)
                    .setLore(
                            "§7Clique com o botão direito",
                            "§7para revelar seu encantamento!",
                            "§0wE_TIER:" + tipoId
                    ).build();

            HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(placeholderBook);
            if (!overflow.isEmpty()) {
                p.getWorld().dropItem(p.getLocation(), overflow.get(0));
                p.sendMessage("§cSeu inventário está cheio! O livro foi dropado no chão.");
            }

            p.sendMessage("§aVocê comprou um Livro " + tipoNomeDisplay + "!");
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);

        } else {
            p.sendMessage("§cVocê não tem XP suficiente! Faltam " + (custo - playerXpTotal) + " XP.");
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1f, 1f);
        }
    }

    private void handleBottlePurchase(Player p, ItemStack bottleItem) {
        ItemMeta meta = bottleItem.getItemMeta();
        if (meta.getLore() == null || meta.getLore().isEmpty()) {
            return;
        }

        String loreCusto = meta.getLore().get(0);
        int custo = 0;
        int playerXpTotal = XPUtil.getTotalExperience(p);

        if (loreCusto.contains(formatarNumero(main.getConfiguracao().getInt("CustoFrasco.Simples")))) {
            custo = main.getConfiguracao().getInt("CustoFrasco.Simples");
        } else if (loreCusto.contains(formatarNumero(main.getConfiguracao().getInt("CustoFrasco.Normal")))) {
            custo = main.getConfiguracao().getInt("CustoFrasco.Normal");
        } else if (loreCusto.contains(formatarNumero(main.getConfiguracao().getInt("CustoFrasco.Avancado")))) {
            custo = main.getConfiguracao().getInt("CustoFrasco.Avancado");
        } else if (loreCusto.contains(formatarNumero(playerXpTotal))) {
            custo = playerXpTotal;
        } else {
            return;
        }

        if (custo == 0) return;

        if (playerXpTotal >= custo) {
            XPUtil.setTotalExperience(p, playerXpTotal - custo);

            ItemStack frascoXP = new ItemBuilder(Material.EXP_BOTTLE, 1, (short) 0)
                    .setName("§aFrasco de Experiência")
                    .setLore(
                            "§7Valor: §e" + formatarNumero(custo) + " XP",
                            "§f",
                            "§7Clique com o botão direito",
                            "§7para resgatar.",
                            "§0§0§0XP:" + custo
                    ).build();

            HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(frascoXP);
            if (!overflow.isEmpty()) {
                p.getWorld().dropItem(p.getLocation(), overflow.get(0));
                p.sendMessage("§cSeu inventário está cheio! O frasco foi dropado no chão.");
            }

            p.sendMessage("§aVocê comprou um Frasco com " + formatarNumero(custo) + " XP!");
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);

        } else {
            p.sendMessage("§cVocê não tem XP suficiente!");
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1f, 1f);
        }
    }

    private String formatarNumero(int numero) {
        return String.format("%,d", numero).replace(",", ".");
    }
}