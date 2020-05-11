package pl.extollite.guilds.window.manage;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import me.onebone.economyapi.EconomyAPI;
import pl.extollite.guilds.data.ConfigData;

import java.util.Arrays;

public class PromoteGuildWindow extends FormWindowCustom {
    public PromoteGuildWindow(String name, int defaultLevel) {
        super(ConfigData.board_promote);
        this.addElement(new ElementDropdown(name, Arrays.asList("Member", "King", "CoLeader", "Leader"), defaultLevel));
    }
}
