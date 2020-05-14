package pl.extollite.guilds.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.window.manage.BoardGuildWindow;
import pl.extollite.guilds.window.manage.InfoGuildWindow;
import pl.extollite.guilds.window.manage.InfoPickGuildWindow;
import pl.extollite.guilds.window.manage.ShopGuildWindow;

import java.util.List;
import java.util.Map;

public class ShopListener implements Listener {

    @EventHandler
    public void onResponse(PlayerFormRespondedEvent event) {
        if (event.wasClosed())
            return;
        Player player = event.getPlayer();
        if (event.getWindow() instanceof ShopGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            String name = response.getClickedButton().getText();
            if(name.equals(ConfigData.window_close))
                return;
            Guild guild = GuildManager.getPlayerGuild(player);
            if(name.equals(ConfigData.window_back)){
                player.showFormWindow(new BoardGuildWindow(guild, player));
                return;
            }
            String[] split = name.split("\n");
            Map.Entry<Double, List<String>> entry = ConfigData.shops.get(split[0]);
            if(guild.getDeposit() < entry.getKey()){
                player.sendMessage(ConfigData.prefix+ConfigData.shop_not_enough_money);
            } else {
                guild.setDeposit(guild.getDeposit() - entry.getKey());
                for(String command : entry.getValue()){
                    Guilds.getInstance().getServer().dispatchCommand(Guilds.getInstance().getServer().getConsoleSender(), command.replace("%player_name%", player.getName()));
                }
            }
        }
    }
}
