package pl.extollite.guilds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import java.util.HashMap;
import java.util.Map;

public class LeaveCommand extends CommandManager {

    public LeaveCommand() {
        super("guildleave", "", "/guild leave");
        Map<String, CommandParameter[]> parameters = new HashMap<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("guild.command.leave", null, Permission.DEFAULT_TRUE);
        Guilds.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!GuildManager.hasPlayerGuild(p)) {
                p.sendMessage(ConfigData.prefix + ConfigData.cmd_info_no_guild);
                return true;
            }
            Guild guild = GuildManager.getPlayerGuild(p);
            if(guild.getMemberLevel(p) == GuildManager.MemberLevel.Leader){
                p.sendMessage(ConfigData.prefix + ConfigData.cmd_create_in_guild);
                return true;
            }
            guild.removeMember(p);
            GuildManager.removePlayerGuild(p);
            guild.save();
            Guilds.getInstance().getServer().broadcastMessage(ConfigData.prefix + ConfigData.leave_success_announce.replace("%player%", p.getName()).replace("%tag%", guild.getTag()));
        }
        return true;
    }
}
