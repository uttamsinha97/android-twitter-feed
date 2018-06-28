package com.android_twitter_show_timelines_demo.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android_twitter_show_timelines_demo.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

import java.util.Locale;

/**
 * Created by sonu on 17/01/18.
 */

public class SearchTimelineFragment extends Fragment {

    private Context context;
    private RecyclerView searchTimelineRecyclerView;
    private EditText searchQuery;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TweetTimelineRecyclerViewAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public SearchTimelineFragment() {
    }

    public static SearchTimelineFragment newInstance() {

        Bundle args = new Bundle();

        SearchTimelineFragment fragment = new SearchTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_timeline_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        setUpSearchQuery(view);
    }

    /**
     * set up recycler view
     *
     * @param view of the fragment
     */
    private void setUpRecyclerView(View view) {
        searchTimelineRecyclerView = view.findViewById(R.id.search_timeline_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        searchTimelineRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * set up search query
     *
     * @param view
     */
    private void setUpSearchQuery(View view) {
        searchQuery = view.findViewById(R.id.enter_search_query);

        //implement editor action listener to trigger query when user click on search icon from Keyboard
        searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                //check if user clicked on Search icon or not
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    //get the text from edit text
                    String searchQuery = textView.getText().toString().trim();

                    //check if query should not empty
                    if (!TextUtils.isEmpty(searchQuery)) {

                        //hide keyboard
                        hideKeyboard(textView);

                        //search entered query
                        doTwitterSearch(searchQuery);

                    } else {
                        //if query is empty show toast
                        Toast.makeText(context, "Please enter something to search.", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });
    }

    /**
     * method to trigger Tweet search on query base
     *
     * @param query
     */
    private void doTwitterSearch(String query) {

        //NOTE : Filter settings using array list
        //Removes Tweets containing specified keywords in Tweet text. Uses localized case-insensitive matching
       /* final List<String> keywords = Arrays.asList("any_keyword1", "any_keyword1", "any_keyword2", "so_on");

        //Removes Tweets containing specified hashtags. Uses localized case-insensitive matching
        final List<String> hashTags = Arrays.asList("hashTag1", "hashTag2", "hashTag3", "so_on");

        //Removes Tweets from specified user or replies to specified user. Uses case-insensitive matching
        final List<String> handles = Arrays.asList("userName1", "userName2", "userName3", "so_on");

        //Removes Tweet containing URLs from specified domain. Supports internationalized domain names
        final List<String> urls = Arrays.asList("url1", "url2", "url3", "so_on");

        final FilterValues filterValues = new FilterValues(keywords, hashTags, handles, urls);*/

        /*  ================    OR     ================  */

        //NOTE : You can easily load your filter settings using a JSON configuration file and GSON
        /*InputStream inputStream = context.getResources().openRawResource(R.raw.timeline_filter_values);
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        FilterValues filterValues = new Gson().fromJson(reader, FilterValues.class);*/


        //final TimelineFilter timelineFilter = new BasicTimelineFilter(filterValues, Locale.ENGLISH);


        //build the Search TimeLine
        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(query)//the search query for Tweets
                .languageCode(Locale.ENGLISH.getLanguage())//set the language code
                .maxItemsPerRequest(50)//Max number of items to return per request
                .build();

        //create adapter for RecyclerView
        adapter = new TweetTimelineRecyclerViewAdapter.Builder(context)
                .setTimeline(searchTimeline)//set created timeline
                //.setTimelineFilter(timelineFilter) //set timeline filter if any required
                //action callback to listen when user like/unlike the tweet
                .setOnActionCallback(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        //do something on success response
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        //do something on failure response
                    }
                })
                //set tweet view style
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .build();

        //finally set created adapter to recycler view
        searchTimelineRecyclerView.setAdapter(adapter);
    }

    /**
     * set up swipe refresh layout
     *
     * @param view of the fragment
     */
    private void setUpSwipeRefreshLayout(View view) {

        //find the id of swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.search_swipe_refresh_layout);

        //implement refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //return if adapter is null
                if (adapter == null)
                    return;

                //make set refreshing true
                swipeRefreshLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        //on success response make refreshing false
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, "Tweets refreshed.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                        Toast.makeText(context, "Failed to refresh tweets.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * method to hide keyboard manually
     *
     * @param view of the current context
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
