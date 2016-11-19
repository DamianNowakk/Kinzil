package engineeringwork.pl.kinzil.counter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.movisens.smartgattlib.Characteristic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;
import engineeringwork.pl.kinzil.containers.DatabaseHelper;
import engineeringwork.pl.kinzil.containers.Trip;

public class CounterFragment extends Fragment {
    View view;
    long startTime;
    long elapsedHours;
    long elapsedMinutes;
    long elapsedSeconds;
    double maxSpeed;
    double averageSpeed;
    double distance;
    int calories;
    String time;
    String date;
    boolean isTripStarted;
    DatabaseHelper db;
    TextView averageSpeedTextView, maxSpeedTextView, distanceTextView, speedTextView, timeTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.counter_fragment, container, false);

        addListeners(view);
        db = DatabaseHelper.getInstance(getActivity());
        distanceTextView = (TextView) view.findViewById(R.id.result1);
        timeTextView = (TextView) view.findViewById(R.id.result2);
        averageSpeedTextView = (TextView) view.findViewById(R.id.result3);
        maxSpeedTextView = (TextView) view.findViewById(R.id.result4);
        speedTextView = (TextView) view.findViewById(R.id.result6);
        maxSpeed = 0;

        return view;
    }

    public void setButtonStatus (boolean state, String buttonText, String stateMessage) {
        final Button button = (Button) view.findViewById(R.id.startTrip);
        button.setEnabled(state);
        button.setText(buttonText);
        TextView stateView = (TextView) view.findViewById(R.id.StateView);
        stateView.setText(stateMessage);
    }

    private void addListeners(View view) {
        final Button button = (Button) view.findViewById(R.id.startTrip);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(!isTripStarted) {
                    startTrip();
                    button.setText("STOP");
                    isTripStarted = true;
                }
                else {
                    stopTrip();
                    button.setText("START");
                    isTripStarted = false;
                }
            }
        });

        setButtonStatus(false, "START" ,"Not connected");
    }

    private  void stopTrip() {
        ((MainActivity)getActivity()).stopNotifications();
        String map = ((MainActivity)getActivity()).getmSectionsPagerAdapter().getMap();
        String  login = ((MainActivity)getActivity()).getLogin();
        Trip newTrip = new Trip(20, login, maxSpeed, averageSpeed, distance/1000, 0, time, date, map);
        db.tripInsert(newTrip);
        Toast.makeText((MainActivity)getActivity(), "Trip saved", Toast.LENGTH_LONG).show();
    }

    private void startTrip() {
        BluetoothGattCharacteristic cscMeasurementChar = ((MainActivity)getActivity()).getmBluetoothLeService().getCharacteristic(Characteristic.CSC_MEASUREMENT);
        boolean isConnected = ((MainActivity)getActivity()).isConnectedToDevice();
        if (cscMeasurementChar != null && isConnected) {
            ((MainActivity)getActivity()).readCharacteristic(cscMeasurementChar);
        }
        else {
            setButtonStatus(false, "START" ,"Disconnected");
        }

        calories = 0;
        maxSpeed = 0;
        averageSpeed = 0;
        distance = 0;
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        startTime = System.currentTimeMillis();
    }

    private long countTimeElapsed() {
        return System.currentTimeMillis() - startTime;
    }

    private void updateTimeTextView() {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long different = countTimeElapsed();

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        elapsedSeconds = different / secondsInMilli;

        time = String.valueOf(elapsedHours) + "h " + String.valueOf(elapsedMinutes) + "m " + String.valueOf(elapsedSeconds) + "s";
        timeTextView.setText(time);
    }

    public void changeText(String speedString, String wheelTime, Double newDistance){
        updateTimeTextView();

        double speed = Double.parseDouble(speedString);
        if(speed > maxSpeed)
            maxSpeed = speed;
        String speedShort = String.format("%.2f", speed);
        speedTextView.setText(speedShort + " km/h");

        distance += newDistance;
        String distanceString = String.format("%.2f", distance/1000);
        distanceTextView.setText(distanceString + " km");

        String maxSpeedShort = String.format("%.2f", maxSpeed);
        maxSpeedTextView.setText(maxSpeedShort + "km/h");

        double hoursInMilli = 3600000;
        double totalTime = (double)countTimeElapsed()/hoursInMilli;
        averageSpeed = (distance/1000)/totalTime;
        String averageSpeedString = String.format("%.2f", averageSpeed);
        averageSpeedTextView.setText(averageSpeedString + "km/h");

        //TODO liczenie kalorii
    }
}
