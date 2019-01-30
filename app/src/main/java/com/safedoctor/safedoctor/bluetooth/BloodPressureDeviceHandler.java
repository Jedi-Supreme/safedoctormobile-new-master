package com.safedoctor.safedoctor.bluetooth;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import com.contec.jar.contec08a.DeviceCommand;
import com.contec.jar.contec08a.DevicePackManager;

import cn.com.contec.jar.util.PrintBytes;
import de.greenrobot.event.EventBus;

import android.R.integer;
import android.util.Log;


public class BloodPressureDeviceHandler  extends DevicesHandler{
    private static final String TAG = "BloodPressureDevice";


    public static final int e_pack_pressure_back = 0x46;
    DevicePackManager mPackManager = new DevicePackManager();
    private DeviceData mDeviceData;
    public static final int e_pack_hand_back = 0x48;
    public static final int e_pack_oxygen_back = 0x47;
    private int mType = 0;

    public  BloodPressureDeviceHandler()
    {
        super();
    }

    public BloodPressureDeviceHandler(IDeviceDataAvailable dataavailablelistener)
    {
        super(dataavailablelistener);
    }

    @Override
    public synchronized void write(byte[] buf, int count,OutputStream pOutputStream) throws Exception {

        //PrintBytes.printData(buf, count);
        int state = mPackManager.arrangeMessage(buf, count, mType);
        Log.e("BloodPressureDevice", "Received new data from Device: Type=> "+mType + ", State:=>"+ state);
        int x = DevicePackManager.Flag_User;
        switch (state)
        {
            case e_pack_hand_back:
                switch (mType) {
                    case 9:
                        mType = 5;
                        pOutputStream.write(DeviceCommand.DELETE_BP());
                        break;
                    case 0:
                        mType = 3;
                        Log.e("BloodPressureDevice", "Request to reset proper time on device");
                        pOutputStream.write(DeviceCommand.correct_time_notice);
                        break;
                    case 1:
                        mType = 2;
                        pOutputStream.write(DeviceCommand.REQUEST_OXYGEN());
                        break;
                    case 7:
                        mType = 8;
                        pOutputStream.write(DeviceCommand.REQUEST_OXYGEN());
                        break;
                    case 2:
                        mType = 5;
                        pOutputStream.write(DeviceCommand.DELETE_OXYGEN());
                        break;
                    case 8:
                        mType = 5;
                        pOutputStream.write(DeviceCommand.DELETE_OXYGEN());
                        break;
                    case 3:
                        mType = 1;

                       if (x == 0x11) {
                            mType = 7;// Three users
                        } else {
                            mType = 1;// Single user
                        }


                        Log.e("BloodPressureDevice", "Requesting for data from device");
                        //pOutputStream.write(DeviceCommand.REQUEST_NORMAL_BLOOD_PRESSURE());
                        pOutputStream.write(DeviceCommand.REQUEST_BLOOD_PRESSURE());
                        break;
                }
                break;
            case 0x30:// Confirm that the correct time is correct
                Log.e("BloodPressureDevice", "Confirm correct time set on device");
                pOutputStream.write(DeviceCommand.Correct_Time());
                break;
            case 0x40:// Correct time
                pOutputStream.write(DeviceCommand.REQUEST_HANDSHAKE());
                break;
            case e_pack_pressure_back:

               // Log.e(TAG, "New Data In:=>"+mPackManager.mDeviceData.mData_normal_blood);
                Log.e(TAG, "New Data In:=>"+mPackManager.mDeviceData.mData_blood);
                //Log.e(TAG, "New Data In:=>"+mPackManager.mDeviceData.mData_oxygen);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {////Prevent last command to blood pressure device not receive
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ArrayList<byte[]> _dataList = mPackManager.mDeviceData.mData_blood;
                int _size = _dataList.size();

                if (_size == 0)
                {
                    Log.e(TAG, "No Data retrieved from Blood pressure device");
                }

                DeviceData _mData = new DeviceData(null);
                for (int i = 0; i < _size; i++)
                {
                    byte[] _byte = _dataList.get(i);
                    byte[] _data = new byte[12];


                    int  lowPre= mPackManager.mDeviceData.mData_blood.get(i)[2] & 0xff;
                    int  hiPre= (mPackManager.mDeviceData.mData_blood.get(i)[0] << 8 | mPackManager.mDeviceData.mData_blood.get(i)[1]) & 0xff;
                    int mPluse = mPackManager.mDeviceData.mData_blood.get(i)[3] & 0xff;
                    int mAvager = mPackManager.mDeviceData.mData_blood.get(i)[4] & 0xff;

                    int mYear =(mPackManager.mDeviceData.mData_blood.get(i)[5] & 0xff) + 2000;
                    int mMouth = mPackManager.mDeviceData.mData_blood.get(i)[6] & 0xff;
                    int mDay = mPackManager.mDeviceData.mData_blood.get(i)[7] & 0xff;
                    int mHour = mPackManager.mDeviceData.mData_blood.get(i)[8] & 0xff;
                    int mMin = mPackManager.mDeviceData.mData_blood.get(i)[9] & 0xff;
                    int mSec = mPackManager.mDeviceData.mData_blood.get(i)[10] & 0xff;

                    BloodPressureData bpdata = new BloodPressureData();
                    bpdata.setHighpressure(hiPre);
                    bpdata.setLowperssure(lowPre);
                    bpdata.setMpaulse(mPluse);
                    bpdata.setMaverge(mAvager);
                    bpdata.setDate(mYear+"-"+ mMouth+"-"+mDay+" "+mHour+":"+mMin+":"+mSec);

                   // BluetoothConfigs.TEMPDATA = bpdata;

                    Log.e(TAG,"New Data:=>"+"date："+bpdata.getDate()+"\n"+"high pressure:"+hiPre+"Low pressure:"+lowPre+"Pulse rate:"+mPluse+"Average pressure:"+mAvager);

                    if(this.dataavailablelistener != null)
                    {
                        this.dataavailablelistener.OnDataAvailable(bpdata);
                    }
                }

                //Will enable all these block only then am ready to deploy
               /* pOutputStream.write(DeviceCommand.REPLAY_CONTEC08A());
                try
                {
                    Thread.sleep(300);
                } catch (InterruptedException e) {////Prevent last command to blood pressure device not receive
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                */
                //Will only enable this when am ready to deploy...
               // pOutputStream.write(DeviceCommand.DELETE_BP());

                break;
            case 0x31://Time correction failed
                Log.e(TAG, "Failed to confirm the correction time");
                break;
            case 0x41:
                Log.e(TAG, "Correction time failed");
                break;
            case 0x50:
                Log.e(TAG, "successfully deleted");
                break;
            case 0x51:
                Log.e(TAG, "failed to delete");
                break;
        }

    }

    @Override
    public DeviceType getDeviceType() {

        return DeviceType.BLOODPRESSURE;
    }

    @Override
    public void setOnDataAvailable(IDeviceDataAvailable dataavailablelistener) {
        this.dataavailablelistener = dataavailablelistener;
    }

}
