package id.gis.collection.ui.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.gis.collection.R;
import id.gis.collection.data.entity.PlaceEntity;
import id.gis.collection.utils.IqbalUtils;

import static java.lang.Double.parseDouble;

public class MainVpAdapter extends RecyclerView.Adapter<MainVpAdapter.ViewHolder> {

    private List<PlaceEntity> mList = new ArrayList<>();
    SlidingUpPanelLayout slidingUpPanelLayout;
    private GoogleMap mMap;

    public MainVpAdapter(SlidingUpPanelLayout mSuplSurveys, GoogleMap map) {
        this.slidingUpPanelLayout = mSuplSurveys;
        this.mMap = map;
    }

    public void setPlaceList(List<PlaceEntity> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MainVpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_fragment, parent, false);
        return new MainVpAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainVpAdapter.ViewHolder holder, int position) {
        PlaceEntity mData = mList.get(position);
        holder.mTvSurvey.setText(mData.getName());
        holder.mTvAddress.setText(IqbalUtils.alamat(mData.getAddress()));
        holder.mTvTgl.setText(IqbalUtils.convertTimeDisplay(mData.getCreatedAt()));
        Double lat = Double.valueOf(mData.getLat());
        Double lng = Double.valueOf(mData.getLng());
        holder.mLItem.setOnClickListener(v -> {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17);
            mMap.animateCamera(cameraUpdate);
        });
        holder.mTvProject.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lItemFragment)
        LinearLayout mLItem;

        @BindView(R.id.tv_survey)
        TextView mTvSurvey;

        @BindView(R.id.tv_project)
        TextView mTvProject;

        @BindView(R.id.tv_tgl)
        TextView mTvTgl;

        @BindView(R.id.tv_address)
        TextView mTvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
