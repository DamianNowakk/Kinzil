package engineeringwork.pl.kinzil.counter;

import com.movisens.smartgattlib.GattByteBuffer;

import engineeringwork.pl.kinzil.activity.MainActivity;

public class CscParser {
    public Long currCumulativeWheelRevolutions, prevCumulativeWheelRevolutions;
    public float currLastWheelEventTime, prevLastWheelEventTime;
    private double speed, speedKmH, newDistance;
    private int counter = 0;
    private boolean hasPreviousData = false;

    public void getData(byte[] data) {
        GattByteBuffer bb = GattByteBuffer.wrap(data);

        int flags = bb.getInt8();

        if(hasWheelData(flags)){
            currCumulativeWheelRevolutions = bb.getUint32();
            currLastWheelEventTime = (float)bb.getUint16() / 1024.0F;
        }
        else {
            currCumulativeWheelRevolutions = 0L;
            currLastWheelEventTime = 0;
        }
    }

    public void processData() {
        if(hasPreviousData == true) {
            double wheelTimeDiff = currLastWheelEventTime - prevLastWheelEventTime;
            if (wheelTimeDiff > 0){
                double wheelSize =  MainActivity.getmWheelSize() / 1000.0;
                long revs = currCumulativeWheelRevolutions - prevCumulativeWheelRevolutions;
                speed = revs * wheelSize / wheelTimeDiff;
                speedKmH = speed * 3.6;
                newDistance = revs * wheelSize;
            }
            else {
                if(counter == 4) {
                    counter = 0;
                    speed = speedKmH = newDistance = 0;
                }
                else
                {
                    newDistance = 0;
                    speed = speed/2;
                    speedKmH = speed * 3.6;
                    counter++;
                }
            }
        }

        prevCumulativeWheelRevolutions = currCumulativeWheelRevolutions;
        prevLastWheelEventTime = currLastWheelEventTime;
        hasPreviousData = true;
    }

    private boolean hasWheelData(int flags) {
        return (flags & 1) != 0;
    }

    public void reset(){
        hasPreviousData = false;
        speed = 0;
        speedKmH = 0;
        newDistance = 0;
    }

    public double getSpeed(){
        return speed;
    }

    public double getSpeedKmH(){
        return speedKmH;
    }

    public double getNewDistance () { return newDistance; }
}
