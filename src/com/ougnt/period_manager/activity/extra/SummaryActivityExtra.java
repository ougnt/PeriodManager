package com.ougnt.period_manager.activity.extra;

import com.ougnt.period_manager.handler.HttpHelper;
import com.ougnt.period_manager.repository.SummaryRepository;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wacharint on 11/11/2016 AD.
 */
public class SummaryActivityExtra {
    private static String NextMenstrualFromExtra = "NextMenstrualFromExtra";
    private static String NextMenstrualToExtra = "NextMenstrualToExtra";
    private static String NextOvulationFromExtra = "NextOvulationFromExtra";
    private static String NextOvulationToExtra = "NextOvulationToExtra";
    private static String NextOvulationDateExtra = "NextOvulationDateExtra";

    public static String SummaryActivityExtraExtra = "SummaryActivityExtraExtra";

    public DateTime expectedNextMenstrualDateFrom;
    public DateTime expectedNextMenstrualDateTo;
    public DateTime expectedNextOvulationDateFrom;
    public DateTime expectedNextOvulationDateTo;
    public DateTime expectedNextOvulationDate;

    public SummaryActivityExtra() {

    }

    public static SummaryActivityExtra fromJson(String jsonExtra) {

        try {
            SummaryActivityExtra ret = new SummaryActivityExtra();
            JSONObject json = new JSONObject(jsonExtra);

            ret.expectedNextMenstrualDateFrom = DateTime.parse(json.getString(NextMenstrualFromExtra));
            ret.expectedNextMenstrualDateTo = DateTime.parse(json.getString(NextMenstrualToExtra));
            ret.expectedNextOvulationDateFrom = DateTime.parse(json.getString(NextOvulationFromExtra));
            ret.expectedNextOvulationDateTo = DateTime.parse(json.getString(NextOvulationToExtra));
            ret.expectedNextOvulationDate = DateTime.parse(json.getString(NextOvulationDateExtra));
            return ret;
        } catch (JSONException e) {
            HttpHelper.sendErrorLog(e);
            return null;
        }
    }

    public static SummaryActivityExtra fromSummaryRepository(SummaryRepository summary) {
        SummaryActivityExtra extra = new SummaryActivityExtra();
        extra.expectedNextOvulationDateTo = summary.expectedOvulationDateTo;
        extra.expectedNextOvulationDateFrom = summary.expectedOvulationDateFrom;
        extra.expectedNextOvulationDate = summary.expectedOvulationDate;
        extra.expectedNextMenstrualDateFrom = summary.expectedMenstrualDateFrom;
        extra.expectedNextMenstrualDateTo = summary.expectedMenstrualDateTo;
        return extra;
    }

    public SummaryRepository toSummaryRepository() {
        SummaryRepository repo = new SummaryRepository();
        repo.expectedOvulationDateTo = this.expectedNextOvulationDateTo;
        repo.expectedOvulationDateFrom = this.expectedNextOvulationDateFrom;
        repo.expectedOvulationDate = this.expectedNextOvulationDate;
        repo.expectedMenstrualDateFrom = this.expectedNextMenstrualDateFrom;
        repo.expectedMenstrualDateTo = this.expectedNextMenstrualDateTo;
        return repo;
    }

    public String toJson() {
        return String.format("{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}",
                NextMenstrualFromExtra, expectedNextMenstrualDateFrom.toString("yyyy-MM-dd"),
                NextMenstrualToExtra, expectedNextMenstrualDateTo.toString("yyyy-MM-dd"),
                NextOvulationFromExtra, expectedNextOvulationDateFrom.toString("yyyy-MM-dd"),
                NextOvulationToExtra, expectedNextOvulationDateTo.toString("yyyy-MM-dd"),
                NextOvulationDateExtra, expectedNextOvulationDate.toString("yyyy-MM-dd"));
    }
}
