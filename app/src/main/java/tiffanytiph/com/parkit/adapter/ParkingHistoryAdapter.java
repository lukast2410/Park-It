package tiffanytiph.com.parkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;

public class ParkingHistoryAdapter extends RecyclerView.Adapter<ParkingHistoryAdapter.MyViewHolder> {
    private ArrayList<Transaction> transactions;
    private HistoryClickListener listener;
    private Context context;
    private SimpleDateFormat df, dfSimple;

    public ParkingHistoryAdapter(ArrayList<Transaction> transactions, HistoryClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
        this.df = new SimpleDateFormat("dd MMMM yyyy");
        this.dfSimple = new SimpleDateFormat("EEE dd MMM yyyy");
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HistoryClickListener listener;
        private TextView tvParkingLotName, tvPaymentMethod, tvVehicleType, tvTime, tvTotalPrice;
        private ImageView ivVehicleType;

        public MyViewHolder(@NonNull View view, HistoryClickListener listener) {
            super(view);
            this.listener = listener;
            tvParkingLotName = view.findViewById(R.id.tvMallNamePH);
            tvPaymentMethod = view.findViewById(R.id.tvPaymentMethodPH);
            tvVehicleType = view.findViewById(R.id.tvVehicleTypePH);
            tvTime = view.findViewById(R.id.tvTimePH);
            tvTotalPrice = view.findViewById(R.id.tvTotalPricePH);
            ivVehicleType = view.findViewById(R.id.ivVehicleTypePH);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onHistoryClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking_history_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int bookingFee = 3000;
        ParkingLot parkingLot = ParkingLot.getOneParkingLot(context, transactions.get(position).getParkingLotId());
        assert parkingLot != null;
        holder.tvParkingLotName.setText(parkingLot.getParkingName());
        holder.tvPaymentMethod.setText(transactions.get(position).getPaymentMethod());
        holder.tvVehicleType.setText(transactions.get(position).getVehicleType());
        String time = getTransactionTime(transactions.get(position));
        holder.tvTime.setText(time);
        int diff = calculateTotalHour(transactions.get(position));
        int totalPrice = diff * (transactions.get(position).getVehicleType().equals("Car") ?
                parkingLot.getCarPricePerHour() : parkingLot.getMotorPricePerHour()) + bookingFee;
        String priceText = "Rp. " + totalPrice + ",00";
        holder.tvTotalPrice.setText(priceText);
        holder.ivVehicleType.setImageResource(transactions.get(position).getVehicleType().equals("Car") ?
                R.drawable.ic_baseline_directions_car_24:
                R.drawable.ic_baseline_directions_bike_24);
    }

    private int calculateTotalHour(Transaction transaction) {
        int between = transaction.getLeaveHour() - transaction.getEnterHour();
        if (transaction.getLeaveHour() < transaction.getEnterHour()){
            between = (24 * 60) - transaction.getEnterHour() + transaction.getLeaveHour();
        }
        int hour = (int) Math.ceil(between / 60.0);
        return hour;
    }

    private String getTransactionTime(Transaction transaction) {
        String result = "";
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(df.parse(transaction.getTransactionDate())));
            result += dfSimple.format(c.getTime()) + ", ";
            result += Transaction.convertHourToString(transaction.getEnterHour()) + " - ";
            result += Transaction.convertHourToString(transaction.getLeaveHour());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public interface HistoryClickListener{
        void onHistoryClicked(int position);
    }
}
