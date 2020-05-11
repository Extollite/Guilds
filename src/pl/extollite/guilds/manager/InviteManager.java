package pl.extollite.guilds.manager;

import cn.nukkit.Player;
import me.onebone.economyapi.EconomyAPI;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;

import java.util.HashMap;
import java.util.Map;

public class InviteManager {
    private static final Map<Player, Player> invitations = new HashMap<>();

    public static boolean putInvitation(Player invited, Player inviting){
        boolean put = invitations.putIfAbsent(invited, inviting) != null;
        if(put){
            Guilds.getInstance().getServer().getScheduler().scheduleDelayedTask(Guilds.getInstance(), () -> {
                if(invitations.containsKey(invited)){
                    invitations.remove(invited);
                    if(invited != null)
                        invited.sendMessage(ConfigData.prefix+ConfigData.invite_expire);
                    if(inviting != null)
                        inviting.sendMessage(ConfigData.prefix+ConfigData.invite_expire);
                }
            }, ConfigData.invitation_expire*20);
            Guild guild = GuildManager.getPlayerGuild(inviting);
            invited.sendMessage(ConfigData.prefix+ConfigData.invite_invite_get.replace("%tag%", guild.getTag()).replace("%player%", inviting.getName()));
            inviting.sendMessage(ConfigData.prefix+ConfigData.invite_invited);
        }
        return put;
    }

    public static void acceptInvite(Player player){
        Player inviting = invitations.remove(player);
        Guild guild = GuildManager.getPlayerGuild(inviting);
        if(guild.getMembers().size() >= ConfigData.guild_max_size){
            inviting.sendMessage(ConfigData.prefix+ConfigData.invite_size);
            player.sendMessage(ConfigData.prefix+ConfigData.invite_size);
            return;
        }
        double cost = ConfigData.guild_join_cost.get(guild.getMembers().size()+1);
        if(EconomyAPI.getInstance().reduceMoney(inviting, cost) != EconomyAPI.RET_SUCCESS){
            inviting.sendMessage(ConfigData.prefix+ConfigData.invite_money.replace("%money%", String.valueOf(cost)));
            player.sendMessage(ConfigData.prefix+ConfigData.invite_money.replace("%money%", String.valueOf(cost)));
            return;
        }
        guild.addMember(player);
        guild.save();
    }

    public static void declineInvite(Player player){
        Player inviting = invitations.remove(player);
        inviting.sendMessage(ConfigData.prefix+ConfigData.invite_decline);
    }
}
