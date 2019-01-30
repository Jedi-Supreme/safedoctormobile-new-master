package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.Referral;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;

import java.util.List;

/**
 * Created by oteng on 17/01/2018.
 */

public class ReferralsAdapter extends RecyclerView.Adapter<ReferralsAdapter.MyViewHolder> {

    private List<Referral> facilityinfos;
    private Context context;

    public ReferralsAdapter(List<Referral> facilityinfo, Context context){
        this.facilityinfos = facilityinfo;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.referral_list_item, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
        return new ReferralsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Referral referral = facilityinfos.get(position);



            holder.hospital_name.setText(referral.getRefferedprovider().getName());
            holder.hospital_location.setText(referral.getRemarks());
            holder.hospital_over.setText("Dr. "+referral.getDoctor().getFirstname()+" "+referral.getDoctor().getLastname());



        if (referral.getRefferedprovider().getLogo()!= null || !TextUtils.isEmpty(referral.getRefferedprovider().getLogo())) {
            Tools.displayImageRound(context, holder.hospital_logo, referral.getRefferedprovider().getLogo());
            holder.hospital_logo.setVisibility(View.VISIBLE);
        }else{
            //holder.hospital_logo.setVisibility(View.GONE);
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
