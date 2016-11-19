package engineeringwork.pl.kinzil.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;
import engineeringwork.pl.kinzil.containers.DatabaseHelper;
import engineeringwork.pl.kinzil.containers.Trip;
import engineeringwork.pl.kinzil.containers.TripArrayAdapter;
import engineeringwork.pl.kinzil.containers.ViewAnimations;

public class HistoryFragment extends ListFragment implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, MenuItem.OnMenuItemClickListener, MainActivity.Callbacks, OnMapReadyCallback {
    View view;

    private String login;
    private Boolean isDetailsViewVisible = false;

    private ArrayList<Trip> trips = new ArrayList<>();
    private ArrayAdapter<Trip> adapter;
    private DrawerLayout drawer;
    private View detailsView;
    private DatabaseHelper databaseHelper;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);
        detailsView  = view.findViewById(R.id.details);
        setHasOptionsMenu(true);

        drawer = ((DrawerLayout)((MainActivity)getActivity()).findViewById(R.id.drawer_layout));
        databaseHelper = DatabaseHelper.getInstance(getContext());
        login = ((MainActivity)getActivity()).getLogin();

        getDataFromDataBase();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        adapter = new TripArrayAdapter(getActivity(), 0, trips);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
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

        final Button button = (Button) detailsView.findViewById(R.id.routeOverlay);
        button.setOnClickListener(new View.OnClickListener(){
            //TODO logika tego czegos
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Wcislem guzik", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFromDataBase(){
        databaseHelper.tripInsert(new Trip(1,login,20.0f,21.0f,22.0f,11,"6h30m","11-11-2016","hahaniewiemjak"));
        ArrayList<Trip> tripsToAdd = databaseHelper.getTrip(login);
        if(tripsToAdd != null){
            trips.addAll(tripsToAdd);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        populateDetailsView(trips.get(position));
        ViewAnimations.expand(detailsView, view.getHeight());
        isDetailsViewVisible = true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        Trip trip = trips.get(position);
        new AlertDialog.Builder(getContext())
                .setTitle("Usuwanie")
                .setMessage("Czy napewno chcesz usunąć tę jazdę?")
                .setPositiveButton("tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        trips.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .show();
        return true;
    }

    //TODO: wrocic do tabview przy kliknieciu
    @Override
    public void onBackPressedCallBack() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(isDetailsViewVisible) {
            ViewAnimations.collapse(detailsView);
            isDetailsViewVisible = false;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        MenuItem deleteDataBase = menu.add("Usuń wszytkie wycieczki");
        deleteDataBase.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getTitle().toString().equalsIgnoreCase("Usuń wszystkie wycieczki")){
            trips.clear();
            adapter.notifyDataSetChanged();
            databaseHelper.deleteDatabase();
            return true;
        }
        return false;
    }
}
