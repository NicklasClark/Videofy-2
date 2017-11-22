package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.BlockUserListAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.blockuser.BlockUsers;
import com.cncoding.teazer.model.profile.blockuser.Followers;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockUserList extends AppCompatActivity {

    Context context;
    List<Followers> list;
    RecyclerView recyclerView;
    BlockUserListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.layout)
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user_list);
        ButterKnife.bind(this);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        getBlockUserList();
    }
    public void getBlockUserList()
    {
        int i=1;
        ApiCallingService.Friends.getBlockedUsers(i,context).enqueue(new Callback<BlockUsers>() {
            @Override
            public void onResponse(Call<BlockUsers> call, Response<BlockUsers> response) {
                if(response.code()==200)
                {
                    try
                    {
                        list= response.body().getFollowers();
                        adapter=new BlockUserListAdapter(context,list);
                        recyclerView.setAdapter(adapter);
                        layout.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
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
            public void onFailure(Call<BlockUsers> call, Throwable t) {

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
