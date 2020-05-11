package pl.extollite.guilds.window.quest;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.quest.Quest;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

public class QuestActiveWindow extends FormWindowSimple {
    public QuestActiveWindow(Quest quest) {
        super(quest.getName(), ConfigData.quest_active_content+quest.getType().toString()+":\n"+quest.getComponentsString());
        if(quest.getType() == QuestType.COLLECT){
            this.addButton(new ElementButton(ConfigData.quest_collect_donate));
        }
        this.addButton(new ElementButton(ConfigData.window_close));
    }
}
