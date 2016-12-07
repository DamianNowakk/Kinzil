package engineeringwork.pl.kinzil.counter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
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
    long startTime, startTimeFilter, stopTime;
    double maxSpeed;
    double speed;
    double averageSpeed;
    double distance;
    int calories;
    String time;
    String date;
    boolean isTripStarted, isTripStopped = false;
    DatabaseHelper db;
    TextView averageSpeedTextView, maxSpeedTextView, distanceTextView, speedTextView, timeTextView, timeWithStopsTextView, caloriesTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.counter_fragment, container, false);

        addListeners(view);
        db = DatabaseHelper.getInstance(getActivity());
        distanceTextView = (TextView) view.findViewById(R.id.result1);
        timeTextView = (TextView) view.findViewById(R.id.result2);
        timeWithStopsTextView = (TextView) view.findViewById(R.id.timeWithStops);
        averageSpeedTextView = (TextView) view.findViewById(R.id.result3);
        maxSpeedTextView = (TextView) view.findViewById(R.id.result4);
        speedTextView = (TextView) view.findViewById(R.id.result6);
        caloriesTextView = (TextView) view.findViewById(R.id.result5);
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

        ((MainActivity)getActivity()).getmBluetoothLeService().reset();
        calories = 0;
        maxSpeed = 0;
        averageSpeed = 0;
        distance = 0;
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        startTime = System.currentTimeMillis();
        startTimeFilter = System.currentTimeMillis();
    }

    private long countTimeElapsed(long startTimeLocal) {
        return System.currentTimeMillis() - startTimeLocal;
    }

    private void updateTimeTextView(long startTimeLocal, TextView textViewToChange) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long different = countTimeElapsed(startTimeLocal);

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;

        //time = String.format("%02d", elapsedHours) + ":" + String.valueOf(elapsedMinutes) + ":" + String.valueOf(elapsedSeconds);
        time = String.format("%02d", elapsedHours) + ":" + String.format("%02d", elapsedMinutes) + ":" + String.format("%02d", elapsedSeconds);
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //String time2 = sdf.format(different);
        textViewToChange.setText(time);
    }

    public void subtractTime(){
        startTimeFilter += System.currentTimeMillis() - stopTime;
        isTripStopped = false;
    }

    public void startStopTime()
    {
        stopTime = System.currentTimeMillis();
        isTripStopped = true;
    }

    public void changeText(String speedString, String wheelTime, Double newDistance){
        updateTimeTextView(startTime, timeTextView);
        if(isTripStopped == false)
            updateTimeTextView(startTimeFilter, timeWithStopsTextView);

        speed = Double.parseDouble(speedString);
        if(speed > maxSpeed)
            maxSpeed = speed;
        String speedS = Integer.toString((int)speed);
        speedTextView.setText(speedS);

        distance += newDistance;
        String distanceString = String.format("%.2f", distance/1000);
        distanceTextView.setText(distanceString);

        String maxSpeedString = Integer.toString((int)maxSpeed);
        maxSpeedTextView.setText(maxSpeedString);

        double hoursInMilli = 3600000;
        double totalTime = (double)countTimeElapsed(startTime)/hoursInMilli;
        averageSpeed = (distance/1000)/totalTime;
        //String averageSpeedString = String.format("%.2f", averageSpeed);
        String averageSpeedString = Integer.toString((int)averageSpeed);
        averageSpeedTextView.setText(averageSpeedString);

        double calories = MainActivity.getUserWeight()  * (double)countTimeElapsed(startTime)/60000.0 * (0.6345* averageSpeed * averageSpeed + 0.7563 * averageSpeed + 36.725)/3600;
        String caloriesString = Integer.toString((int)calories);
        caloriesTextView.setText(caloriesString);
    }
}
