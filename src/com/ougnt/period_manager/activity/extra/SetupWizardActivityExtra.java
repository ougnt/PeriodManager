package com.ougnt.period_manager.activity.extra;

import org.json.JSONException;
import org.json.JSONObject;

public class SetupWizardActivityExtra {

    private static final String PeriodLengthJson = "PeriodLength";
    private static final String CycleLengthJson = "CycleLength";
    private static final String NotificationBeforeOvulation = "NotificationBeforeOvulation";
    private static final String NotificationBeforeMenstrual = "NotificationBeforeMenstrual";

    public static final int NotNotifyMe = -1;

    public static SetupWizardActivityExtra fromJson(String extra) {
        SetupWizardActivityExtra ret = new SetupWizardActivityExtra();

        try {
            JSONObject json = new JSONObject(extra);
            ret.periodLength = json.getInt(PeriodLengthJson);
            ret.cycleLength = json.getInt(CycleLengthJson);
            ret.notifyBeforeOvulation = json.getInt(NotificationBeforeOvulation);
            ret.notifyBeforeMenstrual = json.getInt(NotificationBeforeMenstrual);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String toJson(){
        return String.format("{'%s':'%s', '%s':'%s','%s':'%s','%s':'%s'}",
                PeriodLengthJson, periodLength,
                CycleLengthJson, cycleLength,
                NotificationBeforeOvulation, notifyBeforeOvulation,
                NotificationBeforeMenstrual, notifyBeforeMenstrual);
    }

    public int periodLength;
    public int cycleLength;
    public int notifyBeforeOvulation;
    public int notifyBeforeMenstrual;
}
