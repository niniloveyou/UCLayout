package deadline.uclayout;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by deadline on 2017/6/17.
 * 语音效果
 */

public class UcVoiceView extends View{

    private static final float BEZIER_FACTOR = 0.552284749831f;

    private int animationDuration = 500;

    private int backgroundColor = Color.parseColor("#3C69FF");

    private Drawable voiceDrawable = null;

    private Paint mPaint = null;

    private Path path = null;

    private int size = 0;

    private float mRadius;

    private float centerX, centerY;

    private float anchorX, anchorY;

    private float friction, alpha;

    private AnimatorSet animatorSet;

    public UcVoiceView(Context context) {
        this(context, null);
    }

    public UcVoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UcVoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    private void setUp() {
        path = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * 执行显示
     */
    public void showVoice(){
        clearAnimation();
        getAnimationSet(new float[]{0, 1}, true);
    }

    /**
     * 执行隐藏
     */
    public void hideVoice(){
        clearAnimation();
        getAnimationSet(new float[]{1, 0}, false);
    }

    private void getAnimationSet(float[] values, boolean show){
        animatorSet = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(values);
        animator.setDuration(animationDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if(friction != value){
                    friction = value;
                    invalidate();
                }
            }
        });

        ValueAnimator animator2 = ValueAnimator.ofFloat(values);
        if(show) {
            animator2.setStartDelay(animationDuration / 2);
        }
        animator2.setDuration(animationDuration / 2);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if(alpha != value){
                    alpha = value;
                }
            }
        });

        animatorSet.playTogether(animator, animator2);
        animatorSet.start();
    }

    /**
     * 结束动画
     */
    public void clearAnimation(){
        if(animatorSet != null){
            animatorSet.cancel();
            animatorSet.removeAllListeners();
            animatorSet = null;
        }
    }

    /**
     * 高度是宽度的2倍，方便做动画
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        size = Math.min(width, height);
        int spec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(spec, 2 * spec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(backgroundColor);
        calculatePath();
        canvas.drawCircle(centerX, centerY, mRadius, mPaint);
        canvas.drawPath(path, mPaint);
        voiceDrawable.draw(canvas);
    }

    private void calculatePath(){
        mRadius = size / 2;

        centerX = mRadius;

        centerY = size * (1.5f - friction);

        anchorX = centerX;
        anchorY = size * (2 - alpha);

        //左切点
        float p1X = 0;
        float p1Y = centerY;

        //左边贝塞尔曲线的控制点1
        float p3X = p1X;
        float p3Y = p1Y + mRadius * BEZIER_FACTOR * (1 - friction);

        //左边贝塞尔曲线的控制点2
        float p4X = mRadius * (1 -  BEZIER_FACTOR);
        float p4Y = anchorY;

        //右切点
        float p2X = centerX + mRadius;
        float p2Y = centerY;

        //右边贝塞尔曲线的控制点1
        float p5X = p2X;
        float p5Y = p2Y + mRadius * BEZIER_FACTOR * (1 - friction);

        //右边贝塞尔曲线的控制点2
        float p6X = anchorX  + mRadius * BEZIER_FACTOR;
        float p6Y = anchorY;

        path.reset();
        path.moveTo(p2X, p2Y);
        path.cubicTo(p5X, p5Y, p6X, p6Y, anchorX, anchorY);
        path.cubicTo(p4X, p4Y, p3X, p3Y, p1X, p1Y);
        path.lineTo(p2X, p2Y);
        path.close();
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Drawable getVoiceDrawable() {
        return voiceDrawable;
    }

    public void setVoiceDrawable(Drawable voiceDrawable) {
        this.voiceDrawable = voiceDrawable;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }
}
