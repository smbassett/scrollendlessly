package info.scottb.scrollendlessly;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * ContentAdapter for the content RecyclerView. Adapts each content item to a row
 * inside the RecyclerView.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    // This ViewHolder class provides a reference to all the views in the RecyclerView layout.
    // This gives us fast access to these views when we want to update them as the user scrolls
    // through the items.
    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mImageTitleDescriptionTV;
        public TextView mTitleTV;
        public TextView mDescriptionTV;
        public ImageView mImage;
        public ImageView mLargeImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageTitleDescriptionTV = (RelativeLayout) itemView.findViewById(R.id.content_image_title_description);
            mTitleTV = (TextView) itemView.findViewById(R.id.content_title);
            mDescriptionTV = (TextView) itemView.findViewById(R.id.content_description);
            mImage = (ImageView) itemView.findViewById(R.id.content_image);
            mLargeImage = (ImageView) itemView.findViewById(R.id.content_large_image);
        }
    }

    private List<Content> mContents; // The list of content items to display
    private Context mContext;

    public ContentAdapter(Context context, List<Content> contents) {
        mContents = contents;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout for the row
        View contentView = inflater.inflate(R.layout.item_content, parent, false);

        // Return a viewholder for the row (provides fast access sub-views for quick updates)
        ViewHolder viewHolder = new ViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder viewHolder, int position) {
        // Get the data for the item that should be shown in this position.
        Content content = mContents.get(position);

        // We are 'recycling' a view that was previously used for content that has scrolled
        // off the screen. We need to update the view to show the new content that should now be
        // visible.

        // Display the new data in the view, using the viewholder for quick sub-view access.
        if (content.isImageOnly()) {
            // This content item is just a large image, so hide the title, description, and
            // small image sub-views.
            viewHolder.mImageTitleDescriptionTV.setVisibility(View.GONE);
            viewHolder.mLargeImage.setVisibility(View.VISIBLE);

            // Clear the small image, as well as the description and title textviews
            viewHolder.mImage.setImageDrawable(null);
            viewHolder.mDescriptionTV.setText("");
            viewHolder.mTitleTV.setText("");

            // Use Picasso to download, cache, and display the image.
            Picasso.with(getContext())
                    .load(content.getImageUrl())
                    .placeholder(R.drawable.image_placeholder_large)
                    .into(viewHolder.mLargeImage);

        } else {
            // This content item has a title, description, and small image.
            // Hide the large image and show the title, description, and small image sub-view.
            viewHolder.mLargeImage.setVisibility(View.GONE);
            viewHolder.mImageTitleDescriptionTV.setVisibility(View.VISIBLE);

            // Clear the large image
            viewHolder.mLargeImage.setImageDrawable(null);

            viewHolder.mTitleTV.setText(content.getTitle());
            viewHolder.mDescriptionTV.setText(content.getDescription());

            Picasso.with(getContext())
                    .load(content.getImageUrl())
                    .into(viewHolder.mImage);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mContents.size();
    }
}
