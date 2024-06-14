package com.brandedpatil.saavkar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class InterestPaidDetailsAdapter extends ArrayAdapter<InterestPaidDetails> {

    private Context mContext;
    private List<InterestPaidDetails> mInterestPaidDetailsList;

    public InterestPaidDetailsAdapter(Context context, List<InterestPaidDetails> interestPaidDetailsList) {
        super(context, 0, interestPaidDetailsList);
        mContext = context;
        mInterestPaidDetailsList = interestPaidDetailsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_interest_paid_details, parent, false);
        }

        InterestPaidDetails currentItem = mInterestPaidDetailsList.get(position);

        TextView textViewDate = listItem.findViewById(R.id.textViewDate);
        textViewDate.setText(currentItem.getDate());

        TextView textViewAmountCollected = listItem.findViewById(R.id.textViewAmountCollected);
        textViewAmountCollected.setText("Amount Collected: " + currentItem.getAmountCollected());

        TextView textViewRemainingInterest = listItem.findViewById(R.id.textViewRemainingInterest);
        textViewRemainingInterest.setText("Remaining Interest: " + currentItem.getRemainingInterest());

        return listItem;
    }
}