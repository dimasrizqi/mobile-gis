package id.gis.collection.model;

/**
 * Created by arieridwan on 13/05/18.
 */

public class Place {

    private int id;
    private String name;
    private String description;
    private String wide_area;
    private String address;
    private float lat;
    private float lng;

    public Place() {
    }

    public Place(int id, String name, String description, String wide_area, String address, float lat, float lng) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.wide_area = wide_area;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWide_area() {
        return wide_area;
    }

    public void setWide_area(String wide_area) {
        this.wide_area = wide_area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

}
