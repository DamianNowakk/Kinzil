package engineeringwork.pl.kinzil.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;

public class BluetoothFragment extends Fragment {
    View view;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean mScanning;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private ArrayList<String> bluetoothDevicesNames;
    private ListView discoveredDevicesListBox;
    private StableArrayAdapter adapter;
    Button buttonOne;

    private static final int SCAN_TIME = 10000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bluetooth_fragment, container, false);
        mHandler = new Handler();
        bluetoothDevices = new ArrayList<>();
        bluetoothDevicesNames = new ArrayList<>();
        addListeners(view);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.low_energy_bluetooth_is_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.bluetooth_is_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
            //return;
        }

        return view;
    }

    private void addListeners(View view) {
        buttonOne = (Button) view.findViewById(R.id.scanButton);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                bluetoothDevicesNames.clear();
                bluetoothDevices.clear();
                scanLeDevice(true);
            }
        });

        adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, bluetoothDevicesNames);

        discoveredDevicesListBox = (ListView) view.findViewById(R.id.discoveredDevices);
        discoveredDevicesListBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                sendName(position);
            }
        });

        discoveredDevicesListBox.setAdapter(adapter);
    }

    private void sendName(int id){
        final Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("DEVICE_NAME", bluetoothDevices.get(id).getName());
        intent.putExtra("DEVICE_ADDRESS", bluetoothDevices.get(id).getAddress());
        startActivity(intent);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String deviceName = device.getName();
                            if(deviceName == null)
                                deviceName = "Unknown device";

                            if(!bluetoothDevicesNames.contains(deviceName)) {
                                bluetoothDevicesNames.add(deviceName);
                                adapter.notifyDataSetChanged();
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
                    buttonOne.setEnabled(true);
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_TIME);
            buttonOne.setEnabled(false);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            buttonOne.setEnabled(true);
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
