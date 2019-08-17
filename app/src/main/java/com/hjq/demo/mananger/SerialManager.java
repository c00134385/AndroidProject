package com.hjq.demo.mananger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class SerialManager {
    private static SerialManager ourInstance;

    public SerialPortFinder mSerialPortFinder;
    private SerialPort mSerialPort = null;
    private Context mContext;

    public static SerialManager getInstance() {
        return ourInstance;
    }

    private SerialManager(Context context) {
        this.mContext = context;
        mSerialPortFinder = new SerialPortFinder();
    }

    public static void init(Context context) {
        ourInstance = new SerialManager(context);
    }

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            SharedPreferences sp = mContext.getSharedPreferences("android_serialport_api.sample_preferences", Context.MODE_PRIVATE);
            String path = sp.getString("DEVICE", "");
            int baudrate = Integer.decode(sp. getString("BAUDRATE", "-1"));

            Log.e("TAG","path="+path+"     baudrate="+baudrate);
            /* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
