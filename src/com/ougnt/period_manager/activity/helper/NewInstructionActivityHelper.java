package com.ougnt.period_manager.activity.helper;

import android.view.View;
import com.ougnt.period_manager.activity.NewInstructionActivity;

public class NewInstructionActivityHelper implements View.OnClickListener {

    private NewInstructionActivity mainActivity;

    public NewInstructionActivityHelper(NewInstructionActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == NewInstructionActivity.NextButtonId) {
            if(mainActivity.currentComponent == mainActivity.extra.components.size() - 1) {
                mainActivity.finish();
            } else {
                mainActivity.setContentView(mainActivity.generateContent(++mainActivity.currentComponent));
            }
        }
    }
}
