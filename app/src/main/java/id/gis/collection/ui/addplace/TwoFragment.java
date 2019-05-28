package id.gis.collection.ui.addplace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.iceteck.silicompressorr.SiliCompressor;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.api.ui.PickVideoActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.gis.collection.R;
import id.gis.collection.ui.login.LoginActivity;

/**
 * Created by dell on 20/08/18.
 */

public class TwoFragment extends Fragment {

    private static final String TAG = "TWO FRAGMENT";
    @BindView(R.id.labelVideo)
    RelativeLayout mLabelVideo;

    @BindView(R.id.rvVideo)
    RecyclerView rvVideo;

    @BindView(R.id.emptyVideo)
    LinearLayout mLLEmptyVideo;

    @BindView(R.id.add_video)
    RelativeLayout addVideo;

    private Unbinder unbinder;
    boolean isConverted = false;
    private DialogPlus dialogPlus;
    private String vid;
    private VideoPicker videoPicker;
    private CameraVideoPicker vidCameraPicker;
    private List<String> videos = new ArrayList<>();
    private VideoAdapter vidAdapter;
    private ProgressDialog progressDialog;

    public TwoFragment newInstance(List<String> intentVid) {
        // Required empty public constructor
        Bundle args = new Bundle();
        args.putStringArrayList("intentVid", (ArrayList<String>) intentVid);
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getStringArrayList("intentVid").size() > 0){
            this.videos = getArguments().getStringArrayList("intentVid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null && Objects.requireNonNull(getArguments().getStringArrayList("intentVid")).size() > 0){
            rvVideo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
            mLLEmptyVideo.setVisibility(View.INVISIBLE);
            vidAdapter = new VideoAdapter(videos, getActivity(), mLLEmptyVideo, addVideo);
            rvVideo.setAdapter(vidAdapter);
        }
        return view;
    }

    @OnClick(R.id.labelVideo)
    public void initVideo(){
        rvVideo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        String [] name = {"Record Video", "File Video"};
        int [] icon = {R.drawable.ic_videocam, R.drawable.ic_personal_video};
        dialogPlus= DialogPlus.newDialog(getContext())
            .setExpanded(false)
            .setAdapter(new GridAdapter(getContext(), name, icon))
            .setContentHolder(new GridHolder(2))
            .setGravity(Gravity.BOTTOM)
            .setCancelable(true)
            .setOnItemClickListener((dialog, item, view, position) -> {
                if (position == 0 ){
                    vidCameraPicker = new CameraVideoPicker(getActivity());
                    vidCameraPicker.setVideoPickerCallback(new VideoPickerCallback() {
                        @Override
                        public void onVideosChosen(List<ChosenVideo> list) {
                            for (int i = 0; i < list.size(); i ++){
                                videos.add(videos.size(), list.get(i).getOriginalPath());
                            }
                            mLLEmptyVideo.setVisibility(View.INVISIBLE);
                            vidAdapter = new VideoAdapter(videos, getActivity(), mLLEmptyVideo, addVideo);
                            rvVideo.setAdapter(vidAdapter);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                    vidCameraPicker.pickVideo();
                }else{
                    videoPicker = new VideoPicker(getActivity());
                    videoPicker.setVideoPickerCallback(new PickVideoActivity() {
                        @Override
                        public void onVideosChosen(List<ChosenVideo> list) {
                            for (int i = 0; i < list.size(); i ++){
//                                String outputFileAbsolutePath = Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(list.get(i).getOriginalPath()), null, null);
//                                String[] command = {"-y", "-i", list.get(i).getOriginalPath(),"-preset", "ultrafast", "-crf", "20", "-vcodec", "libx264", outputFileAbsolutePath};
//                                execFFmpegBinary(command, outputFileAbsolutePath);
//                                try {
//                                    String filePath = SiliCompressor.with(getActivity()).compressVideo(list.get(i).getOriginalPath(), outputFileAbsolutePath);
                                    videos.add(videos.size(), list.get(i).getOriginalPath());
//                                } catch (URISyntaxException e) {
//                                    e.printStackTrace();
//                                }
                            }
                            mLLEmptyVideo.setVisibility(View.INVISIBLE);
                            vidAdapter = new VideoAdapter(videos, getActivity(), mLLEmptyVideo, addVideo);
                            rvVideo.setAdapter(vidAdapter);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                    videoPicker.shouldGeneratePreviewImages(true);
                    videoPicker.allowMultiple();
                    videoPicker.pickVideo();
                }
            }).create();
        dialogPlus.show();
    }

    public void setVideoPicker(Intent data){
        dialogPlus.dismiss();
        videoPicker.submit(data);
    }

    public void setVideoCameraPicker(Intent data){
        dialogPlus.dismiss();
        vidCameraPicker.submit(data);
    }


    public List<String> getListVideo(){
        return videos;
    }
}
