package tiffanytiph.com.parkit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.model.ParkingLot;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {
    private ArrayList<ParkingLot> parkingLots;
    private SearchClickListener listener;

    public SearchHistoryAdapter(ArrayList<ParkingLot> parkingLots, SearchClickListener listener) {
        this.parkingLots = parkingLots;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SearchClickListener listener;
        private TextView tvParkingLotName;


        public MyViewHolder(@NonNull View view, SearchClickListener listener) {
            super(view);
            this.listener = listener;
            tvParkingLotName = view.findViewById(R.id.tvMallNameSH);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onSearchClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvParkingLotName.setText(parkingLots.get(position).getParkingName());
    }

    @Override
    public int getItemCount() {
        return parkingLots.size();
    }

    public interface SearchClickListener{
        void onSearchClicked(int position);
    }
}
