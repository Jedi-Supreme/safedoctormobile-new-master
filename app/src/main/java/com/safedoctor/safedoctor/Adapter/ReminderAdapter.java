package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.Model.responses.Userphoto;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityDoctorsProfile;
import com.safedoctor.safedoctor.UI.Activities.CallActivity;
import com.safedoctor.safedoctor.UI.Activities.Consultation;
import com.safedoctor.safedoctor.UI.Activities.VideoChat;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AppointmentsViewHolder;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.Tools;

import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import static com.safedoctor.safedoctor.localpersistance.Appointmenttbl.setReminder;

public class ReminderAdapter extends RecyclerView.Adapter<AppointmentsViewHolder> {

    private Context context;
    private List<ConfirmedAppointmentContentModel> appointmentModels;
    private String TAG="ReminderAdapter";
    //int tab;

    private AppointmentsAdapter.OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnMoreButtonClickListener(final AppointmentsAdapter.OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public ReminderAdapter(Context context, List<ConfirmedAppointmentContentModel> appointmentModels) {
        this.context = context;
        this.appointmentModels = appointmentModels;

    }

    @Override
    public AppointmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.appointment_list_item, parent, false);

        return new AppointmentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AppointmentsViewHolder holder, final int mposition)
    {
        final ConfirmedAppointmentContentModel  booking = appointmentModels.get(mposition);

        if(booking.getAppointment().getDoctorphoto() != null && !booking.getAppointment().getDoctorphoto().isEmpty())
        {
            Tools.displayImageRound(context, holder.doctor_img, booking.getAppointment().getDoctorphoto() );
        }

        holder.appointmentDate.setText(AuxUtils.Date.formateDate(booking.getAppointment().getBookingdate(),"E, d MMMM, yyyy"));
        holder.appointmentDoctorName.setText(booking.getAppointment().getDoctorname());
        holder.apptStartTimeTv.setText("Start time: " + AuxUtils.Date.formateTime(booking.getDetails().get(0).getStarttime()));
        holder.apptEndTimeTv.setText("End time: " + AuxUtils.Date.formateTime(booking.getDetails().get(0).getEndtime()));
        holder.bookingno.setText("Booking No: " + booking.getAppointment().getBookingnumber());


        holder.appointmentStatus.setTextColor(context.getResources().getColor(R.color.appointment_confirmed));
        holder.appointmentStatus.setText(AppConstants.getStatuses().get(booking.getAppointment().getStatusid()));


        if (booking.getAppointment().getDoctoruserid() == null) {
            holder.appointmentDoctorName.setText(R.string.no_doc_available);
        }

        switch (booking.getAppointment().getConsultationchattypeid())
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


        holder.statuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(booking.getAppointment().getDoctoruserid() == null || booking.getAppointment().getDoctoruserid().isEmpty())
                {
                    return;
                }
                AppConstants.bookingid = appointmentModels.get(mposition).getAppointment().getBookingid();
                switch (booking.getAppointment().getConsultationchattypeid())
                {
                    case 1:
                        context.startActivity(new Intent(context,Consultation.class)
                                .putExtra("starttime",booking.getDetails().get(0).getStarttime())
                                .putExtra("endtime",booking.getDetails().get(0).getEndtime()));
                        break;
                    case 2:
                        context.startActivity(new Intent(context,CallActivity.class)
                                .putExtra("starttime",booking.getDetails().get(0).getStarttime())
                                .putExtra("endtime",booking.getDetails().get(0).getEndtime()));
                        break;
                    case 3:
                        context.startActivity(new Intent(context,VideoChat.class)
                                .putExtra("starttime",booking.getDetails().get(0).getStarttime())
                                .putExtra("endtime",booking.getDetails().get(0).getEndtime()));
                        break;
                }


            }
        });

        holder.reminder_layout.setVisibility(View.VISIBLE);
        holder.notilabel.setText("Reminder ");

        if(booking.getAppointment().getRemind()==null || booking.getAppointment().getRemind()==0){
            holder.toggleSwitch.setCheckedTogglePosition(0,false);
        }else{
            holder.toggleSwitch.setCheckedTogglePosition(1,false);
        }


        holder.toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {

                if(isChecked){
                    if(position==0){//on

                        booking.getAppointment().setRemind(0);
                        holder.toggleSwitch.setCheckedTogglePosition(0,false);
                        setReminder(booking.getAppointment().getBookingnumber(),0);
                    }else if(position==1){//off
                        holder.toggleSwitch.setCheckedTogglePosition(1,false);
                        booking.getAppointment().setRemind(1);
                        setReminder(booking.getAppointment().getBookingnumber(),1);
                    }





                    appointmentModels.set(mposition,booking);
                }

            }
        });


        holder.more.setVisibility(View.GONE);
        holder.statuslayout.setClickable(false);




        holder.doctor_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(booking.getAppointment().getDoctoruserid() == null || booking.getAppointment().getDoctoruserid().isEmpty())
                {
                    return;
                }

                Intent intent = new Intent(context, ActivityDoctorsProfile.class);

                DoctorOutObj obj = new DoctorOutObj();

                UserAccount acc = new UserAccount();
                acc.setFirstname(booking.getAppointment().getDoctorname());
                acc.setId(booking.getAppointment().getDoctoruserid());
                obj.setDoctor(acc);

                if(booking.getAppointment().getDoctorphoto() != null && !booking.getAppointment().getDoctorphoto().isEmpty())
                {
                    Userphoto photo = new Userphoto();
                    photo.setPhoto(booking.getAppointment().getDoctorphoto());

                    obj.setPicture(photo);
                }

                intent.putExtra("doctor",obj);
                intent.putExtra("viewonlymode",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(booking.getAppointment().getDoctoruserid() == null || booking.getAppointment().getDoctoruserid().isEmpty())
        {
            holder.statuslayout.setVisibility(View.GONE);
        }


    }

    private void onMoreButtonClick(final View view, final ConfirmedAppointmentContentModel appt) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, appt, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_appt_actions);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return appointmentModels.size();
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ConfirmedAppointmentContentModel obj, MenuItem item);
    }
}
