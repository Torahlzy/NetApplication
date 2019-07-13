package com.torahli.myapplication.game.constant;

public class ExpConstant {
    /**
     * 获得玩家的升级经验
     *
     * @param level
     * @return
     */
    public static long getLevelMaxExp(int level) {
        int baseLevelPow = (level % 10 > 0 ? 1 : 0) + level / 10;//4得到1,10得到1,13得到2
        if (baseLevelPow <= 1) {
            return (long) (100 * Math.pow(1.5f, level));
        } else if (baseLevelPow == 2) {
            return (long) (100 * Math.pow(1.5f, 10) * Math.pow(1.4f, level - 10));
        } else if (baseLevelPow == 3) {
            return (long) (100 * Math.pow(1.5f, 10) * Math.pow(1.4f, 10)
                    * Math.pow(1.3f, level - 20));
        } else if (baseLevelPow == 4) {
            return (long) (100 * Math.pow(1.5f, 10) * Math.pow(1.4f, 10)
                    * Math.pow(1.3f, 10) * Math.pow(1.2f, level - 30));
        } else {
            return (long) (100 * Math.pow(1.5f, 10) * Math.pow(1.4f, 10)
                    * Math.pow(1.3f, 10) * Math.pow(1.2f, 10)
                    * Math.pow(1.1f, level - 40));
        }
    }

    /**
     * 获得杀死怪物的经验
     *
     * @param level
     * @return
     */
    public static long getDefeatExp(int level) {
        long levelMaxExp = getLevelMaxExp(level);
        int baseLevelPow = (level % 10 > 0 ? 1 : 0) + level / 10;//4得到1,10得到1,13得到2
        if (level < 1) {
            level = 1;
        }
        return levelMaxExp / (10 * (level - baseLevelPow));
    }

}
