package tiffanytiph.com.parkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import tiffanytiph.com.parkit.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private LayoutInflater layoutInflater;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> words) {
        super(context, resource, words);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.custom_spinner_item, null, true);
        String item = getItem(position);
        TextView text = (TextView) rowView.findViewById(R.id.spinner_item);
        text.setText(item);
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.custom_spinner_item, parent, false);
        String item = getItem(position);
        TextView text = (TextView) convertView.findViewById(R.id.spinner_item);
        text.setText(item);
        return convertView;
    }
}
