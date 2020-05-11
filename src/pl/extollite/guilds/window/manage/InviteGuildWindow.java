package pl.extollite.guilds.window.manage;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;

public class InviteGuildWindow extends FormWindowSimple {
    public InviteGuildWindow() {
        super(ConfigData.board_invite, ConfigData.invite_content);
        for(Player player : Guilds.getInstance().getServer().getOnlinePlayers().values()){
            this.addButton(new ElementButton(player.getName()));
        }
        this.addButton(new ElementButton(ConfigData.window_back));
    }
}
