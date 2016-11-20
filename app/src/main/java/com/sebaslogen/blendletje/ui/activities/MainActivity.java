package com.sebaslogen.blendletje.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.modules.MainActivityModule;
import com.sebaslogen.blendletje.ui.presenters.MainContract;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.ViewActions {

    @Inject
    MainContract.UserActions mUserActions;
    private TextView mText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.text);

        ((BlendletjeApp) getApplication()).getCommandsComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
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
