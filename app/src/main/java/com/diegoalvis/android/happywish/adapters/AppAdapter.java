package com.diegoalvis.android.happywish.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegoalvis.android.happywish.R;
import com.diegoalvis.android.happywish.models.Application;
import com.diegoalvis.android.happywish.models.Category;
import com.diegoalvis.android.happywish.views.fragments.ListCatFragment;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by diegoalvis on 1/23/17.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private final List<Application> applications;
    private Context context;

    OnItemClickListener listener;

    public AppAdapter(Context context, List<Application> applications, OnItemClickListener listener){
        this.applications = applications;
        this.context = context;
        this.listener = listener;
    }



    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application,parent,false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AppAdapter.AppViewHolder holder, final int position) {
        final Application application = applications.get(position);

        holder.nameApp.setText(application.getName());
        holder.authorApp.setText(application.getRights());
        holder.categoryApp.setText(application.getCategory().getCategory());
        holder.priceApp.setText(NumberFormat.getNumberInstance(Locale.US).format(application.getPrice()));

        if(application.getPrice() == 0.0)
            holder.priceApp.setText(R.string.free_label);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickApp(applications.get(position));
            }
        });

        // Load app image
        try {
            Picasso.with(context)
                    .load(application.getImage())
                    .placeholder(R.drawable.circle_white_150dp)
                    .into(holder.imageApp);

        } catch (Exception e) {
            Log.e("ALVIS", "Error al cargar la iamgen");
        }

    }

    @Override
    public int getItemCount() {
        return (applications == null) ? 0 : applications.size();
    }


    public static class AppViewHolder extends RecyclerView.ViewHolder{

        protected CircleImageView imageApp;
        protected TextView nameApp, authorApp, categoryApp, priceApp;

        public AppViewHolder(View v){
            super(v);
            imageApp    = (CircleImageView) v.findViewById(R.id.app_image);
            nameApp = (TextView) v.findViewById(R.id.app_name);
            authorApp   = (TextView) v.findViewById(R.id.app_author);
            categoryApp = (TextView) v.findViewById(R.id.app_category);
            priceApp    = (TextView) v.findViewById(R.id.app_price);
        }
    }

}
