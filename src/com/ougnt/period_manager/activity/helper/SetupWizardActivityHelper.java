package com.ougnt.period_manager.activity.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.SetupWizardActivity;
import com.ougnt.period_manager.activity.extra.SetupWizardActivityExtra;

public class SetupWizardActivityHelper implements View.OnClickListener {

    public SetupWizardActivityHelper(SetupWizardActivity swa) {
        activity = swa;
        extra = new SetupWizardActivityExtra();
        stage = Stage.PeriodLengthSetting;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_wizard_next_button: {
                switch (stage) {
                    case PeriodLengthSetting:
                        if (activity.iDontKnowCheckBox.isChecked()) {
                            periodLength = 7;
                        } else {
                            periodLength = activity.numberPicker.getValue();
                        }

                        Toast.makeText(activity, activity.getResources().getText(R.string.setting_next), Toast.LENGTH_SHORT).show();

                        activity.iDontKnowCheckBox.setChecked(false);
                        activity.numberPicker.setMaxValue(40);
                        activity.numberPicker.setValue(28);
                        activity.instructionText.setText(activity.getResources().getText(R.string.setting_how_long_does_your_period_cycle));
                        stage = Stage.PeriodCycleSetting;
                        break;
                    case PeriodCycleSetting:
                        if (activity.iDontKnowCheckBox.isChecked()) {
                            cycleLength = 28;
                        } else {
                            cycleLength = activity.numberPicker.getValue();
                        }

                        Toast.makeText(activity, activity.getResources().getText(R.string.setting_next), Toast.LENGTH_SHORT).show();
                        activity.iDontKnowCheckBox.setChecked(false);
                        activity.numberPicker.setMaxValue(10);
                        activity.numberPicker.setValue(1);
                        activity.instructionText.setText(activity.getResources().getText(R.string.setting_when_you_want_me_to_remind_you_before_your_ovulation_date));
                        stage = Stage.OvulationNotificationSetting;
                        activity.iDontKnowCheckBox.setText(activity.getResources().getText(R.string.setting_do_not_notify_me));
                        break;
                    case OvulationNotificationSetting:
                        if (activity.iDontKnowCheckBox.isChecked()) {
                            notifyBeforeOvulation = SetupWizardActivityExtra.NotNotifyMe;
                        } else {
                            notifyBeforeOvulation = activity.numberPicker.getValue();
                        }

                        Toast.makeText(activity, activity.getResources().getText(R.string.setting_next), Toast.LENGTH_SHORT).show();
                        activity.iDontKnowCheckBox.setChecked(false);
                        activity.numberPicker.setMaxValue(10);
                        activity.numberPicker.setValue(1);
                        activity.instructionText.setText(activity.getResources().getText(R.string.setting_when_you_want_me_to_remind_you_before_your_next_period));
                        stage = Stage.MenstrualNotificationSetting;
                        break;
                    case MenstrualNotificationSetting:
                        if (activity.iDontKnowCheckBox.isChecked()) {
                            notifyBeforeMenstrual = SetupWizardActivityExtra.NotNotifyMe;
                        } else {
                            notifyBeforeMenstrual = activity.numberPicker.getValue();
                        }

                        Toast.makeText(activity, activity.getResources().getText(R.string.setting_next), Toast.LENGTH_SHORT).show();
                        activity.instructionText.setText(activity.getResources().getText(R.string.setting_when_you_want_me_to_remind_you_before_your_next_period));
                        stage = Stage.Done;
                        setupExtraAndFinishTheActivity();
                        break;
                }
                break;
            }
        }
    }

    private void setupExtraAndFinishTheActivity() {
        extra.periodLength = periodLength;
        extra.cycleLength = cycleLength;
        extra.notifyBeforeOvulation = notifyBeforeOvulation;
        extra.notifyBeforeMenstrual = notifyBeforeMenstrual;

        Intent retIntent = new Intent();
        retIntent.putExtra(SetupWizardActivity.ExtraKey, extra.toJson());
        activity.setResult(Activity.RESULT_OK, retIntent);
        activity.finish();
    }

    private int periodLength;
    private int cycleLength;
    private int notifyBeforeOvulation;
    private int notifyBeforeMenstrual;
    private SetupWizardActivityExtra extra;
    private Stage stage;
    public SetupWizardActivity activity;

    public enum Stage {
        PeriodLengthSetting,
        PeriodCycleSetting, OvulationNotificationSetting, MenstrualNotificationSetting, Done,
    }
}
