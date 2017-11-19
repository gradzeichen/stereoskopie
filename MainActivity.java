package gradzeichen.gcam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static Button b1, b2, b3, b4;
    public static TextView tv;
    protected static BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    public static BluetoothDevice bd1, bd2;
    public static OutputStream o1 = null, o2 = null;
    byte[] cl = "click\n".getBytes();
    static Context con = null;
    static PackageManager pm = null;
    static Activity ac = null;
    static String eye = "X";
    public static UUID ui = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if ( bd1 == null )
                    bd1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                else
                    bd1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                info("Devs: " + ( bd1 != null ? bd1.getName() : "no dev" ) + " " + ( bd2 != null ? bd2.getName() : "no dev" ));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        tv = (TextView) findViewById(R.id.textView);
        BA = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        con = getApplicationContext();
        pm = getPackageManager();
        ac = this;
        b2.setVisibility(View.INVISIBLE);
    }

    public void startshutter(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
        String q = "";
        try {
            gradzeichen.gcam.Shutter s = new gradzeichen.gcam.Shutter();
            s.start();
            b1.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
            b4.setVisibility(View.INVISIBLE);
            info("Waiting for cameras to connect.\nRefresh screen to show button.");
        }
        catch ( Exception e ) {
            info(e.toString() + "\nFehler on " + q);
        }
    }

    public void startcameral(View v){
        eye = "L";
        startcamera(v);
    }

    public void startcamerar(View v){
        eye = "R";
        startcamera(v);
    }

    public void startcamera(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
        String q = "start";
        try {
            pairedDevices = BA.getBondedDevices();
            Object[] bda = pairedDevices.toArray();
            q = "devs";
            bd1 = ((BluetoothDevice)bda[0]);
            q = "first";
            if ( bd1 != null ) {
                q = "found";
                gradzeichen.gcam.Camera c = new gradzeichen.gcam.Camera(bd1);
                c.start();
                q = "started 1";
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
                b4.setVisibility(View.INVISIBLE);
                q = "connected";
            }
            else {
                q = "none";
                info("No server found");
            }
        }
        catch ( Exception e ) {
            info(e.toString() + "\nCamera " + q);
        }
    }

    public void click (View v) {
        String q = "Clicking Shutter Button";
        try {
            long l = System.currentTimeMillis();
            q = "Shutter Button clicked";
            o1.write(cl);
            q = "Camera 1 clicked, Now Camera 2";
            o2.write(cl);
            q = "Both Cameras clicked";
            o1.flush();
            q = "Camera 1 written";
            o2.flush();
            q = "Both Cameras written";
            l = System.currentTimeMillis() - l;
            q = "Click duration estimated";
            info("clicked (milliseconds: " + l + ") + \n\nThis is a proof of concept only.");
            q = "Shutter Button click finished";
        }
        catch ( Exception e ) {
            info( "Fehler " + q + "\n" + e.toString());
        }
    }

    public static void info(String s) {
        tv.setText(s +  "\n\n" + new Date());
        Toast.makeText(con, "Info: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}