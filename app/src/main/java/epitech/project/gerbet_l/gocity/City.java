package epitech.project.gerbet_l.gocity;

import java.io.Serializable;

public class City implements Serializable {
    private String id;
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    public User creator;

    /*
    ** CONSTRUCTORS
    */
    public City() {
        this.id = null;
        this.title = null;
        this.address = null;
        this.latitude = 0;
        this.longitude = 0;
        this.creator = null;
    }

    public City(String cityId, String name, String address ,double lat, double lng, User creator){
        System.out.println("new City created");
        id = cityId;
        title = name;
        this.address = address;
        latitude = lat;
        longitude = lng;
        this.creator = creator;
    }

    /*
    ** GET FUNCTIONS
    */
    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public User getCreator() {
        return creator;
    }


    /*
    ** SET FUNCTIONS
    */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
