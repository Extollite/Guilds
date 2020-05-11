package pl.extollite.guilds.window.manage;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import java.util.Map;
import java.util.UUID;

public class PromotePickGuildWindow extends FormWindowSimple {
    public PromotePickGuildWindow(Guild guild, Player player) {
        super(ConfigData.board_promote, ConfigData.invite_content);
        for(Map.Entry<UUID, GuildManager.MemberLevel> entry : guild.getMembers().entrySet()){
            if(entry.getValue() == GuildManager.MemberLevel.Leader || player.getUniqueId().equals(entry.getKey()))
                continue;
            this.addButton(new ElementButton(Guilds.getInstance().getServer().getOfflinePlayer(entry.getKey()).getName()));
        }
        this.addButton(new ElementButton(ConfigData.window_back));
    }
}
