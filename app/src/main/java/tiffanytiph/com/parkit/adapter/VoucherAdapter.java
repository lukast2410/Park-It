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
import java.util.Date;

import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.model.Voucher;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {
    private ArrayList<Voucher> vouchers;
    private VoucherClickListener listener;
    private SimpleDateFormat df;

    public VoucherAdapter(ArrayList<Voucher> vouchers, VoucherClickListener listener) {
        this.vouchers = vouchers;
        this.listener = listener;
        this.df = new SimpleDateFormat("dd MMMM yyyy");
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private VoucherClickListener listener;
        private TextView tvDiscount, tvExpired;
        private ImageView ivVehicleType;

        public MyViewHolder(@NonNull View view, VoucherClickListener listener) {
            super(view);
            this.listener = listener;
            tvDiscount = view.findViewById(R.id.tvDiscountVC);
            tvExpired = view.findViewById(R.id.tvExpiredVC);
            ivVehicleType = view.findViewById(R.id.ivVehicleTypeVC);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onVoucherClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bonus_voucher_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String discount = "Rp. " + vouchers.get(position).getDiscount() + ",00";
        holder.tvDiscount.setText(discount);
        int diffDays = getDifferenceDate(vouchers.get(position).getExpiredDate());
        String expires = "Expired today";
        if (diffDays < 0) expires = "Expired!";
        else if(diffDays > 0) expires = "Expires in " + diffDays + " days";
        holder.tvExpired.setText(expires);
        holder.ivVehicleType.setImageResource(vouchers.get(position).getVehicleType().equals("Car") ?
                R.drawable.ic_baseline_directions_car_24:
                R.drawable.ic_baseline_directions_bike_24);
    }

    private int getDifferenceDate(String expiredDate) {
        try {
            Date expired = df.parse(expiredDate);
            Date now = Calendar.getInstance().getTime();
            assert expired != null;
            long different = expired.getTime() - now.getTime();
            if (different <= 0) return -1;

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            return (int) elapsedDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public interface VoucherClickListener{
        void onVoucherClicked(int position);
    }
}
