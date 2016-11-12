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
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.activity.MainActivity;
import engineeringwork.pl.kinzil.containers.Trip;
import engineeringwork.pl.kinzil.containers.TripArrayAdapter;

public class HistoryFragment extends ListFragment implements AdapterView.OnItemClickListener, MainActivity.Callbacks {
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
        try {
            expand();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void expand() throws InterruptedException {
        final View v = view.findViewById(R.id.details);
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation(){

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                //v.getLayoutParams().height = targetHeight * (int)interpolatedTime;
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.MATCH_PARENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse() {
        final View v = view.findViewById(R.id.details);
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation(){

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    public void onBackPressedCallBack() {
        Log.d("Kliklem","Tyl");
        collapse();
    }
}
