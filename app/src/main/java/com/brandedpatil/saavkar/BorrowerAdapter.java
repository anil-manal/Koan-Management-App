package com.brandedpatil.saavkar;
// BorrowerAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.brandedpatil.saavkar.R;
import com.brandedpatil.saavkar.Borrower;





public class BorrowerAdapter extends RecyclerView.Adapter<BorrowerAdapter.ViewHolder> {

    private List<Borrower> borrowers;

    public BorrowerAdapter(List<Borrower> borrowers) {
        this.borrowers = borrowers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrower_remind, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Borrower borrower = borrowers.get(position);

        // Set borrower details to TextViews
        holder.textViewBorrowerName.setText("Name: " + borrower.getBorrowerName());
        // Add more lines to set other details

    }

    @Override
    public int getItemCount() {
        return borrowers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBorrowerName;
        // Add more TextViews for other borrower details

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBorrowerName = itemView.findViewById(R.id.textViewBorrowerName);
            // Initialize other TextViews here
        }
    }
}
