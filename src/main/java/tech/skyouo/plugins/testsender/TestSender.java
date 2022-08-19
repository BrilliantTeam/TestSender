package tech.skyouo.plugins.testsender;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.skyouo.plugins.testsender.listener.AuctionListEvent;
import tech.skyouo.plugins.testsender.listener.ItemPHEvent;
import tech.skyouo.plugins.testsender.listener.PlayerChatEvent;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public final class TestSender extends JavaPlugin {
    public static JDA jda;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            jda = JDABuilder.createDefault(
                    new String(Base64.getDecoder().decode("<YOUR TOKEN>"), "UTF-8")
            ).build().awaitReady();
        } catch (InterruptedException | LoginException | UnsupportedEncodingException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getServer().getPluginManager().registerEvents(new PlayerChatEvent(this), this);
        getServer().getPluginManager().registerEvents(new AuctionListEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        jda.shutdown();
    }
}
