import java.io.Serializable;

public class Result implements Serializable{
    public  double distance;
    public double speed;
    public double elevation;
    public long time;
    public String key;
    
    
    public Result(double distance, double speed, double elevation,long time, String key ){
        this.distance = distance;
        this.speed = speed;
        this. elevation = elevation;
        this.time = time;
        this.key = key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return key;
    }

    public double getDistance() {
        return distance;
    }


    public void setDistance(double distance) {
        this.distance = distance;
    }


    public double getSpeed() {
        return speed;
    }


    public void setSpeed(double speed) {
        this.speed = speed;
    }


    public double getElevation() {
        return elevation;
    }


    public void setElevation(double elevation) {
        this.elevation = elevation;
    }


    public long getTime() {
        return time;
    }


    public void setTime(long time) {
        this.time = time;
    }
    
}
