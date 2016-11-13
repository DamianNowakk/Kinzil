package engineeringwork.pl.kinzil.history;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;
import engineeringwork.pl.kinzil.containers.Trip;
import engineeringwork.pl.kinzil.containers.TripArrayAdapter;

public class HistoryFragment extends ListFragment implements AdapterView.OnItemClickListener, MainActivity.Callbacks {
    View view;
    private ArrayList<Trip> historyData = new ArrayList<>();
    private View detailsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);
        detailsView  = view.findViewById(R.id.details);

        getDataFromDataBase();

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
        populateDetailsView(historyData.get(position));
        expand();
    }

    @Override
    public void onBackPressedCallBack() {
        Log.d("Kliklem","Tyl");
        collapse();
    }

    //TODO: osobna klasa?
    public void expand() {
        detailsView.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getHeight();

        detailsView.getLayoutParams().height = 1;
        detailsView.setVisibility(View.VISIBLE);
        Animation a = new Animation(){

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                //v.getLayoutParams().height = targetHeight * (int)interpolatedTime;
                detailsView.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.MATCH_PARENT
                        : (int)(targetHeight * interpolatedTime);
                detailsView.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(targetHeight / detailsView.getContext().getResources().getDisplayMetrics().density));
        detailsView.startAnimation(a);
    }

    //TODO: osobna klasa?
    public void collapse() {
        final int initialHeight = detailsView.getMeasuredHeight();

        Animation a = new Animation(){

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    detailsView.setVisibility(View.GONE);
                }else{
                    detailsView.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    detailsView.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / detailsView.getContext().getResources().getDisplayMetrics().density));
        detailsView.startAnimation(a);
    }

    private void populateDetailsView(Trip trip) {

        ImageView time = (ImageView) detailsView.findViewById(R.id.image);
        TextView avgSpeed = (TextView)detailsView.findViewById(R.id.avgSpeed);
        TextView date = (TextView)detailsView.findViewById(R.id.date);
        TextView distance = (TextView) detailsView.findViewById(R.id.distance);

        avgSpeed.setText(String.valueOf(trip.getAvgSpeed()));
        date.setText(String.valueOf(trip.getDate()));
        distance.setText(String.valueOf(trip.getDistance()));
        time.setImageResource(R.mipmap.ic_time_icon);

    }

    private void getDataFromDataBase(){
        historyData.add(new Trip(1, "aaa", 2.0f, 2.0f, 2.0f,123,"111", "1", "chybanie00"));
        historyData.add(new Trip(2, "aaa1", 3.0f, 3.0f, 3.0f,123,"111", "2", "chybanie00"));
        historyData.add(new Trip(3, "aaa2", 4.0f, 4.0f, 4.0f,123,"111", "3", "chybanie00"));
        historyData.add(new Trip(4, "aaa3", 5.0f, 5.0f, 5.0f,123,"111", "4", "chybanie00"));
        historyData.add(new Trip(5, "aaa", 2.0f, 2.0f, 2.0f,123,"111", "1", "chybanie00"));
        historyData.add(new Trip(6, "aaa1", 3.0f, 3.0f, 3.0f,123,"111", "2", "chybanie00"));
        historyData.add(new Trip(7, "aaa2", 4.0f, 4.0f, 4.0f,123,"111", "3", "chybanie00"));
        historyData.add(new Trip(8, "aaa3", 5.0f, 5.0f, 5.0f,123,"111", "4", "chybanie00"));
    }
}
