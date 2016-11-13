package engineeringwork.pl.kinzil.containers;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public final class ViewAnimations {

    public static void expand(final View detailsView, final int targetHeight) {
        detailsView.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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

    public static void collapse(final View detailsView) {
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

}
