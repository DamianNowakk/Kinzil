package engineeringwork.pl.kinzil;


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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ArrayAdapter;

public class PopUpMapMenu {

    private Activity activity;

    private Switch trackingSwitch;
    private Switch satelliteSwitch;
    private Spinner raidTypeSpinner;
    private Button saveButton;
    private Button cancelButton;


    private boolean isTracking;
    private boolean isSatellite;
    private int idRaidType;

    public boolean getIsTracking() {
        return isTracking;
    }

    public boolean getIsSatellite() {
        return isSatellite;
    }

    public int getIdRaidType() {
        return idRaidType;
    }

    public PopUpMapMenu(Activity activity)
    {
        this.activity = activity;
        updatedata();
    }

    private void updatedata()
    {
        isTracking = false;
        isSatellite = false;
        idRaidType = 0;
        //TO DO - AFTER DATABASE
    }

    public void show()
    {
        View popupView = activity.getLayoutInflater().inflate(R.layout.map_popup, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        trackingSwitch = (Switch) popupView.findViewById(R.id.tracking_switch_popup);
        satelliteSwitch = (Switch) popupView.findViewById(R.id.satellite_switch_popup);
        raidTypeSpinner = (Spinner) popupView.findViewById(R.id.raid_type_spinner_popup);
        saveButton = (Button) popupView.findViewById(R.id.save_button_popup);
        cancelButton = (Button) popupView.findViewById(R.id.cancel_button_popup);

        setData(popupWindow);

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

    private void setData(final PopupWindow popupWindow)
    {
        trackingSwitch.setChecked(isTracking);
        satelliteSwitch.setChecked(isSatellite);
        raidTypeSpinner.setSelection(idRaidType);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //TO DO - AFTER DATABASE
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

}
