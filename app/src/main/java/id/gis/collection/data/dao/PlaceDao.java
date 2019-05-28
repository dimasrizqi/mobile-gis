package id.gis.collection.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import id.gis.collection.data.entity.PlaceEntity;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(List<PlaceEntity> places);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPlace(PlaceEntity place);

    @Query("select * from place")
    LiveData<List<PlaceEntity>> getPlaces();

    @Query("select * from place where id = :placeId")
    LiveData<PlaceEntity> getPlaces(int placeId);

    @Query("select * from place where id = :placeId")
    PlaceEntity loadPlaceSync(int placeId);

    @Update(onConflict = REPLACE)
    void updatePlace(PlaceEntity place);

    @Delete
    void deletePlace(PlaceEntity place);

}
