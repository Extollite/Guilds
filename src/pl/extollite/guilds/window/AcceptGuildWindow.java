package pl.extollite.guilds.window;

import cn.nukkit.form.window.FormWindowModal;
import pl.extollite.guilds.data.ConfigData;

public class AcceptGuildWindow extends FormWindowModal {
    public AcceptGuildWindow() {
        super(ConfigData.board_invite, ConfigData.invite_window, ConfigData.window_accept, ConfigData.window_decline);
    }
}
