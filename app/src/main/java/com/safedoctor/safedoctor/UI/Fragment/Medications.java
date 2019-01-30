package com.safedoctor.safedoctor.UI.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.widget.LineItemDecoration;
import com.safedoctor.safedoctor.Adapter.MedicationsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.Drugs;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedication;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class Medications extends Fragment implements OnTaskCompleted
{
    private View parent_view;

    private RecyclerView recyclerView;
    private MedicationsAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private TextView txt_no_item;
    private List<Patientprofilemedication> items;

    private View root;
    private AppCompatActivity parentactivity;

    private Common common;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<Patientprofilemedication>> dataresponse;
    private ProgressDialog progress;
    private Integer editcontactid = null;


    public static Medications newInstance()
    {
        Medications medications = new Medications();

        return  medications;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_medications, container, false);

        //parent_view = root.findViewById(android.R.id.content);

        parent_view = root.findViewById(R.id.lyt_parent);

        parentactivity = (AppCompatActivity)getActivity();

        // initToolbar();
        initComponent();

        return  root;

    }


    private void initComponent()
    {
        common = new Common(getActivity().getApplicationContext(),this);
        mSafeDoctorService = ApiUtils.getAPIService();
        progress =  new ProgressDialog(this.getContext());
        progress.setMessage("Working...Please Wait");

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        txt_no_item = (TextView) root.findViewById(R.id.txt_no_item);

        items = AppConstants.CACHE_MEDICATIONS;

        //set data and list adapter
        mAdapter = new MedicationsAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        if(mAdapter.getItemCount() > 0)
        {
            txt_no_item.setVisibility(View.GONE);
        }

        mAdapter.setOnClickListener(new MedicationsAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, Patientprofilemedication medication, int pos) {
                if (mAdapter.getSelectedItemCount() > 0)  // select mode
                {
                    enableActionMode(pos);
                }
                else // view mode
                {


                    showCustomDialog(pos,medication,false);

                    //Toast.makeText(getActivity().getApplicationContext(), "Read: " + person.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, Patientprofilemedication obj, int pos) {
                enableActionMode(pos);
            }

        });

        actionModeCallback = new ActionModeCallback();

        ((FloatingActionButton) root.findViewById(R.id.fab_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(null,null,true);
            }
        });

    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = parentactivity.startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onTaskCompleted(Object result) {

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(parentactivity, R.color.blue_grey_700);
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                deleteInboxes();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(getActivity(), R.color.red_600);
        }
    }

    private void deleteInboxes() {

        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--)
        {
            progress.show();
            final int pos = selectedItemPositions.get(i);
            final Patientprofilemedication p = mAdapter.getItem(selectedItemPositions.get(i));
            Call<SwagArrayResponseModel> call = mSafeDoctorService.deleteMedication(TokenString.tokenString,AppConstants.patientID, p.getId());

            call.enqueue(new Callback<SwagArrayResponseModel>() {
                @Override
                public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {

                    progress.dismiss();
                    if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(), FormLogin.class);
                        startActivity(intent);
                    }
                    else if (response.isSuccessful())
                    {
                        mAdapter.removeData(pos);
                        mAdapter.notifyDataSetChanged();

                        if (mAdapter.getItemCount()== 0)
                        {
                            txt_no_item.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Could not delete. Please try again later", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SwagArrayResponseModel> call, Throwable throwable) {
                    progress.dismiss();
                    try {
                        Toast.makeText(getActivity().getApplicationContext(), "Network Error, please try again", Toast.LENGTH_LONG).show();
                        throwable.printStackTrace();
                    }
                    catch (Exception e)
                    {

                    }
                }
            });



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {

        }
        else
        {
            Toast.makeText(parentactivity.getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCustomDialog(final Integer pos,final Patientprofilemedication medication, final boolean addmode)
    {
        final Dialog dialog = new Dialog(parentactivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.popup_medications);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final AppCompatEditText spndrug = (AppCompatEditText) dialog.findViewById(R.id.spndrug);

        spndrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common.displayDrugList(getContext(), AppConstants.CACHE_FORMDATA.getDrugs(),spndrug,null,null);
            }
        });

        final AppCompatEditText txtremark = (AppCompatEditText) dialog.findViewById(R.id.txtremark);

        final LinearLayout input_content  = (LinearLayout) dialog.findViewById(R.id.input_content);
        final LinearLayout view_content  = (LinearLayout) dialog.findViewById(R.id.view_content);
        final ImageButton editdata = (ImageButton) dialog.findViewById(R.id.editdata);
        final TextView lblinfo = (TextView)dialog.findViewById(R.id.lblinfo);

        final TextView lbldrug = (TextView) dialog.findViewById(R.id.lbldrug);
        final TextView lblremark = (TextView) dialog.findViewById(R.id.lblremark);

        if(!addmode && null != medication) //view mode :will not allow changing
        {
            input_content.setVisibility(View.GONE);
            view_content.setVisibility(View.VISIBLE);
            editdata.setVisibility(View.VISIBLE);
            lblinfo.setText("View your medication");

            editcontactid = medication.getId();

            lbldrug.setText(medication.getDrug().getName());
            lblremark.setText(medication.getRemarks());
        }
        else
        {
            lblinfo.setText("Add new medication");
            input_content.setVisibility(View.VISIBLE);
            view_content.setVisibility(View.GONE);
            editcontactid = null;
        }


        editdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_content.setVisibility(View.VISIBLE);
                view_content.setVisibility(View.GONE);
                editdata.setVisibility(View.GONE);
                lblinfo.setText("Edit your medication");
                editcontactid = medication.getId();
                spndrug.setText(medication.getDrug().getName());
                spndrug.setTag(medication.getDrugid());
                txtremark.setText(medication.getRemarks());

            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spndrug.getText().toString().isEmpty())
                {
                    spndrug.setError("Field is required");
                    return;
                }

                final   Patientprofilemedication m = new Patientprofilemedication();
                m.setId(editcontactid); // Null when its for post
                m.setPatientid(AppConstants.patientID);
                m.setDrugid(AuxUtils.getCharTagValue(spndrug.getTag(), true));
                m.setDrug(new Drugs(AuxUtils.getCharTagValue(spndrug.getTag(), true),spndrug.getText().toString()));
                m.setRemarks(AuxUtils.getTextValue(txtremark.getText(),true));
                m.setCreatedtime(AuxUtils.Date.getNow());

                List<Patientprofilemedication> medlist = new ArrayList<Patientprofilemedication>();
                medlist.add(m);

                progress.show();
                if(addmode) // post
                {
                    Call<SwagArrayResponseModel<List<Patientprofilemedication>>> call = mSafeDoctorService.saveMedications(TokenString.tokenString,AppConstants.patientID, medlist);

                    call.enqueue(new Callback<SwagArrayResponseModel<List<Patientprofilemedication>>>() {
                        @Override
                        public void onResponse(Call<SwagArrayResponseModel<List<Patientprofilemedication>>> call, Response<SwagArrayResponseModel<List<Patientprofilemedication>>> response) {

                            progress.dismiss();
                            if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), FormLogin.class);
                                startActivity(intent);
                            }
                            else if (response.isSuccessful())
                            {
                                dataresponse = response.body();

                                if(dataresponse.getData() != null && dataresponse.getData().size() > 0)
                                {
                                    Patientprofilemedication saved = dataresponse.getData().get(0);

                                    m.setId(saved.getId());
                                    items.add(m);
                                    mAdapter.notifyDataSetChanged();

                                    if (mAdapter.getItemCount() > 0)
                                    {
                                        txt_no_item.setVisibility(View.GONE);
                                    }
                                    dialog.dismiss();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SwagArrayResponseModel<List<Patientprofilemedication>>> call, Throwable throwable) {
                            progress.dismiss();
                            try {
                                Toast.makeText(getActivity().getApplicationContext(), "Network Error, please try again", Toast.LENGTH_LONG).show();
                                throwable.printStackTrace();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    });


                }
                else //put
                {


                    Call<SwagArrayResponseModel<List<Patientprofilemedication>>> call = mSafeDoctorService.updateMedications(TokenString.tokenString,AppConstants.patientID, medlist);

                    call.enqueue(new Callback<SwagArrayResponseModel<List<Patientprofilemedication>>>() {
                        @Override
                        public void onResponse(Call<SwagArrayResponseModel<List<Patientprofilemedication>>> call, Response<SwagArrayResponseModel<List<Patientprofilemedication>>> response) {

                            progress.dismiss();
                            if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "Your session has expired. Please Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), FormLogin.class);
                                startActivity(intent);
                            }
                            else if (response.isSuccessful())
                            {
                                dataresponse = response.body();

                                if(dataresponse.getData() != null && dataresponse.getData().size() > 0 )
                                {
                                    Patientprofilemedication saved = dataresponse.getData().get(0);

                                    items.remove(pos);

                                    m.setId(saved.getId());
                                    items.get(pos).setCreatedtime(m.getCreatedtime());
                                    items.get(pos).setDrugid(m.getDrugid());
                                    items.get(pos).setDrug(m.getDrug());
                                    items.get(pos).setRemarks(m.getRemarks());
                                    items.get(pos).setPatientid(m.getPatientid());

                                    mAdapter.notifyDataSetChanged();

                                    dialog.dismiss();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SwagArrayResponseModel<List<Patientprofilemedication>>> call, Throwable throwable) {
                            progress.dismiss();
                            try {
                                Toast.makeText(getActivity().getApplicationContext(), "Network Error, please try again", Toast.LENGTH_LONG).show();
                                throwable.printStackTrace();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    });
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
