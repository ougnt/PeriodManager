package com.ougnt.period_manager.activity.helper;

public class FlagHelper {

    public static final long EmotionFlag = 0xF;
    public static final int EmotionNothingIcon = 0x0;
    public static final int EmotionHappyIcon = 0x1;
    public static final int EmotionSadIcon = 0x2;
    public static final int EmotionAngryIcon = 0x3;

    public static int GetEmotionFlag(long flags) {
        return (int) (flags & EmotionFlag);
    }
}
