package com.safedoctor.safedoctor.bluetooth;


import java.io.OutputStream;

public class DevicesManager
{
	private TemperatureDeviceHandler tempdevice;
	private BloodPressureDeviceHandler nibpdevice;

	public DeviceType deviceType;

	public DevicesManager(DeviceType deviceType, IDeviceDataAvailable dataAvailablelistener)
	{
		this.deviceType = deviceType;

		switch (deviceType)
		{
			case TEMPERATURE:
				tempdevice = new TemperatureDeviceHandler(dataAvailablelistener);
				break;
			case BLOODPRESSURE:
				nibpdevice = new BloodPressureDeviceHandler(dataAvailablelistener);
				break;
			default:
				// might do something here later
		}

	}

	public synchronized void write(byte[] buf, int count,OutputStream pOutputStream) throws Exception
	{

		switch (deviceType)
		{
			case TEMPERATURE:
				tempdevice.write(buf,count,pOutputStream);
				break;
			case BLOODPRESSURE:
				nibpdevice.write(buf,count,pOutputStream);
				break;
			default:
				throw new RuntimeException("Device type not supported");
				// might do something here later
		}
	}
}
