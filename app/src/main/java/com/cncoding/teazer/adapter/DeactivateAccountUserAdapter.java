package com.cncoding.teazer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.ui.fragment.fragment.FragmentDeactivateAccount;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.logout;

/**
 *
 * Created by farazhabib on 06/12/17.
 */

public class DeactivateAccountUserAdapter extends RecyclerView.Adapter<DeactivateAccountUserAdapter.ViewHolder> {

    private List<DeactivateTypes> list;
     Context context;
    public static final int UNBLOCK_STATUS=2;
    FragmentDeactivateAccount fragment;
    boolean flag=true;


    public DeactivateAccountUserAdapter(Context context, List<DeactivateTypes> list, FragmentDeactivateAccount fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }
    @Override
    public DeactivateAccountUserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_deactivate_account, viewGroup, false);
        return new DeactivateAccountUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeactivateAccountUserAdapter.ViewHolder viewHolder, final int i) {

        final DeactivateTypes cont = list.get(i);
        try {

            final boolean ownReason= cont.getOwnReason();

            viewHolder.deactivateReason.setText(cont.getTitle());

            viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ownReason) {
                       if(flag) {
                           fragment.visibleCommentLayout(flag, cont.getDeactivateId());
                           flag = false;
                       } else{
                           fragment.visibleCommentLayout(flag, cont.getDeactivateId());
                           flag = true;

                       }
                    }
                    else openAlert(viewHolder,cont.getDeactivateId(),"");

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

    public void openAlert(final DeactivateAccountUserAdapter.ViewHolder viewHolder,final int id, final String text)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Confirm Deactivation...");
        alertDialog.setMessage("Are you sure you want deactivate your account");
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which) {

                DeactivateAccountRequest deactivateAccountRequest=new DeactivateAccountRequest(id,text);
                deactivateID(deactivateAccountRequest);



            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {



            }
        });
        alertDialog.show();
    }


    public void deactivateID(DeactivateAccountRequest deactivateAccountRequest)
    {
        ApiCallingService.User.deactivateAccount(context,deactivateAccountRequest).enqueue(new Callback<ResultObject>() {

            @Override
            public void onResponse(Call<ResultObject>call, Response<ResultObject> response) {

                if (response.code()==200) {
                    try {
                      if(response.body().getStatus())
                      {
                          logout(context, (Activity) context);
                        //  Toast.makeText(context,"Your account has been deactivate",Toast.LENGTH_SHORT).show();

                      }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProximaNovaRegularTextView deactivateReason;
        CardView cardview;
        View line;
        Button unblock;
        public ViewHolder(View view) {
            super(view);
            deactivateReason = view.findViewById(R.id.deactivateReason);
            cardview = view.findViewById(R.id.cardview);

        }
    }


}
