package tiffanytiph.com.parkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.MyViewHolder> {
    private ArrayList<Transaction> transactions;
    private CurrentClickListener listener;
    private Context context;

    public CurrentAdapter(ArrayList<Transaction> transactions, CurrentClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CurrentClickListener listener;
        private TextView tvParkingLotName, tvPaymentMethod, tvVehicleType, tvTime;

        public MyViewHolder(@NonNull View view, CurrentClickListener listener) {
            super(view);
            this.listener = listener;
            tvParkingLotName = view.findViewById(R.id.tvMallNameCP);
            tvPaymentMethod = view.findViewById(R.id.tvPaymentMethodCP);
            tvVehicleType = view.findViewById(R.id.tvVehicleTypeCP);
            tvTime = view.findViewById(R.id.tvTimeCP);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCurrentClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_parking_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ParkingLot parkingLot = ParkingLot.getOneParkingLot(context, transactions.get(position).getParkingLotId());
        assert parkingLot != null;
        holder.tvParkingLotName.setText(parkingLot.getParkingName());
        holder.tvPaymentMethod.setText(transactions.get(position).getPaymentMethod());
        holder.tvVehicleType.setText(transactions.get(position).getVehicleType());
        String time = "Starts from " + Transaction.convertHourToString(transactions.get(position).getEnterHour());
        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public interface CurrentClickListener{
        void onCurrentClicked(int position);
    }
}
