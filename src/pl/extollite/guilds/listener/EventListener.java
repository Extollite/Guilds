package pl.extollite.guilds.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.potion.Effect;
import cn.yescallop.essentialsnk.EssentialsAPI;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

import pl.extollite.guilds.quest.Quest;
import pl.extollite.guilds.quest.QuestIdMeta;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInHand().getEnchantment(Enchantment.ID_SILK_TOUCH) != null) {
            return;
        }
        if (!ConfigData.expTurnOffLevels.contains(event.getPlayer().getLevel().getName())) {
            if (GuildManager.hasPlayerGuild(event.getPlayer())) {
                Quest quest = GuildManager.getPlayerGuild(event.getPlayer()).getActiveQuest();
                if (quest != null && quest.getType() == QuestType.DESTROY) {
                    if (((QuestIdMeta) quest).removeComponent(event.getBlock().getId(), event.getBlock().getDamage())) {
                        finlizeQuest(event.getPlayer());
                    }
                    GuildManager.getPlayerGuild(event.getPlayer()).save(false);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!ConfigData.expTurnOffLevels.contains(event.getEntity().getLevel().getName())) {
            EntityDamageEvent damageEvent = entity.getLastDamageCause();
            if (damageEvent instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) damageEvent).getDamager();
                if (damager instanceof Player) {
                    Player player = (Player) damager;
                    if (GuildManager.hasPlayerGuild(player)) {
                        Quest quest = GuildManager.getPlayerGuild(player).getActiveQuest();
                        if (quest != null && quest.getType() == QuestType.MOB) {
                            if (quest.removeComponent(entity.getNetworkId())) {
                                finlizeQuest(player);
                            }
                            GuildManager.getPlayerGuild(player).save(false);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (GuildManager.hasPlayerGuild(player)) {
            Guild guild = GuildManager.getPlayerGuild(player);
            guild.giveBonus(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (GuildManager.hasPlayerGuild(player)) {
            Guild guild = GuildManager.getPlayerGuild(player);
            guild.removeBonus(player);
        }
    }

    private void finlizeQuest(Player player) {
        GuildManager.getPlayerGuild(player).finalizeQuest();
    }

    @EventHandler
    public void onPlayerCmd(PlayerCommandPreprocessEvent ev){
        Player player = ev.getPlayer();
        if(player.isOp())
            return;
        String msg = ev.getMessage();
        msg = msg.trim();
        String[] cmd = msg.split("\\s+");
/*        Guilds.getInstance().getLogger().info(cmd[0].substring(1));*/
        if(!GuildManager.hasPlayerGuild(player))
            return;
        if(GuildManager.getPlayerGuild(player).getLevel() < 10)
            return;
        if("fly".equalsIgnoreCase(cmd[0].substring(1))){
            boolean fly = EssentialsAPI.getInstance().switchCanFly(player);
            if(fly)
                player.sendMessage(ConfigData.prefix+ConfigData.fly_active);
            else
                player.sendMessage(ConfigData.prefix+ConfigData.fly_deactive);
            ev.setCancelled();
        }
    }
}
