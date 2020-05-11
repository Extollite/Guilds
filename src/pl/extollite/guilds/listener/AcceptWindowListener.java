package pl.extollite.guilds.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseModal;
import pl.extollite.guilds.manager.InviteManager;
import pl.extollite.guilds.window.AcceptGuildWindow;

public class AcceptWindowListener implements Listener {
    @EventHandler
    public void onResponse(PlayerFormRespondedEvent event) {
        if (event.wasClosed())
            return;
        if (event.getWindow() instanceof AcceptGuildWindow) {
            FormResponseModal response = (FormResponseModal)event.getResponse();
            if(response.getClickedButtonId() == 0){
                InviteManager.acceptInvite(event.getPlayer());
            } else {
                InviteManager.declineInvite(event.getPlayer());
            }
        }
    }
}
