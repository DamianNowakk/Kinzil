package engineeringwork.pl.kinzil.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;

public class BluetoothActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean mScanning;
    private ArrayList<BluetoothDevice> bluetoothDevices;


    private static final int SCAN_TIME = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mHandler = new Handler();
        bluetoothDevices = new ArrayList<>();
        addListenerToButton();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.low_energy_bluetooth_is_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.bluetooth_is_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void addListenerToButton() {
        Button buttonOne = (Button) findViewById(R.id.scanButton);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                scanLeDevice(true);
            }
        });

        Button buttonTwo = (Button) findViewById(R.id.button2);
        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(bluetoothDevices.size() > 0) {
                    TextView devicesFoundText = (TextView) findViewById(R.id.devicesFound);
                    devicesFoundText.setText(bluetoothDevices.get(0).getName() + "\n" + bluetoothDevices.get(0).getAddress());
                }

                if(bluetoothDevices.size() > 1) {
                    TextView devicesFound2Text = (TextView) findViewById(R.id.devicesFound2);
                    if (bluetoothDevices.get(1) != null)
                        devicesFound2Text.setText(bluetoothDevices.get(1).getName() + "\n" + bluetoothDevices.get(1).getAddress());
                }
            }
        });

        Button buttonThree = (Button) findViewById(R.id.connectButton);
        buttonThree.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                sendName(0);
            }
        });

        Button button4 = (Button) findViewById(R.id.connectButton2);
        button4.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                sendName(1);
            }
        });
    }

    private void sendName(int id){
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("DEVICE_NAME",(bluetoothDevices.get(id).getName()));
        intent.putExtra("DEVICE_ADDRESS", bluetoothDevices.get(id).getAddress());
        startActivity(intent);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!bluetoothDevices.contains(device)) {
                                bluetoothDevices.add(device);
                            }
                        }
                    });
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //invalidateOptionsMenu();
                }
            }, SCAN_TIME);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
}
