package com.yc.english.group.view.activitys;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.yc.english.R;
import com.yc.english.group.view.fragments.ClassMainFragment;

/**
 * Created by wanglin  on 2017/7/24 18:17.
 */

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_test);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, new ClassMainFragment());
        ft.commit();

    }
}
