package tech.skyouo.plugins.testsender.listener;

import com.loohp.interactivechat.api.events.InventoryPlaceholderEvent;
import com.loohp.interactivechat.api.events.ItemPlaceholderEvent;
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

import static tech.skyouo.plugins.testsender.TestSender.jda;
import static tech.skyouo.plugins.testsender.listener.AuctionListEvent.translateEnchantments;

public class ItemPHEvent {

    public static void onEventHeld(Player player, ItemStack item) {
        TextChannel discordChannel = jda.getChannelById(TextChannel.class, "914360488723382352");
        if (discordChannel == null) return;

        discordChannel.sendMessage(
                String.format(
                        "<a:R5_RICE:868583772692545638>` %s %s : 展示了 [%d 個 %s] `",
                        ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player, "%playerTitle_use%")),
                        player.getDisplayName(),
                        item.getAmount(),
                        buildItem(item, player, item.getItemMeta().hasDisplayName())
                )
        ).queue();
    }


    public static void onInventoryHeld(Player player, InventoryPlaceholderEvent.InventoryPlaceholderType type) {
        TextChannel discordChannel = jda.getChannelById(TextChannel.class, "914360488723382352");

        if (discordChannel == null) return;

        MessageAction action = null;
        switch (type) {
            case INVENTORY:
            case INVENTORY1_UPPER:
                action = discordChannel.sendMessage(
                        String.format(
                                "<a:R5_RICE:868583772692545638>` %s %s : 展示了 [背包] `",
                                ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player, "%playerTitle_use%")),
                                player.getDisplayName()
                        )
                );
                break;
            case ENDERCHEST:
                action = discordChannel.sendMessage(
                        String.format(
                                "<a:R5_RICE:868583772692545638>` %s %s : 展示了 [終界箱] `",
                                ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player, "%playerTitle_use%")),
                                player.getDisplayName()
                        )
                );
                break;
            case INVENTORY1_LOWER:
                action = discordChannel.sendMessage(
                        String.format(
                                "<a:R5_RICE:868583772692545638>` %s %s : 展示了 [物品欄] `",
                                ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player, "%playerTitle_use%")),
                                player.getDisplayName()
                        )
                );
                break;
        }

        try {
            action.queue();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static String buildItem(ItemStack itemStack, Player player, boolean hasCustomName) {
        String name;

        if (hasCustomName) {
            name = itemStack.getItemMeta().getDisplayName() + " (" + PlaceholderAPI.setPlaceholders(player, "%locale_" + itemId(itemStack) + "%") + ")";
        } else {
            name = PlaceholderAPI.setPlaceholders(player, "%locale_" + itemId(itemStack) + "%");
        }

        return ChatColor.stripColor(name);
    }

    private static String itemId(ItemStack item) {
        return (item.getType().equals(Material.POTION) || item.getType().equals(Material.LINGERING_POTION) || item.getType().equals(Material.SPLASH_POTION) || item.getType().equals(Material.TIPPED_ARROW)) ?
                "item.minecraft." + item.getType().toString().toLowerCase(Locale.ROOT) + ".effect." + ((PotionMeta) item.getItemMeta()).getBasePotionData().getType().toString().toLowerCase(Locale.ROOT) : item.getType().toString().toLowerCase(Locale.ROOT);
    }
}
