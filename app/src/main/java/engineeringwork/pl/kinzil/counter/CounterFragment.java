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

import com.movisens.smartgattlib.Characteristic;

import java.util.concurrent.TimeUnit;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;

public class CounterFragment extends Fragment {
    View view;
    long startTime;
    long elapsedHours;
    long elapsedMinutes;
    long elapsedSeconds;
    double maxSpeed;
    boolean isTripStarted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.counter_fragment, container, false);

        addListeners(view);
        maxSpeed = 0;

        return view;
    }

    public void setButtonStatus (boolean state, String buttonText) {
        final Button button = (Button) view.findViewById(R.id.startTrip);
        button.setEnabled(state);
        button.setText(buttonText);
    }

    private void addListeners(View view) {
        final Button button = (Button) view.findViewById(R.id.startTrip);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //clearUI
                if(!isTripStarted) {
                    startTrip();
                    button.setText("STOP");
                    isTripStarted = true;
                }
                else {
                    //TODO stop reading
                    button.setText("START");
                    isTripStarted = false;
                }
            }
        });

        setButtonStatus(false, "Counter not connected");
    }

    private void startTrip() {
        BluetoothGattCharacteristic cscMeasurementChar = ((MainActivity)getActivity()).getCharacteristic(Characteristic.CSC_MEASUREMENT);
        if (cscMeasurementChar != null) {
            ((MainActivity)getActivity()).readCharacteristic(cscMeasurementChar);
        }

        startTime = System.currentTimeMillis();
    }

    private long countTimeElapsed() {
        return System.currentTimeMillis() - startTime;
    }

    private void updateTimeTextView() {
        TextView timeTextView = (TextView) view.findViewById(R.id.result2);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long different = countTimeElapsed();

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        elapsedSeconds = different / secondsInMilli;

        timeTextView.setText(String.valueOf(elapsedHours) + " h " + String.valueOf(elapsedMinutes) + " m " + String.valueOf(elapsedSeconds) + " s");
    }

    public void changeText(String cscData, String speedString, String wheelTime, Double distance){
        updateTimeTextView();

        TextView textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(cscData);

        TextView speedTextView = (TextView) view.findViewById(R.id.result6);
        double speed = Double.parseDouble(speedString);
        if(speed > maxSpeed)
            maxSpeed = speed;
        String speedShort = String.format("%.2f", speed);
        speedTextView.setText(speedShort + " km/h");

        TextView distanceTextView = (TextView) view.findViewById(R.id.result1);
        String distanceString = String.format("%.2f", distance/1000);
        distanceTextView.setText(distanceString + " m");

        TextView maxSpeedTextView = (TextView) view.findViewById(R.id.result4);
        String maxSpeedShort = String.format("%.2f", maxSpeed);
        maxSpeedTextView.setText(maxSpeedShort + "km/h");

        TextView averageSpeedTextView = (TextView) view.findViewById(R.id.result3);
        double hoursInMilli = 3600000;
        double totalTime = (double)countTimeElapsed()/hoursInMilli;
        double averageSpeed = (distance/1000)/totalTime;
        String averageSpeedString = String.format("%.2f", averageSpeed);
        averageSpeedTextView.setText(averageSpeedString + "km/h");

        //liczenie kalorii
    }
}
