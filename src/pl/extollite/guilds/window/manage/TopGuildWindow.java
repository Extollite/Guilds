package pl.extollite.guilds.window.manage;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.manager.GuildManager;

public class TopGuildWindow extends FormWindowSimple {
    public TopGuildWindow() {
        super(ConfigData.top_title, ConfigData.top_content+GuildManager.getGuildListString());
        this.addButton(new ElementButton(ConfigData.window_close));
    }
}