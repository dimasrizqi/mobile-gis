package id.gis.collection.ui.addplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import id.gis.collection.R;
import retrofit2.http.Url;

import static com.kbeanie.multipicker.utils.StreamHelper.close;
import static com.kbeanie.multipicker.utils.StreamHelper.flush;

/**
 * Created by dell on 08/08/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{
    private Context context;
    private List<String> videoList;
    private LinearLayout ll;
    private RelativeLayout rl;
    private final int cacheLocation = 100;

    public VideoAdapter(List<String> videoList, Context context, LinearLayout ll, RelativeLayout rl){
        this.context = context;
        this.videoList = videoList;
        this.ll = ll;
        this.rl = rl;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        File video = new File(videoList.get(position));
        Glide.with(context)
            .load(videoList.get(position))
            .apply(new RequestOptions().placeholder(R.drawable.ic_image).centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.img);


        holder.img.setOnClickListener(v -> {
            File recFile = null;
            if (URLUtil.isValidUrl(videoList.get(position))){
                try {
                    URL url = new URL(videoList.get(position));
                    recFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                    org.apache.commons.io.FileUtils.copyURLToFile(url, recFile, 120000, 120000);
                    recFile.deleteOnExit();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                recFile = video;
                recFile.deleteOnExit();
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(recFile), "video/*");
            context.startActivity(intent);
        });

        holder.remover.setOnClickListener(v -> {
            videoList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, videoList.size());
            if (videoList.size() == 0){
                ll.setVisibility(View.VISIBLE);
                rl.setVisibility(View.INVISIBLE);
            }
        });

        holder.imgName.setText(video.getName());

        if(videoList.size() > 0){
            rl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "VIDEO ADAPTER";
        ImageView img;
        ImageView remover;
        TextView imgName;
        public ViewHolder(View view) {
            super(view);
            img = (ImageView) itemView.findViewById(R.id.img);
            remover = (ImageView) itemView.findViewById(R.id.imgRemove);
            imgName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}

