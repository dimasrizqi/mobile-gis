package id.gis.collection.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.gis.collection.R;

public class PlaceItemFragment extends Fragment {

    private static final String EXTRA_SURVEY = "EXTRA_SURVEY";
    private static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    private static final String EXTRA_TGL = "EXTRA_TGL";
    private static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";

    @BindView(R.id.tv_survey)
    TextView mTvSurvey;
    @BindView(R.id.tv_project)
    TextView mTvProject;
    @BindView(R.id.tv_tgl)
    TextView mTvTgl;
    @BindView(R.id.tv_address)
    TextView mTvAddress;

    private Unbinder unbinder;

    private String survey;
    private String project;
    private String tgl;
    private String address;

    public static PlaceItemFragment newInstance(String survey, String project, String tgl, String address) {
        PlaceItemFragment fragment = new PlaceItemFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_SURVEY, survey);
        args.putString(EXTRA_PROJECT, project);
        args.putString(EXTRA_TGL, tgl);
        args.putString(EXTRA_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        survey = getArguments().getString(EXTRA_SURVEY);
        project = getArguments().getString(EXTRA_PROJECT);
        tgl = getArguments().getString(EXTRA_TGL);
        address = getArguments().getString(EXTRA_ADDRESS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_item_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mTvSurvey.setText(survey);
        mTvProject.setText(project);
        mTvTgl.setText(tgl);
        mTvAddress.setText(address);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
