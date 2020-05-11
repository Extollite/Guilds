package pl.extollite.guilds.window.manage;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import java.util.Map;
import java.util.UUID;

public class KickGuildWindow extends FormWindowSimple {
    public KickGuildWindow(Guild guild) {
        super(ConfigData.board_kick, ConfigData.invite_content);
        for(Map.Entry<UUID, GuildManager.MemberLevel> player : guild.getMembers().entrySet()){
            if(player.getValue() == GuildManager.MemberLevel.Leader)
                continue;
            this.addButton(new ElementButton(Guilds.getInstance().getServer().getOfflinePlayer(player.getKey()).getName()));
        }
        this.addButton(new ElementButton(ConfigData.window_back));
    }
}
