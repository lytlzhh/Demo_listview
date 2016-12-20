package com.example.xerdp.demo_listview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.Listview_adapter;
import bean.Bean;
import class_listview.Mylistview;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Mylistview.MyOnRefreshListener {

    private class_listview.Mylistview listview_id;
    private SwipeRefreshLayout activity_main;

    List<Bean> list = null;
    Listview_adapter listview_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listview_id = (Mylistview) findViewById(R.id.listview_id);
        activity_main = (SwipeRefreshLayout) findViewById(R.id.activity_main);

        activity_main.setOnRefreshListener(this);
        list = new ArrayList<>();
        listview_id.setInterface(this);
    }

    public void get_date() {
        for (int i = 0; i < 2; i++) {
            list.add(new Bean("头部"));
        }
    }


    public void show_list(List<Bean> list) {
        if (listview_adapter == null) {
            listview_adapter = new Listview_adapter(list, this);
            listview_id.setAdapter(listview_adapter);
        } else {
            listview_adapter.onDatechange(list);
        }
    }

    @Override
    public void onRefresh() {
        OnRrfesh();
        //activity_main.setRefreshing(false);
    }


    @Override
    public void OnRrfesh() {
        android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                get_date();
                show_list(list);
                listview_id.OnRefresh_complete();
            }
        }, 400);

    }
}
