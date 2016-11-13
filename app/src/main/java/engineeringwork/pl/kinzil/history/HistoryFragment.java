package engineeringwork.pl.kinzil.history;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;
import engineeringwork.pl.kinzil.containers.DatabaseHelper;
import engineeringwork.pl.kinzil.containers.Trip;
import engineeringwork.pl.kinzil.containers.TripArrayAdapter;

public class HistoryFragment extends ListFragment implements AdapterView.OnItemClickListener, MainActivity.Callbacks, OnMapReadyCallback {
    View view;

    private String login;

    private ArrayList<Trip> trips = new ArrayList<>();
    private DrawerLayout drawer;
    private View detailsView;
    private DatabaseHelper databaseHelper;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);
        detailsView  = view.findViewById(R.id.details);

        drawer = ((DrawerLayout)((MainActivity)getActivity()).findViewById(R.id.drawer_layout));
        databaseHelper = DatabaseHelper.getInstance(getContext());
        login = ((MainActivity)getActivity()).getLogin();

        getDataFromDataBase();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<Trip> adapter = new TripArrayAdapter(getActivity(), 0, trips);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private void populateDetailsView(Trip trip) {

        ImageView time = (ImageView) detailsView.findViewById(R.id.image);

        TextView avgSpeed = (TextView)detailsView.findViewById(R.id.avgSpeed);
        TextView  maxSpeed = (TextView) detailsView.findViewById(R.id.maxSpeed);
        TextView date = (TextView)detailsView.findViewById(R.id.date);
        TextView distance = (TextView) detailsView.findViewById(R.id.distance);
        TextView calories = (TextView) detailsView.findViewById(R.id.calories);
        TextView duration = (TextView) detailsView.findViewById(R.id.duration);

        avgSpeed.setText(String.valueOf(trip.getAvgSpeed()));
        date.setText(String.valueOf(trip.getDate()));
        distance.setText(String.valueOf(trip.getDistance()));
        maxSpeed.setText(String.valueOf(trip.getMaxSpeed()));
        calories.setText(String.valueOf(trip.getCalories()));
        duration.setText(String.valueOf(trip.getTime()));
        time.setImageResource(R.mipmap.ic_time_icon);

        //TODO: zamula i trzeba nalozyc trase.
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.replace(R.id.mapView, fragment);
        transaction.commit();
        fragment.getMapAsync(this);

    }

    private void getDataFromDataBase(){
        //databaseHelper.tripInsert(new Trip(1,login,20.0f,21.0f,22.0f,11,"6h30m","11-11-2016","hahaniewiemjak"));
        trips.addAll(databaseHelper.getTrip(login));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        populateDetailsView(trips.get(position));
        expand();
    }

    @Override
    public void onBackPressedCallBack() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            collapse();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }

}
