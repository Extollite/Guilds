package pl.extollite.guilds;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
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
import java.util.concurrent.TimeUnit;

public class Guilds extends PluginBase {
    private static final String format = "yyyy-MM-dd HH:mm:ss Z";

    public static String getFormat() {
        return format;
    }

    private static Guilds instance;

    public static Guilds getInstance() {
        return instance;
    }

    private static final Map<String, Long> immunity = new HashMap<>();
    private static final Map<String, Map.Entry<Float, Long>> bonusExp = new HashMap<>();
    
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
        loadImmunities();
        loadBonusExp();
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
        this.getServer().getCommandMap().register("guild-nocooldown", new NoCooldownCommand());
        this.getServer().getCommandMap().register("guild-bonusexp", new BonusExpCommand());
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

    private void loadImmunities(){
        Config immunities = new Config(Guilds.getInstance().getDataFolder()+"/data-cooldown.yml", Config.YAML);
        for(String key : immunities.getKeys(false)){
            if(GuildManager.getGuildByTag(key) != null){
                long time = immunities.getLong(key);
                if(time > System.currentTimeMillis())
                    Guilds.immunity.put(key, time);
            }
        }
    }

    public static void putImmunity(Guild guild, float days) {
        immunity.put(guild.getTag(), (long) (System.currentTimeMillis()+(days*86400000)));
        Config immunities = new Config(Guilds.getInstance().getDataFolder()+"/data-cooldown.yml", Config.YAML);
        immunities.set(guild.getTag(), immunity.get(guild.getTag()));
        immunities.save();
    }

    public static boolean hasImmunity(Guild guild) {
        if(immunity.containsKey(guild.getTag()))
            return immunity.get(guild.getTag()) > System.currentTimeMillis();
        return false;
    }

    public static long getImmunity(Guild guild){
        return immunity.get(guild.getTag());
    }

    public static String getTimeLeftString(Date date) {
        long millis = date.getTime() - System.currentTimeMillis();
        if(millis <= 0)
            return "00:00:00";
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        return hms;
    }

    private void loadBonusExp(){
        Config exp = new Config(Guilds.getInstance().getDataFolder()+"/data-exp.yml", Config.YAML);
        for(String key : exp.getKeys(false)){
            if(GuildManager.getGuildByTag(key) != null){
                long time = exp.getLong(key+".time");
                float amount = (float) exp.getDouble(key+".exp");
                if(time > System.currentTimeMillis())
                    Guilds.bonusExp.put(key, new AbstractMap.SimpleImmutableEntry<>(amount, time));
            }
        }
    }

    public static void putBonusExp(Guild guild, float amount, float days) {
        bonusExp.put(guild.getTag(), new AbstractMap.SimpleImmutableEntry<>(amount, (long) (System.currentTimeMillis()+(days*86400000))));
        Config exp = new Config(Guilds.getInstance().getDataFolder()+"/data-exp.yml", Config.YAML);
        exp.set(guild.getTag()+".time", bonusExp.get(guild.getTag()).getValue());
        exp.set(guild.getTag()+".exp", bonusExp.get(guild.getTag()).getKey());
        exp.save();
    }

    public static boolean hasBonusExp(Guild guild) {
        if(bonusExp.containsKey(guild.getTag()))
            return bonusExp.get(guild.getTag()).getValue() > System.currentTimeMillis();
        return false;
    }

    public static float getBonusExp(Guild guild){
        return bonusExp.get(guild.getTag()).getKey();
    }

    public static long getBonusExpTime(Guild guild){
        return bonusExp.get(guild.getTag()).getValue();
    }
}
