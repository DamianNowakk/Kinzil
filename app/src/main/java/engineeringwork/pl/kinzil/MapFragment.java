package engineeringwork.pl.kinzil;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.Switch;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener  {
    View view;
    private GoogleMap mMap;
    private Button button;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.map_settings){
            showPopup();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopup() {
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.map_popup, null);
        PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        Switch tv = (Switch) popupView.findViewById(R.id.switch1);
        tv.setChecked(true);
        // Initialize more widgets from `popup_layout.xml`
        //....
        popupWindow.setWidth(400);
        popupWindow.setHeight(180);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.8f;
        wm.updateViewLayout(container, p);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.replace(R.id.mapView, fragment);
        transaction.commit();
        fragment.getMapAsync(this);

        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);

        return view;
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

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16);
                mMap.animateCamera(cu);
            }
        });
    }

    @Override
    public void onClick(View v) {
        mMap.setOnMyLocationChangeListener(null);
    }
}
