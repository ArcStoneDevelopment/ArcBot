package Levels;

/**
 * Provides level - point translation for the level function.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 * @since v1.0
 */
public class LevelConstants {

    /**
     * This method returns the upper bound point count for the given level. If you imagine a level value as having a range
     * of points that fall into that level, this method returns the highest level in that range.
     * <br> For example:
     * <br> * Level 1 : Ranges from 100 to 255.
     * <br> * This method returns 255.
     * @param currentLevel
     * The level whose threshold is desired.
     * @return int - The upper-bound of a given level's range.
     */
    public static int getThreshold(int currentLevel) {
        if (currentLevel == 0) {
            return 100;
        }
        return ((5 * (currentLevel * currentLevel) + 50 * currentLevel + 100) + getThreshold(currentLevel - 1));
    }
}
