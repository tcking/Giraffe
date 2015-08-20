package com.github.tcking.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tcking.example.R;
import com.github.tcking.giraffe.core.Log;
import com.github.tcking.giraffe.helper.PhotoHelper;

import java.io.File;

/**
 * <pre>
 * a easy way to get a photo:
 * 1.take a photo from camera: new PhotoHelper(this).callback(new PhotoHelper.CallBack() {
 *
 * @Override public void done(File imageFile) {
 * <p/>
 * }
 * @Override public void error(Exception e) {
 * Log.e("AppGetPhotoActivity.error error", e);
 * }
 * }).takePhoto();
 * 2.choose a photo : new PhotoHelper(this).callback(...).choosePhoto();
 * <p/>
 * <p/>
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/8/19.
 */
public class AppGetPhotoActivity extends BaseActivity {

    PhotoHelper photoHelper = new PhotoHelper(this)
            .quality(80)
            .maxFileSizeKB(80)
            .maxWidth(300,true)
            .callback(new PhotoHelper.CallBack() {
                @Override
                public void done(File imageFile) {
                    Log.d("AppGetPhotoActivity.done {}", imageFile.getAbsoluteFile());
                    ((ImageView) findViewById(R.id.app_ImageView)).setImageBitmap(PhotoHelper.getBitmap(imageFile));
                    ((TextView) findViewById(R.id.app_TextView_fileSize)).setText(String.format("image file size:%s",
                            android.text.format.Formatter.formatFileSize(AppGetPhotoActivity.this, imageFile.length())));
                }

                @Override
                public void error(Exception e) {
                    Log.e("AppGetPhotoActivity.error error", e);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_get_photo);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.app_take_photo) {
                    photoHelper.autoRotate(true).takePhoto();
                } else {
                    photoHelper.cropping(true).choosePhoto();
                }
            }
        };
        $.id(R.id.app_choose_photo).clicked(listener);
        $.id(R.id.app_take_photo).clicked(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelper.onActivityResult(requestCode, resultCode, data);
    }
}
