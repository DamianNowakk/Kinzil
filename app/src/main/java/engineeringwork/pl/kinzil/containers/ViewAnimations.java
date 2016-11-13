package engineeringwork.pl.kinzil.containers;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public final class ViewAnimations {

    public static void expand(final View viewToExpand, final int targetHeight) {
        viewToExpand.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        viewToExpand.getLayoutParams().height = 1;
        viewToExpand.setVisibility(View.VISIBLE);
        Animation a = new Animation(){

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                viewToExpand.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.MATCH_PARENT
                        : (int)(targetHeight * interpolatedTime);
                viewToExpand.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(targetHeight / viewToExpand.getContext().getResources().getDisplayMetrics().density));
        viewToExpand.startAnimation(a);
    }

    public static void collapse(final View viewToCollapse) {
        final int initialHeight = viewToCollapse.getMeasuredHeight();

        Animation a = new Animation(){

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    viewToCollapse.setVisibility(View.GONE);
                }else{
                    viewToCollapse.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    viewToCollapse.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / viewToCollapse.getContext().getResources().getDisplayMetrics().density));
        viewToCollapse.startAnimation(a);
    }

}
