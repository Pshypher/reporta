package com.example.android.reporta.admin.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.android.reporta.R;

public class PercentArcView extends View {

    private static final String TAG = "PercentArcView";

    private float _ratio = 0.76f;
    // private float _ratio;
    private float _radius;

    // Style attributes
    private int _partPercentColor;
    private int _wholePercentColor;
    private boolean _showPercentage;
    private float _textWidth;
    private float _textHeight;

    // Paint objects
    private Paint _partPercentPaint;
    private Paint _wholePercentPaint;
    private Paint _textPaint;

    // Shape(s)
    private RectF _ovalBounds;
    private Rect _canvasBounds;

    public static final float THICKNESS = 6.33f;
    private static final float TEXT_SIZE = 22f;
    private static final float START_ANGLE = 270f;


    public PercentArcView(Context context) {
        super(context);
        init(context, null);
    }

    public PercentArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PercentArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        _partPercentColor = ResourcesCompat.getColor(getResources(), R.color.opaque_gray1, null);
        _wholePercentColor = ResourcesCompat.getColor(getResources(),
                R.color.opaque_gray1, null);

        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PercentArcView,
                    0, 0);
            _showPercentage = array.getBoolean(R.styleable.PercentArcView_showPercentage, false);
            _partPercentColor = array.getColor(R.styleable.PercentArcView_actualPercentColor, _partPercentColor);
            _wholePercentColor = array.getColor(R.styleable.PercentArcView_totalPercentColor, _wholePercentColor);
            array.recycle();
        }
        initArcStroke();

        if (_showPercentage) {
            initPercentText(context);
        }

        _ovalBounds = new RectF();
        _canvasBounds = new Rect();
    }

    private void initArcStroke() {
        _wholePercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _wholePercentPaint.setStyle(Paint.Style.STROKE);
        _wholePercentPaint.setStrokeWidth(THICKNESS);
        _partPercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _partPercentPaint.setStyle(Paint.Style.STROKE);
        _partPercentPaint.setStrokeWidth(THICKNESS);
        _partPercentPaint.setStrokeCap(Paint.Cap.ROUND);
        _wholePercentPaint.setColor(_wholePercentColor);
        _partPercentPaint.setColor(_partPercentColor);
    }

    private void initPercentText(Context context) {
        _textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        _textPaint.setTextAlign(Paint.Align.LEFT);
        _textPaint.setStyle(Paint.Style.STROKE);
        _textPaint.setTextSize(Math.round(TEXT_SIZE * getResources().getDisplayMetrics().scaledDensity));

        Typeface plain =Typeface.createFromAsset(context.getAssets(),
                "C:\\Users\\Pshypher\\AndroidStudioProjects\\stutern\\Reporta\\app\\src\\main\\res\\font\\avenir_regular.ttf");
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        _textPaint.setTypeface(bold);
        _textPaint.setColor(Color.BLACK);

        Rect textBounds = new Rect();
        String text = (int)(_ratio * 100) + "%";
        _textPaint.getTextBounds(text, 0, text.length(), textBounds);
        _textWidth = _textPaint.measureText(text);
        _textHeight = textBounds.height();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the arc.
        _radius = Math.min(ww, hh) * 0.5f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "onMeasure width: " + MeasureSpec.toString(widthMeasureSpec));
        Log.d(TAG, "onMeasure height: " + MeasureSpec.toString(heightMeasureSpec));

        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Ask for a height that would let the arc
        // get as big as it can
        int minh = getSuggestedMinimumHeight() + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        _ovalBounds.left = getPaddingLeft();
        _ovalBounds.top = getPaddingTop();
        float diameter = _radius * 2;
        _ovalBounds.right = getPaddingLeft() + diameter;
        _ovalBounds.bottom = getPaddingTop() + diameter;

        float theta = _ratio * 360f;
        canvas.drawArc(_ovalBounds, START_ANGLE, 360f, false, _wholePercentPaint);
        canvas.drawArc(_ovalBounds, START_ANGLE, theta, false, _partPercentPaint);
        if (_showPercentage) {
            String text = (int) (_ratio * 100) + "%";
            canvas.getClipBounds(_canvasBounds);
            canvas.drawText(
                    text,
                    _canvasBounds.centerX() - (_textWidth / 2f),
                    _canvasBounds.centerY() + (_textHeight / 3f),
                    _textPaint
            );
        }
    }

    public boolean isShowPercentage() {
        return _showPercentage;
    }

    public void setShowPercentage(boolean flag) {
        this._showPercentage = flag;
    }

    public int getActualPercentColor() {
        return _partPercentColor;
    }

    public void setActualPercentColor(int color) {
        this._partPercentColor = color;
    }

    public int getTotalPercentColor() {
        return _wholePercentColor;
    }

    public void setTotalPercentColor(int color) {
        this._wholePercentColor = color;
    }

    public double getRatio() {
        return _ratio;
    }

    public void setRatio(float ratio) {
        this._ratio = ratio;
    }
}
