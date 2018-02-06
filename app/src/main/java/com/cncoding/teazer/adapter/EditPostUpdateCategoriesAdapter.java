package com.cncoding.teazer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.friends.PublicProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farazhabib on 20/12/17.
 */

public class EditPostUpdateCategoriesAdapter extends RecyclerView.Adapter<EditPostUpdateCategoriesAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Category> categories;
    private Fragment fragment;
    private PublicProfile userProfile;
    ArrayList<Category> usercategoryList;
    boolean flag=false;
    boolean[] selectedPositions;
    List<Integer> selectedcategories;
    List<String> selecteCategriesName;
    boolean checkdataFirst;


    public EditPostUpdateCategoriesAdapter(ArrayList<Category> categories, Context context,ArrayList<Category>usercategoryList) {
        this.categories = categories;
        this.userProfile = userProfile;

        selectedcategories=new ArrayList<>();
        selecteCategriesName=new ArrayList<>();

        this.context = context;
        this.usercategoryList=usercategoryList;

        selectedPositions = new boolean[categories.size()];

        for(int i = 0;i<categories.size();i++)
        {
            selectedPositions[i] = false;
        }

        for (int i=0;i<usercategoryList.size();i++) {

            selectedcategories.add(usercategoryList.get(i).getCategoryId());
            selecteCategriesName.add(usercategoryList.get(i).getCategoryName());


        }

    }
    @Override
    public EditPostUpdateCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);
        return new EditPostUpdateCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EditPostUpdateCategoriesAdapter.ViewHolder holder, final int position) {
        try {
            final Category category = this.categories.get(position);

            for (int i = 0; i < selecteCategriesName.size(); i++)
            {
                if (selecteCategriesName.get(i).equals(category.getCategoryName())) {

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

                        if(selecteCategriesName.size()>1) {
                            selectedcategories.remove((Integer) category.getCategoryId());
                            selecteCategriesName.remove(category.getCategoryName());
                            setCheck(holder.nameView, false);
                            selectedPositions[position] = false;
                        }
                        else
                        {
                            Toast.makeText(context, "Select atleast 1 category", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if(selecteCategriesName.size()<5) {
                            selectedcategories.add(category.getCategoryId());
                            selecteCategriesName.add(category.getCategoryName());
                            setCheck(holder.nameView, true);
                            selectedPositions[position] = true;
                        }
                        else
                        {
                            Toast.makeText(context, "you can select maximum 5 categories", Toast.LENGTH_SHORT).show();

                        }
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
    public List<Integer> getSelectedCategories()
    {
        return selectedcategories;
    }
    public List<String>getSelectedCategoriesName()

    {
        return selecteCategriesName;
    }
}
