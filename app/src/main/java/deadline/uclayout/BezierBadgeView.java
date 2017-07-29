package deadline.uclayout;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 *
 * Created by deadline on 2016/4/29.
 */
public class BezierBadgeView extends FrameLayout{

    private static final String TAG = BezierBadgeView.class.getSimpleName();

    private static final int  DEFAULT_RADIUS = 12;

    private static final int DISMISS_DISTANCE = 150;

    private static final int RECOVERY_DISTANCE = 150;

    private int radius = DEFAULT_RADIUS;

    /**
     * 手指触摸的view
     */
    private ImageView touchView = null;

    /**
     * 松开手结束时的爆炸效果
     */
    private ImageView exploreView = null;

    /**
     * 画红色拉条的画笔
     */
    private Paint mPaint = null;

    /**
     * 记录路径
     */
    private Path mPath = null;

    /**
     * 原始位置
     */
    private Rect mSrcRect = null;
    private int srcX, srcY;

    /**
     * 是否触摸点在touchView上
     */
    private boolean isTouch;

    /**
     * 只记录一次原始位置
     */
    private boolean isFirst;

    /**
     * 是否拖动的距离达到一定距离，用于判断是否需要隐藏红色拉条
     */
    private boolean isOutofDistance;

    private int touchX, touchY;

    private float anchorX, anchorY;

    public BezierBadgeView(Context context) {
        this(context, null);
    }

    public BezierBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBezierBadgeView();
    }

    private void initBezierBadgeView() {

        //自定义view继承自viewGroup需要否则invalidate无效
        setWillNotDraw(false);

        isTouch = false;
        isFirst = false;
        isOutofDistance = false;

        mSrcRect = new Rect(0, 0, 0, 0);

        mPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);


        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //touchView
        touchView = new ImageView(getContext());
        //touchView.setImageResource(R.mipmap.skin_tips_newmessage_ninetynine);

        //exploreView
        exploreView = new ImageView(getContext());
       // exploreView.setImageResource(R.drawable.tip_anim);
        exploreView.setVisibility(GONE);

        addView(touchView, lp);
        addView(exploreView, lp);

        //test
        // exploreView.setX(500f);
        // exploreView.setY(1000f);
    }

    /**
     * 设置touchView的tip
     * @param drawable
     */
    public void setBadgeImage(Drawable drawable){
        if(drawable != null){
            touchView.setImageDrawable(drawable);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                isTouch = mSrcRect.contains((int)event.getX(), (int)event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                if(isTouch) {
                    touchX = (int) event.getX();
                    touchY = (int) event.getY();
                    if(Math.abs(touchY - srcY) > 5 && Math.abs(touchX - srcX) > 5) {
                        anchorX = (event.getX() + srcX) / 2;
                        anchorY = (event.getY() + srcY) / 2;

                        touchView.setX(event.getX() - touchView.getWidth() / 2);
                        touchView.setY(event.getY() - touchView.getHeight() / 2);
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(isTouch){
                    invalidate();
                    if(!getIsOutofDistance(RECOVERY_DISTANCE)){

                        touchView.setX(mSrcRect.left);
                        touchView.setY(mSrcRect.top);

                    } else if(!isOutofDistance) {
                        touchView.setX(mSrcRect.left);
                        touchView.setY(mSrcRect.top);
                    }else{
                        exploreView.setX(event.getX() - exploreView.getWidth() / 2);
                        exploreView.setY(event.getY() - exploreView.getHeight() / 2);
                        touchView.setVisibility(GONE);
                        exploreView.setVisibility(VISIBLE);
                       // exploreView.setImageResource(R.drawable.tip_anim);
                        ((AnimationDrawable) exploreView.getDrawable()).stop();
                        ((AnimationDrawable) exploreView.getDrawable()).start();

                        //动画执行完后remove
                    }
                }
                isTouch = false;
                break;

            default:
                break;

        }
        if(isTouch) {
            return true; //事件消耗
        }
        return super.onTouchEvent(event);
    }

    private boolean getIsOutofDistance(int distance) {

        return Math.sqrt(Math.pow(Math.abs(touchX - srcX), 2) + Math.pow(Math.abs(touchY - srcY), 2)) > distance;

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(!isFirst)
        {
            isFirst = true;
            mSrcRect.left   = touchView.getLeft();
            mSrcRect.top    = touchView.getTop();
            mSrcRect.right  = mSrcRect.left  + touchView.getWidth();
            mSrcRect.bottom = mSrcRect.top + touchView.getHeight();
            srcX = (mSrcRect.left + mSrcRect.right) / 2;
            srcY = (mSrcRect.top + mSrcRect.bottom) / 2;
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {

        isOutofDistance = getIsOutofDistance(DISMISS_DISTANCE);

        if(isTouch && !isOutofDistance){
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
            calculatePath();
            canvas.drawPath(mPath, mPaint);
            canvas.drawCircle((float) srcX, (float) srcY, radius, mPaint); //画原位置的红色小圆点
            canvas.drawCircle((float) touchX, (float) touchY, radius, mPaint);
        }else{
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        }
        super.onDraw(canvas);
    }

    private void calculatePath() {

        float offsetX = (float) (radius * Math.sin(Math.atan((touchY - srcY) / (touchX - srcX))));
        float offsetY = (float) (radius * Math.cos(Math.atan((touchY - srcY) / (touchX - srcX))));
        float x1 = srcX - offsetX;
        float y1 = srcY + offsetY;

        float x2 = touchX - offsetX;
        float y2 = touchY + offsetY;

        float x3 = touchX + offsetX;
        float y3 = touchY - offsetY;

        float x4 = srcX + offsetX;
        float y4 = srcY - offsetY;

        mPath.reset();
        mPath.moveTo(x1, y1);
        mPath.quadTo(anchorX, anchorY, x2, y2);
        mPath.lineTo(x3, y3);
        mPath.quadTo(anchorX, anchorY, x4, y4);
        mPath.lineTo(x1, y1);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    private void release() {
    }
}
