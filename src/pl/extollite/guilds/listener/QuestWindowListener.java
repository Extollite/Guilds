package pl.extollite.guilds.listener;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.manager.QuestManager;
import pl.extollite.guilds.quest.Quest;
import pl.extollite.guilds.quest.QuestIdMeta;
import pl.extollite.guilds.utils.IdMeta;
import pl.extollite.guilds.window.manage.BoardGuildWindow;
import pl.extollite.guilds.window.quest.*;

import java.util.Map;

public class QuestWindowListener implements Listener {
    @EventHandler
    public void onResponse(PlayerFormRespondedEvent event){
        if(event.wasClosed())
            return;
        if(event.getWindow() instanceof QuestActiveWindow){
            FormResponseSimple response = (FormResponseSimple)event.getResponse();
            if(response.getClickedButton().getText().equals(ConfigData.quest_collect_donate)){
                event.getPlayer().showFormWindow(new QuestDonatePickWindow(GuildManager.getPlayerGuild(event.getPlayer()).getActiveQuest(), event.getPlayer()));
            }
        } else if (event.getWindow() instanceof QuestDonatePickWindow){
            FormResponseSimple response = (FormResponseSimple)event.getResponse();
            Player player = event.getPlayer();
            String buttonText = response.getClickedButton().getText();
            Quest quest = GuildManager.getPlayerGuild(player).getActiveQuest();
/*            for(Map.Entry<Object, Integer> entry : quest.getComponents().entrySet()){
                IdMeta idMeta = (IdMeta)entry.getKey();
                String name;
                Item item = Item.get(idMeta.getId(), idMeta.getMeta());
                int amount = entry.getValue();
                if(idMeta.getId() < 255){
                    Block block = Block.get(idMeta.getId(), idMeta.getMeta());
                    name = block.getName();
                } else {
                    name = item.getName();
                }
                if(!name.equals(buttonText))
                    continue;
                Map<Integer, Item> items = player.getInventory().all(item);
                int couner = 0;
                if(!items.isEmpty()){
                    for(Item itemInv : items.values()){
                        couner+=itemInv.getCount();
                        if(couner >= amount){
                            couner = amount;
                            break;
                        }
                    }
                } else {
                    player.sendMessage(ConfigData.prefix+ConfigData.player_donate_donthave);
                    return;
                }
                player.showFormWindow(new QuestDonateAmountWindow(quest, name, couner));
                return;
            }*/
            Item item = Item.fromString(buttonText);
            int amount = quest.getComponents().get(new IdMeta(item.getId(), item.getDamage()));
            Map<Integer, Item> items = player.getInventory().all(item);
            int couner = 0;
            if(!items.isEmpty()){
                for(Item itemInv : items.values()){
                    couner+=itemInv.getCount();
                    if(couner >= amount){
                        couner = amount;
                        break;
                    }
                }
            } else {
                player.sendMessage(ConfigData.prefix+ConfigData.player_donate_donthave);
                return;
            }
            player.showFormWindow(new QuestDonateAmountWindow(quest, buttonText, couner));
        }
        else if(event.getWindow() instanceof QuestDonateAmountWindow){
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            String name = response.getLabelResponse(1);
            int amount = (int)response.getSliderResponse(2);
            if(amount == 0)
                return;
            Item item = Item.fromString(name);
            Player player = event.getPlayer();
            Quest quest = GuildManager.getPlayerGuild(player).getActiveQuest();
            if(((QuestIdMeta) quest).removeComponent(item.getId(), item.getDamage(), amount)){
                GuildManager.getPlayerGuild(player).finalizeQuest();
            }
            GuildManager.getPlayerGuild(event.getPlayer()).save(false);
            Map<Integer, Item> items = player.getInventory().all(item);
            if(!items.isEmpty()){
                System.out.println("1sd");
                for(Map.Entry<Integer, Item> itemInv : items.entrySet()){
                    if(amount >= itemInv.getValue().getCount()){
                        amount -= itemInv.getValue().getCount();
                        player.getInventory().setItem(itemInv.getKey(), Item.get(BlockID.AIR));
                    } else {
                        Item toSet = itemInv.getValue();
                        toSet.setCount(toSet.getCount() - amount);
                        player.getInventory().setItem(itemInv.getKey(), toSet);
                        amount = 0;
                    }
                    if(amount == 0)
                        break;
                }
            }
            player.sendAllInventories();
        } else if(event.getWindow() instanceof QuestPickWindow){
            FormResponseSimple response = (FormResponseSimple)event.getResponse();
            String name = response.getClickedButton().getText();
            if(name.equals(ConfigData.window_back)){
                event.getPlayer().showFormWindow(new BoardGuildWindow(GuildManager.getPlayerGuild(event.getPlayer()), event.getPlayer()));
                return;
            }
            Quest quest = QuestManager.getQuest(name);
            if(quest == null)
                return;
            event.getPlayer().showFormWindow(new QuestAcceptGuildWindow(quest));
        } else if (event.getWindow() instanceof QuestAcceptGuildWindow) {
            FormResponseModal response = (FormResponseModal)event.getResponse();
            Player player = event.getPlayer();
            if(response.getClickedButtonId() == 0){
                String name = ((QuestAcceptGuildWindow) event.getWindow()).getTitle();
                Quest quest = QuestManager.getQuest(name);
                if(quest == null)
                    return;
                Guild guild = GuildManager.getPlayerGuild(player);
                if(!guild.setActiveQuest(quest)){
                    player.sendMessage(ConfigData.prefix+ConfigData.quest_active_different);
                }
                player.sendMessage(ConfigData.prefix+ConfigData.quest_success.replace("%name%", quest.getName()));
                guild.save(false);
            } else {
                player.showFormWindow(new BoardGuildWindow(GuildManager.getPlayerGuild(player), player));
            }
        }
    }
}
