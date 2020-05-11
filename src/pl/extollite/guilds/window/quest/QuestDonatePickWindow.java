package pl.extollite.guilds.window.quest;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.quest.Quest;
import pl.extollite.guilds.quest.QuestIdMeta;
import pl.extollite.guilds.utils.IdMeta;

import java.util.Map;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

public class QuestDonatePickWindow extends FormWindowSimple {
    public QuestDonatePickWindow(Quest quest, Player player) {
        super(quest.getName(), ConfigData.quest_collect_pick);
        for(Map.Entry<Object, Integer> entry : quest.getComponents().entrySet()){
            IdMeta idMeta = (IdMeta)entry.getKey();
            Item item = Item.get(idMeta.getId(), idMeta.getMeta());
            if(!player.getInventory().contains(item))
                continue;
            if(idMeta.getId() < 255){
                addButton(new ElementButton(Block.get(idMeta.getId(), idMeta.getMeta()).getName()));
            } else {
                addButton(new ElementButton(item.getName()));
            }
        }
    }
}
