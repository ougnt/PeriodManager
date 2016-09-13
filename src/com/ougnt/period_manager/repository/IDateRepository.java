package com.ougnt.period_manager.repository;

import org.joda.time.DateTime;

/**
 * Created by wacharint on 9/3/2016 AD.
 */
public abstract class IDateRepository {

    public DateTime date;
    // 1 = Period
    // 2 = PossiblyOvulation
    // 4 = OvulationDate
    // 0 = SafeZone
    public int dateType;
    public String comment;
    public float temperature;

}
