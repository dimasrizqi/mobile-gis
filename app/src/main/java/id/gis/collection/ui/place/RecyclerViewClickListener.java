package id.gis.collection.ui.place;

import android.view.View;

import java.util.List;

import id.gis.collection.data.entity.PlaceEntity;

public interface RecyclerViewClickListener {

    void onItemClick(PlaceEntity placeEntity, View v, List<PlaceEntity> mList, int adapterPosition);
}