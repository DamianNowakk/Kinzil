package engineeringwork.pl.kinzil.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.bluetooth.BluetoothFragment;
import engineeringwork.pl.kinzil.containers.DatabaseHelper;
import engineeringwork.pl.kinzil.containers.Setting;
import engineeringwork.pl.kinzil.counter.BluetoothLeService;
import engineeringwork.pl.kinzil.counter.CounterFragment;
import engineeringwork.pl.kinzil.history.HistoryFragment;
import engineeringwork.pl.kinzil.map.MapFragment;
import engineeringwork.pl.kinzil.setting.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public interface Callbacks {
        public void onBackPressedCallBack();
    }
    private Callbacks mCallbacks;
    private HistoryFragment historyFragment;
    private DatabaseHelper db;

    private String login;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String[] tabs = {"Counter", "Map"};
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private boolean mConnected = false, isStopStarted = false;
    private static int mWheelSize;
    private static int userWeight;
    private static Double userOverallDistance;
    public synchronized static int getUserWeight() { return userWeight; }
    public synchronized static void setUserWeight(int weight) { userWeight = weight; }
    public synchronized static int getmWheelSize() { return mWheelSize; }
    public synchronized static void setmWheelSize(int size) { mWheelSize = size; }
    public synchronized static Double getUserOverallDistance() {
        return userOverallDistance;
    }
    public synchronized static void setUserOverallDistance(Double userOverallDistance) {
        MainActivity.userOverallDistance = userOverallDistance;
    }

    public String getLogin() {
        return login;
    }
    public SectionsPagerAdapter getmSectionsPagerAdapter() { return mSectionsPagerAdapter; }
    public BluetoothLeService getmBluetoothLeService() { return mBluetoothLeService; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences shared = getSharedPreferences("data",MODE_PRIVATE);
        login = shared.getString("login", null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.fragment_layout);
        //fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //fragments
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = DatabaseHelper.getInstance(this);
        Setting tmp = db.getFirstLoginSetting(login);
        if(tmp != null) {
            setmWheelSize(tmp.getWheelSize());
            setUserWeight(tmp.getWeight());
            setUserOverallDistance(tmp.getAllDistance());
        } else {
            tmp = new Setting();
            tmp.setAllDistance(0.0);
            tmp.setWeight(90);
            tmp.setWheelSize(1800);
            tmp.setLogin(login);
            db.settingInsert(tmp);
            setmWheelSize(tmp.getWheelSize());
            setUserWeight(tmp.getWeight());
            setUserOverallDistance(tmp.getAllDistance());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(historyFragment != null && historyFragment.isVisible()) {
            if(!historyFragment.getIsDetailsViewVisible()) {
                mCallbacks.onBackPressedCallBack();
                historyFragment.deleteMenu();
            } else {
                mCallbacks.onBackPressedCallBack();
                return;
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mViewPager.getVisibility() == View.INVISIBLE && tabLayout.getVisibility() == View.GONE) {
            tabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mSectionsPagerAdapter.counterFragment.setButtonStatus(true, "START", "");
                Toast.makeText(MainActivity.this, "Connected to device", Toast.LENGTH_SHORT).show();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mSectionsPagerAdapter.counterFragment.setButtonStatus(false, "START" ,"Disconnected");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String speed = intent.getStringExtra("EXTRA_SPEED");
                String wheelTime = intent.getStringExtra("WHEEL_TIME");
                Double newDistance = intent.getDoubleExtra("NEW_DISTANCE", 0.0);

                if(newDistance == 0 && isStopStarted == false) {
                    mSectionsPagerAdapter.counterFragment.startStopTime();
                    isStopStarted = true;
                } else if(newDistance > 0 && isStopStarted == true) {
                    mSectionsPagerAdapter.counterFragment.subtractTime();
                    isStopStarted = false;
                }
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA), speed, wheelTime, newDistance);
            }
        }
    };

    private void displayData(String cscData, String speed, String wheelTime, Double newDistance) {
        if (cscData != null) {
            mSectionsPagerAdapter.counterFragment.changeText(speed, wheelTime, newDistance);
            Log.d("CSC Measurement data", cscData);
        }
    }

    private void bindUpdateService (Intent intent) {
        mDeviceAddress = intent.getStringExtra("DEVICE_ADDRESS");
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        mConnected = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        isStopStarted = false;

        final int charaProperties = characteristic.getProperties();
        if ((charaProperties | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            // If there is an active notification on a characteristic, clear
            // it first so it doesn't update the data field on the user interface.
            if (mNotifyCharacteristic != null) {
                mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                mNotifyCharacteristic = null;
            }
            mBluetoothLeService.readCharacteristic(characteristic);
        }

        if ((charaProperties | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            mNotifyCharacteristic = characteristic;
            mBluetoothLeService.setCharacteristicNotification(characteristic, true);
        }
    }

    public void stopNotifications()
    {
        if (mNotifyCharacteristic != null) {
            mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
            mNotifyCharacteristic = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();

        //Check if BluetoothActivity send intent with Bluetooth DEVICE_NAME and DEVICE_ADDRESS
        if(intent != null && intent.getStringExtra("DEVICE_NAME") != null) {
            bindUpdateService(intent);
        }

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mConnected){
            unbindService(mServiceConnection);
        }
        mBluetoothLeService = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        tabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_connection) {
            fragmentManager.beginTransaction().replace(R.id.fragment_layout, new BluetoothFragment()).commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.fragment_layout, new SettingsFragment()).commit();
        } else if (id == R.id.nav_history) {
            historyFragment = new HistoryFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout, historyFragment).commit();
            mCallbacks = (Callbacks) historyFragment;
        } else if (id == R.id.nav_logout) {
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("autorun","false");
            editor.apply();
            Intent intent = new Intent(this, UserAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public CounterFragment counterFragment;
        public MapFragment mapFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            counterFragment = new CounterFragment();
            mapFragment = new MapFragment();
        }

        public String getMap()
        {
            return mapFragment.getMapMain();
        }

        public void setMapSecondary(String map)
        {
            try {
                mapFragment.setMapSecondary(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setOverallDistance()
        {
            counterFragment.setOverall();
        }

        public void setStart(Boolean start)
        {
            mapFragment.setStart(start);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return counterFragment;
            } else if (position == 1) {
                return mapFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    public boolean isConnectedToDevice() { return mConnected; }
}
