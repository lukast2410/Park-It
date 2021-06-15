package tiffanytiph.com.parkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tiffanytiph.com.parkit.HomeActivity;
import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.MyViewHolder> {
    private ArrayList<Transaction> transactions;
    private BookedClickListener listener;
    private Context context;

    public BookedAdapter(ArrayList<Transaction> transactions, BookedClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BookedClickListener listener;
        private TextView tvParkingLotName, tvPaymentMethod, tvVehicleType;

        public MyViewHolder(@NonNull View view, BookedClickListener listener) {
            super(view);
            this.listener = listener;
            tvParkingLotName = view.findViewById(R.id.tvMallNameBP);
            tvPaymentMethod = view.findViewById(R.id.tvPaymentMethodBP);
            tvVehicleType = view.findViewById(R.id.tvVehicleTypeBP);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onBookedClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_parking_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ParkingLot parkingLot = ParkingLot.getOneParkingLot(context, transactions.get(position).getParkingLotId());
        assert parkingLot != null;
        holder.tvParkingLotName.setText(parkingLot.getParkingName());
        holder.tvPaymentMethod.setText(transactions.get(position).getPaymentMethod());
        holder.tvVehicleType.setText(transactions.get(position).getVehicleType());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public interface BookedClickListener{
        void onBookedClicked(int position);
    }
}
