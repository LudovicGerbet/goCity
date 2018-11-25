package epitech.project.gerbet_l.gocity;

public class City {
    private String id;
    private String title;
    private double latitude;
    private double longitude;

    public City() {}

    public City(String cityId, String name, double lat, double lng){
        System.out.println("new City created");
        id = cityId;
        title = name;
        latitude = lat;
        longitude = lng;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setTitle(String name) {
        title = name;
    }
}
