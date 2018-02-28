package com.cncoding.teazer.ui.home.profile.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.user.BlockedUser;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.ui.customviews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.profile.adapter.BlockUserListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockUserList extends AppCompatActivity {

    Context context;
    List<BlockedUser> list;
    RecyclerView recyclerView;
    BlockUserListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.blockusertex)
    TextView blockedUserText;
    @BindView(R.id.blockedListEmptyView)
    TextView blockedUserEmptyView;
    boolean nextPage;

    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user_list);
        ButterKnife.bind(this);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#0000000'>Block Users</font>"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layout.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        list=new ArrayList<>();

        endlessRecyclerViewScrollListener= new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(nextPage) {
                    getBlockUserList(page);
                    Toast.makeText(context,"onLoad",Toast.LENGTH_LONG).show();
                }

            }
        };

        getBlockUserList(1);
    }




    public void getBlockUserList(final int pageId)
    {

        ApiCallingService.Friends.getBlockedUsers(pageId,context).enqueue(new Callback<BlockedUsersList>() {
            @Override
            public void onResponse(Call<BlockedUsersList> call, Response<BlockedUsersList> response) {
                if(response.code()==200) {
                    try {
                        list.addAll(response.body().getBlockedUsers());

                        if((list==null||list.size()==0) && pageId == 1) {
                            layout.setVisibility(View.VISIBLE);
                            blockedUserEmptyView.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                        }
                        else {
                            nextPage=response.body().getNextPage();
                            layout.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                          //  list.addAll(response.body().getBlockedUsers());

                            adapter = new BlockUserListAdapter(context, list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                        layout.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(context, "Check your information", Toast.LENGTH_LONG).show();
                    layout.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BlockedUsersList> call, Throwable t) {

                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                layout.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            }
        });
    }
    public void Visibility()
    {
        layout.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
    }
    public void InVisibleVisibile()
    {
        layout.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }
}
