package id.gis.collection.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import id.gis.collection.data.converter.DateConverter;
import id.gis.collection.data.dao.PlaceDao;
import id.gis.collection.data.entity.PlaceEntity;

@Database(entities = {PlaceEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class PlaceDatabase extends RoomDatabase {

    private static volatile PlaceDatabase INSTANCE;

    public abstract PlaceDao placeDao();

}
