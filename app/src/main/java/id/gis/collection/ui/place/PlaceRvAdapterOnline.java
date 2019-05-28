package id.gis.collection.ui.place;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.model.Survey.Result;
import id.gis.collection.ui.addplace.AddPlaceActivity;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dell on 22/07/18.
 */

public class PlaceRvAdapterOnline extends RecyclerView.Adapter {

    private static final String TAG = "PLACE RV ";
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;

    private List<Result> mSurveyData;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ImgDetailAdapter imgDetailAdapter;
    private int totalItemCount, lastVisibleItem;
    private int visibleThreshold = 5;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;

    public PlaceRvAdapterOnline(List<Result> mSurveyData, PlaceActivity mContext,  RecyclerView recyclerView) {
        this.mSurveyData = mSurveyData;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if (onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public int getItemViewType(int position) {
        return mSurveyData.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if ( viewType == VIEW_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.place_item, parent, false);
            vh = new PlaceRvAdapterOnline.PlaceHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate( R.layout.progress_bar, parent, false);
            vh =  new ProgressViewHolder(view);
        }
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaceHolder) {
            final Result surveyData = mSurveyData.get(position);
            List<String> images = new ArrayList<>();
            List<String> videos = new ArrayList<>();
            List<String> files = new ArrayList<>();

            if(!surveyData.getImages().equals("")){
                String[] img = surveyData.getImages().split(",");
                if (img.length > 1) {
                    for (int i = 0; i < img.length; i++) {
                        images.add(i, img[i]);
                    }
                } else {
                    images.add(0, surveyData.getImages());
                }
            }

            if(!surveyData.getVideos().equals("")){
                String[] video = surveyData.getVideos().split(",");
                if (video.length > 1) {
                    for (int i = 0; i < video.length; i++) {
                        videos.add(i, video[i]);
                    }
                } else {
                    videos.add(0, surveyData.getVideos());
                }
            }

            if(!surveyData.getFiles().equals("")){
                String[] file = surveyData.getFiles().split(",");
                if (file.length > 1) {
                    for (int i = 0; i < file.length; i++) {
                        files.add(i, file[i]);
                    }
                } else {
                    files.add(0, surveyData.getFiles());
                }
            }

            ((PlaceHolder) holder).mRvDetailImg.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            ((PlaceHolder) holder).mRvDetailImg.setHasFixedSize(true);

            ((PlaceHolder) holder).mTvProject.setText(IqbalUtils.capitalize(surveyData.getProject() != null ? surveyData.getProject() : ""));
            ((PlaceHolder) holder).mTvSurvey.setText(IqbalUtils.capitalize(surveyData.getNamaSurvei() != null ? surveyData.getNamaSurvei() : ""));
            ((PlaceHolder) holder).mTvJenis.setText(IqbalUtils.capitalize(surveyData.getJenis()) + ", " + IqbalUtils.capitalize(surveyData.getObjek()));
            ((PlaceHolder) holder).mTvKeterangan.setText(surveyData.getKeterangan());
            ((PlaceHolder) holder).mTvAddress.setText(surveyData.getAlamat());
            ((PlaceHolder) holder).mBtnExpand.setOnClickListener(v -> {
                if (((PlaceHolder) holder).expandableLayout.isShown()) {
                    ((PlaceHolder) holder).expandableLayout.setVisibility(View.GONE);

                } else {
                    ((PlaceHolder) holder).expandableLayout.setVisibility(View.VISIBLE);
                }
            });
            ((PlaceHolder) holder).mTvTime.setText(surveyData.getCreatedAt());
            ((PlaceHolder) holder).mPopupMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(mContext, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.item_action, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.hapusItem:
                            new AlertDialog.Builder(mContext).setTitle("Konfirmasi")
                                .setMessage("Yakin menghapus survey " + IqbalUtils.capitalize(surveyData.getNamaSurvei()) + " ?")
                                .setPositiveButton("Iya", (dialog, which) -> {
                                    mSurveyData.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mSurveyData.size());
                                    deleteSurvey(surveyData.getIdSurvei());
                                })
                                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                            break;
                        case R.id.editItem:
                            Intent i = new Intent(mContext, AddPlaceActivity.class);
                            i.putExtra("edit", true);
                            i.putExtra("idSurvey", surveyData.getIdSurvei());
                            i.putExtra("nama", surveyData.getNamaSurvei());
                            i.putExtra("alamat", surveyData.getAlamat());
                            i.putExtra("deskripsi", surveyData.getKeterangan());
                            i.putExtra("desa", surveyData.getDesa());
                            i.putExtra("kota", surveyData.getKota());
                            i.putExtra("provinsi", surveyData.getProvinsi());
                            i.putExtra("kecamatan", surveyData.getKec());
                            i.putExtra("geom", surveyData.getLatitude() + surveyData.getLongitude());
                            i.putExtra("lat", surveyData.getLatitude());
                            i.putExtra("lon", surveyData.getLongitude());
                            i.putExtra("project", surveyData.getProject());
                            i.putExtra("surveiWilayah", surveyData.getWilSurvei());
                            i.putExtra("jenis", surveyData.getIdJenis());
                            i.putExtra("objek", surveyData.getIdObjek());
                            i.putExtra("jenis_bangunan", surveyData.getJnsBangun());
                            i.putExtra("jml_lantai", surveyData.getJumLantai());
                            i.putExtra("audio", surveyData.getRecords());
                            i.putStringArrayListExtra("images", (ArrayList<String>) images);
                            i.putStringArrayListExtra("videos", (ArrayList<String>) videos);
                            i.putStringArrayListExtra("files", (ArrayList<String>) files);
                            mContext.startActivity(i);
                            break;

                    }
                    return false;
                });
                popup.show();
            });

            imgDetailAdapter = new ImgDetailAdapter(images, mContext);
            ((PlaceHolder) holder).mRvDetailImg.setAdapter(imgDetailAdapter);
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    private void deleteSurvey(String idSurvei) {
        String api_key = AppConstant.API_KEY;
        Call<ResponseSignUp> call = RestClient.get().deleteSurvey(api_key, idSurvei);
        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                if (response.isSuccessful()){
                    Toast.makeText(mContext, "Survey Dihapus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                Toast.makeText(mContext, "GAGAL", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSurveyData.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.pbLoadOffset);
        }
    }


    public static class PlaceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_project)
        TextView mTvProject;

        @BindView(R.id.tv_survey)
        TextView mTvSurvey;

        @BindView(R.id.tv_keterangan)
        TextView mTvKeterangan;

        @BindView(R.id.tv_address)
        TextView mTvAddress;

        @BindView(R.id.tv_jenis)
        TextView mTvJenis;

        @BindView(R.id.tv_time)
        TextView mTvTime;

        @BindView(R.id.btnExpand)
        TextView mBtnExpand;

        @BindView(R.id.expandableLayout)
        LinearLayout expandableLayout;

        @BindView(R.id.detailImg)
        RecyclerView mRvDetailImg;

        @BindView(R.id.popupMenu)
        ImageView mPopupMenu;

        public PlaceHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void updateRecycle(List<Result> newList){
        mSurveyData = new ArrayList<>();
        mSurveyData.addAll(newList);
        notifyDataSetChanged();
    }
}
