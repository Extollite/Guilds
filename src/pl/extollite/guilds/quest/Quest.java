package pl.extollite.guilds.quest;

import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import pl.extollite.guilds.data.ConfigData;

import static pl.extollite.guilds.manager.QuestManager.QuestType;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Getter
public abstract class Quest {
    private String id;
    private QuestType type;
    private String name;
    private int exp;
    protected Map<Object, Integer> components = new HashMap<>();

    public Quest(String  id, QuestType type, String name, int exp) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.exp = exp;
    }

    public ConfigSection serialize(){
        return null;
    }

    public boolean removeComponent(Object key, int amount){
        if(components.containsKey(key)){
            int currAmount = components.get(key);
            if(currAmount <= amount){
                components.remove(key);
            } else {
                components.put(key, currAmount - amount);
            }
            if(components.isEmpty())
                return true;
        }
        return false;
    }

    public boolean removeComponent(Object id){
        return removeComponent(id, 1);
    }

    public String getComponentsString(){
        return "";
    }

    public String info(){
        return ConfigData.quest_info.replace("%name%", name)
                .replace("%exp%", String.valueOf(exp))
                .replace("%type%", type.toString())
                .replace("%components%", getComponentsString());
    }
}
