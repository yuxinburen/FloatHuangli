package com.davie.huangli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.*;
import android.widget.TextView;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

public class MainActivity extends Activity {

    private static WindowManager windowManager;
    /**
     * 用于添加到悬浮窗当中的.注意：不要重复添加
     */
    //TextView 变成静态变量
    private static TextView txtView;
//    private  TextView txtView;
    private WindowManager.LayoutParams lp;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //获取 WindowManager 用来添加悬浮窗
        final Context applicationContext = getApplicationContext();
        if (windowManager == null) {

            windowManager = (WindowManager) applicationContext.getSystemService(WINDOW_SERVICE);
        }

        if(txtView!=null){
            //删除悬浮窗
            windowManager.removeView(txtView);
            txtView = null;
        }else { //当前悬浮窗没有创建,就进行初始化
            //准备悬浮窗内容:上下文应该是getApplicationContext();属于全局的上下文
            txtView = new TextView(applicationContext);

            //准备实现复杂的图文混排操作
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append("I Love ");
            int androidStart = sb.length();
            sb.append("Android");

            //需要将"Android“替换成 Android 图标
            //参数 setSpan(Object what,int start,int end,int flag);
            //第一个参数,就是 要设置的样式信息,例如: 替换图片,设置字体,颜色
            //ImageSpan一定要使用带有上下文(Context)的构造

            //指定图片代替文字的效果
            ImageSpan imageSpan = new ImageSpan(this,R.drawable.ic_launcher);
            //第二个参数,要替换字符开始的位置
            //第三个参数,数值的获取是依赖于最后的flag设置的
                    //通常如果采用 起始位置向后计算个数的,那么就是
            //      end 就采用 start+len
            sb.setSpan(
                    imageSpan,
                    androidStart,
                    androidStart+"Android".length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );

            sb.append(" ");
            int currentOffset = sb.length();
            sb.append("I hate iOS");
//            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan();
            //控制 textStyle 属性的Span
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//设置字体粗体

            sb.setSpan(styleSpan, currentOffset, sb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
            sb.setSpan(colorSpan,currentOffset,sb.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //准备悬浮窗内容:上下文应该是getApplicationContext();属于全局的上下文
//        txtView = new TextView(applicationContext);

            txtView.setText(sb);
//            txtView.setTextColor(Color.RED);
            //TextView 支持带有单位的 尺寸 例如: 10sp
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

            //以下需要准备添加到桌面悬浮的设置项和参数
            lp = new WindowManager.LayoutParams();
//        lp.type  type属性用来控制 悬浮窗 是显示在当前某个应用程序中,还是显示在系统桌面中
            //显示在系统桌面上就是TYPE_SYSTEM_OVERLAY
            // 再比如:  TYPE_APPLICATION
            // TYPE_SYSTEM_ALERT
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

            //所有 TYPE_SYSTEM级别的悬浮窗,如果希望原来的应用程序或者其他的应用程序可以进行操作,
            //则必须设置 flag 属性,并且明确标注 不实用焦点.
            // FLAG_NOT_FOCUSABLE 表示 悬浮窗不获取焦点
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            //指定悬浮窗显示的位置
            //x代表当前的悬浮窗显示在界面上的哪个位置,通常,x的取值范围从0到屏幕宽度
            lp.x = 10;
            //y代表当前的悬浮窗显示在界面上的哪个位置,通常,y的取值范围从0到屏幕高度
            lp.y = 50;//y 的含义同上.

            //比重,用于设置悬浮窗的起始位置
            lp.gravity = Gravity.LEFT | Gravity.TOP;

            //宽高会影响窗口的大小.默认全屏,因此必须设置尺寸才可以
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height= WindowManager.LayoutParams.WRAP_CONTENT;

            //设置悬浮窗背景显示方式,如果不设置,默认就是黑色
            lp.format = PixelFormat.TRANSLUCENT;//TRANSLUCENT代表不透明

            //addView添加悬浮窗的时候,一定要使用WindowManager.LayoutParams
            //使用这个类型,能够控制悬浮窗的各种效果
            windowManager.addView(txtView, lp);

            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(applicationContext,ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(intent);
                }
            });

            ////////textView添加触摸事件 ,注意,是内部类,不要用this了.this 代表当前Activity
            txtView.setOnTouchListener(new View.OnTouchListener() {

                private int lastX, lastY;

                /**
                 * ACTION_DOWN
                 *上一次按下事件发生的时间
                 */
                private long lastDownTime;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean ret = false;
                    int actioin = motionEvent.getAction();
                    switch (actioin){
                        case MotionEvent.ACTION_DOWN:
                            ret = true;
                            lastX = (int)motionEvent.getRawX();
                            lastY = (int)motionEvent.getRawY();
                            lastDownTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //触摸事件 就有了x,y

                            //获取手机屏幕上的位置,因为控件上 getX,getY 是相对的
                            //因为悬浮窗坐标是按照手机屏幕尺寸来设置的.
                            float x = motionEvent.getRawX();
                            float y = motionEvent.getRawY();

                            int ix = (int)(x-lastX);
                            int iy = (int)(y-lastY);

                            //设置悬浮窗的新位置
                            lp.x += ix;
                            lp.y += iy;

                            lastX = (int)x;
                            lastY = (int)y;

                            //设置完位置,需要更新窗口
                            windowManager.updateViewLayout(txtView,lp);

                            break;
                        case MotionEvent.ACTION_UP:
                            long ct = System.currentTimeMillis();
                            if(ct-lastDownTime<=300){
                                //TODO 触发事件 onClick的点击
                                txtView.performClick();
                            }
                            break;
                    }
                    return ret;
                }
            });
        }
    }
}
