package engineeringwork.pl.kinzil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static engineeringwork.pl.kinzil.R.string.app_name;

public class CounterFragment extends Fragment {
    View view;
    Button changeUnit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.counter_fragment, container, false);
        changeUnit = (Button) view.findViewById(R.id.changeUnit);

        addListenerToChangeUnitButton();
        return view;
    }

    public void addListenerToChangeUnitButton() {
        changeUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //todo zmiana jednostki
            }
        });
    }

}
