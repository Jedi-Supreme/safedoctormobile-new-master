package com.safedoctor.safedoctor.bluetooth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.contec.jar.eartemperture.EarTempertureDataJar;
import com.safedoctor.safedoctor.R;

public class BluetoothConfigs {


	public static Map<String,HealthDevice> getSupportedDevices ()
	{
		Map<String,HealthDevice> devices = new HashMap<>();

		devices.put("TEMP", new HealthDevice("TEMP","Ear-Temp", R.drawable.contec_ear_temp,"Temperature"));
		devices.put("NIBP", new HealthDevice("NIBP","BPM", R.drawable.contec_bpm,"Blood Pressure"));

		//TODO: will add them here as and when I inteface them
		return devices;
	}

	public static  List<HealthDevice> getSupportedDevicesList()
	{
		List<HealthDevice> lst = new ArrayList<>();

		for(HealthDevice d : getSupportedDevices().values())
		{
			lst.add(d);
		}

		return lst;

	}

	public static HealthDevice getDevice(String name)
	{
		if(name == null)
		{
			return null;
		}
		return getSupportedDevices().get(name.substring(0,4));
	}

	public static DeviceType getDeviceType(String name)
	{
		if(name == null)
		{
			return null;
		}

		return DeviceType.fromValue(name.substring(0,4));
	}

	public static DevicesHandler getDeviceHandlerFromName(String name)
	{
		DeviceType type = getDeviceType(name);

		if(type == null)
		{
			return  null;
		}

		switch (type)
		{
			case TEMPERATURE:
				return  new TemperatureDeviceHandler();
			case BLOODPRESSURE:
				return new BloodPressureDeviceHandler();
				default:
					return  null;

		}
	}
}
