package pl.extollite.guilds.quest;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

@Getter
public class QuestId extends Quest{

    public QuestId(String id, QuestType type, String name, int exp, ConfigSection section) {
        super(id, type, name, exp);
        for(String componentKey : section.getKeys(false)){
            components.put(Integer.parseInt(componentKey), section.getInt(componentKey));
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
            section.set(id+".components."+entry.getKey(), entry.getValue());
        }
        return section;
    }

    @Override
    public String getComponentsString() {
        StringJoiner joiner = new StringJoiner("\n");
        for(Map.Entry<Object, Integer> entry : components.entrySet()){
            joiner.add(Entity.createEntity((Integer)entry.getKey(), new Position(0,0,0)).getName()+": "+entry.getValue());
        }
        return joiner.toString();
    }
}
