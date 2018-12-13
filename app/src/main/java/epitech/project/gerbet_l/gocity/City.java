package epitech.project.gerbet_l.gocity;

import java.io.Serializable;

public class City implements Serializable {
    private String id;
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    public User creator;
    private String pictureId;
    private String description;

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
        this.pictureId = null;
        this.description = null;
    }

    public City(String cityId, String name, String address ,double lat, double lng, User creator, String pictureId, String description){
        System.out.println("new City created");
        id = cityId;
        title = name;
        this.address = address;
        latitude = lat;
        longitude = lng;
        this.creator = creator;
        this.pictureId = pictureId;
        this.description = description;
    }

    /*
    ** GET FUNCTIONS
    */

    public String getId() {
        return id;
    }

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

    public String getPictureId() {
        return pictureId;
    }

    public String getDescription() {
        return description;
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

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
