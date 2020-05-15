package pl.extollite.guilds.utils;

import java.util.HashMap;
import java.util.Map;

public class MobsNames {
    private static final Map<Integer, String> names = new HashMap<>();
    static {
        //Passive
        names.put(10, "Chicken");
        names.put(11, "Cow");
        names.put(12, "Pig");
        names.put(13, "Sheep");
        names.put(14, "Wolf");
        names.put(15, "Villager");
        names.put(16, "Mooshroom");
        names.put(17, "Squid");
        names.put(18, "Rabbit");
        names.put(19, "Bat");
        names.put(20, "Iron Golem");
        names.put(21, "Snow Golem");
        names.put(22, "Ocelot");
        names.put(23, "Horse");
        names.put(24, "Donkey");
        names.put(25, "Mule");
        names.put(26, "Skeleton Horse");
        names.put(27, "Zombie Horse");
        names.put(28, "Polar Bear");
        names.put(29, "Llama");
        names.put(30, "Parrot");
        names.put(31, "Dolphin");

        names.put(74, "Turtle");
        names.put(75, "Cat");

        names.put(108, "Pufferfish");
        names.put(109, "Salmon");

        names.put(111, "Tropical Fish");
        names.put(112, "Cod");
        names.put(113, "Panda");

        names.put(115, "Villager");

        names.put(118, "Wandering Trader");

        names.put(121, "Fox");
        names.put(122, "Bee");

        //Hostile
        names.put(32, "Zombie");
        names.put(33, "Creeper");
        names.put(34, "Skeleton");
        names.put(35, "Spider");
        names.put(36, "Zombie Pigman");
        names.put(37, "Slime");
        names.put(38, "Enderman");
        names.put(39, "Silverfish");
        names.put(40, "Cave Spider");
        names.put(41, "Ghast");
        names.put(42, "Magma Cube");
        names.put(43, "Blaze");
        names.put(44, "Zombie Villager");
        names.put(45, "Witch");
        names.put(46, "Stray");
        names.put(47, "Husk");
        names.put(48, "Wither Skeleton");
        names.put(49, "Guardian");
        names.put(50, "Elder Guardian");

        names.put(52, "Wither");
        names.put(53, "Ender Dragon");
        names.put(54, "Shulker");
        names.put(55, "Endermite");

        names.put(57, "Vindicator");
        names.put(58, "Phantom");
        names.put(59, "Ravager");

        names.put(104, "Evoker");
        names.put(105, "Vex");

        names.put(110, "Drowned");

        names.put(114, "Pillager");

        names.put(116, "Zombie Villager");
    }

    public static String getName(int id) {
        return names.get(id);
    }
}
