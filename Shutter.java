package gradzeichen.gcam;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.icu.lang.UProperty;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.jar.Attributes;

import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;
import static android.content.ContentValues.TAG;

public class Shutter extends Thread {
    private static final String NAME = "gcams";
    private final BluetoothServerSocket ServSocket;

    public Shutter() {
        BluetoothServerSocket tmp = null;
        try {
            tmp = gradzeichen.gcam.MainActivity.BA.listenUsingRfcommWithServiceRecord(NAME, gradzeichen.gcam.MainActivity.ui);
        } catch (IOException e) {
            tmp = null;
        }
        ServSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = ServSocket.accept();
                doShutter(socket);
            } catch (IOException e) {
                MainActivity.info("Error " + e);
                break;
            }
        }
    }

    private void doShutter(BluetoothSocket s) {
        try {
            OutputStream o = s.getOutputStream();
            int i = -1;
            if (gradzeichen.gcam.MainActivity.o1 == null ) {
                gradzeichen.gcam.MainActivity.o1 = o;
                i = 1;
            }
            else {
                gradzeichen.gcam.MainActivity.o2 = o;
                i = 2;
                MainActivity.b2.setVisibility(View.VISIBLE);
            }
            gradzeichen.gcam.MainActivity.info("Stream " + i + " connected");
        }
        catch ( Exception e ) {
        }
    }

    public void cancel() {
        try {
            ServSocket.close();
        } catch (IOException e) {
        }
    }
}