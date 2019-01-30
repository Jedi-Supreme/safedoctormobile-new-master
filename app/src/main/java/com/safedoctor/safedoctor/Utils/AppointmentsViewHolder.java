package com.safedoctor.safedoctor.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

/**
 * Created by Alvin on 22/08/2017.
 */

public class AppointmentsViewHolder extends RecyclerView.ViewHolder {

    public TextView appointmentDoctorName,notilabel;
    public TextView appointmentDate;
    public TextView appointmentStatus;
    public LinearLayout statuslayout;
    public TextView apptStartTimeTv;
    public TextView apptEndTimeTv;
    public ImageView chatTypeImg;
    public TextView bookingno;
    public ImageView doctor_img;
    public ImageButton more;
    public View reminder_layout;

    public ToggleSwitch toggleSwitch;







    public AppointmentsViewHolder(View itemView) {
        super(itemView);

        appointmentDoctorName = (TextView) itemView.findViewById(R.id.appointment_doctor_name);
        appointmentDate = (TextView) itemView.findViewById(R.id.appointment_date);
        appointmentStatus = (TextView) itemView.findViewById(R.id.appointment_status);
        statuslayout = (LinearLayout) itemView.findViewById(R.id.statuslayout);
        apptStartTimeTv = (TextView) itemView.findViewById(R.id.appt_start_time_tv);
        apptEndTimeTv = (TextView) itemView.findViewById(R.id.appt_send_time_tv);
        chatTypeImg = (ImageView) itemView.findViewById(R.id.chat_type_img);
        bookingno = (TextView) itemView.findViewById(R.id.bookingno);
        doctor_img = (ImageView) itemView.findViewById(R.id.doctor_img);
        more = (ImageButton) itemView.findViewById(R.id.more);
        reminder_layout=itemView.findViewById(R.id.reminder_layout);
        toggleSwitch=(ToggleSwitch) reminder_layout.findViewById(R.id.notitoggle);
        notilabel=(TextView)reminder_layout.findViewById(R.id.notilabel);


    }



}
