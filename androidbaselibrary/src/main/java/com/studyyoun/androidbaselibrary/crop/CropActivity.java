package com.studyyoun.androidbaselibrary.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.studyyoun.androidbaselibrary.R;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.utils.CommonGlideUtils;
import com.studyyoun.androidbaselibrary.view.CropLayout;

import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;

public class CropActivity extends CommonBaseActivity implements View.OnClickListener {
    private CropLayout mCropLayout;

    private String mUrl;

    public static void show(Activity context, String imageUrl) {
        Intent lIntent = new Intent(context, CropActivity.class);
        lIntent.putExtra("imageUrl", imageUrl);
        context.startActivityForResult(lIntent, 0x04);
    }

    @Override
    protected void getAllIntentExtraDatas(Intent intent) {
        mUrl = intent.getStringExtra("imageUrl");
    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.ab_activity_crop;
    }

    @Override
    protected void commonInitView(View view) {
        ButterKnife.bind(this);
        mCropLayout = findViewById(R.id.cropLayout);
    }

    @Override
    protected void commonFunction() {
        findViewById(R.id.tv_crop).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_crop) {
            Bitmap bitmap = null;
            FileOutputStream os = null;
            try {
                bitmap = mCropLayout.cropBitmap();
                String path = getFilesDir() + "/crop.jpg";
                os = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();

                Intent intent = new Intent();
                intent.putExtra("crop_path", path);
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null) bitmap.recycle();
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (i == R.id.tv_cancel) {
            finish();

        }
    }

    @Override
    protected void commonDelayFunction() {

        CommonGlideUtils.showImageView(mContext, R.mipmap.ab_toast_default, mUrl, mCropLayout.getImageView());
        mCropLayout.start();
    }
}
