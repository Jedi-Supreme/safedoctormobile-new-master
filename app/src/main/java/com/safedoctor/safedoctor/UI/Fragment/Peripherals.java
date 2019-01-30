package com.safedoctor.safedoctor.UI.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.App;
import com.safedoctor.safedoctor.bluetooth.BloodPressureData;
import com.safedoctor.safedoctor.bluetooth.BluetoothChatService;
import com.safedoctor.safedoctor.bluetooth.BluetoothConfigs;
import com.safedoctor.safedoctor.bluetooth.DevicesHandler;
import com.safedoctor.safedoctor.bluetooth.IChatServiceState;
import com.safedoctor.safedoctor.bluetooth.IDeviceDataAvailable;
import com.safedoctor.safedoctor.bluetooth.TemperatureDeviceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.com.contec.jar.eartemperture.EarTempertureDataJar;

import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by stevkky on 05/24/2018.
 */

public class Peripherals extends Fragment implements IDeviceDataAvailable
{
    Button discoverbluetooth, pairedbluetooth;
    RecyclerView lv;
    BluetoothAdapter btAdapt;
    private BluetoothChatService mChatService;
    public static BluetoothSocket btSocket;

    private Set<BluetoothDevice> pairedDevices;

    private List<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

    private PeripheralAdapter adapter;

    private ProgressDialog progress;

    private Common common;


    private TextView result,title;

   // private static Handler mHandler = null;
    //private Runnable runnable;

    private int newfound = 0;


    private Snackbar mainsnackbar;

    private BluetoothDevice currentdevice;

    public static Fragment newInstance() {
        Peripherals fragment = new Peripherals();
        return  fragment;
    }

    private void dismissMainSnackbar()
    {
        if(mainsnackbar != null && mainsnackbar.isShown())
        {
            mainsnackbar.dismiss();
        }

    }

    private void pause(int milli)
    {
        try
        {
            Thread.sleep(milli);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Searching for new devices...");

        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        getActivity().registerReceiver(searchDevices, intent);


        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        View rootView =  inflater.inflate(R.layout.fragment_list_peripherals, container, false);


        btAdapt = BluetoothAdapter.getDefaultAdapter();
        if(BluetoothAdapter.STATE_OFF == btAdapt.getState())
        {
            btAdapt.enable();
        }

        discoverbluetooth = (Button) rootView.findViewById(R.id.discoverbluetooth);
        pairedbluetooth = (Button) rootView.findViewById(R.id.pairedbluetooth);

        result = (TextView) rootView.findViewById(R.id.result);
        title = (TextView) rootView.findViewById(R.id.title);

        discoverbluetooth.setOnClickListener(new ClickEvent());
        pairedbluetooth.setOnClickListener(new ClickEvent());


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

        common=new Common(App.context);
        Activity activity=getActivity();
        common.fragmentInitOnbackpressed(activity);

        /*mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(BluetoothConfigs.TEMPDATA != null)
                {
                    if(BluetoothConfigs.TEMPDATA instanceof EarTempertureDataJar)
                    {
                        EarTempertureDataJar eartemp = (EarTempertureDataJar)BluetoothConfigs.TEMPDATA;
                        result.setText("Temp Result: " + eartemp.m_data);
                    }

                    if(BluetoothConfigs.TEMPDATA instanceof BloodPressureData)
                    {
                        BloodPressureData bpresult = (BloodPressureData) BluetoothConfigs.TEMPDATA;
                        result.setText("BP Result: SYS:" + bpresult.getHighpressure() + ", DIA: "+ bpresult.getLowperssure());

                    }


                    // Toast.makeText(getActivity(),"New Measurement: "+BluetoothConfigs.TEMPDATA.m_data, Toast.LENGTH_LONG).show();
                }
                mHandler.postDelayed(this, 500);
            }
        };
        runnable.run();
        */

        fetchPairedDevices();
        title.setText("Added Devices");
        result.setText("");
        return rootView;
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

            Toast.makeText(App.context,msg,Toast.LENGTH_LONG).show();
                //Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                       // msg, Snackbar.LENGTH_LONG).show();

        }
    };

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

        handler.setOnDataAvailable(Peripherals.this);
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

    private void fetchPairedDevices()
    {

        setPairedDevices();

        list.clear();
        for(BluetoothDevice bt : pairedDevices)
        {
            if(adapter.canAdd(bt))
            {
                list.add(bt);
            }
        }

        adapter.notifyDataSetChanged();

    }

    private void setPairedDevices()
    {
        pairedDevices = btAdapt.getBondedDevices();
    }


    private boolean isAlreadyPaired(BluetoothDevice device)
    {
        setPairedDevices();

        return pairedDevices.contains(device);
    }

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

                newfound = 0;
                title.setText("New devices (" + newfound + ") found");
                progress.setMessage("Searching for  new devices...");
                progress.show();

            }

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                title.setText("New devices (" + newfound + ") found");
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

                        pairedbluetooth.callOnClick();
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

                if(adapter.canAdd(device) && !isAlreadyPaired(device))
                {
                    newfound++;
                    title.setText("New devices (" + newfound + ") found");

                    list.add(device);
                    adapter.notifyDataSetChanged();
                }

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
    public void OnDataAvailable(final Object data)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                if(data != null)
                {
                    if(data instanceof EarTempertureDataJar)
                    {
                        EarTempertureDataJar eartemp = (EarTempertureDataJar)data;
                        result.setText("Temp Result: " + eartemp.m_data);
                    }

                    if(data instanceof BloodPressureData)
                    {
                        BloodPressureData bpresult = (BloodPressureData) data;
                        result.setText("BP Result: SYS:" + bpresult.getHighpressure() + ", DIA: "+ bpresult.getLowperssure());

                    }


                    // Toast.makeText(getActivity(),"New Measurement: "+BluetoothConfigs.TEMPDATA.m_data, Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {

            dismissMainSnackbar();

            result.setText("");
            if(v == pairedbluetooth)
            {
                title.setText("Added Devices");
                fetchPairedDevices();
            }

            if (v == discoverbluetooth)
            {

                adapter.clear();
                if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {

                    btAdapt.enable();
                    //Toast.makeText(Peripherals.this.getActivity(), "Bluetooth is OFF", Toast.LENGTH_LONG).show();
                    //return;
                }

                if(btAdapt.isDiscovering())
                {
                    btAdapt.cancelDiscovery();

                    try
                    {
                        Thread.sleep(2000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                // lstDevices.clear();
                btAdapt.startDiscovery();


            /*    Intent discoverableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                */

            }
            /*else if (v == btnDis)
            {
                Intent discoverableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            } else if (v == btnExit) {
                try {
                    if (btSocket != null)
                        btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                testBlueTooth.this.finish();
                */

        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        btAdapt.cancelDiscovery();
        getActivity().unregisterReceiver(searchDevices);

    }
}
