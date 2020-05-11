package pl.extollite.guilds.window.quest;

import cn.nukkit.form.window.FormWindowModal;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.quest.Quest;

public class QuestAcceptGuildWindow extends FormWindowModal {
    public QuestAcceptGuildWindow(Quest quest) {
        super(quest.getName(), quest.info(), ConfigData.window_accept, ConfigData.window_decline);
    }
}
