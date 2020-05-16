package pl.extollite.guilds.data;

import cn.nukkit.Player;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.quest.Quest;

import static pl.extollite.guilds.manager.GuildManager.MemberLevel;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public class Guild {
    private String fullName;
    private String tag;
    private Map<UUID, MemberLevel> members = new HashMap<>();
    private double deposit = 0;
    private Quest activeQuest;
    private String lastQuest = "";
    private Date questFinished = new Date(System.currentTimeMillis() - 1);
    private int level = 1;
    private int exp = 0;

    public Guild(String fullName, String tag, Player creator) {
        this.fullName = fullName;
        this.tag = tag;
        members.put(creator.getUniqueId(), MemberLevel.Leader);
    }

    public Guild(String fullName, String tag, double deposit, Quest activeQuest, String lastQuest, Date questFinished, int level, int exp) {
        this.fullName = fullName;
        this.tag = tag;
        this.deposit = deposit;
        this.activeQuest = activeQuest;
        this.lastQuest = lastQuest;
        this.questFinished = questFinished;
        this.level = level;
        this.exp = exp;
    }

    public boolean addMember(UUID uuid, MemberLevel level) {
        return members.putIfAbsent(uuid, level) == null;
    }

    public boolean addMember(Player player, MemberLevel level) {
        return addMember(player.getUniqueId(), level);
    }

    public boolean addMember(UUID uuid) {
        return addMember(uuid, MemberLevel.Member);
    }

    public boolean addMember(Player player) {
        return addMember(player.getUniqueId());
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public void removeMember(Player player) {
        removeMember(player.getUniqueId());
    }

    public void setMemberLevel(UUID uuid, MemberLevel level) {
        members.put(uuid, level);
    }

    public void setMemberLevel(Player player, MemberLevel level) {
        setMemberLevel(player.getUniqueId(), level);
    }

    public void save(boolean all) {
        Config guilds = new Config(Guilds.getInstance().getDataFolder() + "/guilds.yml", Config.YAML);
        if (all) {
            guilds.set(tag + ".name", fullName);
            guilds.set(tag + ".deposit", deposit);
            guilds.set(tag + ".last-quest", lastQuest);
            guilds.set(tag + ".quest-finished", new SimpleDateFormat(Guilds.getFormat()).format(questFinished));
            guilds.set(tag + ".level", level);
            guilds.set(tag + ".exp", exp);
            ConfigSection memberSection = new ConfigSection();
            for (Map.Entry<UUID, MemberLevel> entry : members.entrySet()) {
                memberSection.set(entry.getKey().toString(), entry.getValue().getId());
            }
            guilds.set(tag + ".members", memberSection);
        }
        if (activeQuest != null)
            guilds.set(tag + ".active-quest", activeQuest.serialize());
        else
            guilds.remove(tag + ".active-quest");
        GuildManager.sortGuilds();
        guilds.save(true);
    }

    public void save() {
        save(true);
    }

    public void addExp(int exp) {
        if (Guilds.hasBonusExp(this))
            exp = (int) (exp * Guilds.getBonusExp(this));
        while (exp > 0) {
            if (level == 20)
                return;
            int needExp = ConfigData.levels.get(level);
            if (exp + this.exp > needExp) {
                exp = this.exp + exp - needExp;
                this.exp = 0;
                this.level++;
                Guilds.getInstance().getServer().broadcastMessage(ConfigData.prefix+ConfigData.guild_level_up.replace("%level%", String.valueOf(this.level)).replace("%tag%", this.getTag()));
                for (UUID uuid : members.keySet()) {
                    Optional<Player> player = Guilds.getInstance().getServer().getPlayer(uuid);
                    player.ifPresent(this::giveBonus);
                }
                this.save();
            } else {
                this.exp += exp;
                exp = 0;
            }
        }
        this.save();
    }

    public void finalizeQuest() {
        questFinished = new Date(System.currentTimeMillis());
        lastQuest = activeQuest.getId();
        for (UUID uuid : members.keySet()) {
            Optional<Player> player = Guilds.getInstance().getServer().getPlayer(uuid);
            player.ifPresent(value -> value.sendMessage(ConfigData.prefix + ConfigData.quest_finished.replace("%name%", activeQuest.getName()).replace("%exp%", String.valueOf((int) (activeQuest.getExp() * (Guilds.hasBonusExp(this) ? Guilds.getBonusExp(this) : 1))))));
        }
        int exp = activeQuest.getExp();
        activeQuest = null;
        addExp(exp);
    }

    public MemberLevel getMemberLevel(UUID uuid) {
        return members.get(uuid);
    }

    public MemberLevel getMemberLevel(Player player) {
        return getMemberLevel(player.getUniqueId());
    }

    public void donate(double amount) {
        if (amount <= 0)
            return;
        deposit += amount;
    }

    public String fullInfo() {
        return ConfigData.guild_full_info.replace("%tag%", this.tag)
                .replace("%name%", this.fullName)
                .replace("%money%", String.valueOf(this.deposit))
                .replace("%level%", String.valueOf(this.level))
                .replace("%max_level%", String.valueOf(ConfigData.maxLevel))
                .replace("%exp%", String.valueOf(this.exp))
                .replace("%need_exp%", String.valueOf(ConfigData.levels.get(this.level)))
                .replace("%quest_name%", (activeQuest != null ? activeQuest.getName() : "null"))
                .replace("%date%", getTimeString())
                .replace("%members%", members.entrySet().stream().sorted(Map.Entry.<UUID, MemberLevel>comparingByValue().reversed()).map(o -> Guilds.getInstance().getServer().getOfflinePlayer(o.getKey()).getName() + ": " + o.getValue()).collect(Collectors.joining("\n ", "\n ", "")))
                + (Guilds.hasImmunity(this) ? "\n" + ConfigData.guild_no_cooldown.replace("%time%", Guilds.getTimeLeftString(new Date(Guilds.getImmunity(this)))) : "")
                + (Guilds.hasBonusExp(this) ? "\n" + ConfigData.guild_bonus_exp.replace("%time%", Guilds.getTimeLeftString(new Date(Guilds.getBonusExpTime(this)))).replace("%bonus%", String.valueOf(Guilds.getBonusExp(this))) : "")
                ;
    }

    public String info() {
        return ConfigData.guild_info.replace("%tag%", this.tag)
                .replace("%name%", this.fullName)
                .replace("%level%", String.valueOf(this.level))
                .replace("%max_level%", String.valueOf(ConfigData.maxLevel))
                .replace("%exp%", String.valueOf(this.exp))
                .replace("%need_exp%", String.valueOf(ConfigData.levels.get(this.level)))
                .replace("%members%", members.entrySet().stream().sorted(Map.Entry.<UUID, MemberLevel>comparingByValue().reversed()).map(o -> Guilds.getInstance().getServer().getOfflinePlayer(o.getKey()).getName() + ": " + o.getValue()).collect(Collectors.joining("\n ", "\n ", "")));
    }

    public boolean setActiveQuest(Quest activeQuest) {
        if (this.activeQuest != null)
            return false;
        this.activeQuest = activeQuest.clone();
        return true;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    private String getTimeString() {
        if (Guilds.hasImmunity(this))
            return "00:00:00";
        long millis = (long) (this.questFinished.getTime() + (ConfigData.quest_delay * 3600000) - System.currentTimeMillis());
        if (millis <= 0)
            return "00:00:00";
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        return hms;
    }

    public boolean isCooldownEnded() {
        return System.currentTimeMillis() - this.getQuestFinished().getTime() > ConfigData.quest_delay * 3600000 || Guilds.hasImmunity(this);
    }
    
    public void giveBonus(Player player){
        if (this.getLevel() >= 5) {
            player.addEffect(Effect.getEffect(Effect.REGENERATION).setDuration(1728000));
        }
        if (this.getLevel() >= 15) {
            player.addEffect(Effect.getEffect(Effect.STRENGTH).setDuration(1728000));
        }
        if (this.getLevel() >= 20) {
            player.addEffect(Effect.getEffect(Effect.HASTE).setDuration(1728000));
        }
    }

    public void removeBonus(Player player){
        if (this.getLevel() >= 5) {
            if(player.hasEffect(Effect.REGENERATION) && player.getEffect(Effect.REGENERATION).getAmplifier() == 0)
                player.removeEffect(Effect.REGENERATION);
        }
        if (this.getLevel() >= 15) {
            if(player.hasEffect(Effect.STRENGTH) && player.getEffect(Effect.STRENGTH).getAmplifier() == 0)
                player.removeEffect(Effect.STRENGTH);
        }
        if (this.getLevel() >= 20) {
            if(player.hasEffect(Effect.HASTE) && player.getEffect(Effect.HASTE).getAmplifier() == 0)
                player.removeEffect(Effect.HASTE);
        }
    }
}

