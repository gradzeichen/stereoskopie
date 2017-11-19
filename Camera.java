package gradzeichen.gcam;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static java.io.File.createTempFile;

public class Camera extends Thread {
    private final BluetoothSocket Socket;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static SimpleDateFormat md = new SimpleDateFormat("MMdd-HHmm-ssSSSS");

    public Camera(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MainActivity.ui);
            MainActivity.info("Camera connected\n\nThis is a proof of concept only.");
        } catch (Exception e) {
            MainActivity.info("Error " + e);
        }
        Socket = tmp;
    }

    public void run() {
        try {
            Socket.connect();
            InputStreamReader in = new InputStreamReader(Socket.getInputStream());
            int i = -1;
            char[] b = new char[22];
            while (true) {
                i = in.read(b);
                takepic();
            }
        }
        catch ( Exception e ) {
            MainActivity.info("Error " + e);
            try {
                Socket.close();
            } catch (IOException closeException) {

            }
        }
    }

    void takepic() {
        MainActivity.info("Click");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File("cc");
        try {
            f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "gcam-" + md.format(new Date()) + ".jp" + MainActivity.eye.toLowerCase());
        }
        catch ( Exception e ) {
            MainActivity.info("Exception in takepic " + e);
        }
        String q = "eins";
        try {
            Uri photoURI = FileProvider.getUriForFile(MainActivity.ac, "gradzeichen.gcam.fileprovider", f);
            q = "zwei";
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            q = "drei";
            if (takePictureIntent.resolveActivity(MainActivity.pm) != null) {
                MainActivity.ac.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            q = "vier";
            MainActivity.info("Image captured");
        }
        catch ( Exception e ) {
            MainActivity.info("Exception in takepic 2 " + q + "\n\n" + e);
        }

    }

    public void cancel() {
        try {
            Socket.close();
        } catch (IOException e) {
        }
    }
}