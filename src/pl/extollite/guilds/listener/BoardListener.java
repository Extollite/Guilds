package pl.extollite.guilds.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;
import pl.extollite.guilds.Guilds;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;
import pl.extollite.guilds.manager.GuildManager;
import pl.extollite.guilds.manager.InviteManager;
import pl.extollite.guilds.window.manage.*;
import pl.extollite.guilds.window.quest.QuestActiveWindow;
import pl.extollite.guilds.window.quest.QuestPickWindow;

import java.util.Optional;
import java.util.UUID;

public class BoardListener implements Listener {
    @EventHandler
    public void onResponse(PlayerFormRespondedEvent event) {
        if (event.wasClosed())
            return;
        Player player = event.getPlayer();
        if (event.getWindow() instanceof BoardGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            Guild guild = GuildManager.getPlayerGuild(player);
            if(response.getClickedButton().getText().equals(ConfigData.window_close))
                return;
            switch (response.getClickedButtonId()) {
                case 0:
                    player.showFormWindow(new FullInfoGuildWindow(guild, player));
                    break;
                case 1:
                    if(guild.getActiveQuest() != null){
                        player.showFormWindow(new QuestActiveWindow(guild.getActiveQuest()));
                    } else if(guild.getMemberLevel(player).getId() > GuildManager.MemberLevel.King.getId()) {
                        player.showFormWindow(new QuestPickWindow(guild));
                    }
                    break;
                case 2:
                    player.showFormWindow(new DonateGuildWindow(player));
                    break;
                case 3:
                    if(guild.getLevel() >= 5){
                        player.showFormWindow(new ShopGuildWindow(guild));
                    } else {
                        player.sendMessage(ConfigData.prefix+ConfigData.shop_level);
                    }
                    break;
                case 4:
                    player.showFormWindow(new InviteGuildWindow());
                    break;
                case 5:
                    player.showFormWindow(new KickGuildWindow(guild));
                    break;
                case 6:
                    player.showFormWindow(new PromotePickGuildWindow(guild, player));
                    break;
                case 7:
                    player.showFormWindow(new DeleteGuildWindow());
                    break;
            }
        } else if (event.getWindow() instanceof FullInfoGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            if (response.getClickedButtonId() == 0) {
                player.showFormWindow(new BoardGuildWindow(GuildManager.getPlayerGuild(player), player));
            }
        } else if (event.getWindow() instanceof DonateGuildWindow) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            float amount = response.getSliderResponse(0);
            if(amount > 0 && EconomyAPI.getInstance().reduceMoney(player, amount) == EconomyAPI.RET_SUCCESS){
                GuildManager.getPlayerGuild(player).donate(amount);
                player.sendMessage(ConfigData.prefix+ConfigData.donate_success.replace("%money%", String.valueOf(amount)));
                return;
            }
            player.sendMessage(ConfigData.prefix+ConfigData.donate_not_enough_money);
        } else if (event.getWindow() instanceof InviteGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            String name = response.getClickedButton().getText();
            if(name.equals(ConfigData.window_back)){
                player.showFormWindow(new BoardGuildWindow(GuildManager.getPlayerGuild(player), player));
                return;
            }
            Player invited = Guilds.getInstance().getServer().getPlayerExact(name);
            if (GuildManager.hasPlayerGuild(invited)) {
                player.sendMessage(ConfigData.prefix + ConfigData.invite_in_guild);
                return;
            }
            if (!InviteManager.putInvitation(invited, player)) {
                player.sendMessage(ConfigData.prefix + ConfigData.invite_pending);
                return;
            }
        } else if (event.getWindow() instanceof KickGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            String name = response.getClickedButton().getText();
            if(name.equals(ConfigData.window_back)){
                player.showFormWindow(new BoardGuildWindow(GuildManager.getPlayerGuild(player), player));
                return;
            }
            UUID uuid = Guilds.getInstance().getServer().getOfflinePlayer(name).getUniqueId();
            Guild guild = GuildManager.getPlayerGuild(player);
            guild.removeMember(uuid);
            GuildManager.removePlayerGuild(uuid);
            guild.save();
            Optional<Player> optPlayer = Guilds.getInstance().getServer().getPlayer(uuid);
            optPlayer.ifPresent(guild::removeBonus);
            Guilds.getInstance().getServer().broadcastMessage(ConfigData.prefix+ConfigData.leave_success_announce.replace("%player%", player.getName()).replace("%tag%", guild.getTag()));
        } else if (event.getWindow() instanceof PromotePickGuildWindow) {
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            String name = response.getClickedButton().getText();
            if(name.equals(ConfigData.window_back)){
                player.showFormWindow(new BoardGuildWindow(GuildManager.getPlayerGuild(player), player));
                return;
            }
            UUID uuid = Guilds.getInstance().getServer().getOfflinePlayer(name).getUniqueId();
            GuildManager.MemberLevel level = GuildManager.getPlayerGuild(player).getMemberLevel(uuid);
            player.showFormWindow(new PromoteGuildWindow(name, level.getId()));
        } else if (event.getWindow() instanceof PromoteGuildWindow) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            int id = response.getDropdownResponse(0).getElementID();
            String name = ((ElementDropdown)((PromoteGuildWindow)event.getWindow()).getElements().get(0)).getText();
            GuildManager.MemberLevel level = GuildManager.MemberLevel.fromId(id);
            UUID uuid = Guilds.getInstance().getServer().getOfflinePlayer(name).getUniqueId();
            GuildManager.getPlayerGuild(player).setMemberLevel(uuid, level);
            GuildManager.getPlayerGuild(player).save();
        } else if (event.getWindow() instanceof DeleteGuildWindow) {
            FormResponseModal response = (FormResponseModal)event.getResponse();
            if(response.getClickedButtonId() == 0){
                Guild guild = GuildManager.getPlayerGuild(player);
                for(UUID uuid : guild.getMembers().keySet()){
                    GuildManager.removePlayerGuild(uuid);
                    Optional<Player> optPlayer = Guilds.getInstance().getServer().getPlayer(uuid);
                    optPlayer.ifPresent(guild::removeBonus);
                }
                GuildManager.removeGuild(guild);
                Config guilds = GuildManager.getGuildsConfig();
                guilds.remove(guild.getTag());
                guilds.save(true);
                player.sendMessage(ConfigData.prefix+ConfigData.delete_success.replace("%name%", guild.getFullName()).replace("%tag%", guild.getTag()));
                Guilds.getInstance().getServer().broadcastMessage(ConfigData.prefix+ConfigData.delete_success_announce.replace("%name%", guild.getFullName()).replace("%tag%", guild.getTag()).replace("%player%", player.getName()));
            }
        }
    }
}
