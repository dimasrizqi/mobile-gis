package id.gis.collection.ui.addplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import id.gis.collection.R;

/**
 * Created by dell on 05/08/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
    private Context context;
    private List<String> imageList;
    private LinearLayout ll;
    private RelativeLayout rl;

    public ImageAdapter(List<String> imageList,  Context context, LinearLayout ll, RelativeLayout rl){
        this.context = context;
        this.imageList = imageList;
        this.ll = ll;
        this.rl = rl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        File img = new File(imageList.get(position));
        Glide.with(context)
            .load((imageList.get(position)))
            .apply(new RequestOptions().placeholder(R.drawable.ic_image).centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.img);

        holder.img.setOnClickListener(v -> {
            File recFile = null;
            if (URLUtil.isValidUrl(imageList.get(position))){
                try {
                    URL url = new URL(imageList.get(position));
                    recFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                    org.apache.commons.io.FileUtils.copyURLToFile(url, recFile, 120000, 120000);
                    recFile.deleteOnExit();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                recFile = img;
                recFile.deleteOnExit();
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(recFile), "image/*");
            context.startActivity(intent);

        });

        holder.remover.setOnClickListener(v -> {
            int pos = position;
            imageList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, imageList.size());
            if (imageList.size() == 0){
                ll.setVisibility(View.VISIBLE);
                rl.setVisibility(View.INVISIBLE);
            }
        });

        holder.imgName.setText(img.getName());

        if(imageList.size() > 0){
            rl.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "IMAGE ADAPTER";
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
