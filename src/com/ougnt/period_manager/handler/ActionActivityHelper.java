package com.ougnt.period_manager.handler;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.ActionActivity;
import com.ougnt.period_manager.activity.TemperatureHelpActivity;

/**
 * Created by wacharint on 8/31/2016 AD.
 */
public class ActionActivityHelper implements View.OnClickListener {

    private Activity _activity;

    public ActionActivityHelper(Activity hostActivity) {
        this._activity = hostActivity;
    }

    private void setResultAndFinish(int resultCode) {

        Intent retIntent = new Intent();
        retIntent.putExtra(ActionActivity.ActionExtra, resultCode);
        _activity.setResult(Activity.RESULT_OK, retIntent);
        _activity.finish();
    }

    public void onBackPressed() {
        setResultAndFinish(ActionActivity.ActionNothing);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.period_button: {
                setResultAndFinish(ActionActivity.ActionPeriodButton);
                break;
            }
            case R.id.non_period_button: {
                setResultAndFinish(ActionActivity.ActionNonPeriodButton);
                break;
            }
            case R.id.empty_action: {
                onBackPressed();
                break;
            }
            case R.id.temperature_help: {
                Intent intent = new Intent(_activity, TemperatureHelpActivity.class);
                _activity.startActivity(intent);
                break;
            }
            case R.id.action_save_button: {
                float temperature = 0f;
                String comment = "";
                try {
                    temperature = Float.parseFloat(((EditText) _activity.findViewById(R.id.temperature_edittext)).getText().toString());
                    comment = ((EditText)_activity.findViewById(R.id.action_comment)).getText().toString();
                } catch (NumberFormatException e) {
                    temperature = 0f;
                }

                Intent retIntent = new Intent();
                retIntent.putExtra(ActionActivity.ActionExtra, ActionActivity.ActionSaveButton);
                retIntent.putExtra(ActionActivity.ActionTemperatureExtra, temperature);
                retIntent.putExtra(ActionActivity.ActionCommentExtra, comment);
                _activity.setResult(Activity.RESULT_OK, retIntent);
                _activity.finish();
                break;
            }
        }
    }
}
