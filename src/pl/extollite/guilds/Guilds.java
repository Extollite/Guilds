package pl.extollite.guilds;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import pl.extollite.guilds.command.*;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
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

    public static List<Guild> immunity = new ArrayList<>();

    private GuildCommand guildCommand;

    private PlaceholderAPI papi = PlaceholderAPI.getInstance();

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
        this.getServer().getPluginManager().registerEvents(new InfoWindowListener(), this);
        this.getServer().getPluginManager().registerEvents(new ShopListener(), this);
        registerCommand();
        registerPlaceholders();
    }

    private void registerCommand(){
        guildCommand = new GuildCommand();
        this.getServer().getCommandMap().register("guild", guildCommand);
        guildCommand.registerCommand(new CreateCommand());
        guildCommand.registerCommand(new BoardCommand());
        guildCommand.registerCommand(new AcceptCommand());
        guildCommand.registerCommand(new TopCommand());
        guildCommand.registerCommand(new InfoCommand());
        guildCommand.registerCommand(new LeaveCommand());
    }

    public void registerPlaceholders(){
        for(int i = 0; i < 10; i++){
            int finalI = i;
            papi.staticPlaceholder("guild_top_tag_"+(finalI+1), () -> {
                if(GuildManager.getSorted().size() > finalI){
                    return GuildManager.getSorted().get(finalI).getTag();
                }
                return "";
            }, 1, true);
            papi.staticPlaceholder("guild_top_name_"+(finalI+1), () -> {
                if(GuildManager.getSorted().size() > finalI){
                    return GuildManager.getSorted().get(finalI).getFullName();
                }
                return "";
            }, 1, true);
            papi.staticPlaceholder("guild_top_level_"+(finalI+1), () -> {
                if(GuildManager.getSorted().size() > finalI){
                    return GuildManager.getSorted().get(finalI).getLevel();
                }
                return "";
            }, 1, true);
            papi.staticPlaceholder("guild_top_exp_"+(finalI+1), () -> {
                if(GuildManager.getSorted().size() > finalI){
                    return GuildManager.getSorted().get(finalI).getExp();
                }
                return "";
            }, 1, true);
        }
        papi.visitorSensitivePlaceholder("player_guild_tag", player -> GuildManager.getPlayerGuild(player).getTag());
        papi.visitorSensitivePlaceholder("player_guild_name", player -> GuildManager.getPlayerGuild(player).getFullName());
        papi.visitorSensitivePlaceholder("player_guild_level", player -> GuildManager.getPlayerGuild(player).getLevel());
        papi.visitorSensitivePlaceholder("player_guild_exp", player -> GuildManager.getPlayerGuild(player).getExp());
        papi.visitorSensitivePlaceholder("player_guild_rank", player -> GuildManager.getPlayerGuild(player).getMemberLevel(player));
    }

    public static void putImmunity(Guild guild) {
        immunity.add(guild);
    }

    public static boolean hasImmunity(Guild guild) {
        return immunity.contains(guild);
    }
}
