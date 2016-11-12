package engineeringwork.pl.kinzil.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.containers.Trip;
import engineeringwork.pl.kinzil.containers.TripArrayAdapter;

public class HistoryFragment extends ListFragment implements AdapterView.OnItemClickListener {
    View view;
    private ArrayList<Trip> historyData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);
        historyData.add(new Trip(1, "aaa", 2.0f, 2.0f, 2.0f,123,"111", "69", "chybanie00"));
        historyData.add(new Trip(2, "aaa1", 2.0f, 2.0f, 2.0f,123,"111", "69", "chybanie00"));
        historyData.add(new Trip(3, "aaa2", 2.0f, 2.0f, 2.0f,123,"111", "69", "chybanie00"));
        historyData.add(new Trip(4, "aaa3", 2.0f, 2.0f, 2.0f,123,"111", "69", "chybanie00"));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<Trip> adapter = new TripArrayAdapter(getActivity(), 0, historyData);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),"Item: " + position, Toast.LENGTH_SHORT).show();
    }
}
