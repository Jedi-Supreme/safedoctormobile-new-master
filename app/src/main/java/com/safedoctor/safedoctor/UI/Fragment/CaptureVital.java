package com.safedoctor.safedoctor.UI.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.PeripheralAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.ResendCodeModel;
import com.safedoctor.safedoctor.Model.ResendPhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.Vitalsignscapture;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.bluetooth.BloodPressureData;
import com.safedoctor.safedoctor.bluetooth.BluetoothChatService;
import com.safedoctor.safedoctor.bluetooth.BluetoothConfigs;
import com.safedoctor.safedoctor.bluetooth.DeviceType;
import com.safedoctor.safedoctor.bluetooth.DevicesHandler;
import com.safedoctor.safedoctor.bluetooth.HealthDevice;
import com.safedoctor.safedoctor.bluetooth.IChatServiceState;
import com.safedoctor.safedoctor.bluetooth.IDeviceDataAvailable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.com.contec.jar.eartemperture.EarTempertureDataJar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by stevkky on 07/31/2018.
 */

public class CaptureVital extends Fragment implements IDeviceDataAvailable, OnTaskCompleted
{

    private RecyclerView lv;
    private BluetoothAdapter btAdapt;
    private BluetoothChatService mChatService;

    private Set<BluetoothDevice> pairedDevices;

    private List<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

    private PeripheralAdapter adapter;

    private ProgressDialog progress;


    private Snackbar mainsnackbar;

    private BluetoothDevice currentdevice;

    private TextView result;

    private HealthDevice hdevice;

    private Button refreshlst;

    private SafeDoctorService mSafeDoctorService;
    private Common common;

    private String TAG="CaptureVitalSigns";

    public static Fragment newInstance(HealthDevice device)
    {
        CaptureVital fragment = new CaptureVital();
        Bundle b = new Bundle();
        b.putSerializable("device",device);
        fragment.setArguments(b);

        return  fragment;
    }

    private void dismissMainSnackbar()
    {
        if(mainsnackbar != null && mainsnackbar.isShown())
        {
            mainsnackbar.dismiss();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        common = new Common(getActivity().getApplicationContext(),this);
        mSafeDoctorService = ApiUtils.getAPIService();

        hdevice =  (HealthDevice) getArguments().getSerializable("device");

        View rootView =  inflater.inflate(R.layout.fragment_capture_vital, container, false);

        result =  (TextView) rootView.findViewById(R.id.result);
        refreshlst =  (Button) rootView.findViewById(R.id.refreshlst);


        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);


        btAdapt = BluetoothAdapter.getDefaultAdapter();
        if(BluetoothAdapter.STATE_OFF == btAdapt.getState())
        {
            btAdapt.enable();
        }


        progress = new ProgressDialog(getActivity());
        progress.setMessage("Synhronization in progress ...");

        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        getActivity().registerReceiver(searchDevices, intent);

        lv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        lv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //lv.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        lv.setHasFixedSize(true);


        adapter = new  PeripheralAdapter(this.getActivity(),list);

        lv.setAdapter(adapter);


        adapter.setOnItemClickListener(new PeripheralAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BluetoothDevice device, int position)
            {
                currentdevice = device;
                dismissMainSnackbar();
                ConnectToDevice(device);

            }

            @Override
            public void onItemLongClick(View view, BluetoothDevice device, int position) {
                dismissMainSnackbar();
                Toast.makeText(getContext(),device.getName() + " Long Clicked",Toast.LENGTH_LONG).show();
            }
        });

        refreshlst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fetchPairedDevices();
            }
        });

        fetchPairedDevices();

        if(AppConstants.CACHE_PERIPHERALS == null || AppConstants.CACHE_PERIPHERALS.size() == 0)
        {
            common.getPeripherals();

        }
        return  rootView;
    }


    private void fetchPairedDevices()
    {

        setPairedDevices();

        list.clear();
        for(BluetoothDevice bt : pairedDevices)
        {
          if(DeviceType.fromValue(hdevice.getPrefix()) == DeviceType.fromFullName(bt.getName()))
          {
              if(adapter.canAdd(bt))
              {
                  list.add(bt);
              }
          }

        }

        adapter.notifyDataSetChanged();

    }

    private void setPairedDevices()
    {
        pairedDevices = btAdapt.getBondedDevices();
    }

    private void ConnectToDevice(final BluetoothDevice device)
    {
        result.setText("");
        progress.setMessage("Connecting to device: " + device.getName());
        progress.show();


        DevicesHandler handler = BluetoothConfigs.getDeviceHandlerFromName(device.getName());
        if(handler == null)
        {
            progress.dismiss();
            return;
        }

        handler.setOnDataAvailable(CaptureVital.this);
        mChatService = new BluetoothChatService(getActivity().getApplicationContext(),handler,chatServicestatereceiver);


        // pause(2000);

        Thread mThread = new Thread()
        {
            @Override
            public void run()
            {
                mChatService.start();
                mChatService.connect(device);

                Log.e("SafeDoktor","...thread finished...");
            }
        };
        mThread.start();

        Log.e("SafeDoktor","...thread started...");
    }

    @Override
    public void OnDataAvailable(final Object data) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {

                if(data != null)
                {
                    if(data instanceof EarTempertureDataJar)
                    {
                        EarTempertureDataJar eartemp = (EarTempertureDataJar)data;
                        result.setText(eartemp.m_data + "");
                    }

                    if(data instanceof BloodPressureData)
                    {
                        BloodPressureData bpresult = (BloodPressureData) data;
                        result.setText(bpresult.getHighpressure() + "/"+ bpresult.getLowperssure());

                    }

                   sendCaptureToServer(result.getText().toString(),hdevice.getPrefix(),common.getVitalTypeID(hdevice.getPrefix()), currentdevice.getName());


                    // Toast.makeText(getActivity(),"New Measurement: "+BluetoothConfigs.TEMPDATA.m_data, Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private IChatServiceState chatServicestatereceiver = new IChatServiceState() {
        @Override
        public void OnStateChange(int state, BluetoothDevice device) {

            Log.e("SafeDoktor","Bluetooth State Change: "+ state);

            if(device == null)
            {
                device = currentdevice;
            }

            switch (state)
            {
                case BluetoothChatService.STATE_NONE:

                    break;
                case BluetoothChatService.STATE_LISTEN:

                    break;
                case BluetoothChatService.STATE_CONNECTING:

                    break;
                case BluetoothChatService.STATE_CONNECTED:
                    progress.dismiss();
                    mainsnackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                            "Connected to Device : " + device.getName(), Snackbar.LENGTH_INDEFINITE);
                    mainsnackbar.show();


                    break;

            }

        }

        @Override
        public void OnConnectionFailed(int failuretype, BluetoothDevice device) {

            progress.dismiss();

            if(device == null)
            {
                device = currentdevice;
            }


            String msg = "";
            switch (failuretype)
            {
                case BluetoothChatService.FAILURE_DISCONNECTED:
                    msg= "Disconnected from Selected Device : " + device.getName();
                    break;
                case BluetoothChatService.FAILURE_CONENCTION_ATTEMPT:
                    msg = "Could not connect to Device : " + device.getName();
                    break;
                case BluetoothChatService.FAILURE_CONNECTION_LOST:
                    msg= "Connection Lost to Device : " + device.getName();
                    break;
                default:


            }
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                    msg, Snackbar.LENGTH_LONG).show();

        }
    };

    private BroadcastReceiver searchDevices = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            Log.d("safeDoctor","Broadcast Recieved:=>"+ action);

            Bundle b = intent.getExtras();
            if(b!= null)
            {
                Object[] lstName = b.keySet().toArray();
                for (int i = 0; i < lstName.length; i++) {
                    String keyName = lstName[i].toString();
                    Log.d(keyName, String.valueOf(b.get(keyName)));
                }
            }

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {

                progress.setMessage("Searching for  new devices...");
                progress.show();

            }

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                progress.dismiss();

            }

            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                if(progress.isShowing())
                {
                    progress.dismiss();

                }

                switch (b.getInt(BluetoothAdapter.EXTRA_STATE))
                {
                    case BluetoothAdapter.STATE_ON:

                        break;
                    case BluetoothAdapter.STATE_OFF:
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTED:
                        break;

                }

            }

            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName()==null) {
                    return;
                }

                Log.d("safeDoctor","Device Found: "+device.getName());


            }

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
            {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState())
                {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("safeDoctor",  device.getName() + "=>it is pairing");

                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("safeDoctor", device.getName() +" => finish");
                        Toast.makeText(getActivity().getApplicationContext(),"Device : "+ device.getName() + " Paired Successfully",Toast.LENGTH_LONG).show();
                        progress.dismiss();

                        adapter.removeDevice(device);

                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("safeDoctor", device.getName() +"=>cancel");
                        Toast.makeText(getActivity().getApplicationContext(),"Device : "+ device.getName() + " Not Paired",Toast.LENGTH_LONG).show();

                        progress.dismiss();
                    default:
                        break;
                }

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(searchDevices);

    }

    @Override
    public void onTaskCompleted(Object result) {

    }


    private void sendCaptureToServer(String result, String deviceid, Integer vitaltypeid, String devicename)
    {
        progress.setMessage(getResources().getString(R.string.alert_working));
        progress.show();

        Vitalsignscapture capture = new Vitalsignscapture();
        capture.setResult(result);
        capture.setActualperipheral(devicename);
        capture.setCreateuserid(AppConstants.PatientObj.getPatientid().toString());
        capture.setPeripheralid(deviceid);
        capture.setVitaltypeid(vitaltypeid);
        capture.setPatientid(AppConstants.PatientObj.getPatientid());

        Call<SwagArrayResponseModel<List<Vitalsignscapture>>> call = mSafeDoctorService.setVitalSign(TokenString.tokenString, capture);

        call.enqueue(new Callback<SwagArrayResponseModel<List<Vitalsignscapture>>>()
        {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<Vitalsignscapture>>> call, Response<SwagArrayResponseModel<List<Vitalsignscapture>>> response)
            {
                if(response.isSuccessful()){

                    Log.e(TAG,""+response.body());
                    Toast.makeText(context,"Data saved in Server",Toast.LENGTH_LONG).show();


                }else if(response.code()==500){

                 Toast.makeText(context,"Error occured in saving the data",Toast.LENGTH_LONG).show();

                }else if(response.code()== HttpURLConnection.HTTP_FORBIDDEN || response.code()==HttpURLConnection.HTTP_UNAUTHORIZED){


                    Toast.makeText(context,"You have to sign in first",Toast.LENGTH_LONG).show();

                }

                progress.dismiss();

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<Vitalsignscapture>>> call, Throwable t) {
                progress.dismiss();
                Log.i("Safe", "Fetching error: " + t.getMessage() + "");

            }
        });

    }
}
