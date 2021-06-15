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

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {
    private ArrayList<ParkingLot> nearbyParkingLots;
    private NearbyClickListener listener;

    public NearbyAdapter(ArrayList<ParkingLot> nearbyParkingLots, NearbyClickListener listener) {
        this.nearbyParkingLots = nearbyParkingLots;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NearbyClickListener listener;
        private TextView tvParkingLotName, tvCarSlot, tvMotorSlot,tvDistance;

        public MyViewHolder(@NonNull View view, NearbyClickListener listener) {
            super(view);
            this.listener = listener;
            tvParkingLotName = view.findViewById(R.id.tvMallNameNPL);
            tvCarSlot = view.findViewById(R.id.tv_car_npl);
            tvMotorSlot = view.findViewById(R.id.tvMotorcycleNPL);
            tvDistance = view.findViewById(R.id.tvDistanceNPL);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onNearbyClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_parking_lots_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyAdapter.MyViewHolder holder, int position) {
        holder.tvParkingLotName.setText(nearbyParkingLots.get(position).getParkingName());
        String carSlot = nearbyParkingLots.get(position).getTotalCarSlots() + " Available";
        holder.tvCarSlot.setText(carSlot);
        String motorSlot = nearbyParkingLots.get(position).getTotalMotorSlots() + " Available";
        holder.tvMotorSlot.setText(motorSlot);
        String distance = nearbyParkingLots.get(position).getDistance() + " km";
        holder.tvDistance.setText(distance);
    }

    @Override
    public int getItemCount() {
        return nearbyParkingLots.size();
    }

    public interface NearbyClickListener{
        void onNearbyClicked(int position);
    }
}
