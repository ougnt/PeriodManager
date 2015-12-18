package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.joda.time.DateTime;
import com.ougnt.period_manager.*;

/**
 * * # Created by wacharint on 11/28/15.
 */
public class CommentActivity extends Activity {

    public DateTime targetDate = DateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment);

        String announcementText = getResources().getString(R.string.comment_activity_date_announcement_text);

        TextView header = (TextView) findViewById(R.id.comment_header);
        EditText comment = (EditText) findViewById(R.id.comment_content);
        targetDate = DateTime.parse(getIntent().getStringExtra("Date"));
        String displayDate = targetDate.toString("dd MMM yyyy");
        header.setText(announcementText + displayDate);
        comment.setText(getIntent().getExtras().get("Comment").toString());
    }

    public void commentSave(View view) {

        EditText commentText = (EditText)findViewById(R.id.comment_content);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Date", targetDate);
        returnIntent.putExtra("Comment", commentText.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void commentCancel(View view) {

        setResult(Activity.RESULT_OK, null);
        finish();
    }
}
