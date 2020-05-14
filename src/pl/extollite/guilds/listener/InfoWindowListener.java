package pl.extollite.guilds.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.manager.InviteManager;
import pl.extollite.guilds.window.AcceptGuildWindow;
import pl.extollite.guilds.window.manage.BoardGuildWindow;
import pl.extollite.guilds.window.manage.FullInfoGuildWindow;
import pl.extollite.guilds.window.manage.InfoGuildWindow;
import pl.extollite.guilds.window.manage.InfoPickGuildWindow;

public class InfoWindowListener implements Listener {
    @EventHandler
    public void onResponse(PlayerFormRespondedEvent event) {
        if (event.wasClosed())
            return;
        Player player = event.getPlayer();
        if (event.getWindow() instanceof InfoPickGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            String tag = response.getClickedButton().getText();
            Guild guild = GuildManager.getGuildByTag(tag);
            player.showFormWindow(new InfoGuildWindow(guild));
        } else if (event.getWindow() instanceof InfoGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            if (response.getClickedButtonId() == 0) {
                player.showFormWindow(new InfoPickGuildWindow());
            }
        }
    }
}
