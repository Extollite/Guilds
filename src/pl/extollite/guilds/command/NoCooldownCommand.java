package pl.extollite.guilds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import java.util.HashMap;
import java.util.Map;

public class NoCooldownCommand extends CommandManager {

    public NoCooldownCommand() {
        super("guildnocooldown", "", "/guild nocooldown");
        Map<String, CommandParameter[]> parameters = new HashMap<>();
        parameters.put("nocooldown", new CommandParameter[]{
                new CommandParameter("Guild name", false, GuildManager.getGuildsTags().toArray(new String[0])),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("guild.command.nocooldown", null, Permission.DEFAULT_OP);
        Guilds.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender.hasPermission("guild.command.nocooldown") && args.length == 1){
            Guild guild = GuildManager.getGuildByTag(args[0]);

        }
        return true;
    }
}
