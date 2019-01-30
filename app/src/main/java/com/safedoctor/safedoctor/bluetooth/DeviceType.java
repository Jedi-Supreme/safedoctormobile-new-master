package com.safedoctor.safedoctor.bluetooth;

/**
 * Created by stevkky on 07/05/2018.
 */

public enum DeviceType
{
    TEMPERATURE("TEMP"),
    BLOODPRESSURE("NIBP");

    private final String prefix;

    DeviceType(String prefix)
    {
        this.prefix= prefix;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public static DeviceType fromValue(String prefix)
    {
        for (DeviceType devicetype : DeviceType.values()) {
            if (devicetype.prefix.equalsIgnoreCase(prefix)) {
                return devicetype;
            }
        }

        return null;
    }

    public static DeviceType fromFullName(String name)
    {
        for (DeviceType devicetype : DeviceType.values()) {
            if (devicetype.prefix.equalsIgnoreCase(name.substring(0,4))) {
                return devicetype;
            }
        }

        return null;
    }
}
