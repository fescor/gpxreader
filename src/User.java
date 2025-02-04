public class User {
    String id ;
    int Activities = 0;
    double AvgTime = 0;
    double AvgDistance = 0;
    double AvgElevetion = 0;
    double totallTime = 0;
    double totallDistance = 0;
    double totallElevation = 0;
    

    
    public double getTotallTime() {
        return totallTime;
    }
    public void setTotallTime(double totallTime) {
        this.totallTime = totallTime;
    }
    public double getTotallDistance() {
        return totallDistance;
    }
    public void setTotallDistance(double totallDistance) {
        this.totallDistance = totallDistance;
    }
    public double getTotallElevation() {
        return totallElevation;
    }
    public void setTotallElevation(double totallElevation) {
        this.totallElevation = totallElevation;
    }
    public String getid(){
        return id;
    }
    public void setid(String id){
        this.id = id;
    }
    public int getActivities(){
        return Activities;
    } 
    public void setActivities(int Activities){
        this.Activities = Activities;
    }
    public double getAvgTime(){
        return AvgTime;
    } 
    public double getAvgDistance(){
        return AvgDistance;
    }
    public double getAvgElevetion(){
        return AvgElevetion;
    }
    public void setAvgTime(double AvgTime){
        this.AvgTime = AvgTime;
    }
    public void setAvgDistance(double AvgDistance){
        this.AvgDistance = AvgDistance;
    }
    public void setAvgElevetion(double AvgElevetion){
        this.AvgElevetion = AvgElevetion;
    }

}
