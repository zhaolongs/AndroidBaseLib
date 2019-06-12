# FancyButton
FancyButton 自定义View使用方法 
动态设置 enable 来改变button的样式

效果图：
 
 * setEnalbe(falsg)时 不可点击时

![setEnalbe(falsg)时](https://github.com/zhaolongs/AndroidBaseLib/blob/master/images/fans_enable_false.png)

 * setEnalbe(treu)时 可点击时
 
![setEnalbe(true)时](https://github.com/zhaolongs/AndroidBaseLib/blob/master/images/fans_enable_true.png )


 * 当然还有按下时的效果 这里没有捕捉到效果图

*** 

使用方法 ：



```xml
                    <mehdi.sakout.fancybuttons.FancyButton
                        android:id="@+id/tv_commit_login_data"
                        style="@style/baseButtonLongStyle"
                        android:text="@string/login" />
```

```xml
    <style name="baseButtonLongStyle">
        <item name="android:layout_height">40dp</item>
        <item name="android:layout_marginLeft">24dp</item>
        <item name="android:layout_marginRight">24dp</item>
        <item name="android:layout_marginBottom">15dp</item>
        <item name="android:layout_marginTop">10dp</item>
        <item name="android:gravity">center</item>
        <item name="android:layout_width">match_parent</item>
        <!--设置button 的不可用-->
        <item name="android:enabled">false</item>
        <!--默认显示颜色 可用时-->
        <item name="fb_defaultColor">#ffa411</item>
        <!--不可用时显示背景颜色-->
        <item name="fb_disabledColor">#dcdfe3</item>
        <!--不可用时显示字体颜色-->
        <item name="fb_disabledTextColor">#ffffff</item>
        <!--可用时 按下button的显示颜色-->
        <item name="fb_focusColor">#e18b00</item>
        <!--圆角-->
        <item name="fb_radius">2dp</item>
        <!--默认显示字体颜色-->
        <item name="fb_textColor">#ffffff</item>
        <!--显示字体大小-->
        <item name="fb_textSize">17sp</item>
    </style>


```
