package com.brandedpatil.saavkar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CollectionBorrowersAdapter extends RecyclerView.Adapter<CollectionBorrowersAdapter.ViewHolder> {
    private List<CollectionBorrower> borrowersList;

    public CollectionBorrowersAdapter(List<CollectionBorrower> borrowersList) {
        this.borrowersList = borrowersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrower_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionBorrower borrower = borrowersList.get(position);
        holder.bind(borrower);
    }

    @Override
    public int getItemCount() {
        return borrowersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBorrowerName;
        TextView textViewAmountToCollect;
        TextView textViewCollectionDate;

        ViewHolder(View itemView) {
            super(itemView);
            textViewBorrowerName = itemView.findViewById(R.id.textViewBorrowerName);
            textViewAmountToCollect = itemView.findViewById(R.id.textViewAmountToCollect);
            textViewCollectionDate = itemView.findViewById(R.id.textViewCollectionDate);
        }

        void bind(CollectionBorrower borrower) {
            textViewBorrowerName.setText(borrower.getBorrowerName());
            textViewAmountToCollect.setText("Amount to Collect: " + borrower.getAmountToCollect());
            textViewCollectionDate.setText("Collection Date: " + borrower.getCollectionDate());
        }
    }
}
