package pl.extollite.guilds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.window.manage.InfoGuildWindow;

import java.util.HashMap;
import java.util.Map;

public class BoardCommand extends CommandManager {

    public BoardCommand() {
        super("guildboard", "", "/guild board");
        Map<String, CommandParameter[]> parameters = new HashMap<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("guild.command.board", null, Permission.DEFAULT_TRUE);
        Guilds.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
           if(GuildManager.hasPlayerGuild(p)){
               p.showFormWindow(new InfoGuildWindow(GuildManager.getPlayerGuild(p), p));
                return true;
           }
            p.sendMessage(ConfigData.prefix+ConfigData.cmd_info_no_guild);
        }
        return true;
    }
}
