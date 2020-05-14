package pl.extollite.guilds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.window.manage.TopGuildWindow;

import java.util.HashMap;
import java.util.Map;

public class TopCommand extends CommandManager {

    public TopCommand() {
        super("guildtop", "", "/guild top");
        Map<String, CommandParameter[]> parameters = new HashMap<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("guild.command.top", null, Permission.DEFAULT_TRUE);
        Guilds.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
           p.showFormWindow(new TopGuildWindow());
        }
        return true;
    }
}
