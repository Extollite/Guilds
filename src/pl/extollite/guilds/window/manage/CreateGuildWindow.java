package pl.extollite.guilds.window.manage;

import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import pl.extollite.guilds.data.ConfigData;

public class CreateGuildWindow extends FormWindowCustom {
    public CreateGuildWindow() {
        super(ConfigData.create_title);
        this.addElement(new ElementLabel(ConfigData.create_content.replace("%money%", String.valueOf(ConfigData.guild_cost))));
        this.addElement(new ElementInput(ConfigData.create_tag_label.replace("%tag_size%", String.valueOf(ConfigData.tag_size))));
        this.addElement(new ElementInput(ConfigData.create_full_name.replace("%name_size%", String.valueOf(ConfigData.name_size))));
    }
}
