package engineeringwork.pl.kinzil.counter;

public class CscAnalyser {
    private double mWheelSize = 0.72d; // Default 700mm

    private SpeedCadenceMeasurement prevMeasurement;
    private double mSpeed;
    private double speedKmH;
    private double newDistance;
    private double mCadence;

    public void addData(SpeedCadenceMeasurement currMeasurement) {
        if(currMeasurement == null) return;

        if(prevMeasurement != null) {
            double wheelTimeDiff = currMeasurement.lastWheelEventTime - prevMeasurement.lastWheelEventTime;
            if (wheelTimeDiff > 0){
                long revs = currMeasurement.cumulativeWheelRevolutions - prevMeasurement.cumulativeWheelRevolutions;
                mSpeed = revs * Math.PI * mWheelSize / wheelTimeDiff;
                speedKmH = mSpeed * 3.6;
                newDistance = revs * Math.PI * mWheelSize;
            }else {
                mSpeed = speedKmH = newDistance = 0;
            }

            double crankTimeDiff = currMeasurement.lastCrankEventTime - prevMeasurement.lastCrankEventTime;
            if (crankTimeDiff > 0){
                long revs = currMeasurement.cumulativeCrankRevolutions - prevMeasurement.cumulativeCrankRevolutions;

                mCadence = revs * 60d / crankTimeDiff;
            }
            else {
                mCadence = 0;
            }
        }

        prevMeasurement = currMeasurement;
    }

    public void setWheelSize(double size){
        mWheelSize = size;
    }

    public double getSpeed(){
        return mSpeed;
    }

    public double getSpeedKmH(){
        return speedKmH;
    }

    public double getCadence(){
        return mCadence;
    }

    public double getNewDistance () { return newDistance; }
}
