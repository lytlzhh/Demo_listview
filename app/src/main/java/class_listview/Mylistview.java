package class_listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xerdp.demo_listview.R;

/**
 * Created by xerdp on 2016/12/19.
 */

public class Mylistview extends ListView implements AbsListView.OnScrollListener {

    /*本demo是listview滑动状态：初始正常状态，下拉状态，下拉到一定的位置时的提示是否刷新状态，和正在刷新状态，通过这四种状态来架起整个demo的框架*/
    View view = null;
    LayoutInflater layoutInflater = null;
    /*view的高度*/
    int view_height;

    private int firstvisibleItem;
    boolean ismark;
    int scrollstate;

    int state;
    final int NONE = 0;
    final int PULL = 1;
    final int REFRES = 2;
    final int REFRESING = 3;

    int startY;/*起始位置的高度*/

    MyOnRefreshListener myOnRefreshListener = null;

    public Mylistview(Context context) {
        super(context);
        init_view(context);
    }

    public Mylistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_view(context);
    }

    public Mylistview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_view(context);
    }


    public void init_view(Context context) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.header_view, null);
        measure_view(view);
        view_height = view.getMeasuredHeight();
        Topadding(-view_height);
        this.addHeaderView(view);
        this.setOnScrollListener(this);
    }


    public void measure_view(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        int width = params.width;
        int heght = params.height;
        int final_height;

        if (heght > 0) {
            final_height = MeasureSpec.makeMeasureSpec(heght, MeasureSpec.EXACTLY);
        } else {
            final_height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, final_height);

    }

    public void Topadding(int top_len) {
        view.setPadding(view.getPaddingLeft(), top_len, view.getPaddingRight(), view.getPaddingBottom());
        view.setVisibility(VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstvisibleItem == 0) {
                    // state = NONE;
                    ismark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == REFRES) {
                    state = REFRESING;
                    // ismark = true;
                    /*刷新*/
                    OnRefresh_view();
                    /*刷新listview*/
                    myOnRefreshListener.OnRrfesh();

                } else if (state == PULL) {
                    state = NONE;
                    ismark = false;
                    /*刷新*/
                    OnRefresh_view();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                OnMovew(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void OnRefresh_view() {
        TextView header_text = (TextView) view.findViewById(R.id.header_textview);
        header_text.setTextSize(20);

        ImageView header_image = (ImageView) view.findViewById(R.id.header_imageview);

        RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(800);
        rotateAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation1 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(800);
        rotateAnimation.setFillAfter(true);

        switch (state) {
            case NONE:
                //header_image.clearAnimation();
                Topadding(-view_height);
                break;
            case PULL:

                header_text.setText("下拉刷新!!");
                header_image.setAnimation(rotateAnimation);

                break;
            case REFRES:

                header_text.setText("松开刷新！！");
                header_image.setAnimation(rotateAnimation1);

                break;
            case REFRESING:
                Topadding(view_height);
                header_text.setText("正在刷新!!");
                // header_image.setAnimation(rotateAnimation1);
                header_image.setVisibility(VISIBLE);
                header_image.clearAnimation();
                break;
        }
    }

    public void OnMovew(MotionEvent event) {
        if (!ismark) {
            return;
        }

        int eventY = (int) event.getY();
        int tempY = eventY - startY;
        int finalY = tempY - view_height;

        switch (state) {
            case NONE:
                if (finalY > 0) {
                    state = PULL;
                    OnRefresh_view();
                }
                break;
            case PULL:
                Topadding(finalY);
                if (finalY > view_height + 30 && scrollstate == SCROLL_STATE_TOUCH_SCROLL) {
                    state = REFRES;
                    OnRefresh_view();
                }
                break;

            case REFRES:
                if (finalY < view_height + 30) {
                    state = PULL;
                    OnRefresh_view();
                } else if (finalY <= 0) {
                    state = NONE;
                    OnRefresh_view();
                }
                break;

        }


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollstate = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstvisibleItem = firstVisibleItem;
    }

    public interface MyOnRefreshListener {
        public void OnRrfesh();
    }

    public void setInterface(MyOnRefreshListener myOnRefreshListener) {
        this.myOnRefreshListener = myOnRefreshListener;
    }


    public void OnRefresh_complete() {
        ismark = false;
        state = NONE;
        //Topadding(-view_height);
        OnRefresh_view();
    }

}
