package id.gis.collection.ui.place;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.morphingbutton.impl.IndeterminateProgressButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.gis.collection.R;
import id.gis.collection.data.entity.PlaceEntity;
import id.gis.collection.ui.addplace.AddPlaceActivity;
import id.gis.collection.utils.IqbalUtils;

public class PlaceRvAdapter extends RecyclerView.Adapter<PlaceRvAdapter.ViewHolder> {

    private List<PlaceEntity> mList = new ArrayList<>();
    private RecyclerViewClickListener mListener;

    PlaceRvAdapter(RecyclerViewClickListener listener) {
        this.mListener = listener;
    }

    public void setPlaceList(List<PlaceEntity> list) {
        this.mList = list;
    }

    @Override
    public PlaceRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceEntity mData = mList.get(position);
        holder.bind(mData, mListener, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_survey)
        TextView mTvName;

        @BindView(R.id.tv_address)
        TextView mTvAddress;

        @BindView(R.id.tv_keterangan)
        TextView mTvKeterangan;

        @BindView(R.id.tv_project)
        TextView mTvProject;

        @BindView(R.id.llJenis)
        LinearLayout mllJenis;

        @BindView(R.id.expandableLayout)
        LinearLayout mExLayout;

        @BindView(R.id.btnExpand)
        TextView mBtnExpand;

        @BindView(R.id.popupMenu)
        ImageView mPopupMenu;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final PlaceEntity item, final RecyclerViewClickListener listener, int position) {
            mTvName.setText(item.getName());
            mTvAddress.setText(item.getAddress());
            mTvKeterangan.setText(item.getDescription());
            mTvProject.setVisibility(View.GONE);
            mllJenis.setVisibility(View.GONE);
            mExLayout.setVisibility(View.GONE);
            mBtnExpand.setVisibility(View.GONE);
            mPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, v, mList, position);
                }
            });
        }
    }
}
