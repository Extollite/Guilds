package pl.extollite.guilds.quest;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import pl.extollite.guilds.utils.IdMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

@Getter
public class QuestIdMeta extends Quest {
    public QuestIdMeta(String id, QuestType type, String name, int exp, ConfigSection section) {
        super(id, type, name, exp);
        for (String componentKey : section.getKeys(false)) {
            String[] split = componentKey.split(":");
            components.put(new IdMeta(Integer.parseInt(split[0]), Integer.parseInt(split[1])), section.getInt(componentKey));
        }
    }

    @Override
    public ConfigSection serialize() {
        String id = this.getId();
        ConfigSection section = new ConfigSection();
        section.set(id + ".name", getName());
        section.set(id + ".type", getType());
        section.set(id + ".exp", getExp());
        for(Map.Entry<Object, Integer> entry : components.entrySet()){
            section.set(id+".components."+entry.getKey().toString(), entry.getValue());
        }
        return section;
    }

    public boolean removeComponent(int id, int meta, int amount){
        return removeComponent(new IdMeta(id, meta), amount);
    }

    public boolean removeComponent(int id, int meta){
        return removeComponent(id, meta, 1);
    }

    @Override
    public String getComponentsString() {
        StringJoiner joiner = new StringJoiner("\n");
        for(Map.Entry<Object, Integer> entry : components.entrySet()){
            IdMeta idMeta = (IdMeta)entry.getKey();
            if(idMeta.getId() < 255){
                joiner.add(Block.get(idMeta.getId(), idMeta.getMeta()).getName()+": "+entry.getValue());
            } else {
                joiner.add(Item.get(idMeta.getId(), idMeta.getMeta()).getName()+": "+entry.getValue());
            }
        }
        return joiner.toString();
    }
}
