package pl.extollite.guilds.manager;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import jdk.internal.jline.internal.Nullable;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.quest.Quest;
import pl.extollite.guilds.quest.QuestId;
import pl.extollite.guilds.quest.QuestIdMeta;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class QuestManager {
    private static List<Quest> quests = new LinkedList<>();
    public enum QuestType {
        MOB(0),
        DESTROY(1),
        COLLECT(2);

        private static final Map<Integer, QuestType> LOOKUP = stream(values()).collect(toMap(QuestType::getId, x -> x));

        public final int id;

        QuestType(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }

        @Nullable
        public static QuestType fromId(int id) {
            return LOOKUP.get(id);
        }

        @Override
        public String toString() {
            switch (id){
                case 0:
                    return ConfigData.quest_active_mob;
                case 1:
                    return ConfigData.quest_active_break;
                case 2:
                    return ConfigData.quest_active_collect;
                default:
                    return "";
            }
        }
    }

    public static void init(){
        Config quests = new Config(Guilds.getInstance().getDataFolder()+"/quests.yml", Config.YAML);
        for(String key : quests.getKeys(false)){
            QuestManager.quests.add(serializeQuest(quests, key, key));
        }
    }

    public static Quest getQuest(String name){
        for(Quest quest : quests){
            if(quest.getName().equalsIgnoreCase(name))
                return quest;
        }
        return null;
    }
    public static Quest serializeQuest(Config file, String path){
        return serializeQuest(file, path, file.getString(path+".id"));
    }

    public static Quest serializeQuest(Config file, String path, String id){
        QuestType type = QuestType.fromId(file.getInt(path+".type"));
        String name = file.getString(path+".name");
        int exp =  file.getInt(path+".exp");
        if(type == QuestType.MOB)
            return new QuestId(id, type, name, exp, file.getSection(path+".components"));
        return new QuestIdMeta(id, type, name, exp, file.getSection(path+".components"));
    }

    public static List<Quest> getQuests() {
        return quests;
    }
}
