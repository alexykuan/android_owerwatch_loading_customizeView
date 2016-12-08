package ykuan.projectnews.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Paint;

import ykuan.projectnews.R;


public class OWchildView extends ImageView {
    private static int DEFAULT_SIDE_LENGTH = 80 ;
    private int SIDE_LENGTH;
    private Paint mPaint;
    public OWchildView(Context context) {
        super(context,null);
    }

    public OWchildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getResources().obtainAttributes(attrs,R.styleable.OWchildView);
        SIDE_LENGTH =array.getInt(R.styleable.OWchildView_side_length,DEFAULT_SIDE_LENGTH);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int viewWidth = 2 * SIDE_LENGTH;
        int viewHeight = (int) Math.ceil (Math.sqrt(3) * 1.0f *SIDE_LENGTH);
        setMeasuredDimension(viewWidth,viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getDrawable() == null){
            return;
        }else {
            setUpShader();
            Path mPath = new Path();
            float height = (float) (Math.sqrt(3)*SIDE_LENGTH);
            mPath.moveTo(SIDE_LENGTH/2,0);
            mPath.lineTo(0,height/2);
            mPath.lineTo(SIDE_LENGTH/2,height);
            mPath.lineTo((float) (SIDE_LENGTH*1.5),height);
            mPath.lineTo(2*SIDE_LENGTH,height/2);
            mPath.lineTo((float) (SIDE_LENGTH*1.5),0);
            mPath.lineTo(SIDE_LENGTH/2,0);
            mPath.close();
            canvas.drawPath(mPath,mPaint);
        }
    }
    public void setUpShader(){
        Matrix matrix = new Matrix();
        Drawable drawable = getDrawable();
        if (drawable == null)
        {
            return;
        }
        Bitmap bmp = drawableToBitmap(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scaleHeight ;
        float scaleWidth ;
        scaleWidth =  getWidth() * 1.0f / bmp.getWidth();
        scaleHeight = getHeight() * 1.0f / bmp.getHeight() ;
        matrix.setScale(scaleWidth,scaleHeight);
        mBitmapShader.setLocalMatrix(matrix);
        mPaint =new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10f);
        mPaint.setShader(mBitmapShader);
    }
    public Bitmap drawableToBitmap(Drawable drawable){
        if(getDrawable() instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();}
        else{
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public void setSide_Length(int length){
        if(length >= 0){
            this.SIDE_LENGTH = length;
        }else{
            this.SIDE_LENGTH = DEFAULT_SIDE_LENGTH ;
        }
    }
    public int getSide_Length(){
        return this.SIDE_LENGTH;
    }
}
