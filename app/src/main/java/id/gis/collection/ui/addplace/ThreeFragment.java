package id.gis.collection.ui.addplace;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import id.gis.collection.R;
import id.gis.collection.ui.main.PlaceItemFragment;
import id.gis.collection.utils.IqbalUtils;

/**
 * Created by dell on 20/08/18.
 */

public class ThreeFragment extends Fragment {

    String audioName, audioPath, name;
    private Unbinder unbinder;

    @BindView(R.id.audioText)
    TextView mAudioText;
    private DialogPlus dialogPlus;

    public ThreeFragment newInstance(String audioName){
        Bundle args = new Bundle();
        args.putString("intentAudio", audioName);
        ThreeFragment fragment = new ThreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().getString("intentAudio").isEmpty()){
            this.audioName = URLUtil.guessFileName(getArguments().getString("intentAudio"), null, null);
            try {
                URL url = new URL(getArguments().getString("intentAudio"));
                File recFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                org.apache.commons.io.FileUtils.copyURLToFile(url, recFile, 120000, 120000);
                recFile.deleteOnExit();
                this.audioPath = String.valueOf(recFile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null && !getArguments().getString("intentAudio").isEmpty()){
            setAudioName();
        }
        return view;
    }

    @OnClick(R.id.labelAudio)
    public void recordAudio(View view) {
        String [] names = {"Rekam", "Putar"};
        int [] icon = {R.drawable.ic_mic_lighter, R.drawable.ic_headset};

        dialogPlus = DialogPlus.newDialog(getContext())
            .setExpanded(false)
            .setAdapter(new GridAdapter(getContext(), names, icon))
            .setContentHolder(new GridHolder(2))
            .setGravity(Gravity.BOTTOM)
            .setCancelable(true)
            .setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    if (position == 0){
                        if (name != null && !name.isEmpty()){
                            audioName = name+".wav";
                            audioPath = Environment.getExternalStorageDirectory() + "/" + audioName;
                            int color = getResources().getColor(R.color.blue);
                            int requestCode = 0;
                            AndroidAudioRecorder.with(getActivity())
                                // Required
                                .setFilePath(audioPath)
                                .setColor(color)
                                .setRequestCode(requestCode)

                                // Optional
                                .setSource(AudioSource.MIC)
                                .setChannel(AudioChannel.STEREO)
                                .setSampleRate(AudioSampleRate.HZ_48000)
                                .setAutoStart(false)
                                .setKeepDisplayOn(true)

                                // Start recording
                                .record();
                            dialogPlus.dismiss();
                        }else{
                            Toast.makeText(getActivity(), "Isi Nama Survei", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(!mAudioText.getText().equals("") && audioPath != null){
                            Uri uri = Uri.parse(audioPath);
                            File file = new File(String.valueOf(uri));
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "audio/*");
                            startActivity(intent);
                            dialogPlus.dismiss();
                        }else{
                            Toast.makeText(getActivity(), "Audio Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            })
            .create();
        dialogPlus.show();
    }

    public String getAudio(){
        return audioPath;
    }

    public void setName(String surveyName){
        name = surveyName;
    }

    public void setAudioName(){
        mAudioText.setText(audioName);
        mAudioText.setVisibility(View.VISIBLE);
    }
}
