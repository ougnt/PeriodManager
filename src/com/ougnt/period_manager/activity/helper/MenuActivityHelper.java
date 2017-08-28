package com.ougnt.period_manager.activity.helper;

import android.content.Intent;
import android.view.View;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.activity.MenuActivity;
import com.ougnt.period_manager.google.Log;

public class MenuActivityHelper implements View.OnClickListener {

    public MenuActivityHelper(MenuActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View clickedMenu) {
        Intent retIntent = new Intent();
        activity.log.setCategory(Log.Category.Button);

        switch (clickedMenu.getId()) {
            case R.id.lock_screen_menu:
                activity.log.setAction(Log.Action.ClickLockScreen);
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectLockScreen);
                break;
            case R.id.submit_review_menu:
                activity.log.setAction(Log.Action.ClickSubmitReview);
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectReview);
                break;
            case R.id.setting_language_selecter:
                activity.log.setAction(Log.Action.ClickLanguageSelector);
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectLanguageSelecter);
                break;
            case R.id.summary_menu:
                activity.log.setAction(Log.Action.ClickSummary);
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectSummary);
                break;
            case R.id.help_menu:
                activity.log.setAction(Log.Action.ClickHelp);
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectDisplayHelp);
                break;
            case R.id.setting_menu:
                activity.log.setAction(Log.Action.ClickSetting);
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectDisplaySetting);
                break;
            case R.id.close1:
            case R.id.close2:
                retIntent.putExtra(MenuActivity.SelectedMenuExtra, MenuActivity.SelectNothing);
                break;
        }

        InitialActivity.sendTrafficMessage(activity.log);

        activity.setResult(MenuActivity.RESULT_OK, retIntent);
        activity.finish();
    }

    private MenuActivity activity;
}
