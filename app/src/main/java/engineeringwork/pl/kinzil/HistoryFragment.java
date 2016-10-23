package engineeringwork.pl.kinzil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import engineeringwork.pl.Controllers.DatabaseHandler;
import engineeringwork.pl.Controllers.Ride;

public class HistoryFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);

        Log.d("Number of rows in db:", "halo");
        DatabaseHandler dbhandler = new DatabaseHandler(view.getContext());

        Log.d("Number of rows in db:", String.valueOf(dbhandler.getRidesCount()));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2014);
        calendar.set(Calendar.MONTH,Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH,12);
        dbhandler.addRide(new Ride("abc", calendar.getTime()));
        dbhandler.addRide(new Ride("abc2", calendar.getTime()));
        Log.d("Number of rows in db:", String.valueOf(dbhandler.getRidesCount()));

        return view;
    }
}
