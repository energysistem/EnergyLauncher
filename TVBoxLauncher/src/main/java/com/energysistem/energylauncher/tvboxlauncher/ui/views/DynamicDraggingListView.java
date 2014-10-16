

        package com.energysistem.energylauncher.tvboxlauncher.ui.views;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.animation.ObjectAnimator;
        import android.animation.TypeEvaluator;
        import android.animation.ValueAnimator;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.graphics.drawable.BitmapDrawable;
        import android.inputmethodservice.KeyboardView;
        import android.os.SystemClock;
        import android.util.AttributeSet;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewTreeObserver;
        import android.widget.AbsListView;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.ListView;

        import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
        import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.StableArrayAdapter;

        import java.util.ArrayList;
        import java.util.List;

/**
 * The dynamic listview is an extension of listview that supports cell dragging
 * and swapping.
 *
 * This layout is in charge of positioning the hover cell in the correct location
 * on the screen in response to user touch events. It uses the position of the
 * hover cell to determine when two cells should be swapped. If two cells should
 * be swapped, all the corresponding data set and layout changes are handled here.
 *
 * If no cell is selected, all the touch events are passed down to the listview
 * and behave normally. If one of the items in the listview experiences a
 * long press event, the contents of its current visible state are captured as
 * a bitmap and its visibility is set to INVISIBLE. A hover cell is then created and
 * added to this layout as an overlaying BitmapDrawable above the listview. Once the
 * hover cell is translated some distance to signify an item swap, a data set change
 * accompanied by animation takes place. When the user releases the hover cell,
 * it animates into its corresponding position in the listview.
 *
 * When the hover cell is either above or below the bounds of the listview, this
 * listview also scrolls on its own so as to reveal additional content.
 */



public class DynamicDraggingListView extends ListView {

    public interface OnListChangeListener {
        void onListChanged(View v, boolean cambiado);
    }

    private OnListChangeListener changeListListener;

    public void setOnListChangeListener(OnListChangeListener l) {
        changeListListener = l;
    }


    private final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 15;
    private final int MOVE_DURATION = 170;
    private final int LINE_THICKNESS = 15;


    private enum mDireccionMotionEvent {
        UP,
        DOWN
    }

    private mDireccionMotionEvent mUltimaDireccion;

    public List<DraggableItemApp> mListaApps;

    private int mLastEventY = -1;

    private int mDownY = -1;
    private int mDownX = -1;

    private int mTotalOffset = 0;

    private boolean mListOrderHasChanged = false;
    public boolean getOrdenListaAppsModificada(){return mListOrderHasChanged;}

    private boolean mCellIsMobile = false;
    private boolean mIsMobileScrolling = false;
    private int mSmoothScrollAmountAtEdge = 0;

    private final int INVALID_ID = -1;
    private long mAboveItemId = INVALID_ID;
    private long mMobileItemId = INVALID_ID;
    private long mBelowItemId = INVALID_ID;

    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    private final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    private boolean mIsWaitingForScrollFinish = false;
    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;

    public DynamicDraggingListView(Context context) {
        super(context);
        init(context);
    }

    public DynamicDraggingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DynamicDraggingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        setOnItemLongClickListener(mOnItemLongClickListener);
        setOnItemClickListener(mOnItemClickListener);
        setOnScrollListener(mScrollListener);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mSmoothScrollAmountAtEdge = (int)(SMOOTH_SCROLL_AMOUNT_AT_EDGE / metrics.density);
        setOnKeyListener(mOnKeyListener);
    }

    /**
     * Listen al teclado
     *
     */
    private OnKeyListener mOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i("Tecla pulsada ", v.getId() + " " + event.toString());

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (mCellIsMobile) {
                            //Estamos moviendo la cell
                            mUltimaDireccion = mDireccionMotionEvent.UP;
                            LanzaMotionEvent(mUltimaDireccion, MotionEvent.ACTION_MOVE);
                            return true;
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (mCellIsMobile) {
                            mUltimaDireccion = mDireccionMotionEvent.DOWN;
                            LanzaMotionEvent(mUltimaDireccion,  MotionEvent.ACTION_MOVE);
                            return true;
                        }
                        break;
                }
            }
            return false;
        }
    };


    private void lanzaMotionEventMoveToView(long idView){
        View nextView = getViewForID(idView);

        float x = nextView.getX();
        float y = nextView.getY();

        long downTime = SystemClock.uptimeMillis();
        int metaState = 0;

        long eventTime = downTime + (1000);
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_MOVE,
                x,
                y,
                metaState
        );
        // Dispatch touch event to view
        this.dispatchTouchEvent(motionEvent);
    }

    /**
     * Lanza el motion event para simular un movimiento del cell arriba o abajo     *
     */
    private void LanzaMotionEvent(mDireccionMotionEvent direcc,  int movimiento) {
        int desplazamientoDeMas = 1;
        float y2 = 0.0f;
        float x = 0;
        long downTime;
        long eventTime;
        View nextView = null;

        //Pillamos la view del siguiente/anterior item
        if (movimiento == MotionEvent.ACTION_MOVE) {
            switch (direcc) {
                case UP:
                    if (mAboveItemId != -1) {
                        nextView = getViewForID(mAboveItemId);
                        if (nextView != null) {
                            y2 = nextView.getY() - desplazamientoDeMas;
                        }else {
                            return;
                        }
                    } else {
                        return;
                    }
                    break;
                case DOWN:
                    if (mBelowItemId != -1) {
                        nextView = getViewForID(mBelowItemId);
                        if (nextView != null) {
                            y2 = nextView.getY() - desplazamientoDeMas;
                        }else {
                            return;
                        }
                    } else {
                        return;
                    }
                    break;
            }
            //x = nextView.getX();
            downTime = SystemClock.uptimeMillis();
            int metaState = 0;

            eventTime = downTime + (1000);
            MotionEvent motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_MOVE,
                    x,
                    y2,
                    metaState
            );
            // Dispatch touch event to view
            this.dispatchTouchEvent(motionEvent);
            Log.i("LanzaMotioEvent", "Movido el puntero a : x:" + x + " y:" + y2);


        } else if (movimiento == MotionEvent.ACTION_DOWN) {
            nextView = getViewForID(mMobileItemId);
            y2 = nextView.getY();
            x = nextView.getX();
            downTime = SystemClock.uptimeMillis();
            eventTime = SystemClock.uptimeMillis() + 100;

            Log.i("LanzaMotioEvent", "Movido el puntero a : x:" + x + " y:" + y2);
            int metaState = 0;
            MotionEvent motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_DOWN,
                    x,
                    y2,
                    metaState
            );
            // Dispatch touch event to view
            this.dispatchTouchEvent(motionEvent);
        }
        else if (movimiento == MotionEvent.ACTION_UP){
            downTime = SystemClock.uptimeMillis();
            eventTime = SystemClock.uptimeMillis() + 100;

            Log.i("LanzaMotioEvent", "Levantado el puntero de: x:" + mDownX + " y:" + mDownY);
            int metaState = 0;
            MotionEvent motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_UP,
                    mDownX,
                    mDownY,
                    metaState
            );
            // Dispatch touch event to view
            this.dispatchTouchEvent(motionEvent);
        }


    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            if (mCellIsMobile) {
                mTotalOffset = 0;
                LanzaMotionEvent(mDireccionMotionEvent.DOWN, MotionEvent.ACTION_UP);
            } else {
                int position = pos;
                //int position = pointToPosition(mDownX, mDownY);
                int itemNum = position - getFirstVisiblePosition();

                //position = itemNum = pos;

                View selectedView = getChildAt(itemNum);
                //View selectedView = getSelectedView();
                mMobileItemId = getAdapter().getItemId(position);
                mHoverCell = getAndAddHoverView(selectedView);
                selectedView.setVisibility(INVISIBLE);

                LanzaMotionEvent(mDireccionMotionEvent.DOWN, MotionEvent.ACTION_DOWN);

                mCellIsMobile = true;

                updateNeighborViewsForID(mMobileItemId);
            }
        }
    };


//    *
//     * Listens for long clicks on any items in the listview. When a cell has
//     * been selected, the hover cell is created and set up.

    private OnItemLongClickListener mOnItemLongClickListener =
            new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
//                    mTotalOffset = 0;
//
//                    int position = pointToPosition(mDownX, mDownY);
//                    int itemNum = position - getFirstVisiblePosition();
//
////                    if (position == -1){
//                    position = itemNum = pos;
////                    }
//
//                    View selectedView = getChildAt(itemNum);
//                    mMobileItemId = getAdapter().getItemId(position);
//                    mHoverCell = getAndAddHoverView(selectedView);
//                    selectedView.setVisibility(INVISIBLE);
//
//                    LanzaMotionEvent(mDireccionMotionEvent.DOWN, MotionEvent.ACTION_DOWN );
//
//                    mCellIsMobile = true;
//
//                    updateNeighborViewsForID(mMobileItemId);

                    return true;
                }
            };


    @Override
    public void setOnScrollListener(OnScrollListener l) {

        Log.d("********************tralari***************", "ddfdfdsff");
        super.setOnScrollListener(l);
    }

    /**
     * Creates the hover cell with the appropriate bitmap and of appropriate
     * size. The hover cell's BitmapDrawable is drawn on top of the bitmap every
     * single time an invalidate call is made.
     */
    private BitmapDrawable getAndAddHoverView(View v) {

        int w = v.getWidth();
        int h = v.getHeight();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapWithBorder(v);

        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);

        mHoverCellOriginalBounds = new Rect(left, top, left + w, top + h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);

        drawable.setBounds(mHoverCellCurrentBounds);

        return drawable;
    }

    /** Draws a black border over the screenshot of the view passed in. */
    private Bitmap getBitmapWithBorder(View v) {
        Bitmap bitmap = getBitmapFromView(v);
        Canvas can = new Canvas(bitmap);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(LINE_THICKNESS);
        paint.setColor(Color.BLACK);

        can.drawBitmap(bitmap, 0, 0, null);
        can.drawRect(rect, paint);

        return bitmap;
    }

    /** Returns a bitmap showing a screenshot of the view passed in. */
    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * Stores a reference to the views above and below the item currently
     * corresponding to the hover cell. It is important to note that if this
     * item is either at the top or bottom of the list, mAboveItemId or mBelowItemId
     * may be invalid.
     */
    private void updateNeighborViewsForID(long itemID) {
        int position = getPositionForID(itemID);
        StableArrayAdapter adapter = ((StableArrayAdapter)getAdapter());
        mAboveItemId = adapter.getItemId(position - 1);
        mBelowItemId = adapter.getItemId(position + 1);
    }

    /** Retrieves the view in the list corresponding to itemID */
    public View getViewForID (long itemID) {
        int firstVisiblePosition = getFirstVisiblePosition();
        StableArrayAdapter adapter = ((StableArrayAdapter)getAdapter());
        for(int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int position = firstVisiblePosition + i;
            long id = adapter.getItemId(position);
            if (id == itemID) {
                return v;
            }
        }
        return null;
    }

    /** Retrieves the position in the list corresponding to itemID */
    public int getPositionForID (long itemID) {
        View v = getViewForID(itemID);
        if (v == null) {
            return -1;
        } else {
            return getPositionForView(v);
        }
    }

    /**
     *  dispatchDraw gets invoked when all the child views are about to be drawn.
     *  By overriding this method, the hover cell (BitmapDrawable) can be drawn
     *  over the listview's items whenever the listview is redrawn.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHoverCell != null) {
            mHoverCell.draw(canvas);
        }
    }

    private void hideoverView(){

        final View mobileView = getViewForID(mMobileItemId);

        mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, mobileView.getTop());

        ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(mHoverCell, "bounds",
                sBoundEvaluator, mHoverCellCurrentBounds);
        hoverViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        hoverViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAboveItemId = INVALID_ID;
                mMobileItemId = INVALID_ID;
                mBelowItemId = INVALID_ID;
                mobileView.setVisibility(VISIBLE);
                mHoverCell = null;
                setEnabled(true);
                invalidate();
            }
        });
        hoverViewAnimator.start();
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int)event.getX();
                mDownY = (int)event.getY();
                mActivePointerId = event.getPointerId(0);

                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER_ID) {
                    break;
                }

                int pointerIndex = event.findPointerIndex(mActivePointerId);

                mLastEventY = (int) event.getY(pointerIndex);
                int deltaY = mLastEventY - mDownY;

                if (mCellIsMobile) {

                    //hideoverView();
                    int newtop = mHoverCellOriginalBounds.top + deltaY + mTotalOffset;
                    //int newtop = mHoverCellOriginalBounds.top;


//                   newtop = getSelectedView().getTop();
//
//                   mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, newtop);
//                   mHoverCell.setBounds(mHoverCellCurrentBounds);
//                   invalidate();

                    handleCellSwitch();

                    mIsMobileScrolling = false;
                    handleMobileCellScroll();

                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchEventsEnded();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                /* If a multitouch event took place and the original touch dictating
                 * the movement of the hover cell has ended, then the dragging event
                 * ends and the hover cell is animated to its corresponding position
                 * in the listview. */
                pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                        MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    touchEventsEnded();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * This method determines whether the hover cell has been shifted far enough
     * to invoke a cell swap. If so, then the respective cell swap candidate is
     * determined and the data set is changed. Upon posting a notification of the
     * data set change, a layout is invoked to place the cells in the right place.
     * Using a ViewTreeObserver and a corresponding OnPreDrawListener, we can
     * offset the cell being swapped to where it previously was and then animate it to
     * its new position.
     */
    private void handleCellSwitch() {
        final int deltaY = mLastEventY - mDownY;
        int deltaYTotal = mHoverCellOriginalBounds.top + mTotalOffset + deltaY;


        View belowView = getViewForID(mBelowItemId);
        View mobileView = getViewForID(mMobileItemId);
        View aboveView = getViewForID(mAboveItemId);

        if (mUltimaDireccion == mDireccionMotionEvent.UP){
            deltaYTotal = aboveView.getTop() -1;
        }
        else
        {
            deltaYTotal =  belowView.getTop() +1;
        }

        boolean isBelow = (belowView != null) && (deltaYTotal > belowView.getTop());
        boolean isAbove = (aboveView != null) && (deltaYTotal < aboveView.getTop());

        if (isBelow || isAbove) {

            final long switchItemID = isBelow ? mBelowItemId : mAboveItemId;
            View switchView = isBelow ? belowView : aboveView;
            final int originalItem = getPositionForView(mobileView);

            if (switchView == null) {
                updateNeighborViewsForID(mMobileItemId);
                return;
            }

            //MEtido probar
            int newtop = switchView.getTop();

            mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, newtop);
            mHoverCell.setBounds(mHoverCellCurrentBounds);
            invalidate();
            //sdasdsadsadsd

            swapElements(mListaApps, originalItem, getPositionForView(switchView));

            ((BaseAdapter) getAdapter()).notifyDataSetChanged();

            mDownY = mLastEventY;

            final int switchViewStartTop = switchView.getTop();

            mobileView.setVisibility(View.VISIBLE);
            switchView.setVisibility(View.INVISIBLE);

            updateNeighborViewsForID(mMobileItemId);

            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);

                    View switchView = getViewForID(switchItemID);

                    if (switchView != null) {
                        mTotalOffset += deltaY;

                        int switchViewNewTop = switchView.getTop();
                        int delta = switchViewStartTop - switchViewNewTop;

                        switchView.setTranslationY(delta);

                        ObjectAnimator animator = ObjectAnimator.ofFloat(switchView,
                                View.TRANSLATION_Y, 0);
                        animator.setDuration(MOVE_DURATION);
                        animator.start();
                    }
                    return true;
                }
            });
        }
    }

    private void swapElements(List arrayList, int indexOne, int indexTwo) {
        Object temp = arrayList.get(indexOne);
        arrayList.set(indexOne, arrayList.get(indexTwo));
        arrayList.set(indexTwo, temp);

        if (!mListOrderHasChanged){
            mListOrderHasChanged = true;
            if (changeListListener != null)
                changeListListener.onListChanged(this, mListOrderHasChanged);
        }
    }


    /**
     * Resets all the appropriate fields to a default state while also animating
     * the hover cell back to its correct location.
     */
    private void touchEventsEnded () {
        final View mobileView = getViewForID(mMobileItemId);
        if (mCellIsMobile|| mIsWaitingForScrollFinish) {
            mCellIsMobile = false;
            mIsWaitingForScrollFinish = false;
            mIsMobileScrolling = false;
            mActivePointerId = INVALID_POINTER_ID;

            // If the autoscroller has not completed scrolling, we need to wait for it to
            // finish in order to determine the final location of where the hover cell
            // should be animated to.
            if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
                mIsWaitingForScrollFinish = true;
                return;
            }



            mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, mobileView.getTop());

            ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(mHoverCell, "bounds",
                    sBoundEvaluator, mHoverCellCurrentBounds);
            hoverViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    invalidate();
                }
            });
            hoverViewAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAboveItemId = INVALID_ID;
                    mMobileItemId = INVALID_ID;
                    mBelowItemId = INVALID_ID;
                    mobileView.setVisibility(VISIBLE);
                    mHoverCell = null;
                    setEnabled(true);
                    invalidate();
                }
            });
            hoverViewAnimator.start();
        } else {
            touchEventsCancelled();
        }
    }

    /**
     * Resets all the appropriate fields to a default state.
     */
    private void touchEventsCancelled () {
        View mobileView = getViewForID(mMobileItemId);
        if (mCellIsMobile) {
            mAboveItemId = INVALID_ID;
            mMobileItemId = INVALID_ID;
            mBelowItemId = INVALID_ID;
            mobileView.setVisibility(VISIBLE);
            mHoverCell = null;
            invalidate();
        }
        mCellIsMobile = false;
        mIsMobileScrolling = false;
        mActivePointerId = INVALID_POINTER_ID;
    }

    /**
     * This TypeEvaluator is used to animate the BitmapDrawable back to its
     * final location when the user lifts his finger by modifying the
     * BitmapDrawable's bounds.
     */
    private final static TypeEvaluator<Rect> sBoundEvaluator = new TypeEvaluator<Rect>() {
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left, endValue.left, fraction),
                    interpolate(startValue.top, endValue.top, fraction),
                    interpolate(startValue.right, endValue.right, fraction),
                    interpolate(startValue.bottom, endValue.bottom, fraction));
        }

        public int interpolate(int start, int end, float fraction) {
            return (int)(start + fraction * (end - start));
        }
    };

    /**
     *  Determines whether this listview is in a scrolling state invoked
     *  by the fact that the hover cell is out of the bounds of the listview;
     */
    private void handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds);
    }

    /**
     * This method is in charge of determining if the hover cell is above
     * or below the bounds of the listview. If so, the listview does an appropriate
     * upward or downward smooth scroll so as to reveal new items.
     */
    private boolean handleMobileCellScroll(Rect r) {
        int offset = computeVerticalScrollOffset();
        int height = getHeight();
        int extent = computeVerticalScrollExtent();
        int range = computeVerticalScrollRange();
        int hoverViewTop = r.top;
        int hoverHeight = r.height();

        if (hoverViewTop <= 0 && offset > 0) {
            smoothScrollBy(-mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        if (hoverViewTop + hoverHeight >= height && (offset + extent) < range) {
            smoothScrollBy(mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        return false;
    }


    public void setListaReseteada(){
        mListOrderHasChanged = false;
    }

    public void setAppsList(List<DraggableItemApp> listaApps) {
        mListaApps = listaApps;
        Log.d("----setAppsList(Dynamic Dragging), tamaño nueva Lista Draggable: ",Integer.toString(listaApps.size()));
    }

    private int mPreviousFirstVisibleItem = -1;
    private boolean mIsScrollingUp = false;
    /**
     * This scroll listener is added to the listview in order to handle cell swapping
     * when the cell is either at the top or bottom edge of the listview. If the hover
     * cell is at either edge of the listview, the listview will begin scrolling. As
     * scrolling takes place, the listview continuously checks if new cells became visible
     * and determines whether they are potential candidates for a cell swap.
     */
    private OnScrollListener mScrollListener = new OnScrollListener () {

        //private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;
        private int mCurrentFirstVisibleItem;
        private int mCurrentVisibleItemCount;
        private int mCurrentScrollState;


        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            Log.i("onScroll", "scrolling .................");

            //MEtido probar
            if (mCellIsMobile) {
                View v = getViewForID(mMobileItemId);
                int newtop = v.getTop();
                mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, newtop);
                mHoverCell.setBounds(mHoverCellCurrentBounds);
                invalidate();
            }
            //sdasdsadsadsd

            final int currentFirstVisibleItem = view.getFirstVisiblePosition();
            if (currentFirstVisibleItem > mPreviousFirstVisibleItem) {
                mIsScrollingUp = false;
                checkAndHandleTouchPosition();
            } else if (currentFirstVisibleItem < mPreviousFirstVisibleItem) {
                mIsScrollingUp = true;
                checkAndHandleTouchPosition();
            }
            mPreviousFirstVisibleItem = currentFirstVisibleItem;




            //lw = view;
//            mCurrentFirstVisibleItem = firstVisibleItem;
//            mCurrentVisibleItemCount = visibleItemCount;
//
//            mPreviousFirstVisibleItem = (mPreviousFirstVisibleItem == -1) ? mCurrentFirstVisibleItem
//                    : mPreviousFirstVisibleItem;
//            mPreviousVisibleItemCount = (mPreviousVisibleItemCount == -1) ? mCurrentVisibleItemCount
//                    : mPreviousVisibleItemCount;
//
//            checkAndHandleFirstVisibleCellChange();
//            checkAndHandleLastVisibleCellChange();
//

//
//            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem;
//            mPreviousVisibleItemCount = mCurrentVisibleItemCount;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            mCurrentScrollState = scrollState;
//            mScrollState = scrollState;
//            isScrollCompleted();
        }



        /**
         * This method is in charge of invoking 1 of 2 actions. Firstly, if the listview
         * is in a state of scrolling invoked by the hover cell being outside the bounds
         * of the listview, then this scrolling event is continued. Secondly, if the hover
         * cell has already been released, this invokes the animation for the hover cell
         * to return to its correct position after the listview has entered an idle scroll
         * state.
         */
        private void isScrollCompleted() {
            if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE) {
                if (mCellIsMobile && mIsMobileScrolling) {
                    handleMobileCellScroll();
                } else if (mIsWaitingForScrollFinish) {
                    touchEventsEnded();
                }
            }
        }

        /**
         * Determines if the listview scrolled up enough to reveal a new cell at the
         * top of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID) {

                    Log.e("checkAndHandleFirstVisibleCellChange", "Cambia el primer visible de arriba");

                    updateNeighborViewsForID(mMobileItemId);
                    handleCellSwitch();
                }
            }
        }

        /**
         * Cambia la posicion del puntero cuando hay un scroll
         */
        private void checkAndHandleTouchPosition() {

            if (mCellIsMobile) {
                if (mIsScrollingUp) {

                    //MEtido probar
                    View v = getViewForID(mMobileItemId);
                    int newtop = v.getTop();
                    mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, newtop);
                    mHoverCell.setBounds(mHoverCellCurrentBounds);
                    invalidate();
                    //sdasdsadsadsd

                    Log.i("a", "scrolling up...");
                } else {

                    //MEtido probar
                    View v = getViewForID(mMobileItemId);
                    int newtop = v.getTop();
                    mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left, newtop);
                    mHoverCell.setBounds(mHoverCellCurrentBounds);
                    invalidate();
                    //sdasdsadsadsd

                    Log.i("a", "scrolling down...");
                }
            }
        }

        /**
         * Determines if the listview scrolled down enough to reveal a new cell at the
         * bottom of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleLastVisibleCellChange() {
            int currentLastVisibleItem = mCurrentFirstVisibleItem + mCurrentVisibleItemCount;
            int previousLastVisibleItem = mPreviousFirstVisibleItem + mPreviousVisibleItemCount;
            if (currentLastVisibleItem != previousLastVisibleItem) {
                if (mCellIsMobile && mMobileItemId != INVALID_ID) {

                    Log.e("checkAndHandleFirstVisibleCellChange", "Cambia el primer visible de abajo");

                    updateNeighborViewsForID(mMobileItemId);
                    handleCellSwitch();
                }
            }
        }
    };




}