package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.safedoctor.safedoctor.Model.Consultation;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AppointmentsViewHolder;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.List;

/**
 * Created by oteng on 17/01/2018.
 */

public class OnlineVisitAdapter extends RecyclerView.Adapter<AppointmentsViewHolder> {

    private List<ConsultationProfile> facilityinfos;
    private Context context;

    public OnlineVisitAdapter(List<ConsultationProfile> facilityinfo, Context context){
        this.facilityinfos = facilityinfo;
        this.context = context;
    }

    @Override
    public AppointmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_list_item, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
        return new AppointmentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppointmentsViewHolder holder, int position) {
        ConsultationProfile consultationProfile = facilityinfos.get(position);
        Consultation consultation=consultationProfile.getConsultation();
        holder.appointmentStatus.setVisibility(View.GONE);
        holder.bookingno.setVisibility(View.GONE);
        holder.apptStartTimeTv.setVisibility(View.GONE);
        holder.apptEndTimeTv.setVisibility(View.GONE);

        holder.appointmentDoctorName.setText(consultation.getDoctor().getFullname());
        holder.appointmentDate.setText(AuxUtils.Date.formateDate(consultation.getConendtime(),"E, d MMMM, yyyy"));
        switch (consultation.getConsultationtype().getId())
        {
            case 1:
                holder.chatTypeImg.setImageResource(R.drawable.chat_64px);
                break;
            case 2:
                holder.chatTypeImg.setImageResource(R.drawable.phonecall2_64px);
                break;
            case 3:
                holder.chatTypeImg.setImageResource(R.drawable.vid_64px);
                break;
        }
        holder.more.setVisibility(View.GONE);





    }

    @Override
    public int getItemCount() {
        return facilityinfos.size();
    }


    /*
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
    }*/

}
