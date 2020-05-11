package pl.extollite.guilds.manager;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import jdk.internal.jline.internal.Nullable;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.quest.Quest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class GuildManager {
    public enum MemberLevel {
        Member(0),
        King(1),
        CoLeader(2),
        Leader(3);

        private static final Map<Integer, MemberLevel> LOOKUP = stream(values()).collect(toMap(GuildManager.MemberLevel::getId, x -> x));

        private final int id;

        MemberLevel(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }

        @Nullable
        public static MemberLevel fromId(int id) {
            return LOOKUP.get(id);
        }
    }

    private static final Map<String, Guild> guilds = new HashMap<>();
    private static final Map<UUID, Guild> players = new HashMap<>();

    private static final Queue<String> dirty = new ConcurrentLinkedQueue<>();

    public static void init(){
        Config guilds = new Config(Guilds.getInstance().getDataFolder()+"/guilds.yml", Config.YAML);
        for(String tag : guilds.getKeys(false)){
            String name = guilds.getString(tag+".name");
            double depsite = guilds.getDouble(tag+".deposit");
            String lastQuest = guilds.getString(tag+".last-quest");
            Date questFinished = new Date(System.currentTimeMillis());
            try {
                questFinished = new SimpleDateFormat(Guilds.getFormat()).parse(guilds.getString(tag+".quest-finished"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int level = guilds.getInt(tag+".level");
            int exp = guilds.getInt(tag+".exp");
            Quest activeQuest = null;
            if(guilds.exists(tag+".active-quest"))
                activeQuest = QuestManager.serializeQuest(guilds, tag+".active-quest");
            Guild guild = new Guild(name, tag, depsite, activeQuest, lastQuest, questFinished, level, exp);
            for(String uuidString : guilds.getSection(tag+".members").getKeys(false)){
                UUID uuid = UUID.fromString(uuidString);
                guild.addMember(uuid, MemberLevel.fromId(guilds.getInt(tag+".members."+uuidString)));
                players.put(uuid, guild);
            }
        }
    }

    public static Guild getPlayerGuild(UUID uuid){
        return players.get(uuid);
    }

    public static Guild getPlayerGuild(Player player){
        return getPlayerGuild(player.getUniqueId());
    }

    public static void setPlayerGuild(UUID uuid, Guild guild){
        players.put(uuid, guild);
    }

    public static void setPlayerGuild(Player player, Guild guild){
        setPlayerGuild(player.getUniqueId(), guild);
    }

    public static void removePlayerGuild(UUID uuid){
        players.remove(uuid);
    }

    public static void removePlayerGuild(Player player){
        removePlayerGuild(player.getUniqueId());
    }

    public static boolean hasPlayerGuild(UUID uuid){
        return players.containsKey(uuid);
    }

    public static boolean hasPlayerGuild(Player player){
        return hasPlayerGuild(player.getUniqueId());
    }

    public static Guild getGuildByTag(String tag){
        return guilds.get(tag);
    }

    public static Guild getGuildByName(String name){
        for(Guild guild : guilds.values()){
            if(guild.getFullName().equalsIgnoreCase(name))
                return guild;
        }
        return null;
    }

    public static boolean isGuildExists(String tag){
        return guilds.containsKey(tag);
    }

    public static boolean putGuild(Guild guild) {
        if (isGuildExists(guild.getTag()))
            return false;
        guilds.put(guild.getTag(), guild);
        return true;
    }

    public static void removeGuild(Guild guild) {
        guilds.remove(guild.getTag());
    }
}
