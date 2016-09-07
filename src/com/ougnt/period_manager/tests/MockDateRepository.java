package com.ougnt.period_manager.tests;

import com.ougnt.period_manager.repository.DateRepository;
import com.ougnt.period_manager.repository.IDateRepository;

import org.joda.time.DateTime;

/**
 * Created by wacharint on 9/3/2016 AD.
 */
public class MockDateRepository extends IDateRepository {

    public MockDateRepository() {
        date = DateTime.now();
        dateType = 2;
        comment = "";
        temperature = 0f;
    }
}
