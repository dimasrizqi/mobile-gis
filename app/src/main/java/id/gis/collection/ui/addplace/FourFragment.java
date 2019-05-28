package id.gis.collection.ui.addplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.gis.collection.R;

/**
 * Created by dell on 20/08/18.
 */

public class FourFragment extends Fragment {

    private static final String TAG = "THREE FRAGMENT";
    @BindView(R.id.labelFile)
    RelativeLayout mLabelFile;

    @BindView(R.id.rvFile)
    RecyclerView rvFile;

    @BindView(R.id.emptyFile)
    LinearLayout mLLEmptyFile;

    @BindView(R.id.add_file)
    RelativeLayout addFile;

    private Unbinder unbinder;
    private DialogPlus dialogPlus;

    private FilePicker filePicker;
    private List<String> files = new ArrayList<>();
    private FileAdapter fileAdapter;

    public FourFragment newInstance(List<String> intentFiles) {
        // Required empty public constructor
        Bundle args = new Bundle();
        args.putStringArrayList("intentFiles", (ArrayList<String>) intentFiles);
        FourFragment fragment = new FourFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getStringArrayList("intentFiles").size() > 0){
            this.files = getArguments().getStringArrayList("intentFiles");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null && Objects.requireNonNull(getArguments().getStringArrayList("intentFiles")).size() > 0){
            rvFile.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
            mLLEmptyFile.setVisibility(View.INVISIBLE);
            fileAdapter = new FileAdapter(files, getActivity(), mLLEmptyFile, addFile);
            rvFile.setAdapter(fileAdapter);
        }
        // Inflate the layout for this fragment

        return view;
    }

    @OnClick(R.id.labelFile)
    public void initFile(){
        rvFile.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        filePicker = new FilePicker(getActivity());
        filePicker.setFilePickerCallback(new FilePickerCallback() {
            @Override
            public void onFilesChosen(List<ChosenFile> list) {
                for (int i = 0; i < list.size(); i ++){
                    files.add(files.size(), list.get(i).getOriginalPath());
                }

                mLLEmptyFile.setVisibility(View.INVISIBLE);
                fileAdapter = new FileAdapter(files, getActivity(), mLLEmptyFile, addFile);
                rvFile.setAdapter(fileAdapter);
            }

            @Override
            public void onError(String s) {

            }
        });
        filePicker.setMimeType("application/pdf");
        filePicker.allowMultiple();
        filePicker.pickFile();
    }

    public void setFilePicker(Intent data){
        filePicker.submit(data);
    }

    public List<String> getListFiles(){
        return this.files;
    }
}
