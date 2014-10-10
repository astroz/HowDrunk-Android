package com.example.test;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Omatic on 6/5/13.
 */
public class BubbleView extends View implements TaskFragment.TaskCallbacks{
    private boolean showBubbles;
    private int bubbleSpeed, bubbleCapacity, viewHeight, viewWidth, mTextColor = 0xffffffff;
    private float mTextHeight;
    private Paint mTextPaint, mBubblePaint, mShadowPaint;
    private int stateToSave;

    private ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
    private BubbleFactory bf;

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                        attrs,
                        R.styleable.BubbleView,
                        0, 0);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        try {
            showBubbles = a.getBoolean(R.styleable.BubbleView_showBubbles, false);
            bubbleSpeed = a.getInteger(R.styleable.BubbleView_bubbleSpeed, height / 2);
            bubbleCapacity = a.getInteger(R.styleable.BubbleView_bubbleCapacity, 0);
            viewHeight = a.getInteger(R.styleable.BubbleView_viewHeight, height);
            viewWidth = a.getInteger(R.styleable.BubbleView_viewWidth, width);
        } finally {
            a.recycle();
        }

        // NOTE: Try for X = (max between height and width) + 100px or so, make bubble field X by X so orientation changes dont disrupt things.
        initBubbles();
        initCanvas();

    }

    private void initCanvas(){
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(0xffFF9900);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xffFF9900);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }
    
    // will end up with bubbles alist full of bubbles.
    private void initBubbles(){
        bf = new BubbleFactory(viewHeight * 1.2f, viewWidth * 1.2f, 40, bubbleSpeed); // TODO: Get better reading of appropriate bubble sizes
        // based on screen size, set number of bubbles to be created. TODO: Multiply by user setting for "bubble frequency"
        int numBubbles = (viewHeight * viewWidth) / 50000; // 1200 X 800 will result in 18 bubbles by default.
        // create new bubbles with random sizes and speeds in ranges allowed
        for (int x = 0; x < numBubbles; x++) // create bubbles randomly across screen, at depths 50 to 300 pixels below bottom of screen
            bubbles.add(bf.makeBubbleAt(Utils.random.nextFloat() * viewWidth, viewHeight + 50f + (Utils.random.nextFloat() * 250)));
    }

    int debugNum = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        debugNum++;
        super.onDraw(canvas);
        //Log.d("onDraw", "Entering onDraw");
        for (Bubble b : bubbles){
            canvas.drawCircle(b.getX(), b.getY(), b.getSize(), mBubblePaint);
            b.update();
            if (bubbles.indexOf(b) == 1 && debugNum % 20 == 0)
                Log.d("BubbleView", "Drawing circle " + bubbles.indexOf(b) + " of size " + b.getSize() + " at " + b.getX() + "," + b.getY());
        }
        invalidate();
    }

    //http://stackoverflow.com/questions/3542333/how-to-prevent-custom-views-from-losing-state-across-screen-orientation-changes/3542895#3542895
    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.stateToSave;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.stateToSave = ss.stateToSave;
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    @Override
    protected void onSizeChanged(int newW, int newH, int oldW, int oldH) {
        super.onSizeChanged(newW, newH, oldW, oldH);

        Log.d("BubbleView", "Entering onSizeChanged");
        Log.d("BubbleView","oldw = " + oldW + " oldh = " + oldH + " w = " + newW + " h = " + newH);
        if (oldH != 0 && oldW != 0) {

            if ((newW > newH) && (oldW < oldH)) {} // port to land
            if ((newW > newH) && (oldW < oldH)) {} // land to port

            for (Bubble b : bubbles) { // transform the bubble coordinates
                float bubbleX = b.getX(), bubbleY = b.getY();
                b.setX(newW * (bubbleX / oldW));
                b.setY(newW * (bubbleY / oldH));

                if (bubbles.indexOf(b) == 1)Log.d("BubbleView","Bubble " + bubbles.indexOf(b) + " moved to " + b.getX() + "," + b.getY());
            }
        }
        // expand the canvas to the new size.
        // move the bubbles to the appropriate adjusted position

    }

    public boolean isShowBubbles() {
        return showBubbles;
    }

    public void setShowBubbles(boolean showBubbles) {
        this.showBubbles = showBubbles;
    }

    public int getBubbleSpeed() {
        return bubbleSpeed;
    }

    public void setBubbleSpeed(int bubbleSpeed) {
        this.bubbleSpeed = bubbleSpeed;
    }

    public int getBubbleCapacity() {
        return bubbleCapacity;
    }

    public void setBubbleCapacity(int bubbleCapacity) {
        this.bubbleCapacity = bubbleCapacity;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    // The four methods below are called by the TaskFragment when new
    // progress updates or results are available. The MainActivity
    // should respond by updating its UI to indicate the change.

    @Override
    public void onPreExecute() {}

    @Override
    public void onProgressUpdate(int percent) {}

    @Override
    public void onCancelled() {}

    @Override
    public void onPostExecute() {}

}

class Bubble {
    // keep track of where it is and its size, speed, transparancy.
    private float x, y, size; // location tracker
    private double speed;
    private int alpha;

    public Bubble (float x, float y, float size, double speed, int alpha) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.alpha = alpha;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void transformCoords(String s){

    }

    public float getSize() {
        return size;
    }

    public void update(){ // increase x by its speed
        y -= speed;
    }

    public void pop(){
        // expand the size by 1.5 over .25 seconds
        // set size to 0 for 15 seconds
        // reset size to original
    }

    public void draw(){}
}

class BubbleFactory {
    float maxX, maxY, maxSize; // used in random value generation
    double maxSpeed;
    boolean alphaRandom; //true if each bubble is to have a different alpha

    public BubbleFactory (int maxX, int maxY) {
        new BubbleFactory(maxX, maxY, 15, 30, true);
    }

    public BubbleFactory (float maxX, float maxY, float maxSize, double maxSpeed) {
        new BubbleFactory(maxX, maxY, maxSize, maxSpeed, true);
    }

    public BubbleFactory (float maxX, float maxY, float maxSize, double maxSpeed, boolean alphaRandom) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxSize = maxSize;
        this.maxSpeed = maxSpeed;
        this.alphaRandom = alphaRandom;
    }

    // returns a very random bubble
    public Bubble makeBubble(){
        return new Bubble(
            Utils.random.nextFloat() * maxX,
            Utils.random.nextFloat() * maxY,
            1 + ((maxSize - 1) * Utils.random.nextFloat()),
            1 + ((maxSpeed - 1) * Utils.random.nextDouble()),
            getAlpha());
    }

    public Bubble makeBubbleWithSpeed (double speed){
        return new Bubble(
            Utils.random.nextFloat() * maxX,
            Utils.random.nextFloat() * maxY,
            1 + (maxSize - 1) *  Utils.random.nextFloat(),
            speed,
            getAlpha());
    }

    public Bubble makeBubbleWithSize (float size){
        return new Bubble(
            Utils.random.nextFloat() * maxX,
            Utils.random.nextFloat() * maxY,
            size,
            1 + (maxSpeed - 1) *  Utils.random.nextDouble(),
            getAlpha());
    }

    public Bubble makeBubbleAt (float x, float y){
        return new Bubble(
            x,
            y,
            25,//1 + (maxSize - 1) *  Utils.random.nextFloat(),
            1 + (maxSpeed - 1) *  Utils.random.nextDouble(),
            getAlpha());
    }

    private int getAlpha(){
        return alphaRandom ? Utils.random.nextInt(235) + 51 : 255; // minimum alpha is 51 (20%)
    }
}

class TaskFragment extends Fragment {
    static interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        mTask = new DummyTask();
        mTask.execute();
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class DummyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
        @Override
        protected Void doInBackground(Void... ignore) {
            for (int i = 0; !isCancelled() && i < 100; i++) {
                SystemClock.sleep(100);
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(percent[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(Void ignore) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }
        }
    }

}

class Utils {
    public static final Random random = new Random();

}
