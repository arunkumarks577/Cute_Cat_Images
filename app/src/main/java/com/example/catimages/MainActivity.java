package com.example.catimages;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    CatViewAdapter adapter;
    List<Cats> cats;
    public static CatDBHelper catDBHelper;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.fab3)
    FloatingActionButton fab3;
    @InjectView(R.id.fab2)
    FloatingActionButton fab2;
    @InjectView(R.id.fab1)
    FloatingActionButton fab1;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;
    @InjectView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @InjectView(R.id.loading)
    TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Thread.UncaughtExceptionHandler defaultEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                String msg = e.getMessage();
                int mj = 0;
            }
        });
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        catDBHelper = new CatDBHelper(MainActivity.this);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        final Call<List<Cats>> call = jsonPlaceHolderApi.getCats(10);
        RetrofitInitialCall(call);


        /*if(savedInstanceState != null){
            // scroll to existing position which exist before rotation.

            recyclerView.scrollToPosition(savedInstanceState.getInt("position"));
        }*/

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading.setVisibility(View.VISIBLE);
                loading.setText("Loading....");
                recyclerView.setVisibility(View.INVISIBLE);
                if (adapter != null) {
                    adapter.clear();
                }
                RetrofitInitialCall(call.clone());
            }
        });
    }

    public void LoadAdapter() {
        List<LocalCats> localCats = catDBHelper.getAllCatImages();
        if (adapter == null && localCats.size()>0) {
            adapter = new CatViewAdapter(MainActivity.this, localCats);
            ShowInGridView();
            recyclerView.setAdapter(adapter);
            stopswiperefresh(true);
        } else if(adapter != null && localCats.size()>0){
            adapter.cats = localCats;
            adapter.notifyDataSetChanged();
            stopswiperefresh(true);
        }else
        {
            loading.setVisibility(View.VISIBLE);
            loading.setText("No Internet Connectivity/Internal Data, Please turn on mobile data and refresh again");
            stopswiperefresh(false);
        }
    }

    public void stopswiperefresh(boolean value) {
        if (swiperefresh.isRefreshing()) {
            swiperefresh.setRefreshing(false);
        }
        if(value) {
            loading.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void LoadAdapter(List<Cats> catsList, final Call call) {
        if (adapter == null) {
            ShowInGridView();
        }
            adapter = new CatViewAdapter(MainActivity.this, catsList);
            adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position) {
                    RetrofitCall(call);
                }
            });
            recyclerView.setAdapter(adapter);
        stopswiperefresh(true);
    }

    public void RetrofitInitialCall(Call call) {
        call.enqueue(new Callback<List<Cats>>() {
            @Override
            public void onResponse(Call<List<Cats>> call, Response<List<Cats>> response) {
                if (response.isSuccessful()) {
                    List<Cats> newcats = response.body();
                    if (cats != null)
                        cats.addAll(newcats);
                    else
                        cats = newcats;
                    LoadAdapter(cats, call);
                } else {
                    LoadAdapter();
                }
            }

            @Override
            public void onFailure(Call<List<Cats>> call, Throwable t) {
                LoadAdapter();
            }
        });
    }

    public void RetrofitCall(Call call) {
        Call<List<Cats>> newcall = call.clone();
        newcall.enqueue(new Callback<List<Cats>>() {
            @Override
            public void onResponse(Call<List<Cats>> call, Response<List<Cats>> response) {
                if (response.isSuccessful()) {
                    List<Cats> newcats = response.body();
                    int position = cats.size() - 1;
                    if (cats != null && cats.size() != 0)
                        cats.addAll(position, newcats);
                    else
                        cats = newcats;
                    if (adapter == null)
                        adapter = new CatViewAdapter(MainActivity.this, cats);
                    if (position >= 0)
                        adapter.notifyItemRangeChanged(position, newcats.size());
                    stopswiperefresh(true);
                }
            }

            @Override
            public void onFailure(Call<List<Cats>> call, Throwable t) {
                stopswiperefresh(false);
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("position", recyclerView.getChildLayoutPosition(recyclerView.getFocusedChild())); // get current recycle view position here.
        //your other code
        super.onSaveInstanceState(savedInstanceState);
    }


    @OnClick({R.id.fab3, R.id.fab2, R.id.fab1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab3:
                ShowInGridView();
                break;
            case R.id.fab2:
                ShowInListView();
                break;
            case R.id.fab1:
                ShowInViewPager();
                break;
        }
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        if (isOpen) {
            fab3.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.startAnimation(fab_close);
            fab.startAnimation(fab_anticlock);
            fab3.setClickable(false);
            fab2.setClickable(false);
            fab1.setClickable(false);
            isOpen = false;
        } else {
            fab3.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.startAnimation(fab_open);
            fab.startAnimation(fab_clock);
            fab3.setClickable(true);
            fab2.setClickable(true);
            fab1.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            ShowInGridView();
        }
    }

    private void ShowInGridView() {
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 100);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, noOfColumns));
    }

    private void ShowInListView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void ShowInViewPager() {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(recyclerView);
    }
}