package com.android.recipe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.recipe.R;
import com.android.recipe.fragment.MFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrimaryActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_head;
    private String[] titles;
    private List<TextView> texts;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_primary);
        titles=new String[]{"频道1","频道2","频道3","频道4","频道5","频道6","频道7","频道8","频道9","频道10"};
        getID();
        texts=new ArrayList<>();
        for (int i=0;i<titles.length;i++){
            TextView textView=new TextView(this);
            texts.add(textView);
            //给控件设置文字
            textView.setText(titles[i]);
            //给空间设置字体大小
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            if(i==0){
                textView.setTextColor(Color.RED);
            }
            textView.setId(i);
            //初始化一个配置参数
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            //给参数设置margin值
            layoutParams.setMargins(25,15,25,15);
            textView.setOnClickListener(this);

            //把参数摆放的参数设置近期
            ll_head.addView(textView,layoutParams);
        }

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NotNull
            @Override
            public Fragment getItem(int position) {
                return MFragment.newInstance(titles[position]);
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });

        //viewpage 的监听事件
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0;i<texts.size();i++){
                    if(i==position){
                        texts.get(position).setTextColor(Color.RED);
                    }else{
                        texts.get(i).setTextColor(Color.BLACK);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

    }
    //获取控件id
    public void getID() {
        ll_head = findViewById(R.id.ll_head);
        HorizontalScrollView hsv = findViewById(R.id.hsv);
        pager = findViewById(R.id.viewpage);
    }

    //textview点击监听,切换到对应的fragment
    @Override
    public void onClick(View v) {
        int id=v.getId();
        pager.setCurrentItem(id);

    }
}