package engineeringwork.pl.kinzil.map;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.PopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    View view;
    private GoogleMap mMap;
    private PopUpMapMenu popUpMapMenu;
    private ArrayList<Location> locationArrayListMain = new ArrayList<>();
    private Polyline polylineFinalMain;
    private ArrayList<Location> locationArrayListSecondary = null ;
    private Polyline polylineFinalSecondary;

    public String getMapMain()
    {
        JSONObject map = new JSONObject();
        try {
            JSONObject mainObj = new JSONObject();
            JSONArray locationArray = new JSONArray();
            mainObj.put("Size", locationArrayListMain.size());
            for(int i = 0; i< locationArrayListMain.size(); i++)
            {
                JSONObject location = new JSONObject();
                location.put("Longitude", locationArrayListMain.get(i).getLongitude());
                location.put("Latitude", locationArrayListMain.get(i).getLatitude());
                locationArray.put(location);
            }
            mainObj.put("Location", locationArray);
            return mainObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMapSecondary(String map) throws JSONException {
        locationArrayListSecondary = new ArrayList<>();
        JSONObject obj = new JSONObject(map);
        int size =  Integer.parseInt(obj.getString("Size"));
        JSONArray location = obj.getJSONArray("Location");
        for(int i = 0; i < size; i++)
        {
            Location loc = new Location("dummyprovider");
            String longitude = location.getJSONObject(i).getString("Longitude");
            String latitude = location.getJSONObject(i).getString("Latitude");
            loc.setLongitude( Double.parseDouble(longitude));
            loc.setLatitude( Double.parseDouble(latitude));
            locationArrayListSecondary.add(loc);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        popUpMapMenu = new PopUpMapMenu(getActivity());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.map_settings){
            popUpMapMenu.show();
            popUpMapMenu.getPopUpWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setMap();
                }
            });
        }
        return super.onOptionsItemSelected(item);
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
        setMap();

    }

    private void setMap()
    {
        tracking();
        satellite(popUpMapMenu.getMapSetting().isSatellite());
        showRoute(popUpMapMenu.getMapSetting().isShowRoute());
        showSecondaryRoute(popUpMapMenu.getMapSetting().isShowSecondaryRoute());
    }

    private void  showRoute(Boolean isShow)
    {
        if(isShow)
            drawPrimaryLinePath();
        else if(polylineFinalMain != null)
            polylineFinalMain.remove();
    }

    private void  showSecondaryRoute(Boolean isShow)
    {
        if(isShow)
            drawSecondaryLinePath();
        else if(polylineFinalSecondary != null)
            polylineFinalSecondary.remove();
    }

    private void tracking()
    {
        if(popUpMapMenu.getMapSetting().isTracking())
            mMap.getUiSettings().setZoomControlsEnabled(false);
        else
            mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(popUpMapMenu.getMapSetting().isTracking()) {
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), popUpMapMenu.getMapSetting().getZoom());
                    mMap.animateCamera(cu);
                }

                if(locationArrayListMain.size() == 0) {
                    locationArrayListMain.add(location);
                }
                else {
                    if(checkLocation(locationArrayListMain.get(locationArrayListMain.size() - 1), location)) {
                        locationArrayListMain.add(location);
                        if(popUpMapMenu.getMapSetting().isShowRoute()) {
                            drawPrimaryLinePath();
                        }
                    }
                }
            }
        });
    }
    private boolean checkLocation(Location a, Location b)
    {
        float result = a.distanceTo(b);
        return result > 5;
    }
    private synchronized void drawPrimaryLinePath()
    {
        if ( mMap == null )
            return;
        if ( locationArrayListMain == null )
            return;
        if ( locationArrayListMain.size() < 2 )
            return;
        if(polylineFinalMain != null)
            polylineFinalMain.remove();
        PolylineOptions options = new PolylineOptions();
        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );
        for ( Location locRecorded : locationArrayListMain )
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }
        polylineFinalMain = mMap.addPolyline( options );
    }

    private void drawSecondaryLinePath()
    {
        if ( mMap == null )
            return;
        if ( locationArrayListSecondary == null )
            return;
        if ( locationArrayListSecondary.size() < 2 )
            return;
        if(polylineFinalSecondary != null)
            polylineFinalSecondary.remove();
        PolylineOptions options = new PolylineOptions();
        options.color( Color.parseColor( "#999999" ) );
        options.width( 5 );
        options.visible( true );
        for ( Location locRecorded : locationArrayListSecondary )
        {
            options.add( new LatLng( locRecorded.getLatitude(), locRecorded.getLongitude()));
        }
        polylineFinalSecondary = mMap.addPolyline( options );
    }

    private void satellite(boolean isSatellite)
    {
        if(isSatellite)
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
