package com.studyyoun.androidbaselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studyyoun.androidbaselibrary.R;
import com.studyyoun.androidbaselibrary.view.CustomShareLoadingView;


public class LoadingDialog extends Dialog {

    private LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private View mLayout;
        private TextView mMessage;

        private View.OnClickListener mButtonClickListener;
        private LoadingDialog mDialog;
        private final CustomShareLoadingView mLoadingView;

        public Builder(Context context) {
            mDialog = new LoadingDialog(context, R.style.progress_dialog);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局文件
            mLayout = inflater.inflate(R.layout.progress_dialog_layout, null, false);
            //添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mMessage = mLayout.findViewById(R.id.dialog_progress_message);
            mLoadingView = mLayout.findViewById(R.id.loading_share);
        }

        /**
         * 通过 ID 设置 Dialog 图标
         */
        public Builder setIcon(int resId) {
            return this;
        }

        /**
         * 用 Bitmap 作为 Dialog 图标
         */
        public Builder setIcon(Bitmap bitmap) {
            return this;
        }

        /**
         * 设置 Dialog 标题
         */
        public Builder setTitle(String title) {
            return this;
        }

        /**
         * 设置 Message
         */
        public Builder setMessage(String message) {
            mMessage.setText(message);
            return this;
        }

        /**
         * 设置按钮文字和监听
         */
        public Builder setButton(String text, View.OnClickListener listener) {
            mButtonClickListener = listener;
            return this;
        }

        public LoadingDialog create() {
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);                //用户可以点击后退键关闭 Dialog
            mDialog.setCanceledOnTouchOutside(false);   //用户不可以点击外部来关闭 Dialog
            mLoadingView.start();
            return mDialog;
        }

        public Builder dismiss() {
            mLoadingView.close();
            mDialog.dismiss();
            return this;
        }
    }


}
