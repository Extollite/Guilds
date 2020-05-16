package pl.extollite.guilds.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import me.onebone.economyapi.EconomyAPI;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.window.manage.CreateGuildWindow;

public class CreateWindowListener implements Listener {
    @EventHandler
    public void onResponse(PlayerFormRespondedEvent event) {
        if (event.wasClosed())
            return;
        if(event.getWindow() instanceof CreateGuildWindow) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            String tag = response.getInputResponse(1);
            String name = response.getInputResponse(2);
            Player player = event.getPlayer();
            tag = tag.toUpperCase().replaceAll("[^A-Z]", "");
            if(tag.length() != 4){
                player.sendMessage(ConfigData.prefix+ConfigData.create_incorrect_tag.replace("%tag_size%", String.valueOf(ConfigData.tag_size)));
                return;
            }
            boolean nonalphanumeric = name.matches("^.*[^a-zA-Z0-9 ].*$");
            if(nonalphanumeric || name.length() > ConfigData.name_size){
                player.sendMessage(ConfigData.prefix+ConfigData.create_incorrect_name.replace("%name_size%", String.valueOf(ConfigData.name_size)));
                return;
            }
            if(EconomyAPI.getInstance().reduceMoney(player, ConfigData.guild_cost) != EconomyAPI.RET_SUCCESS){
                player.sendMessage(ConfigData.prefix+ConfigData.create_not_enough_money.replace("%money%", String.valueOf(ConfigData.guild_cost)));
                return;
            }
            Guild guild = new Guild(name, tag, player);
            if(GuildManager.putGuild(guild)){
                GuildManager.setPlayerGuild(player, guild);
                guild.save();
                player.sendMessage(ConfigData.prefix+ConfigData.create_success.replace("%name%", name).replace("%tag%", tag));
                Guilds.getInstance().getServer().broadcastMessage(ConfigData.prefix+ConfigData.create_success_announce.replace("%name%", name).replace("%tag%", tag).replace("%player%", player.getName()));
            } else {
                player.sendMessage(ConfigData.prefix+ConfigData.create_same_tag);
            }
        }
    }

}
