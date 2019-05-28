package id.gis.collection.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import id.gis.collection.data.dao.PlaceDao;
import id.gis.collection.data.entity.PlaceEntity;
import io.reactivex.Completable;

/**
 * Created by arieridwan on 13/05/18.
 */

public class PlaceRepositoryImpl implements PlaceRepository {

    PlaceDao placeDao;

    public PlaceRepositoryImpl(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    @Override
    public Completable addPlace(PlaceEntity place) {
        if (place == null){
            return Completable.error(new IllegalArgumentException("Place cannot be null"));
        }
        return Completable.fromAction(() -> placeDao.addPlace(place));
    }


    @Override
    public LiveData<List<PlaceEntity>> getPlaces() {
        return placeDao.getPlaces();
    }

    @Override
    public Completable deletePlace(PlaceEntity place) {
        if (place == null){
            return Completable.error(new IllegalArgumentException("Place cannot be null"));
        }
        return Completable.fromAction(() -> placeDao.deletePlace(place));
    }
}
