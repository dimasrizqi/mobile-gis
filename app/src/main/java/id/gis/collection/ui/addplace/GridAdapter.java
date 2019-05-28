package id.gis.collection.ui.addplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.gis.collection.R;

/**
 * Created by dell on 22/07/18.
 */

public class GridAdapter extends BaseAdapter {

    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;
    public GridAdapter(Context context, String[] nameList, int[] icon){
        result = nameList;
        context = context;
        imageId = icon;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView name;
        ImageView icon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.grid_layout, null);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.icon = (ImageView) rowView.findViewById(R.id.icon);

        holder.name.setText(result[position]);
        holder.icon.setImageResource(imageId[position]);

        return rowView;
    }
}
