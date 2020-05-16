package pl.extollite.guilds.window.quest;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.QuestManager;
import pl.extollite.guilds.quest.Quest;

import java.util.Date;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

public class QuestPickWindow extends FormWindowSimple {
    public QuestPickWindow(Guild guild) {
        super(ConfigData.board_quest, ConfigData.quest_pick_content);
        for(Quest quest : QuestManager.getQuests()){
            if(guild.getLastQuest().equals(quest.getId()) && !guild.isCooldownEnded())
                continue;
            this.addButton(new ElementButton(quest.getName()));
        }
        this.addButton(new ElementButton(ConfigData.window_back));
    }
}
