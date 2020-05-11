package pl.extollite.guilds.window.manage;

import cn.nukkit.form.window.FormWindowModal;
import pl.extollite.guilds.data.ConfigData;

public class DeleteGuildWindow extends FormWindowModal {
    public DeleteGuildWindow() {
        super(ConfigData.board_delete, ConfigData.delete_content, ConfigData.window_accept, ConfigData.window_decline);
    }
}
