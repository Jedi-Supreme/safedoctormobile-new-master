package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.Adapter.HealthPostsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.HealthPost;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.UI.Activities.PostDetail;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.ClickListener;
import com.safedoctor.safedoctor.Utils.DividerItemDecoration;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.RecyclerTouchListener;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

public class HealthPosts extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListener{
    private List<HealthPost.Content> mHealthPostList;
    private RecyclerView recyclerView;
    public static HealthPostsAdapter mHealthPostsAdapter;
    private View rootView;
    private SafeDoctorService mSafeDoctorService;
    private SwipeRefreshLayout mSwipeRefresh;
    private SearchView searchView;


    public HealthPosts() {
    }

    public static Fragment newInstance() {
        HealthPosts fragment = new HealthPosts();
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSafeDoctorService = ApiUtils.getAPIService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_posts, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.healthTipsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, this));
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                if (!AppConstants.isNetworkAvailable()){
                    AppConstants.displayNoNetworkAlert(getActivity(), getActivity().findViewById(R.id.main_layout));
                    mSwipeRefresh.setRefreshing(false);
                }
                else{
                    GetHealthTips();
                }
            }
        });




        Activity activity=getActivity();
        new Common(context).fragmentInitOnbackpressed(activity);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                if (!AppConstants.isNetworkAvailable()){
                    AppConstants.displayNoNetworkAlert(getActivity(), getActivity().findViewById(R.id.main_layout));
                    mSwipeRefresh.setRefreshing(false);
                }
                else{
                    GetHealthTips();
                }
            }
        });
    }

    private void GetHealthTips() {
        mSwipeRefresh.setRefreshing(true);
        //Make Http Call

        mSafeDoctorService.getHealthLibrary()
                .enqueue(new Callback<HealthPost>() {
                    @Override
                    public void onResponse(@NonNull Call<HealthPost> call, @NonNull Response<HealthPost> response) {
                        mSwipeRefresh.setRefreshing(false);
                        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN){
                            startActivity(new Intent(getActivity(), FormLogin.class));
                            getActivity().finish();
                        }
                        else if (response.isSuccessful()){
                            mHealthPostList = response.body().getData().getContent();
                            AppConstants.CACHE_HEALTH_POST=mHealthPostList;
                            AppConstants.healthlib=mHealthPostList.size();

                            mHealthPostsAdapter = new HealthPostsAdapter(getActivity(), mHealthPostList);
                            recyclerView.setAdapter(mHealthPostsAdapter);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<HealthPost> call, @NonNull Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onRefresh() {
        GetHealthTips();
    }
    @Override
    public void onClick(View view, int position) {
        HealthPost.Content healthPost = mHealthPostList.get(position);
        Intent openPostIntent = new Intent(getActivity(), PostDetail.class);
        openPostIntent.putExtra(AppConstants.POST_TITLE, healthPost.getTitle());
        openPostIntent.putExtra(AppConstants.POST_LINK, healthPost.getLinks());
        openPostIntent.putExtra(AppConstants.POST_CREATED_AT, healthPost.getCreatetime());
        openPostIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(openPostIntent);
        //Toast.makeText(getContext(),healthPost.g,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static void searchitem(String newText){
        if(mHealthPostsAdapter!=null)
            mHealthPostsAdapter.getFilter().filter(newText);
    }
}
