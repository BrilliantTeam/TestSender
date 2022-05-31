package tech.skyouo.plugins.testsender.listener;

import com.loohp.interactivechat.api.events.InventoryPlaceholderEvent;
import com.loohp.interactivechat.api.events.ItemPlaceholderEvent;
import com.loohp.interactivechat.objectholders.ICPlayer;
import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import mineverse.Aust1n46.chat.channel.ChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class PlayerChatEvent implements Listener {
    static Pattern p = Pattern.compile(".*(?:買|賣|售|收|購|誰能|誰可|誰給|徵|求|搶|收購).*(?:\\d|一|二|三|四|五|六|七|八|九|十|零|壹|貳|叄|肆|伍|陸|柒|捌|玖|拾)+.+");
    static Pattern p2 = Pattern.compile(".*(?:\\[i\\]|\\[item\\]|\\[I\\]|\\[Item\\]|\\[ender\\]|\\[Ender\\]|\\[inv\\]|\\[Inv\\])+.*");

    public static JavaPlugin plugin;

    public PlayerChatEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    };

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
        if (p.matcher(e.getMessage()).matches()) {
            // e.setCancelled(true);
            MineverseChatPlayer mp = MineverseChatAPI.getMineverseChatPlayer(e.getPlayer());
            mp.setCurrentChannel(ChatChannel.getChannel("貿易"));

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                mp.setCurrentChannel(ChatChannel.getDefaultChannel());
            }, 10L);
        } else if (p2.matcher(e.getMessage()).matches()) {
            if (e.getMessage().contains("[i]") || e.getMessage().contains("[item]") || e.getMessage().contains("[I]") || e.getMessage().contains("[Item]")) {
                ItemPHEvent.onEventHeld(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());
            } else if (e.getMessage().contains("[ender]") || e.getMessage().contains("[Ender]")) {
                ItemPHEvent.onInventoryHeld(e.getPlayer(), InventoryPlaceholderEvent.InventoryPlaceholderType.ENDERCHEST);
            } else if (e.getMessage().contains("[inv]") || e.getMessage().contains("[Inv]")) {
                ItemPHEvent.onInventoryHeld(e.getPlayer(), InventoryPlaceholderEvent.InventoryPlaceholderType.INVENTORY);
            }
        }
        // e.getPlayer().getInventory().getItemInMainHand().getType().getKey().getNamespace()
    }
}
