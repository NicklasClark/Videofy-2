package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.model.profile.followerprofile.Category;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farazhabib on 03/12/17.
 */

public class ChangeCategoriesAdapter extends RecyclerView.Adapter<ChangeCategoriesAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Pojos.Category> categories;
    private Fragment fragment;
//    private SparseBooleanArray selectedCategoriesArray;
//    private SparseArray<Pojos.Category> selectedCategories;
    private PublicProfile userProfile;
    List<Category> usercategoryList;
    boolean flag=false;
    boolean[] selectedPositions;
    List<Integer> selectedcategories;

    public ChangeCategoriesAdapter(ArrayList<Pojos.Category> categories, Context context,List<Category> usercategoryList) {
        this.categories = categories;
        this.userProfile = userProfile;
//        selectedCategoriesArray = new SparseBooleanArray();
//        selectedCategories = new SparseArray<>();
        selectedcategories=new ArrayList<>();
        for (int i=0;i<usercategoryList.size();i++)
        {
            selectedcategories.add(usercategoryList.get(i).getCategoryId());
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
    public void onBindViewHolder(final ChangeCategoriesAdapter.ViewHolder holder, int position) {
        final Pojos.Category category = this.categories.get(position);

       for(int i=0;i<usercategoryList.size();i++)
       {
          if(usercategoryList.get(i).getCategoryName().equals(category.getCategoryName()))
          {
              Log.d("Categories",String.valueOf(usercategoryList.get(i).getCategoryId()));
              holder.nameView.setChecked(true);
              selectedPositions[position] = true;

          }

       }

        holder.nameView.setText(category.getCategoryName());

        if(selectedPositions[position])
            holder.nameView.setChecked(true);
        else {
            holder.nameView.setChecked(false);
        }



        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // selectedcategories.add(usercategoryList.get(i).getCategoryId());

                boolean ischecked=holder.nameView.isChecked();
                if(ischecked){
                    selectedcategories.remove((Integer)category.getCategoryId());
                    holder.nameView.setChecked(false);
                }
                else
                    {
                        if(!selectedcategories.contains(category.getCategoryId())) {
                            selectedcategories.add(category.getCategoryId());
                            holder.nameView.setChecked(true);
                        }

                    }



            }
        });
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
