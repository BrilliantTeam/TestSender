package tech.skyouo.plugins.testsender.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

import static tech.skyouo.plugins.testsender.TestSender.jda;

public class AuctionListEvent implements Listener {
    @EventHandler
    public void onAuctionListEvent(studio.trc.bukkit.crazyauctionsplus.api.events.AuctionListEvent e) {
        if (!e.getShopType().getName().equals("Buy") && !e.getShopType().getName().equals("Sell")) return;

        TextChannel tradeChannel = jda.getChannelById(TextChannel.class, "967051322144751626");
        if (tradeChannel == null) return;

        if (e.getItem().getType().equals(Material.POTION) || e.getItem().getType().equals(Material.LINGERING_POTION) || e.getItem().getType().equals(Material.SPLASH_POTION) || e.getItem().getType().equals(Material.TIPPED_ARROW)) {
            onPotionHandler(e, tradeChannel);
            return;
        }

        MessageAction action;

        Map<Enchantment, Integer> enchantmentIntegerMap = !e.getItem().getType().equals(Material.ENCHANTED_BOOK) ? e.getItem().getEnchantments() : ((EnchantmentStorageMeta) e.getItem().getItemMeta()).getStoredEnchants();

        if (enchantmentIntegerMap.size() == 0) {
            if (e.getShopType().getName().equals("Sell")) {
                action = tradeChannel
                        .sendMessage(
                                String.format(
                                        "<a:R0_RICE:868583519948009492>` %s %s 在 出售商場 上架了 %d 個價格為 %.1f 的 %s `",
                                        ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                        e.getPlayer().getName(),
                                        e.getItem().getAmount(),
                                        e.getMoney(),
                                        PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + e.getItem().getType() + "%")
                                )
                        );
            } else {
                action = tradeChannel
                        .sendMessage(
                                String.format(
                                        "<a:R0_RICE:868583519948009492>` %s %s 在 收購商場 願意以 %.1f 的價格收購 %d 個 %s `",
                                        ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                        e.getPlayer().getName(),
                                        e.getMoney(),
                                        e.getItem().getAmount(),
                                        PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + e.getItem().getType() + "%")
                                )
                        );
            }
        } else {
            if (enchantmentIntegerMap.size() == 1) {
                if (e.getShopType().getName().equals("Sell")) {
                    action = tradeChannel
                            .sendMessage(
                                    String.format(
                                            "<a:R0_RICE:868583519948009492>` %s %s 在 出售商場 上架了 %d 個價格為 %.1f 的 %s %s `",
                                            ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                            e.getPlayer().getName(),
                                            e.getItem().getAmount(),
                                            e.getMoney(),
                                            translateEnchantments(e.getPlayer(), e.getItem()),
                                            PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + e.getItem().getType() + "%")
                                    )
                            );
                } else {
                    action = tradeChannel
                            .sendMessage(
                                    String.format(
                                            "<a:R0_RICE:868583519948009492>` %s %s 在 收購商場 願意以 %.1f 的價格收購 %d 個 %s %s `",
                                            ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                            e.getPlayer().getName(),
                                            e.getMoney(),
                                            e.getItem().getAmount(),
                                            translateEnchantments(e.getPlayer(), e.getItem()),
                                            PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + e.getItem().getType() + "%")
                                    )
                            );
                }
            } else {
                if (e.getShopType().getName().equals("Sell")) {
                    action = tradeChannel
                            .sendMessage(
                                    String.format(
                                            "<a:R0_RICE:868583519948009492>` %s %s 在 出售商場 上架了 %d 個價格為 %.1f 的 %s 的 %s `",
                                            ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                            e.getPlayer().getName(),
                                            e.getItem().getAmount(),
                                            e.getMoney(),
                                            translateEnchantments(e.getPlayer(), e.getItem()),
                                            PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + e.getItem().getType() + "%")
                                    )
                            );
                } else {
                    action = tradeChannel
                            .sendMessage(
                                    String.format(
                                            "<a:R0_RICE:868583519948009492>` %s %s 在 收購商場 願意以 %.1f 的價格收購 %d 個 %s 的 %s `",
                                            ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                            e.getPlayer().getName(),
                                            e.getMoney(),
                                            e.getItem().getAmount(),
                                            translateEnchantments(e.getPlayer(), e.getItem()),
                                            PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + e.getItem().getType() + "%")
                                    )
                            );
                }
            }
        }

        action.queue();
    }

    protected static String translateEnchantments(Player player, ItemStack itemStack) {
        Map<Enchantment, Integer> enchantmentIntegerMap = !itemStack.getType().equals(Material.ENCHANTED_BOOK) ? itemStack.getEnchantments() : ((EnchantmentStorageMeta) itemStack.getItemMeta()).getStoredEnchants();
        StringJoiner stringJoiner = new StringJoiner(" 及 ");

        for (Enchantment e : enchantmentIntegerMap.keySet()) {
            stringJoiner.add(
                    PlaceholderAPI.setPlaceholders(player, "等級%locale_level." + enchantmentIntegerMap.get(e) + "% %locale_" + e.getKey().getKey() + "%")
            );
        }

        return stringJoiner.toString();
    }

    private void onPotionHandler(studio.trc.bukkit.crazyauctionsplus.api.events.AuctionListEvent e, TextChannel tradeChannel) {
        PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();
        if (meta == null) return;
        String key = "item.minecraft." + e.getItem().getType().toString().toLowerCase(Locale.ROOT) + ".effect." + meta.getBasePotionData().getType().toString().toLowerCase(Locale.ROOT);

        MessageAction action;

        if (e.getShopType().getName().equals("Sell")) {
            action = tradeChannel
                    .sendMessage(
                            String.format(
                                    "<a:R0_RICE:868583519948009492>` %s %s 在 出售商場 上架了 %d 個價格為 %.1f 的 %s `",
                                    ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                    e.getPlayer().getName(),
                                    e.getItem().getAmount(),
                                    e.getMoney(),
                                    PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + key + "%")
                            )
                    );
        } else {
            action = tradeChannel
                    .sendMessage(
                            String.format(
                                    "<a:R0_RICE:868583519948009492>` %s %s 在 收購商場 願意以 %.1f 的價格收購 %d 個 %s `",
                                    ChatColor.stripColor(PlaceholderAPI.setPlaceholders(e.getPlayer(), "%playerTitle_use%")),
                                    e.getPlayer().getName(),
                                    e.getMoney(),
                                    e.getItem().getAmount(),
                                    PlaceholderAPI.setPlaceholders(e.getPlayer(), "%locale_" + key + "%")
                            )
                    );
        }

        action.queue();
    }
}
