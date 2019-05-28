package id.gis.collection.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "place")
public class PlaceEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String address;
    private String geom;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private float lat;
    private float lng;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public PlaceEntity(int id, String name, String description, String address, float lat, float lng, String geom, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.geom = geom;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.createdAt = createdAt;
    }

    public PlaceEntity(PlaceEntity placeEntity) {
        this.id = placeEntity.id;
        this.name = placeEntity.name;
        this.description = placeEntity.description;
        this.geom = placeEntity.geom;
        this.address = placeEntity.address;
        this.lat = placeEntity.lat;
        this.lng = placeEntity.lng;
        this.createdAt = placeEntity.createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getGeom() {
        return geom;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

}
