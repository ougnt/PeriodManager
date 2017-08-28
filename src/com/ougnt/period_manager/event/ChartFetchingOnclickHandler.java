package com.ougnt.period_manager.event;

import android.view.View;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.handler.ChartHandler;

public class ChartFetchingOnclickHandler implements View.OnClickListener {

    private ChartHandler _handler;

    public ChartFetchingOnclickHandler(ChartHandler handler) {

        _handler = handler;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.chart_fetch_previous_data_button : {

                _handler.fetchPreviousDataThenRefresh(20);
                break;
            }
            case R.id.chart_fetch_next_data_button : {

                _handler.fetchNextDataThenRefresh(20);
                break;
            }
            default: // do nothing not handle
        }
    }
}
