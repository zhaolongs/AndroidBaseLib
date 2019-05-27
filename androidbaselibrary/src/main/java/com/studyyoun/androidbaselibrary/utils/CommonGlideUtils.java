package com.studyyoun.androidbaselibrary.utils;
/**
 * Created by zhaolong on 2017/9/29.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.studyyoun.androidbaselibrary.view.CustomShareLoadingView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * class infation
 */
public class CommonGlideUtils {

    public static void showImageView(final Context context, final int errorimg, String url,
                                     final ImageView imgeview, final CustomShareLoadingView loadingView, final View defaultView) {

        if (url == null || url.trim().equals("")) {
            imgeview.setImageDrawable(context.getResources().getDrawable(errorimg));
            return;
        }
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imgeview.setImageDrawable(context.getResources().getDrawable(errorimg));
                if (loadingView != null) {
                    loadingView.close();
                    loadingView.setVisibility(View.GONE);
                }
                if (defaultView != null) {
                    defaultView.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (loadingView != null) {
                    loadingView.close();
                    loadingView.setVisibility(View.GONE);
                }
                if (defaultView != null) {
                    defaultView.setVisibility(View.GONE);
                }
                return false;
            }
        }).into(imgeview);
    }

    public static void showImageView(final Context context, final int errorimg, String url,
                                     final ImageView imgeview) {
        showImageView(context, errorimg, url, imgeview, null);

    }

    public static void showImageView(final Context context, final int errorimg, String url,
                                     final ImageView imgeview, CustomShareLoadingView loadingView) {
        showImageView(context, errorimg, url, imgeview, loadingView, null);

    }


    public static void showGif(Context context, String gifUrl, ImageView imgeview) {


    }

    @SuppressLint("CheckResult")
    public static void showImageView(final Context context, String url,
                                     final loadingCallBack callBack) {


        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                callBack.onSuccess(drawable2Bitmap(resource));
            }
        });
    }

    public interface loadingCallBack{
        void onSuccess(Bitmap bitmap);
        void onFaile();
    }
    static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    private static void setBitmapToImg(Bitmap resource, ImageView imgeview) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resource.compress(Bitmap.CompressFormat.PNG, 100, baos);



            int lHeight = resource.getHeight();
            LogUtils.d("max img height "+lHeight);

            ViewGroup.LayoutParams lLayoutParams = imgeview.getLayoutParams();
            if (lLayoutParams != null) {
                LogUtils.d("lLayoutParams.height "+lLayoutParams.height);
                lLayoutParams.height=lHeight;
                imgeview.setLayoutParams(lLayoutParams);
                InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            }
            InputStream isBm = new ByteArrayInputStream(baos.toByteArray());

            //BitmapRegionDecoder newInstance(InputStream is, boolean isShareable)
            //用于创建BitmapRegionDecoder，isBm表示输入流，只有jpeg和png图片才支持这种方式，
            // isShareable如果为true，那BitmapRegionDecoder会对输入流保持一个表面的引用，
            // 如果为false，那么它将会创建一个输入流的复制，并且一直使用它。即使为true，程序也有可能会创建一个输入流的深度复制。
            // 如果图片是逐步解码的，那么为true会降低图片的解码速度。如果路径下的图片不是支持的格式，那就会抛出异常
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(isBm, true);

            final int imgWidth = decoder.getWidth();
            final int imgHeight = decoder.getHeight();

            BitmapFactory.Options opts = new BitmapFactory.Options();

            //计算图片要被切分成几个整块，
            // 如果sum=0 说明图片的长度不足3000px，不进行切分 直接添加
            // 如果sum>0 先添加整图，再添加多余的部分，否则多余的部分不足3000时底部会有空白
            int sum = imgHeight / 3000;

            int redundant = imgHeight % 3000;

            List<Bitmap> bitmapList = new ArrayList<>();

            //说明图片的长度 < 3000
            if (sum == 0) {
                //直接加载
                bitmapList.add(resource);
            } else {
                Rect mRect = new Rect(0, 0, 0, 0);
                //说明需要切分图片
                for (int i = 0; i < sum; i++) {
                    //需要注意：mRect.set(left, top, right, bottom)的第四个参数，
                    //也就是图片的高不能大于这里的4096
                    mRect.set(0, i * 3000, imgWidth, (i + 1) * 3000);
                    Bitmap bm = decoder.decodeRegion(mRect, opts);
                    bitmapList.add(bm);
                }

                //将多余的不足3000的部分作为尾部拼接
                if (redundant > 0) {
                    mRect.set(0, sum * 3000, imgWidth, imgHeight);
                    Bitmap bm = decoder.decodeRegion(mRect, opts);
                    bitmapList.add(bm);
                }

            }

            Bitmap bigbitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
            Canvas bigcanvas = new Canvas(bigbitmap);

            Paint paint = new Paint();
            int iHeight = 0;

            //将之前的bitmap取出来拼接成一个bitmap
            for (int i = 0; i < bitmapList.size(); i++) {
                Bitmap bmp = bitmapList.get(i);
                bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
                iHeight += bmp.getHeight();

                bmp.recycle();
                bmp = null;
            }

            imgeview.setImageBitmap(bigbitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
