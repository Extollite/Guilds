package pl.extollite.guilds.window.manage;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import me.onebone.economyapi.EconomyAPI;
import pl.extollite.guilds.data.ConfigData;

public class DonateGuildWindow extends FormWindowCustom {
    public DonateGuildWindow(Player player) {
        super(ConfigData.board_donate);
        this.addElement(new ElementSlider(ConfigData.donate_amount, 0, (float)EconomyAPI.getInstance().myMoney(player), 1));
    }
}
