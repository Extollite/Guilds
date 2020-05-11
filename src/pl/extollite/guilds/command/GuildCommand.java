package pl.extollite.guilds.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.utils.TextFormat;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;

import java.util.*;

public class GuildCommand extends CommandManager {

    private static Map<String, Command> commands = null;

    public GuildCommand() {
        super("guild", "", "/guild <command>");
        Permission permission = new Permission("guilds.command", null, Permission.DEFAULT_TRUE);
        Guilds.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
        commands = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!this.testPermissionSilent(sender)) {
            sender.sendMessage(ConfigData.cmd_no_permission);
            return false;
        }
        if (args.length < 1) {
            sendUsage(sender);
            return false;
        }
        Command cmd = commands.get(args[0]);
        if (cmd == null) {
            sendUsage(sender);
            return false;
        }
        String[] newArgs = args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
        cmd.execute(sender, cmd.getName(), newArgs);
        return false;
    }

    private void updateArguments() {
        Map<String, CommandParameter[]> params = new HashMap<>();
        commands.forEach((k, v) -> {
            List<CommandParameter> p = new ArrayList<>();
            p.add(new CommandParameter(k, false, new String[]{k}));
            v.getCommandParameters().values().forEach(s -> p.addAll(Arrays.asList(s)));
            params.put(k, p.toArray(new CommandParameter[0]));
        });
        this.setCommandParameters(params);
    }

    public void registerCommand(Command command) {
        commands.put(command.getName().replace("guild", "").toLowerCase(), command);
        this.updateArguments();
    }

    static public void sendUsage(CommandSender sender) {
        sender.sendMessage(TextFormat.GREEN + "-- Guild " + Guilds.getInstance().getDescription().getVersion() + " Commands --");
        for(Command command : commands.values()){
            sender.sendMessage(command.getUsage());
        }
    }

}
