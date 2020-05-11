package pl.extollite.guilds.window.manage;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;

import static pl.extollite.guilds.manager.GuildManager.MemberLevel.*;

public class BoardGuildWindow extends FormWindowSimple {
    public BoardGuildWindow(Guild guild, Player player) {
        super(guild.getTag(), ConfigData.board_content);
        this.addButton(new ElementButton(ConfigData.board_info));
        this.addButton(new ElementButton(ConfigData.board_quest));
        this.addButton(new ElementButton(ConfigData.board_donate));
        this.addButton(new ElementButton(ConfigData.board_shop));
        if(guild.getMemberLevel(player).getId() >= King.getId()){
            this.addButton(new ElementButton(ConfigData.board_invite));
        }
        if(guild.getMemberLevel(player).getId() >= CoLeader.getId()){
            this.addButton(new ElementButton(ConfigData.board_kick));
        }
        if(guild.getMemberLevel(player) == Leader){
            this.addButton(new ElementButton(ConfigData.board_promote));
            this.addButton(new ElementButton(ConfigData.board_delete));
        }
        this.addButton(new ElementButton(ConfigData.window_close));
    }
}
