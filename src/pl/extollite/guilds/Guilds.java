package pl.extollite.guilds;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import pl.extollite.guilds.command.AcceptCommand;
import pl.extollite.guilds.command.CreateCommand;
import pl.extollite.guilds.command.GuildCommand;
import pl.extollite.guilds.command.BoardCommand;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.listener.*;
import pl.extollite.guilds.listener.EventListener;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.manager.QuestManager;

import java.util.*;

public class Guilds extends PluginBase {
    private static final String format = "yyyy-MM-dd HH:mm:ss Z";

    public static String getFormat() {
        return format;
    }

    private static Guilds instance;

    public static Guilds getInstance() {
        return instance;
    }

    private GuildCommand guildCommand;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        List<String> authors = this.getDescription().getAuthors();
        this.getLogger().info(TextFormat.DARK_GREEN + "Plugin by " + authors.get(0));
        ConfigData.init();

        QuestManager.init();
        GuildManager.init();
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getPluginManager().registerEvents(new QuestWindowListener(), this);
        this.getServer().getPluginManager().registerEvents(new CreateWindowListener(), this);
        this.getServer().getPluginManager().registerEvents(new AcceptWindowListener(), this);
        this.getServer().getPluginManager().registerEvents(new BoardListener(), this);
        registerCommand();
    }

    private void registerCommand(){
        guildCommand = new GuildCommand();
        this.getServer().getCommandMap().register("guild", guildCommand);
        guildCommand.registerCommand(new CreateCommand());
        guildCommand.registerCommand(new BoardCommand());
        guildCommand.registerCommand(new AcceptCommand());
    }
}
