package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.R;

import java.util.List;

/**
 * Created by oteng on 17/01/2018.
 */

public class PartnerDoctorsAdapter extends RecyclerView.Adapter<PartnerDoctorsAdapter.MyViewHolder> {

    private List<UserAccount> facilityinfos;
    Context context;

    public PartnerDoctorsAdapter(List<UserAccount> facilityinfo, Context context){
        this.facilityinfos = facilityinfo;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specialties_list_item, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
        return new PartnerDoctorsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserAccount facilityinfo = facilityinfos.get(position);


        if (facilityinfo.getFirstname() != null) {
            holder.hospital_name.setText(facilityinfo.getFirstname() +" "+facilityinfo.getLastname());
            holder.hospital_location.setText(facilityinfo.getEmailaddress());
            holder.hospital_over.setText(facilityinfo.getPrimarycellno());
        }


    }

    @Override
    public int getItemCount() {
        return facilityinfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView hospital_name;
        private TextView hospital_location;
        private TextView hospital_over;
        private ImageView hospital_logo;

        private MyViewHolder(View view) {
            super(view);

            this.hospital_name = (TextView) view.findViewById(R.id.hospital_name);
            this.hospital_location = (TextView) view.findViewById(R.id.hospital_location);
            this.hospital_over = (TextView) view.findViewById(R.id.hospital_over);
            this.hospital_logo = (ImageView) view.findViewById(R.id.hospital_logo);

        }
    }

}
