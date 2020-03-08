package com.torahli.myapplication.framwork.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.torahli.myapplication.R;


/**
 * 需求1：一边固定，另一边按照图片的尺寸来设置长度。显示方式是centerCrop
 * 需求2：控件的高宽比固定
 */
public class SquareImagView extends androidx.appcompat.widget.AppCompatImageView {
    /**
     * 高度除以宽度
     */
    private float ratio;

    public SquareImagView(Context context) {
        super(context);
    }

    public SquareImagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImagView);
        //根据属性名称获取对应的值，属性名称的格式为类名_属性名
        ratio = typedArray.getFloat(R.styleable.SquareImagView_ratio, 1.0f);
        typedArray.recycle();
    }

    public SquareImagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0) {
            heightSize = (int) (widthSize * ratio + 0.5f);//根据宽度和比例计算高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY & ratio != 0) {
            widthSize = (int) (heightSize / ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        } else {
            heightSize = (int) (widthSize * ratio + 0.5f);//按宽度的模式设置
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, widthMode);
        }
        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 设置宽高比
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
