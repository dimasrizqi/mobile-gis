package id.gis.collection.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import id.gis.collection.data.entity.PlaceEntity;
import io.reactivex.Completable;

/**
 * Created by arieridwan on 13/05/18.
 */

public interface PlaceRepository {

    Completable addPlace(PlaceEntity place);

    LiveData<List<PlaceEntity>> getPlaces();

    Completable deletePlace(PlaceEntity place);
}
