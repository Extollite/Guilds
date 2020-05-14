package pl.extollite.guilds.window.manage;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;

import java.util.Map;
import java.util.UUID;

public class InfoPickGuildWindow extends FormWindowSimple {
    public InfoPickGuildWindow() {
        super(ConfigData.guild_info_title, ConfigData.guild_info_content);
        for(String tag : GuildManager.getGuildsTags()){
            this.addButton(new ElementButton(tag));
        }
        this.addButton(new ElementButton(ConfigData.window_close));
    }
}
