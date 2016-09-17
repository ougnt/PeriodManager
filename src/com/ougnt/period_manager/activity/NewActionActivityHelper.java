package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ougnt.period_manager.DateMeter;
import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.extra.ActionActivityExtra;

import static com.ougnt.period_manager.DateMeter.Menstrual;
import static com.ougnt.period_manager.DateMeter.Nothing;
import static com.ougnt.period_manager.DateMeter.OvulationDate;
import static com.ougnt.period_manager.DateMeter.PossiblyOvulation;


public class NewActionActivityHelper implements View.OnClickListener {

    public NewActionActivityHelper(NewActionActivity activity) {
        this.activity = activity;
        extra = parseExtras();
        setUpDisplay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_action_button_image: {

                if (isActionButtonPushed) {
                    activity.actionButton.setBackground(ContextCompat.getDrawable(activity, R.drawable.blue_button));
                    isActionButtonPushed = false;
                } else {
                    activity.actionButton.setBackground(ContextCompat.getDrawable(activity, R.drawable.blue_button_push));
                    isActionButtonPushed = true;
                }
                break;
            }
            case R.id.action_temperature_help: {
                Intent intent = new Intent(activity, TemperatureHelpActivity.class);
                activity.startActivity(intent);
                break;
            }
            case R.id.action_save_button: {
                Intent retIntent = new Intent();

                extra.temperature = Double.parseDouble(activity.temperatureInput.getText().toString());
                extra.comment = String.valueOf(activity.commentInput.getText());
                extra.isButtonPush = isActionButtonPushed;
                extra.isCancel = false;
                retIntent.putExtra(NewActionActivity.ExtraKey, extra.toJson());
                activity.setResult(Activity.RESULT_OK, retIntent);
                activity.finish();
            }
        }
    }

    public void setUpDisplay() {

        activity.dateTextView.setText(extra.date.toString(activity.getResources().getString(R.string.short_date_format)));
        activity.actionButtonLabel.setText(getButtonText(extra.dateType));
        String temperatureDisplay = extra.temperature + "";
        activity.temperatureInput.setText(temperatureDisplay);
        activity.commentInput.setText(extra.comment);
        isActionButtonPushed = false;
    }

    public String getButtonText(int dateType) {
        switch (dateType) {
            case Menstrual:
                return "Click me if this day is not in your period";
            case Nothing:
            case OvulationDate:
            case PossiblyOvulation:
            default:
                return "Click me if your period start this day";
        }
    }

    private ActionActivityExtra parseExtras() {
        String key = activity.getIntent().getExtras().getString(NewActionActivity.ExtraKey);
        return ActionActivityExtra.fromJsonString(key);
    }

    public NewActionActivity activity;
    public ActionActivityExtra extra;
    public boolean isActionButtonPushed;
}
