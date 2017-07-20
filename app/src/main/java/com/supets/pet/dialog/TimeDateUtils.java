package com.supets.pet.dialog;

public class TimeDateUtils {

    public static String formatRecordTime(int time) {
        int yu = (int) (time % 60);
        return time / 60 + ":" + yu / 10 + "" + yu % 10;
    }

}
