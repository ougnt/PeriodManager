package com.ougnt.period_manager.handler;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.repository.DateRepository;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class ChartHandler {

    private Context _context;
    private LineChart _lineChart;
    private List<DateRepository> _dateRepositories;

    public ChartHandler(Context context, LineChart lineChart) {

        _context = context;
        _lineChart = lineChart;
    }

    public void fetchPreviousDataThenRefresh(int days) {

        if(_dateRepositories == null) {
            initialChart(20);
        }

        fetchDataThenRefresh(-days);
    }

    public void fetchNextDataThenRefresh(int days) {

        if(_dateRepositories == null) {
            initialChart(20);
        }

        fetchDataThenRefresh(days);
    }

    public void initialChart(int days) {

        _dateRepositories = DateRepository.getDateRepositories(
                _context,
                DateTime.now().minusDays(days),
                DateTime.now().plusDays(days));

        fetchDataThenRefresh(0);
    }

    private void fetchDataThenRefresh(int days) {

        LinkedList<String> dataX = new LinkedList<>();
        LinkedList<Entry> dataY = new LinkedList<>();

        List<DateRepository> dateRepositoriesForChart ;

        if(days < 0) {

            dateRepositoriesForChart = DateRepository.getDateRepositories(_context,
                    _dateRepositories.get(0).date.plusDays(days),
                    _dateRepositories.get(_dateRepositories.size() -1 ).date);
        } else if (days > 0 ) {

            dateRepositoriesForChart = DateRepository.getDateRepositories(_context,
                    _dateRepositories.get(0).date,
                    _dateRepositories.get(_dateRepositories.size() -1 ).date.plusDays(days));

        } else {
            dateRepositoriesForChart = _dateRepositories;
        }

        // copy back to use next time
        _dateRepositories = dateRepositoriesForChart;

        for (int dateIndex = 0; dateIndex < dateRepositoriesForChart.size(); dateIndex++) {

            dataX.add(dateRepositoriesForChart.get(dateIndex).date.toString("yyyy-MM-dd"));
            dataY.add(new Entry(dateRepositoriesForChart.get(dateIndex).temperature, dateIndex));
        }

        LineData temperatureData = new LineData(dataX);
        LineDataSet dataSet = new LineDataSet(dataY, "temperature");

        dataSet.setColor(ContextCompat.getColor(_context, R.color.chart_line_color));
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                df.format(value);
                return df.format(value);
            }
        });
        dataSet.setLineWidth(3f);

        temperatureData.addDataSet(dataSet);

        // Color of the text explained the point info
        temperatureData.setValueTextColor(ContextCompat.getColor(_context, R.color.chart_value_text));
        temperatureData.setValueTextSize(14f);

        _lineChart.setData(temperatureData);

        _lineChart.setBackgroundColor(ContextCompat.getColor(_context, R.color.chart_background_color));
        _lineChart.setDescription("");
        _lineChart.animateX(100);
        _lineChart.animateY(100);
        _lineChart.getXAxis().setLabelsToSkip(6);
        _lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        _lineChart.getAxisLeft().setAxisMinValue(36);
        _lineChart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                df.format(value);
                return df.format(value);
            }
        });
        _lineChart.getAxisRight().setAxisMinValue(36);
        _lineChart.getAxisRight().setGranularity(0.01f);
        _lineChart.getAxisRight().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                df.format(value);
                return df.format(value);
            }
        });

        _lineChart.zoom(3f, 1f, 0, 0);
    }
}
