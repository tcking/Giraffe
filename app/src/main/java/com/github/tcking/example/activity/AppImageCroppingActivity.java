package com.github.tcking.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.tcking.example.R;
import com.github.tcking.giraffe.core.Log;
import com.github.tcking.giraffe.helper.PhotoHelper;
import com.github.tcking.giraffe.manager.DeviceManager;
import com.github.tcking.giraffe.ui.AppImageCroppingView;

import java.io.File;
import java.io.IOException;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/20.
 */
public class AppImageCroppingActivity extends BaseActivity {
    private File imageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_image_cropping);
        imageFile = (File) getIntent().getSerializableExtra("imageFile");
        $.id(R.id.app_cropping_view).image(imageFile, DeviceManager.getInstance().getScreenWidthInPx());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_image_cropping,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_using) {
            Intent data = new Intent();
            AppImageCroppingView croppingView= (AppImageCroppingView) findViewById(R.id.app_cropping_view);
            try {
                PhotoHelper.saveBitmap2File(croppingView.getCroppedImage(), imageFile);
                data.putExtra("imageFile", imageFile);
                setResult(Activity.RESULT_OK, data);
                finish();
                return true;
            } catch (IOException e) {
                Log.e("AppImageCroppingActivity.onOptionsItemSelected error", e);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
