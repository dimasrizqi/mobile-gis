package id.gis.collection.ui.addplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iceteck.silicompressorr.SiliCompressor;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.ui.PickImageActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.gis.collection.R;


/**
 * Created by dell on 20/08/18.
 */

public class OneFragment extends Fragment {

    private static final String TAG = "ONE FRAGMENT";
    @BindView(R.id.labelImage)
    RelativeLayout mLabelImage;

    @BindView(R.id.rvImage)
    RecyclerView rvImage;

    @BindView(R.id.emptyImg)
    LinearLayout mLLEmptyImg;

    @BindView(R.id.add_img)
    RelativeLayout addImg;

    private Unbinder unbinder;
    private DialogPlus dialogPlus;

    private ImagePicker imagePicker;
    private CameraImagePicker imgCameraPicker;
    private List<String> images = new ArrayList<>();
    private ImageAdapter imgAdapter;

    public OneFragment newInstance(List<String> intentImg){
        Bundle args = new Bundle();
        args.putStringArrayList("intentImg", (ArrayList<String>) intentImg);
        OneFragment fragment = new OneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getStringArrayList("intentImg").size() > 0){
            this.images = getArguments().getStringArrayList("intentImg");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        if (getArguments() != null && getArguments().getStringArrayList("intentImg").size() > 0){
            rvImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
            mLLEmptyImg.setVisibility(View.INVISIBLE);
            imgAdapter = new ImageAdapter(images, getContext(), mLLEmptyImg, addImg);
            rvImage.setAdapter(imgAdapter);
        }
        return view;
    }

    @OnClick(R.id.labelImage)
    public void initImage(){
        rvImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        String [] name = {"Camera", "Photo"};
        int [] icon = {R.drawable.ic_camera, R.drawable.ic_image};
        dialogPlus = DialogPlus.newDialog(getContext())
            .setExpanded(false)
            .setAdapter(new GridAdapter(getContext(), name, icon))
            .setContentHolder(new GridHolder(2))
            .setGravity(Gravity.BOTTOM)
            .setCancelable(true)
            .setOnItemClickListener((dialog, item, view, position) -> {
                if (position == 0){
                    imgCameraPicker = new CameraImagePicker(getActivity());
                    imgCameraPicker.setImagePickerCallback(new ImagePickerCallback() {
                        @Override
                        public void onImagesChosen(List<ChosenImage> list) {
                            for (int i = 0; i < list.size(); i ++){
                                String file = SiliCompressor.with(getActivity()).compress(list.get(i).getOriginalPath(), new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(list.get(i).getOriginalPath()), null, null)));
                                images.add(images.size(), file);
                            }
                            mLLEmptyImg.setVisibility(View.INVISIBLE);
                            imgAdapter = new ImageAdapter(images, getContext(), mLLEmptyImg, addImg);
                            rvImage.setAdapter(imgAdapter);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                    imgCameraPicker.setDebugglable(true);
                    imgCameraPicker.pickImage();
                }else{
                    imagePicker = new ImagePicker(getActivity());
                    imagePicker.setImagePickerCallback(new PickImageActivity() {
                        @Override
                        public void onImagesChosen(List<ChosenImage> list) {
                            for (int i = 0; i < list.size(); i ++){
                                String file = SiliCompressor.with(getActivity()).compress(list.get(i).getOriginalPath(), new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(list.get(i).getOriginalPath()), null, null)));
                                images.add(images.size(), file);
                            }
                            mLLEmptyImg.setVisibility(View.INVISIBLE);
                            imgAdapter = new ImageAdapter(images, getContext(), mLLEmptyImg, addImg);
                            rvImage.setAdapter(imgAdapter);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                    imagePicker.allowMultiple();
                    imagePicker.shouldGenerateThumbnails(true);
                    imagePicker.pickImage();
                }
            })
            .create();
        dialogPlus.show();
    }

    public void setImagePicker(Intent data){
        dialogPlus.dismiss();
        imagePicker.submit(data);
    }

    public void setImageCameraPicker(Intent data){
        dialogPlus.dismiss();
        imgCameraPicker.submit(data);
    }

    public List<String> getListImage(){
        return images;
    }
}
