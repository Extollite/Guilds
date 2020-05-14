package pl.extollite.guilds.window.manage;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import pl.extollite.guilds.data.ConfigData;
import pl.extollite.guilds.data.Guild;

import java.util.List;
import java.util.Map;

public class ShopGuildWindow extends FormWindowSimple {
    public ShopGuildWindow(Guild guild) {
        super(ConfigData.board_shop, ConfigData.shop_content.replace("%money%", String.valueOf(guild.getDeposit())));
        for(Map.Entry<String, Map.Entry<Double, List<String>>> entry : ConfigData.shops.entrySet()){
            this.addButton(new ElementButton(entry.getKey()+"\n"+ConfigData.shop_price+entry.getValue().getKey()));
        }
        this.addButton(new ElementButton(ConfigData.window_back));
        this.addButton(new ElementButton(ConfigData.window_close));
    }
}
