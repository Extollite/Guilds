package pl.extollite.guilds.command;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import java.util.HashMap;
import java.util.Map;

public class BonusExpCommand extends CommandManager {

    public BonusExpCommand() {
        super("guild-bonusexp", "", "/guild-bonusexp");
        Map<String, CommandParameter[]> parameters = new HashMap<>();
        parameters.put("bonusexp", new CommandParameter[]{
                new CommandParameter("Guild name", false, GuildManager.getGuildsTags().toArray(new String[0])),
                new CommandParameter("Amount", CommandParamType.FLOAT, false),
                new CommandParameter("Days", CommandParamType.FLOAT, false)
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("guild.command.bonusexp", null, Permission.DEFAULT_OP);
        Guilds.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender.hasPermission("guild.command.bonusexp") && args.length == 3){
            Guild guild = GuildManager.getGuildByTag(args[0]);
            float amount = Float.parseFloat(args[1]);
            float days = Float.parseFloat(args[2]);
            Guilds.putBonusExp(guild, amount, days);
            sender.sendMessage(ConfigData.prefix+"Guild "+guild.getTag()+" has now bonus exp x"+amount+" for "+days+" day(s)!");
        }
        return true;
    }
}
