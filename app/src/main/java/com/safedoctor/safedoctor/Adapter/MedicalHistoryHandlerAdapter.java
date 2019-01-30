package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Api.enums.QuestionType;
import com.safedoctor.safedoctor.Model.responses.Medicalhistoryquestion;
import com.safedoctor.safedoctor.Model.responses.Medicalhistoryquestionvalues;
import com.safedoctor.safedoctor.Model.responses.Patientmedicalhistoryline;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedicalhistory;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class MedicalHistoryHandlerAdapter extends RecyclerView.Adapter<MedicalHistoryHandlerAdapter.ViewHolder>
{
    private Context ctx;
    private List<Medicalhistoryquestion> items;
    private OnClickListener onClickListener = null;


    private List<Medicalhistoryquestionvalues> finiteitems = new ArrayList<Medicalhistoryquestionvalues>();


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MedicalHistoryHandlerAdapter(Context mContext, List<Medicalhistoryquestion> items) {
        this.ctx = mContext;
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView question, lblanswer_info;
        public Switch answer;
        public LinearLayout infinite_block;
        public CardView lyt_parent;
        public RadioGroup finite_input;
        public NachoTextView infinite_input;
        public AppCompatEditText txtremark;
        public Button btnapply;


        public ViewHolder(View view)
        {
            super(view);
            question = (TextView) view.findViewById(R.id.question);
            lblanswer_info = (TextView) view.findViewById(R.id.lblanswer_info);
            answer = (Switch) view.findViewById(R.id.answer);
            infinite_block = (LinearLayout) view.findViewById(R.id.infinite_block);
            lyt_parent = (CardView) view.findViewById(R.id.lyt_parent);
            finite_input = (RadioGroup) view.findViewById(R.id.finite_input);
            infinite_input = (NachoTextView) view.findViewById(R.id.infinite_input);
            txtremark = (AppCompatEditText) view.findViewById(R.id.txtremark);
            btnapply = (Button) view.findViewById(R.id.btnapply);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Medicalhistoryquestion question = items.get(position);
        final Patientprofilemedicalhistory useranswer = AppConstants.CACHE_FORMDATA.getPatientprofilemedicalhistory(question.getId());

        finiteitems = question.getAnswervalues();
        holder.finite_input.removeAllViews();

        if(useranswer != null)
        {
            if(useranswer.isAnswer() != null) {
                holder.answer.setChecked(useranswer.isAnswer().booleanValue());
            }

            holder.txtremark.setText(useranswer.getRemarks());
        }


        //Now generate dynamic radio buttons
        if(finiteitems != null && finiteitems.size() > 0)
        {
            for(Medicalhistoryquestionvalues v : finiteitems)
            {
                RadioButton rbn = new RadioButton(ctx);
                rbn.setId(v.getId());
                rbn.setText(v.getDescription());
                rbn.setTag(v.getId());

                if( useranswer != null)
                {
                    Patientmedicalhistoryline line = useranswer.getPatientmedicalhistoryline(v.getId());
                    if(line != null)
                    {
                        rbn.setChecked(true);
                    }
                    else
                    {
                        rbn.setChecked(false);
                    }

                }

                holder.finite_input.addView(rbn);

            }
        }

        holder.infinite_input.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        // displaying text view data

      holder.question.setText(question.getQuestion());

        //now set the infinite list already enetered by user

        if( useranswer != null) {
            List<String> infinite_answers = new ArrayList<>();
            for (Patientmedicalhistoryline line : useranswer.getPatientmedicalhistorylines()) {
                if (line.getAnswer() != null && !line.getAnswer().isEmpty()) {
                    infinite_answers.add(line.getAnswer());
                }
            }

            if (infinite_answers.size() > 0) {
                holder.infinite_input.setText(infinite_answers);
            }
        }

        if((question.getQuestiontypeid() == QuestionType.YES_NO_NOLIST.getNumber()) || (question.getQuestiontypeid() == 0))
        {
            holder.infinite_block.setVisibility(View.GONE);
            holder.finite_input.setVisibility(View.GONE);
            holder.lblanswer_info.setVisibility(View.GONE);
            holder.infinite_input.setVisibility(View.GONE);
        }

        if(question.getQuestiontypeid() == QuestionType.YES_NO_WITH_FINITELIST.getNumber())
        {
            holder.infinite_block.setVisibility(View.GONE);
            holder.infinite_input.setVisibility(View.GONE);
        }

        if(question.getQuestiontypeid() == QuestionType.YES_NO_WITH_INFINITELIST.getNumber())
        {
            holder.finite_input.setVisibility(View.GONE);

        }
        if(question.getQuestiontypeid() == QuestionType.FINITE_LIST.getNumber())
        {
            holder.infinite_block.setVisibility(View.GONE);
            holder.infinite_input.setVisibility(View.GONE);
            holder.answer.setVisibility(View.GONE);

        }

        if(question.getQuestiontypeid() == QuestionType.INFINITE_LIST.getNumber())
        {
            holder.finite_input.setVisibility(View.GONE);
            holder.answer.setVisibility(View.GONE);

        }


        // set the click listener on the apply button
        holder.btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {

                    if(useranswer != null) {
                        onClickListener.onItemClick(v, holder, items.get(position), useranswer.getId() , position);
                    }
                    else
                    {
                        onClickListener.onItemClick(v, holder, items.get(position), null , position);
                    }
                }
            }
        });

    }

    public Medicalhistoryquestion getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface OnClickListener
    {
        void onItemClick(View view, ViewHolder holder, Medicalhistoryquestion obj, Integer answerid, int pos);

        //void onItemLongClick(View view,ViewHolder holder, Medicalhistoryquestion obj, int pos);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
