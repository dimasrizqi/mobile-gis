package id.gis.collection.ui.addplace;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import id.gis.collection.data.dao.PlaceDao;
import id.gis.collection.data.entity.PlaceEntity;
import id.gis.collection.repository.PlaceRepository;
import id.gis.collection.repository.PlaceRepositoryImpl;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by arieridwan on 13/05/18.
 */

public class AddPlaceViewModel extends ViewModel {

    PlaceRepository placeRepository;

    public AddPlaceViewModel() {
    }

    public void initViewModel(PlaceDao placeDao) {
        placeRepository = new PlaceRepositoryImpl(placeDao);
    }

    @SuppressLint({"CheckResult", "RxLeakedSubscription"})
    MutableLiveData<Boolean> addPlace(String name, String description, String address, float lat, float lng, String geom, String created) {

        MutableLiveData<Boolean> status = new MutableLiveData<>();
        PlaceEntity placeEntity = new PlaceEntity(0, name, description, address, lat, lng, geom, created);

        placeRepository.addPlace(placeEntity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->  status.setValue(true), throwable -> status.setValue(false));

        return status;
    }

    @SuppressLint({"CheckResult", "RxLeakedSubscription"})
    MutableLiveData<Boolean> deletePlace(PlaceEntity placeEntity) {

        MutableLiveData<Boolean> status = new MutableLiveData<>();

        placeRepository.deletePlace(placeEntity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->  status.setValue(true), throwable -> status.setValue(false));

        return status;
    }

}

