package id.gis.collection.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import id.gis.collection.data.dao.PlaceDao;
import id.gis.collection.data.entity.PlaceEntity;
import id.gis.collection.repository.PlaceRepository;
import id.gis.collection.repository.PlaceRepositoryImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by arieridwan on 13/05/18.
 */

public class MainViewModel extends ViewModel {

    PlaceRepository placeRepository;

    public MainViewModel() {
    }

    public void initViewModel(PlaceDao placeDao) {
        placeRepository = new PlaceRepositoryImpl(placeDao);
    }

    public LiveData<List<PlaceEntity>> getPlaces() {
        return placeRepository.getPlaces();
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
