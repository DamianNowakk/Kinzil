package engineeringwork.pl.kinzil.counter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.movisens.smartgattlib.Characteristic;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;

public class CounterFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.counter_fragment, container, false);
        addListenerToButton(view);


        return view;
    }

    public void changeText(String data){
        TextView textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(data);
    }

    private void addListenerToButton(View view) {
        Button changeUnitButton = (Button) view.findViewById(R.id.changeUnit);
        changeUnitButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                BluetoothLeService mBluetoothLeService = ((MainActivity)getActivity()).getmBluetoothLeService();
                if (mBluetoothLeService != null) {
                    BluetoothGattCharacteristic cscMeasurementChar = ((MainActivity)getActivity()).getCharacteristic(Characteristic.CSC_MEASUREMENT);
                    if (cscMeasurementChar != null)
                        ((MainActivity)getActivity()).readCharacteristic(cscMeasurementChar);
                }
            }
        });
    }
}
