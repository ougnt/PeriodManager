package com.ougnt.period_manager.activity.extra;

import com.google.android.gms.phenotype.Flag;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;


public class ActionActivityExtra {

    private static final String DateTypeJson = "DateType";
    private static final String DateJson = "Date";
    private static final String CommentJson = "Comment";
    private static final String TemperatureJson = "Temperature";
    private static final String IsButtonPushed = "IsButtonPushed";
    private static final String Flags = "Flags";

    public ActionActivityExtra() {
        isButtonPush = false;
        isCancel = true;
    }

    public static ActionActivityExtra fromJsonString(String extra) {

        ActionActivityExtra ret = new ActionActivityExtra();

        try {
            JSONObject json = new JSONObject(extra);
            ret.dateType = json.getInt(DateTypeJson);
            ret.date = DateTime.parse(json.getString(DateJson));
            ret.comment = json.getString(CommentJson);
            ret.temperature = json.getDouble(TemperatureJson);
            ret.isButtonPush = json.getBoolean(IsButtonPushed);
            ret.flags = json.getLong(Flags);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String toJson() {

        return String.format("{'%s':'%s', '%s':'%s','%s':'%s','%s':'%s','%s':'%s','%s':'%s'}",
                DateJson, date.toString("yyyy-MM-dd"),
                DateTypeJson, dateType,
                TemperatureJson, temperature,
                CommentJson, comment,
                IsButtonPushed, isButtonPush,
                Flags, flags);
    }

    public int dateType;
    public DateTime date;
    public String comment;
    public double temperature;
    public boolean isButtonPush;
    public boolean isCancel;
    public long flags;
}
