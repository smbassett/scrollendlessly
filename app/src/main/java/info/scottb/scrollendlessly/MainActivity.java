package info.scottb.scrollendlessly;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * A fragment containing a RecyclerView, which is what we use to display infinitely scrollable
     * images and text to the user.
     */
    public static class ContentFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ContentAdapter adapter;
        List<Content> contents;

        public ContentFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ContentFragment newInstance(int sectionNumber) {
            ContentFragment fragment = new ContentFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Look up the RecyclerView in the fragment layout
            RecyclerView rvContents = (RecyclerView) rootView.findViewById(R.id.rvContent);

            // Create an adapter for our content, initially passing it an empty list.
            // We'll fetch our initial data in a moment and update the list.
            contents = new ArrayList<>();
            adapter = new ContentAdapter(getContext(), contents);
            rvContents.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rvContents.setLayoutManager(linearLayoutManager);

            // Create a scroll listener for the RecyclerView. We'll use the scroll listener
            // to detect when the user is close to the bottom of the list, and when we therefore
            // need to fetch more data from the server.
            EndlessRecyclerViewScrollListener scrollListener =
                    new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                            // This is fired when the user approaches the end of the list
                            loadNextDataFromApi(page);
                        }
                    };

            rvContents.addOnScrollListener(scrollListener);

            // Request the first set of data to add to the adapter and display.
            // Data will be fetched and added to the RecyclerView asynchronously.
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            int offset = 0;
            requestData(sectionNumber, offset);

            return rootView;
        }

        // Load additional data from the server. This is called when the user has almost scrolled
        // to the bottom of the list.
        public void loadNextDataFromApi(int offset) {
            Toast.makeText(getContext(), "Fetching additional data", Toast.LENGTH_SHORT).show();
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            requestData(sectionNumber, offset);
        }

        /**
         * Gets additional data from the server, and updates the content list and the adapter
         * when the data is received.
         *
         * @param sectionNumber the currently displayed tab
         * @param offset        the number of pages of data that have already been retrieved
         */
        private void requestData(int sectionNumber, int offset) {
            String url = ContentUrl.getUrl(sectionNumber, offset);
            StringRequest jsonStringRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // Parse the response in an AsyncTask to avoid doing work on the UI thread
                            // as the user scrolls
                            new HandleResponseTask().execute(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // The requested data was not found -- in this simple implementation,
                            // this means that no more data is available to fetch.
                            Toast.makeText(getContext(), "No more data to fetch", Toast.LENGTH_SHORT).show();
                        }
                    });

            VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(jsonStringRequest);
        }

        private class HandleResponseTask extends AsyncTask<String, Integer, Integer> {
            protected Integer doInBackground(String... response) {
                // Convert the JSON array we received from the server into a list of Content objects.
                List<Content> newContent = Content.fromJson(response[0]);

                // Insert the new content objects into the existing contents list
                contents.addAll(newContent);
                return newContent.size();
            }

            @Override
            protected void onPostExecute(Integer newContentItemCount) {
                // notify the adapter that the data set has changed.
                int newContentStart = contents.size() - newContentItemCount;
                adapter.notifyItemRangeInserted(newContentStart, newContentItemCount);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.city_life);
                case 1:
                    return getString(R.string.fashion);
                case 2:
                    return getString(R.string.food);
            }
            return null;
        }
    }
}
