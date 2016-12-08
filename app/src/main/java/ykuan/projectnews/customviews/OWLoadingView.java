package ykuan.projectnews.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import ykuan.projectnews.R;

public class OWLoadingView extends ViewGroup {
    private static final int PADDING = 3 ;
    private int [] left_p =new int[7];
    private int [] top_p =new int[7];
    private int [] right_p =new int[7];
    private int [] bottom_p =new int[7];
    private int currentViewId = 0 ;
    private boolean At_Once = true ;
    private Boolean flag_Increase = true ;
    private Handler addHandler,removeHandler;
    private OWchildView[] oWchildViews;
    private int side_length;
    public OWLoadingView(Context context) {
        super(context);

    }

    public OWLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        oWchildViews = new OWchildView[7];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        for(int i=0;i<7;i++) {
            oWchildViews[i]= new OWchildView(context,attrs);
            oWchildViews[i].setImageResource(R.drawable.img1);
            oWchildViews[i].setLayoutParams(params);
            addView(oWchildViews[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View[] views = new View[getChildCount()];
//        测算单个view的正六边形边长
        OWchildView oWchildView = (OWchildView) getChildAt(0);
        side_length = oWchildView.getSide_Length();
//        初始化7个view坐标
        int size1p5 = (int) Math.ceil(1.5*side_length);
        int sizeqrt = (int) Math.ceil(Math.sqrt(3)*side_length/2);
        left_p = new int[]{size1p5+PADDING,2*PADDING+3*side_length,2*PADDING+3*side_length,
                PADDING+size1p5,0,0,PADDING+size1p5};
        top_p = new int[]{0,PADDING+sizeqrt,2*PADDING+3*sizeqrt,
                2*PADDING+4*sizeqrt,PADDING*2+3*sizeqrt,PADDING+sizeqrt,PADDING+2*sizeqrt};
        right_p =new int[]{2*side_length+size1p5+PADDING,2*PADDING+5*side_length,2*PADDING+5*side_length,
                PADDING+2*side_length+size1p5,2*side_length,2*side_length,PADDING+2*side_length+size1p5};
        bottom_p= new int[]{2*sizeqrt,PADDING+3*sizeqrt,2*PADDING+5*sizeqrt,2*PADDING+6*sizeqrt,2*PADDING+5*sizeqrt,
                PADDING+3*sizeqrt,PADDING+4*sizeqrt};
        for(int i= 0 ;i<getChildCount();i++){
            views[i] =getChildAt(i);
            measureChild(views[i],widthMeasureSpec,heightMeasureSpec);
            ViewInfo info = new ViewInfo(
                    left_p[i],top_p[i],right_p[i],bottom_p[i]);
            views[i].setTag(info);
        }

        setMeasuredDimension(5*side_length+2*PADDING,
                (int) Math.ceil(Math.sqrt(3)*3*side_length)+2*PADDING);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, final int r, int b) {
        /*View[] views = new View[getChildCount()];
        for(int i=0;i<getChildCount();i++){
            views[i] =getChildAt(i);
            views[i].setVisibility(INVISIBLE);
            ViewInfo info = (ViewInfo) views[i].getTag();
            views[i].layout(info.l,info.t,info.r,info.b);
        }*/
        if(At_Once){
            addHandler = new Handler();
            removeHandler = new Handler();
            addHandler.postDelayed(addRunnable,200);
            At_Once = false ;
        }
    }


    public class ViewInfo{
        private int l,r,t,b;
        public ViewInfo(int l,int t,int r,int b){
            this.l = l;
            this.t = t;
            this.r = r;
            this.b = b;
        }
    }
    public void removeChildView(int position){
        getChildAt(position).setVisibility(INVISIBLE);
        ScaleAnimation animation = new ScaleAnimation(1,0.5f,1,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(200);
        getChildAt(position).startAnimation(animation);
    }
    public void addChildView(int position){
        getChildAt(position).setVisibility(VISIBLE);
        ViewInfo info = (ViewInfo) getChildAt(position).getTag();
        getChildAt(position).layout(info.l,info.t,info.r,info.b);
        ScaleAnimation animation = new ScaleAnimation(0.5f,1,0.5f,1,
                Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(200);
        getChildAt(position).startAnimation(animation);
        //invalidate();
    }
    public Runnable addRunnable = new Runnable() {
        @Override
        public void run() {
            addChildView(currentViewId);
            currentViewId++;
            if(currentViewId == 7){
                currentViewId = 6;
                addHandler.removeCallbacks(addRunnable);
                removeHandler.postDelayed(removeRunnable,200);
            }
            else{
                addHandler.postDelayed(addRunnable,200);
            }
        }
    };

    public Runnable removeRunnable =new Runnable() {
        @Override
        public void run() {
            removeChildView(6-currentViewId);
            currentViewId--;
            if(currentViewId == -1){
                currentViewId = 0 ;
                removeHandler.removeCallbacks(removeRunnable);
                addHandler.postDelayed(addRunnable,200);
            }else{
                removeHandler.postDelayed(removeRunnable,200);
            }
        }
    };

}
