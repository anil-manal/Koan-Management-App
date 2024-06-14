package com.brandedpatil.saavkar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BorrowerListAdapter extends RecyclerView.Adapter<BorrowerListAdapter.BorrowerViewHolder> {

    private final Context context;
    private final List<Borrower> borrowerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Borrower borrower);
    }

    public BorrowerListAdapter(Context context, List<Borrower> borrowerList, OnItemClickListener listener) {
        this.context = context;
        this.borrowerList = borrowerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BorrowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrower_card, parent, false);
        return new BorrowerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowerViewHolder holder, int position) {
        Borrower borrower = borrowerList.get(position);

        holder.textViewBorrowerName.setText("Name: " + borrower.getBorrowerName());

        // Display the amount to collect
        if (holder.textViewAmountToCollect != null) {
            String amountToCollect = borrower.getMonthlyInterest();
            holder.textViewAmountToCollect.setText("Amount to Collect: " + amountToCollect);
        }

        // Display the collection date
        if (holder.textViewCollectionDate != null) {
            Date collectionDate = borrower.getLoanStartDate();
            if (collectionDate != null) {
                holder.textViewCollectionDate.setText("Collection Date: " + formatDate(collectionDate));
            }
        }

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(borrower);
            }
        });
    }

    @Override
    public int getItemCount() {
        return borrowerList.size();
    }

    static class BorrowerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBorrowerName, textViewAmountToCollect, textViewCollectionDate;

        BorrowerViewHolder(View itemView) {
            super(itemView);
            textViewBorrowerName = itemView.findViewById(R.id.textViewBorrowerName);
            textViewAmountToCollect = itemView.findViewById(R.id.textViewAmountToCollect);
            textViewCollectionDate = itemView.findViewById(R.id.textViewCollectionDate);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public void add(Borrower borrower) {
        borrowerList.add(borrower);
        notifyItemInserted(borrowerList.size() - 1);
    }

    public void clear() {
        borrowerList.clear();
        notifyDataSetChanged();
    }
}
