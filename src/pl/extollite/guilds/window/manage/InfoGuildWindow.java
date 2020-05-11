package pl.extollite.guilds.window.manage;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;

public class InfoGuildWindow extends FormWindowSimple {
    public InfoGuildWindow(Guild guild, Player player) {
        super(guild.getTag(), guild.info());
        this.addButton(new ElementButton(ConfigData.window_back));
        this.addButton(new ElementButton(ConfigData.window_close));
    }
}
