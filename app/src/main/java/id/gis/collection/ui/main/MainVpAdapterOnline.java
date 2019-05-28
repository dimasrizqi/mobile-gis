package id.gis.collection.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.gis.collection.R;
import id.gis.collection.model.Survey.Result;
import id.gis.collection.ui.place.PlaceRvAdapterOnline;
import id.gis.collection.utils.IqbalUtils;

import static java.lang.Double.parseDouble;

/**
 * Created by dell on 22/07/18.
 */

public class MainVpAdapterOnline extends RecyclerView.Adapter<MainVpAdapterOnline.ViewHolder> {

    private Context mContext;
    private GoogleMap mMap;
    private List<Result> mSurveyData;
    private SlidingUpPanelLayout mSuplSurveys;

    public MainVpAdapterOnline(List<Result> mSurveyData, Context mContext, GoogleMap mMap, SlidingUpPanelLayout mSuplSurveys){
        this.mContext = mContext;
        this.mSurveyData = mSurveyData;
        this.mMap = mMap;
        this.mSuplSurveys = mSuplSurveys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_fragment, parent, false);
        return new MainVpAdapterOnline.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTvSurvey.setText(mSurveyData.get(position).getNamaSurvei());
        holder.mTvProject.setText(mSurveyData.get(position).getProject());
        holder.mTvAddress.setText(IqbalUtils.alamat(mSurveyData.get(position).getAlamat()));
        holder.mTvTgl.setText(IqbalUtils.convertTimeDisplay(mSurveyData.get(position).getCreatedAt()));
        holder.mLItem.setOnClickListener(v -> {
            mSuplSurveys.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(parseDouble(mSurveyData.get(position).getLatitude()),
                    parseDouble(mSurveyData.get(position).getLongitude())), 17);
            mMap.animateCamera(cameraUpdate);
        });
    }

    @Override
    public int getItemCount() {
        return mSurveyData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
