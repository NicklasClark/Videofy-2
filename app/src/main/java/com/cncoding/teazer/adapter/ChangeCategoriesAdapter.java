package com.cncoding.teazer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.friends.PublicProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Created by farazhabib on 03/12/17.
 */

public class ChangeCategoriesAdapter extends RecyclerView.Adapter<ChangeCategoriesAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Category> categories;
    private Fragment fragment;
    private PublicProfile userProfile;
    ArrayList<Category> usercategoryList;
    boolean flag=false;
    boolean[] selectedPositions;
    List<Integer> selectedcategories;
    List<String> selectedCategoriesName;

    public ChangeCategoriesAdapter(ArrayList<Category> categories, Context context,ArrayList<Category> usercategoryList) {
        this.categories = categories;

        selectedcategories=new ArrayList<>();
        selectedCategoriesName=new ArrayList<>();

        for (int i=0;i<usercategoryList.size();i++)
        {
            selectedcategories.add(usercategoryList.get(i).getCategoryId());
            selectedCategoriesName.add(usercategoryList.get(i).getCategoryName());
        }

        this.context = context;
        this.usercategoryList=usercategoryList;
        selectedPositions = new boolean[categories.size()];
        for(int i = 0;i<categories.size();i++)
        {
            selectedPositions[i] = false;
        }

    }

    @Override
    public ChangeCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);
        return new ChangeCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChangeCategoriesAdapter.ViewHolder holder, final int position) {
        try {

            final Category category = this.categories.get(position);
           /// Log.d("Categories", String.valueOf(usercategoryList.size()));

            for (int i = 0; i < selectedCategoriesName.size(); i++) {
                if (selectedCategoriesName.get(i).equals(category.getCategoryName())) {
                    setCheck(holder.nameView, true);
                    selectedPositions[position] = true;
                }
            }
            holder.nameView.setText(category.getCategoryName());

            if (selectedPositions[position]) {
                setCheck(holder.nameView, true);

            } else {
                setCheck(holder.nameView, false);

            }
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    boolean ischecked = holder.nameView.isChecked();
                    if (ischecked) {
                        if(selectedCategoriesName.size()>5) {
                            selectedcategories.remove((Integer) category.getCategoryId());
                            selectedCategoriesName.remove((category.getCategoryName()));
                            setCheck(holder.nameView, false);
                            selectedPositions[position] = false;
                        }
                        else
                        {
                            Toast.makeText(context,"Select atleast minimum 5 categories",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        selectedCategoriesName.add((category.getCategoryName()));
                        selectedcategories.add(category.getCategoryId());
                            setCheck(holder.nameView, true);
                            selectedPositions[position] = true;

                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context,"Something went wrong please try again",Toast.LENGTH_LONG).show();

        }
    }
    private void setCheck(AppCompatCheckedTextView textView, boolean checked) {
        textView.setChecked(checked);
        if (textView.isChecked()) {
            textView.setTextColor(Color.parseColor("#26C6DA"));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_accent, 0);
        }
        else {
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootLayout;
        private ProximaNovaRegularCheckedTextView nameView;

        ViewHolder(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.categories_item_layout);
            nameView = view.findViewById(R.id.chip);
        }
    }
    public List<Integer> getSelectedCategories() {
        return selectedcategories;
   }
}
