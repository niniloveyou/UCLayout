package deadline.uclayout;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author deadline
 * @time 2016/12/4
 *
 * UC浏览器首页效果
 */
public class UCLayout extends ViewGroup{

    private static final String TAG = UCLayout.class.getSimpleName();

    static final int STATE_OPEN = 0;
    static final int STATE_CLOSED = 1;
    static final int STATE_PULLING = 2;
    static final int STATE_SHRINK = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_OPEN, STATE_CLOSED,
            STATE_PULLING,STATE_SHRINK})
    public @interface State{

    }

    private int mState = STATE_OPEN;

    private int mTouchSlop;

    private int mDownY;

    //当前滑动百分比
    private int mFriction;

    private ViewDragHelper mDragHelper;

    public UCLayout(Context context) {
        this(context, null);
    }

    public UCLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public UCLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpUcLayout(attrs);
    }

    private void setUpUcLayout(AttributeSet attrs) {
        //获取属性
    }

    public void setState(@State int state){
        this.mState = state;
        onStateChanged(state, mState);
    }

    public @State int getState(){
        return mState;
    }

    /**
     * 状态变化时的回调
     * @param oldState
     * @param currentState
     */
    protected void onStateChanged(int oldState, int currentState) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //检查各个子View的合法性以及获取子View


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
