package id.gis.collection.ui.addplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Rectangle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kbeanie.multipicker.api.entity.ChosenFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import id.gis.collection.R;

/**
 * Created by dell on 08/08/18.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>
{
    private Context context;
    private List<String> fileList;
    private LinearLayout ll;
    private RelativeLayout rl;

    public FileAdapter(List<String> fileList, Context context, LinearLayout ll, RelativeLayout rl){
        this.context = context;
        this.fileList = fileList;
        this.ll = ll;
        this.rl = rl;
    }

    @NonNull
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new FileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.ViewHolder holder, int position) {

        Glide.with(context)
            .load(R.drawable.pdf)
            .into(holder.img);

        holder.img.setOnClickListener(v -> {
            File recFile = null;
            if (URLUtil.isValidUrl(fileList.get(position))){
                try {
                    URL url = new URL(fileList.get(position));
                    recFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                    org.apache.commons.io.FileUtils.copyURLToFile(url, recFile, 120000, 120000);
                    recFile.deleteOnExit();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                recFile = new File(fileList.get(position));
                recFile.deleteOnExit();
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(recFile), "application/pdf");
            context.startActivity(intent);
        });

        holder.remover.setOnClickListener(v -> {
            fileList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fileList.size());
            if (fileList.size() == 0){
                ll.setVisibility(View.VISIBLE);
                rl.setVisibility(View.INVISIBLE);
            }
        });

        holder.imgName.setText(new File(fileList.get(position)).getName());

        if(fileList.size() > 0){
            rl.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return fileList.size();
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
