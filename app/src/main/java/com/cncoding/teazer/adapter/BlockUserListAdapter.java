package com.cncoding.teazer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.model.user.BlockedUser;
import com.cncoding.teazer.ui.fragment.activity.BlockUserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by farazhabib on 15/11/17.
 */

public class BlockUserListAdapter extends RecyclerView.Adapter<BlockUserListAdapter.ViewHolder> {

    private List<BlockedUser> list;
    private Context context;
    public static final int UNBLOCK_STATUS=2;


    public BlockUserListAdapter(Context context, List<BlockedUser> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public BlockUserListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_block_users, viewGroup, false);
        return new BlockUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlockUserListAdapter.ViewHolder viewHolder, int i) {

        BlockedUser cont = list.get(i);
        try {
            final String followername = cont.getUserName();
            final int blockuserId = cont.getUserId();
            viewHolder.blockusers.setText(followername);
            viewHolder.unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAlert(blockuserId, viewHolder);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void openAlert(final int blockuserId, final BlockUserListAdapter.ViewHolder viewHolder)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Confirm Unblock...");
        alertDialog.setMessage("Are you sure you want unblock this user");
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which) {
                ((BlockUserList)context).Visibility();

                unBlock(blockuserId,UNBLOCK_STATUS,viewHolder);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void unBlock(int userId, int status, final BlockUserListAdapter.ViewHolder viewHolder) {
        ApiCallingService.Friends.blockUnblockUser(userId, status, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                    try {
                        boolean b = response.body().getStatus();
                        if (b == true)
                        {
                            Toast.makeText(context, "You have Unblocked this user", Toast.LENGTH_LONG).show();
                            viewHolder.cardview.setVisibility(View.GONE);
                            ((BlockUserList)context).InVisibleVisibile();
                        }
                        else
                        {
                            Toast.makeText(context, "You have not Unblocked this user", Toast.LENGTH_LONG).show();
                            ((BlockUserList)context).InVisibleVisibile();
                        }
                    }
                    catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                        ((BlockUserList)context).InVisibleVisibile();
                    }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                ((BlockUserList)context).InVisibleVisibile();
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView blockusers;
        CardView cardview;
        View line;
        Button unblock;
        public ViewHolder(View view) {
            super(view);
            blockusers = view.findViewById(R.id.user_name);
            unblock = view.findViewById(R.id.unblock_button);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
