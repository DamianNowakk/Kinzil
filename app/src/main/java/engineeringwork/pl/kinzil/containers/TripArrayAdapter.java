package engineeringwork.pl.kinzil.containers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import engineeringwork.pl.kinzil.R;

public class TripArrayAdapter extends ArrayAdapter<Trip> {
    private Context context;
    private List<Trip> trips;

    public TripArrayAdapter(Context context, int resource, ArrayList<Trip> objects) {
        super(context, resource, objects);
        this.context = context;
        this.trips = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Trip trip = trips.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trip_item_layout, null);

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        TextView avgSpeed = (TextView) view.findViewById(R.id.avgSpeed);
        ImageView time = (ImageView) view.findViewById(R.id.image);

        date.setText(trip.getDate());
        distance.setText(String.valueOf(trip.getDistance()));
        avgSpeed.setText(String.valueOf(trip.getAvgSpeed()));
        //TODO: w zaleznosci od godziny ustawic odpowiedni obrazek
        time.setImageResource(R.mipmap.ic_time_icon);

        return view;
    }
}
