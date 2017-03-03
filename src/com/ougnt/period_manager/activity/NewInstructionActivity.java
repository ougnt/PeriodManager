package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.activity.extra.NewInstructionActivityExtra;
import com.ougnt.period_manager.activity.helper.NewInstructionActivityHelper;
import com.ougnt.period_manager.activity.helper.UtilHelper;

public class NewInstructionActivity extends Activity {

    public static final String InstructionExtra = "InstructionExtra";
    public static final int NextButtonId = UtilHelper.GenerateId();
    public int currentComponent = 0;
    public NewInstructionActivityExtra extra;

    private NewInstructionActivityHelper helper = new NewInstructionActivityHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extra = NewInstructionActivityExtra.fromJsonString(getIntent().getExtras().getString(InstructionExtra));

        if(extra.components.size() > 0)
            setContentView(generateContent(0));
        else
            finish();
    }

    public FrameLayout generateContent(int componentIndex){

        FrameLayout ret = new FrameLayout(this);

        LinearLayout viewPart = new LinearLayout(this);
        viewPart.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout leftLayout = new LinearLayout(this);
        leftLayout.setLayoutParams(new LinearLayout.LayoutParams(extra.components.get(componentIndex).startX, ViewGroup.LayoutParams.MATCH_PARENT));
        leftLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.instruction_grey));

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, extra.components.get(componentIndex).startY));
        topLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.instruction_grey));
        LinearLayout viewLayout = new LinearLayout(this);
        viewLayout.setLayoutParams(new LinearLayout.LayoutParams(extra.components.get(componentIndex).width, extra.components.get(componentIndex).height));
        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getWindowManager().getDefaultDisplay().getHeight() - extra.components.get(componentIndex).startY - extra.components.get(componentIndex).height));
        bottomLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.instruction_grey));
        innerLayout.addView(topLayout);
        innerLayout.addView(viewLayout);
        innerLayout.addView(bottomLayout);

        LinearLayout rightLayout = new LinearLayout(this);
        rightLayout.setLayoutParams(new LinearLayout.LayoutParams( this.getWindowManager().getDefaultDisplay().getWidth() - extra.components.get(componentIndex).startX - extra.components.get(componentIndex).width, ViewGroup.LayoutParams.MATCH_PARENT));
        rightLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.instruction_grey));

        viewPart.addView(leftLayout);
        viewPart.addView(innerLayout);
        viewPart.addView(rightLayout);

        TextView textPart = new TextView(this);
        textPart.setText(extra.components.get(componentIndex).text);
        textPart.setGravity(Gravity.CENTER);

        textPart.setTextColor(ContextCompat.getColor(this, R.color.main_bg));

        FrameLayout.LayoutParams textPartParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textPartParams.gravity = Gravity.CENTER_HORIZONTAL;
        textPart.setLayoutParams(textPartParams);

        Button nextButton = new Button(this);
        nextButton.setText(getResources().getText(R.string.instruction_next_button));
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.END;
        nextButton.setLayoutParams(buttonParams);
        nextButton.setOnClickListener(helper);

        nextButton.setId(NextButtonId);

        ret.addView(viewPart);
        ret.addView(textPart);
        ret.addView(nextButton);

        return ret;
    }
}
