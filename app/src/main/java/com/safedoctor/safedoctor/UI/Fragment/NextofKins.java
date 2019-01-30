package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.safedoctor.safedoctor.Adapter.ContactPersonsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.Patientcontactperson;
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
 * Created by Stevkkys on 9/17/2017.
 */

public class NextofKins extends Fragment implements OnTaskCompleted
{
    private View parent_view;

    private RecyclerView recyclerView;
    private ContactPersonsAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private TextView txt_no_item;
    private List<Patientcontactperson> items;

    private View root;
    private AppCompatActivity parentactivity;

    private Common common;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<Patientcontactperson>> dataresponse;
    private ProgressDialog progress;
    private Integer editcontactid = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_nextofkins, container, false);

        //parent_view = root.findViewById(android.R.id.content);

        parent_view = root.findViewById(R.id.lyt_parent);

        parentactivity = (AppCompatActivity)getActivity();

        // initToolbar();
        initComponent();
       // Toast.makeText(getActivity(), "Long press for multi selection", Toast.LENGTH_SHORT).show();

        return  root;

    }


    public static NextofKins newInstance()
    {
        NextofKins  nextofkins = new NextofKins();
        return  nextofkins;
    }


    private void initToolbar() {
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        parentactivity.setSupportActionBar(toolbar);
        parentactivity.getSupportActionBar().setTitle("Contact Persons");
        parentactivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(getActivity(), R.color.colorPrimary);
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

        items = AppConstants.CACHE_NEXT_OFKINS;

        //set data and list adapter
        mAdapter = new ContactPersonsAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        if(mAdapter.getItemCount() > 0)
        {
            txt_no_item.setVisibility(View.GONE);
        }

        mAdapter.setOnClickListener(new ContactPersonsAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, Patientcontactperson person, int pos) {
                if (mAdapter.getSelectedItemCount() > 0)  // select mode
                {
                    enableActionMode(pos);
                }
                else // view mode
                {


                    showCustomDialog(pos,person,false);

                    //Toast.makeText(getActivity().getApplicationContext(), "Read: " + person.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, Patientcontactperson obj, int pos) {
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
            final Patientcontactperson p = mAdapter.getItem(selectedItemPositions.get(i));
            Call<SwagArrayResponseModel> call = mSafeDoctorService.deleteNextofKins(TokenString.tokenString,AppConstants.patientID, p.getId());

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

    private void showCustomDialog(final Integer pos, final Patientcontactperson person, final boolean addmode)
    {
        editcontactid = null;
        final Dialog dialog = new Dialog(parentactivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.popup_contactperson);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final AppCompatEditText txtname = (AppCompatEditText) dialog.findViewById(R.id.txtname);

        final LinearLayout input_content  = (LinearLayout) dialog.findViewById(R.id.input_content);
        final LinearLayout view_content  = (LinearLayout) dialog.findViewById(R.id.view_content);
        final ImageButton editdata = (ImageButton) dialog.findViewById(R.id.editdata);
        final TextView lblinfo = (TextView)dialog.findViewById(R.id.lblinfo);

        final AppCompatEditText txtemail = (AppCompatEditText) dialog.findViewById(R.id.txtemail);
        final AppCompatEditText txtphone = (AppCompatEditText) dialog.findViewById(R.id.txtphone);
        final AppCompatEditText txthomeaddress = (AppCompatEditText) dialog.findViewById(R.id.txthomeaddress);
        final AppCompatEditText txtworkaddress = (AppCompatEditText) dialog.findViewById(R.id.txtworkaddress);

        final AppCompatEditText spnrelationship = (AppCompatEditText) dialog.findViewById(R.id.spnrelationship);
        spnrelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common.displayDropDownList(getContext(), AppConstants.CACHE_FORMDATA.getRelation(),spnrelationship,null,null);
            }
        });

        // get the display only fields

        final TextView lblname = (TextView) dialog.findViewById(R.id.lblname);
        final TextView lblrelationship = (TextView) dialog.findViewById(R.id.lblrelationship);
        final TextView lblphone = (TextView) dialog.findViewById(R.id.lblphone);
        final TextView lblemail = (TextView) dialog.findViewById(R.id.lblemail);
        final TextView lblhomeaddress= (TextView) dialog.findViewById(R.id.lblhomeaddress);
        final TextView lblworkaddress = (TextView) dialog.findViewById(R.id.lblworkaddress);
        final TextView txtfrmtitle = (TextView)dialog.findViewById(R.id.txtfrmtitle);

        txtfrmtitle.setText("NEXT OF KIN");
        if(!addmode && null != person) //view mode :will not allow changing
        {
            input_content.setVisibility(View.GONE);
            view_content.setVisibility(View.VISIBLE);
            editdata.setVisibility(View.VISIBLE);
            lblinfo.setText("View next of kin");
            editcontactid = person.getId();

            lblname.setText(person.getName());
            lblemail.setText(person.getEmails());
            lblphone.setText(person.getContactnumbers());
            lblhomeaddress.setText(person.getHomeaddress());
            lblworkaddress.setText(person.getWorkaddress());
            BasicObject basicobj = AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getRelation(),
                    person.getRelationshipid());
            lblrelationship.setText(basicobj.getName());
        }
        else
        {
            lblinfo.setText("Add new next of kin");
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
                lblinfo.setText("Edit next of kin");

                editcontactid = person.getId();

                txtname.setText(person.getName());
                txtemail.setText(person.getEmails());
                txtphone.setText(person.getContactnumbers());
                txthomeaddress.setText(person.getHomeaddress());
                txtworkaddress.setText(person.getWorkaddress());
                AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getRelation(),
                        person.getRelationshipid()),spnrelationship);

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

                if(txtname.getText().toString().isEmpty())
                {
                    txtname.setError("Field is required");
                    return;
                }

                if(txtemail.getText().toString().isEmpty())
                {
                    txtemail.setError("Field is required");
                    return;
                }

                if(txtphone.getText().toString().isEmpty())
                {
                    txtphone.setError("Field is required");
                    return;
                }
                if(spnrelationship.getText().toString().isEmpty())
                {
                    spnrelationship.setError("Field is required");
                    return;
                }


                final Patientcontactperson p = new Patientcontactperson();
                p.setId(editcontactid); // Null when its for post
                p.setName(AuxUtils.getTextValue(txtname.getText(),false));
                p.setContactnumbers(AuxUtils.getTextValue(txtphone.getText(),true));
                p.setEmails(AuxUtils.getTextValue(txtemail.getText(),true));
                p.setHomeaddress(AuxUtils.getTextValue(txthomeaddress.getText(),true));
                p.setPatientid(AppConstants.patientID);
                p.setRelationshipid(AuxUtils.getTagValue(spnrelationship.getTag(), true));
                p.setWorkaddress(AuxUtils.getTextValue(txtworkaddress.getText(),true));

                List<Patientcontactperson> person = new ArrayList<Patientcontactperson>();
                person.add(p);

                if(addmode) // post
                {
                    progress.show();

                    Call<SwagArrayResponseModel<List<Patientcontactperson>>> call = mSafeDoctorService.saveNextofKins(TokenString.tokenString,AppConstants.patientID, person);

                    call.enqueue(new Callback<SwagArrayResponseModel<List<Patientcontactperson>>>() {
                        @Override
                        public void onResponse(Call<SwagArrayResponseModel<List<Patientcontactperson>>> call, Response<SwagArrayResponseModel<List<Patientcontactperson>>> response) {

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
                                    Patientcontactperson saved = dataresponse.getData().get(0);

                                    p.setId(saved.getId());
                                    items.add(p);
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
                        public void onFailure(Call<SwagArrayResponseModel<List<Patientcontactperson>>> call, Throwable throwable) {
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

                    progress.show();

                    Call<SwagArrayResponseModel<List<Patientcontactperson>>> call = mSafeDoctorService.updateNextofKins(TokenString.tokenString,AppConstants.patientID, person);

                    call.enqueue(new Callback<SwagArrayResponseModel<List<Patientcontactperson>>>() {
                        @Override
                        public void onResponse(Call<SwagArrayResponseModel<List<Patientcontactperson>>> call, Response<SwagArrayResponseModel<List<Patientcontactperson>>> response) {

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
                                    Patientcontactperson saved = dataresponse.getData().get(0);

                                    items.remove(pos);

                                    p.setId(saved.getId());
                                    items.get(pos).setName(p.getName());
                                    items.get(pos).setWorkaddress(p.getWorkaddress());
                                    items.get(pos).setHomeaddress(p.getHomeaddress());
                                    items.get(pos).setEmails(p.getEmails());
                                    items.get(pos).setContactnumbers(p.getContactnumbers());
                                    items.get(pos).setRelationshipid(p.getRelationshipid());

                                    mAdapter.notifyDataSetChanged();

                                    dialog.dismiss();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SwagArrayResponseModel<List<Patientcontactperson>>> call, Throwable throwable) {
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
