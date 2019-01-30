package com.safedoctor.safedoctor.bluetooth;

import java.io.OutputStream;

/**
 * Created by stevkky on 07/08/2018.
 */

public abstract class  DevicesHandler
{

    DevicesHandler()
    {

    }

    DevicesHandler (IDeviceDataAvailable dataavailablelistener)
    {
        this.dataavailablelistener = dataavailablelistener;
    }

    protected IDeviceDataAvailable dataavailablelistener;

    public abstract void write(byte[] buf, int count,OutputStream pOutputStream) throws Exception;

    public abstract  DeviceType getDeviceType();

    public abstract void  setOnDataAvailable(IDeviceDataAvailable dataavailablelistener);


}
