package info.scottb.scrollendlessly;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Endless RecyclerView Scroll Listener
 *
 * Adapted from:
 * https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_ITEMS_THRESHOLD = 10;
    private int mCurrentPage = 0;
    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = true;
    private int mStartingPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

        // If current total item count is somehow less than what it was previously, assume we are
        // in a bad state and reset.
        if (totalItemCount < mPreviousTotalItemCount) {
            mCurrentPage = mStartingPageIndex;
            mPreviousTotalItemCount = totalItemCount;
            // If no items are visible, we are still loading data
            if (totalItemCount == 0) {
                mLoading = true;
            }
        }

        // If the total number of items is greater than it was previously, we have loaded new data.
        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        // If we are approaching the end of the list, request the next page of data
        if (!mLoading && (lastVisibleItemPosition + VISIBLE_ITEMS_THRESHOLD) > totalItemCount) {
            mCurrentPage++;
            onLoadMore(mCurrentPage, totalItemCount, view);
            mLoading = true;
        }
    }

    // Called when more items should be loaded
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}