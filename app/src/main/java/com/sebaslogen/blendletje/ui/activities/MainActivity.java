package com.sebaslogen.blendletje.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.ui.presenters.MainContract;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.ViewActions {

    private MainContract.UserActions mUserActions;
    private TextView mText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.text);

        mUserActions = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserActions.attachView();
    }

    @Override
    protected void onPause() {
        mUserActions.deAttachView();
        super.onPause();
    }

    @Override
    public void showTitle(final String text) {
        mText.setText(text);
    }
}
