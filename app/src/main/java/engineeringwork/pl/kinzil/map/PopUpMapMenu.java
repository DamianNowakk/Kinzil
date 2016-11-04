package engineeringwork.pl.kinzil.map;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;
import engineeringwork.pl.kinzil.activity.UserAddActivity;
import engineeringwork.pl.kinzil.containers.DatabaseHelper;
import engineeringwork.pl.kinzil.containers.MapSetting;

public class PopUpMapMenu {

    private DatabaseHelper db;
    private Activity activity;

    private Switch trackingSwitch;
    private Switch satelliteSwitch;
    private Spinner raidTypeSpinner;
    private Button saveButton;
    private Button cancelButton;
    private SeekBar zoomSeekBar;
    private TextView zoomTextView;



    private MapSetting mapSetting;

    public MapSetting getMapSetting() {
        return mapSetting;
    }

    private PopupWindow popupWindow;

    public PopupWindow getPopUpWindow() {
        return popupWindow;
    }

    public PopUpMapMenu(Activity activity)
    {
        this.activity = activity;
        db = DatabaseHelper.getInstance(activity);
        update();

    }



    private void update()
    {
        mapSetting = db.getFirstLoginMapSettings(((MainActivity)activity).getLogin());
        if(mapSetting == null)
            mapSetting = new MapSetting(((MainActivity)activity).getLogin(), false, false, 16, 0);

        //TO DO - AFTER DATABASE
    }

    public void show()
    {
        View popupView = activity.getLayoutInflater().inflate(R.layout.map_popup, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        trackingSwitch = (Switch) popupView.findViewById(R.id.tracking_switch_popup);
        satelliteSwitch = (Switch) popupView.findViewById(R.id.satellite_switch_popup);
        raidTypeSpinner = (Spinner) popupView.findViewById(R.id.raid_type_spinner_popup);
        saveButton = (Button) popupView.findViewById(R.id.save_button_popup);
        cancelButton = (Button) popupView.findViewById(R.id.cancel_button_popup);
        zoomSeekBar = (SeekBar) popupView.findViewById(R.id.zoomSeekbar);
        zoomTextView = (TextView) popupView.findViewById(R.id.zoomTextView);

        setData();
        setZoomSeekBarandZoomTextView();
        setButton(popupWindow);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        //set transparent background
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.8f;
        wm.updateViewLayout(container, p);
    }

    private void setData()
    {
        trackingSwitch.setChecked(mapSetting.isTracking());
        satelliteSwitch.setChecked(mapSetting.isSatellite());
        raidTypeSpinner.setSelection(mapSetting.getType());
        zoomSeekBar.setProgress(mapSetting.getZoom());
        zoomTextView.setText(String.valueOf(mapSetting.getZoom()));
    }

    private void updateData()
    {
        mapSetting.setTracking(trackingSwitch.isChecked());
        mapSetting.setSatellite(satelliteSwitch.isChecked());
        mapSetting.setType(raidTypeSpinner.getSelectedItemPosition());
        mapSetting.setZoom(zoomSeekBar.getProgress());
        //save in database
        MapSetting tmp = db.getFirstLoginMapSettings(mapSetting.getLogin());
        if(tmp == null) {
            if (db.mapSettingInsert(mapSetting))
                Toast.makeText(activity, "Data save", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, "Error, data not save", Toast.LENGTH_LONG).show();
        }
        else {
            mapSetting.setId(tmp.getId());
            if (db.mapSettingUpdate(mapSetting))
                Toast.makeText(activity, "Data update", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, "Error, data not update", Toast.LENGTH_LONG).show();
        }
    }

    private void setButton(final PopupWindow popupWindow) {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                popupWindow.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void setZoomSeekBarandZoomTextView()
    {
        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoomTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
