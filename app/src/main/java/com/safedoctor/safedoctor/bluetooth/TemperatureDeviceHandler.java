package com.safedoctor.safedoctor.bluetooth;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import cn.com.contec.jar.eartemperture.DeviceCommand;
import cn.com.contec.jar.eartemperture.DevicePackManager;
import cn.com.contec.jar.eartemperture.EarTempertureDataJar;
import cn.com.contec.jar.util.PrintBytes;

public class TemperatureDeviceHandler  extends  DevicesHandler{

	private static final String TAG = "TemperatureDevice";


	public  TemperatureDeviceHandler()
	{
		super();
	}

	public TemperatureDeviceHandler(IDeviceDataAvailable listener) {

		super(listener);
	}


	DevicePackManager mDevicePackManager = new DevicePackManager();
	private int mType = 0;

	@Override
	public synchronized void write(byte[] buf, int count,OutputStream pOutputStream) throws Exception
	{
		PrintBytes.printData(buf, count);
		int state = mDevicePackManager.arrangeMessage(buf, count);
		switch (state)
		{
			case 1:// Received successfully
				for ( int i = 0; i < mDevicePackManager.m_DeviceDatas.size(); i++)
				{
					final EarTempertureDataJar temp = mDevicePackManager.m_DeviceDatas.get(i);
					Log.e(TAG,temp.toString());

					if(this.dataavailablelistener !=null)
					{
						this.dataavailablelistener.OnDataAvailable(temp);
					}

					//BluetoothConfigs.TEMPDATA = temp;
					//System.out(mDevicePackManager.m_DeviceDatas.get(i).toString()+ "\n");

				}
				pOutputStream.write(DeviceCommand.command_delData());
				mDevicePackManager.m_DeviceDatas.clear();
				break;
			case 2:// Receive failed
				break;
			case 3:// set time success
							pOutputStream.write(DeviceCommand.command_queryDataNum());
				break;
			case 4:// set time failed
				break;
			case 5://del data success
				Log.e(TAG, "successfully deleted");
				break;
			case 6://del data failed
				break;
			case 7:
				break;
			case 8:// Conduct equipment school
				pOutputStream.write(DeviceCommand.command_VerifyTime());
				break;
			case 9:// Device has data Send command to request all data
				pOutputStream.write(DeviceCommand.command_requestAllData());
				break;
		}
	}

	@Override
	public DeviceType getDeviceType() {

		return DeviceType.TEMPERATURE;
	}

	@Override
	public void setOnDataAvailable(IDeviceDataAvailable dataavailablelistener) {
		this.dataavailablelistener = dataavailablelistener;
	}

}
