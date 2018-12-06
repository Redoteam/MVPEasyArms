package com.yl.acoreui.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.yl.core.integration.AppManager;
import com.yl.acoreui.R;
import com.yl.acoreui.app.Constant;
import com.yl.acoreui.common.adapter.BigImagePreviewAdapter;

import java.util.ArrayList;
import java.util.List;

/***
 * 大图浏览页面，Use {@link PreviewImageActivity#gotoPreviewImageActivity(Context, List, List, int)}
 */
public class PreviewImageActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview_image);
        AppManager.getAppManager().addActivity(this);
        viewPager = findViewById(R.id.viewpage);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra(Constant.KEY_BEAN);
        ArrayList<String> titls = intent.getStringArrayListExtra(Constant.KEY_BEAN_2);
        BigImagePreviewAdapter bigImagePreviewAdapter = new BigImagePreviewAdapter(this, titls, urls);
        viewPager.setAdapter(bigImagePreviewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    Glide.with(PreviewImageActivity.this).pauseRequests();
                } else if (state == 2) {
                    Glide.with(PreviewImageActivity.this).resumeRequests();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
            viewPager = null;
        }
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    public static void gotoPreviewImageActivity(Context context, @NonNull List<String> images, @Nullable List<String> desc, int showIndex) {
        Intent intent = new Intent(context, PreviewImageActivity.class);
        intent.putStringArrayListExtra(Constant.KEY_BEAN, (ArrayList<String>) images);
        intent.putStringArrayListExtra(Constant.KEY_BEAN_2, (ArrayList<String>) desc);
        intent.putExtra(Constant.KEY_INT_1, showIndex);
        context.startActivity(intent);
    }
}
