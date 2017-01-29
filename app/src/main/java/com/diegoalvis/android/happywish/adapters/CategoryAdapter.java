package com.diegoalvis.android.happywish.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegoalvis.android.happywish.R;
import com.diegoalvis.android.happywish.models.Category;

import java.util.List;

/**
 * Created by diegoalvis on 1/24/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder> {

    private final List<Category> categories;
    private Context context;

    OnItemClickListener listener;
    
    public CategoryAdapter(Context context, List<Category> categories, OnItemClickListener listener){
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent,false);
        return new CategoryAdapter.CatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CatViewHolder holder, final int position) {
        final Category category = categories.get(position);

        holder.nameCat.setText(category.getCategory());
        // set category URL
        String htmlLink = String.format(context.getString(R.string.footer_label_url_cat) + " <a href='%s'>" + context.getString(R.string.here_label) + "</a>", category.getUrl());
        holder.urlCat.setText(Html.fromHtml(htmlLink));
        holder.urlCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(category.getUrl()));
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickCategory(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (categories == null) ? 0 : categories.size();
    }

    public static class CatViewHolder extends RecyclerView.ViewHolder{

        protected TextView nameCat, urlCat;

        public CatViewHolder(View v){
            super(v);
            nameCat = (TextView) v.findViewById(R.id.cat_name);
            urlCat  = (TextView) v.findViewById(R.id.cat_url);
        }

   }

    

}
