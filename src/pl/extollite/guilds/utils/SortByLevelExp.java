package pl.extollite.guilds.utils;

import pl.extollite.guilds.data.Guild;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class SortByLevelExp implements Comparator<Guild> {

    @Override
    public int compare(Guild guild1, Guild guild2) {

        // for comparison
        int level1 = guild1.getLevel();
        int level2 = guild2.getLevel();

        if (level1 > level2) {
            return -1;
        } else if(level1 < level2) {
            return 1;
        } else {
            int exp1 = guild1.getExp();
            int exp2 = guild2.getExp();
            if(exp1 > exp2)
                return -1;
            else if(exp1 == exp2)
                return 0;
            return 1;
        }
    }
}
