package com.studyyoun.androidbaselibrary.base;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import com.studyyoun.androidbaselibrary.utils.LogUtils;
import com.studyyoun.androidbaselibrary.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分类基类
 *
 * 1、提示 showMsg(String message)
 * 2、EditText 设置输入监听 setEditWatcher(final EditText editText, final int tagCount)
 * 3、字数计算 count(String text)
 */
public class CommonBaseItem {

    public interface EditWatcherCallBack {
        void currentTextCount(int count, int tagCount);
    }

    private Context mContext;

    public CommonBaseItem(Context context) {
        mContext = context;
    }

    protected void showMsg(String message) {
        ToastUtils.show(message, mContext);
    }

    /**
     * EditText 设置输入监听
     * @param editText 监听对象
     * @param tagCount 限字字数
     */
    protected void setEditWatcher(final EditText editText, final int tagCount) {
        setEditWatcher(editText, tagCount, null);
    }

    protected void setEditWatcher(final EditText editText, final int tagCount, final EditWatcherCallBack callBack) {
        setEditWatcher(editText, tagCount, false, callBack);
    }
    /**
     * EditText 设置输入监听
     * @param editText 监听对象
     * @param tagCount 限字字数
     * @param isShowToast 显示超限字数提示
     * @param callBack 当前字数回调
     */
    protected void setEditWatcher(final EditText editText, final int tagCount, final boolean isShowToast, final EditWatcherCallBack callBack) {
        editText.addTextChangedListener(
                new TextWatcher() {

                    private int start;
                    private int end;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        start = editText.getSelectionStart();
                        end = editText.getSelectionEnd();
                        int count = count(s.toString());
                        if (count > tagCount && start > 0) {
                            LogUtils.e("超过 " + tagCount + " 个字");
                            if (isShowToast) {
                                ToastUtils.show("最多可输入" + tagCount, mContext);
                            }
                            s.delete(start - 1, end);
                            editText.setText(s);
                            editText.setSelection(s.length());
                            if (callBack != null) {
                                callBack.currentTextCount(tagCount, tagCount);
                            }
                        } else {
                            if (callBack != null) {
                                callBack.currentTextCount(count, tagCount);
                            }
                        }
                    }
                });
    }

    protected int count(String text) {
        int len = text.length();
        int skip;
        int letter = 0;
        int chinese = 0;
        for (int i = 0; i < len; i += skip) {
            int code = text.codePointAt(i);
            skip = Character.charCount(code);
            if (code == 10) {
                continue;
            }
            String s = text.substring(i, i + skip);
            if (isChinese(s)) {
                chinese++;
            } else {
                letter++;
            }
        }
        letter = letter % 2 == 0 ? letter / 2 : (letter / 2 + 1);
        int result = chinese + letter;
        return result;
    }

    // 完整的判断中文汉字和符号
    protected boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    public InputFilter typeFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[a-zA-Z|\u4e00-\u9fa5]+");
            Matcher m = p.matcher(source.toString());
            if (!m.matches()) return "";
            return null;
        }
    };

}
