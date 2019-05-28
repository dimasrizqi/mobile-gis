package id.gis.collection.ui.place;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import id.gis.collection.BuildConfig;
import id.gis.collection.R;
import id.gis.collection.ui.addplace.ImageAdapter;

/**
 * Created by dell on 13/08/18.
 */

public class ImgDetailAdapter extends RecyclerView.Adapter<ImgDetailAdapter.ViewHolder> {

    private Context context;
    private List<String> imageList;

    public ImgDetailAdapter(List<String> imageList, Context context){
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImgDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgDetailAdapter.ViewHolder holder, int position) {
        float factor = holder.img.getContext().getResources().getDisplayMetrics().density;
        holder.img.getLayoutParams().height = (int) (100 * factor);
        holder.img.getLayoutParams().width = (int) (100 * factor);

        Glide.with(context)
            .load(imageList.get(position))
            .apply(new RequestOptions().placeholder(R.drawable.ic_image).centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.img);

        holder.img.setOnClickListener(v -> {
            try {
                URL url = new URL(imageList.get(position));
                File recFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                org.apache.commons.io.FileUtils.copyURLToFile(url, recFile, 120000, 120000);
                recFile.deleteOnExit();

                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);
                Uri photoURI;
                if (Build.VERSION.SDK_INT >= 24){
                    photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", recFile);
                }else{
                    photoURI = Uri.fromFile(recFile);
                }

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(photoURI, "image/*");
                context.startActivity(Intent.createChooser(intent, "Image"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        holder.remover.setVisibility(View.GONE);
        holder.imgName.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "IMAGE DETAIL ADAPTER";
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
