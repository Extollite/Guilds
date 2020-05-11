package pl.extollite.guilds.window.quest;

import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.quest.Quest;

public class QuestDonateAmountWindow extends FormWindowCustom {
    public QuestDonateAmountWindow(Quest quest, String name, int amount) {
        super(quest.getName());
        this.addElement(new ElementLabel(ConfigData.quest_collect_pick_amount));
        this.addElement(new ElementLabel(name));
        this.addElement(new ElementSlider("", 0, amount,1 ));
    }
}
