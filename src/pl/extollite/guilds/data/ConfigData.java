package pl.extollite.guilds.data;

import cn.nukkit.utils.Config;
import pl.extollite.guilds.Guilds;

import java.util.*;

public class ConfigData {
    public static String prefix;

    public static List<String> expTurnOffLevels;
    public static List<String> bonusesTurnOffLevels;

    public static int maxLevel;
    public static Map<Integer, Integer> levels = new HashMap<>();

    public static int guild_cost;
    public static int guild_max_size;
    public static Map<Integer, Double> guild_join_cost = new HashMap<>();

    public static Map<String, Map.Entry<Double, List<String>>> shops = new HashMap<>();

    public static int tag_size;
    public static int name_size;

    public static int invitation_expire;

    public static double quest_delay;

    public static String create_title;
    public static String create_content;
    public static String create_tag_label;
    public static String create_full_name;
    public static String create_incorrect_tag;
    public static String create_incorrect_name;
    public static String create_same_tag;
    public static String create_not_enough_money;
    public static String create_success;
    public static String create_success_announce;

    public static String delete_success;
    public static String delete_success_announce;

    public static String board_content;
    public static String board_info;
    public static String board_invite;
    public static String board_promote;
    public static String board_kick;
    public static String board_quest;
    public static String board_donate;
    public static String board_delete;
    public static String board_shop;

    public static String invite_content;
    public static String invite_in_guild;
    public static String invite_expire;
    public static String invite_pending;
    public static String invite_invited;
    public static String invite_invite_get;
    public static String invite_decline;
    public static String invite_size;
    public static String invite_money;
    public static String invite_window;
    public static String invite_success_announce;

    public static String top_title;
    public static String top_content;

    public static String guild_info_title;
    public static String guild_info_content;

    public static String shop_content;
    public static String shop_price;
    public static String shop_not_enough_money;

    public static String donate_amount;
    public static String donate_success;
    public static String donate_not_enough_money;

    public static String delete_content;

    public static String leave_success_announce;

    public static String cmd_no_permission;

    public static String cmd_create_in_guild;
    public static String cmd_info_no_guild;

    public static String guild_info;

    public static String quest_info;

    public static String quest_active_content;
    public static String quest_active_mob;
    public static String quest_active_break;
    public static String quest_active_collect;
    public static String quest_collect_donate_success;

    public static String quest_pick_content;

    public static String quest_success;

    public static String quest_finished;

    public static String quest_collect_donate;
    public static String quest_collect_pick;
    public static String quest_collect_pick_amount;
    public static String player_donate_donthave;
    public static String quest_active_different;

    public static String window_close;
    public static String window_back;
    public static String window_accept;
    public static String window_decline;

    public static void init(){
        Config cfg = Guilds.getInstance().getConfig();
        prefix = cfg.getString("prefix");
        expTurnOffLevels = cfg.getStringList("exp-turnoff-levels");
        bonusesTurnOffLevels = cfg.getStringList("bonus-turnoff-levels");
        maxLevel = cfg.getInt("max-level");
        for(String key : cfg.getSection("levels").getKeys(false)){
            levels.put(Integer.parseInt(key), cfg.getInt("levels."+key));
        }

        guild_cost = cfg.getInt("guild-cost");
        guild_max_size = cfg.getInt("guild-max-size");

        for(String key : cfg.getSection("shop").getKeys(false)){
            shops.put(cfg.getString("shop."+key+".name"), new AbstractMap.SimpleImmutableEntry<>(cfg.getDouble("shop."+key+".cost"), cfg.getStringList("shop."+key+".commands")));
        }

        for(String key : cfg.getSection("guild-join-cost").getKeys(false)){
            int count = Integer.parseInt(key);
            guild_join_cost.put(count, cfg.getDouble("guild-join-cost."+key));
        }

        tag_size = cfg.getInt("tag-size");
        name_size = cfg.getInt("name-size");

        invitation_expire = cfg.getInt("invitation-expire");

        quest_delay = cfg.getDouble("quest-delay");

        Config lang = new Config(Guilds.getInstance().getDataFolder()+"/language.yml", Config.YAML);
        if(lang.getAll().isEmpty()){
            Guilds.getInstance().saveResource("language.yml", "language.yml", true);
            lang.reload();
        }
        create_title = lang.getString("create-title");
        create_content = lang.getString("create-content");
        create_tag_label = lang.getString("create-tag-label");
        create_full_name = lang.getString("create-full-name");
        create_incorrect_tag = lang.getString("create-incorrect-tag");
        create_incorrect_name = lang.getString("create-incorrect-name");
        create_same_tag = lang.getString("create-same-tag");
        create_not_enough_money = lang.getString("create-not-enough-money");
        create_success = lang.getString("create-success");
        create_success_announce = lang.getString("create-success-announce");

        delete_success = lang.getString("delete-success");
        delete_success_announce = lang.getString("delete-success-announce");

        board_content = lang.getString("board-content");
        board_info = lang.getString("board-info");
        board_invite = lang.getString("board-invite");
        board_promote = lang.getString("board-promote");
        board_kick = lang.getString("board-kick");
        board_quest = lang.getString("board-quest");
        board_donate = lang.getString("board-donate");
        board_delete = lang.getString("board-delete");
        board_shop = lang.getString("board-shop");

        invite_content = lang.getString("invite-content");
        invite_in_guild = lang.getString("invite-in-guild");
        invite_expire = lang.getString("invite-expire");
        invite_pending = lang.getString("invite-pending");
        invite_invited = lang.getString("invite-invited");
        invite_invite_get = lang.getString("invite-invite-get");
        invite_decline = lang.getString("invite-decline");
        invite_size = lang.getString("invite-size");
        invite_money = lang.getString("invite-money");
        invite_window = lang.getString("invite-window");
        invite_success_announce = lang.getString("invite-success-announce");

        top_title = lang.getString("top-title");
        top_content = lang.getString("top-content");

        guild_info_title = lang.getString("guild-info-title");
        guild_info_content = lang.getString("guild-info-content");

        shop_content = lang.getString("shop-content");
        shop_price = lang.getString("shop-price");
        shop_not_enough_money = lang.getString("shop-not-enough-money");

        donate_amount = lang.getString("donate-amount");
        donate_success = lang.getString("donate-success");
        donate_not_enough_money = lang.getString("donate-not-enough-money");

        delete_content = lang.getString("delete-content");

        leave_success_announce = lang.getString("leave-success-announce");

        quest_active_content = lang.getString("quest-active-content");
        quest_active_mob = lang.getString("quest-active-mob");
        quest_active_break = lang.getString("quest-active-break");
        quest_active_collect = lang.getString("quest-active-collect");
        quest_collect_donate_success = lang.getString("quest-collect-donate-success");

        quest_collect_donate = lang.getString("quest-collect-donate");
        quest_collect_pick = lang.getString("quest-collect-pick");
        quest_collect_pick_amount = lang.getString("quest-collect-pick-amount");

        quest_pick_content = lang.getString("quest-pick-content");

        quest_success = lang.getString("quest-success");

        quest_finished = lang.getString("quest-finished");

        quest_active_different = lang.getString("quest-active-different");

        player_donate_donthave = lang.getString("player-donate-donthave");

        window_close = lang.getString("window-close");
        window_back = lang.getString("window-back");
        window_accept = lang.getString("window-accept");
        window_decline = lang.getString("window-decline");

        cmd_no_permission = lang.getString("cmd-no-permission");

        cmd_create_in_guild = lang.getString("cmd-create-in-guild");
        cmd_info_no_guild = lang.getString("cmd-info-no-guild");

        guild_info = String.join("\n", lang.getStringList("guild-info"));

        quest_info = String.join("\n", lang.getStringList("quest-info"));
    }
}
