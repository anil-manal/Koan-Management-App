package com.brandedpatil.saavkar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;
import java.util.Calendar;
import android.widget.DatePicker;
import java.util.Map;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.brandedpatil.saavkar.databinding.ActivityBorrowerProfileBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;

public class BorrowerProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ActivityBorrowerProfileBinding binding;
    private String borrowerId;
    private FirebaseAuth firebaseAuth;
    private Borrower borrower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBorrowerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        borrowerId = getIntent().getStringExtra("borrowerId");

        if (borrowerId != null) {
            Log.d("BorrowerProfileActivity", "Borrower ID: " + borrowerId);
            fetchBorrowerDetailsFromFirebase();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard) {
            startActivity(new Intent(BorrowerProfileActivity.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(BorrowerProfileActivity.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(BorrowerProfileActivity.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(BorrowerProfileActivity.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(BorrowerProfileActivity.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchBorrowerDetailsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Borrowers")
                .child(borrowerId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    borrower = dataSnapshot.getValue(Borrower.class);

                    if (borrower != null) {
                        binding.txtBorrowerName.setText(borrower.getBorrowerName());
                        binding.txtMobileNumber.setText(borrower.getMobileNumber());
                        binding.txtAmountToBorrow.setText(borrower.getAmountToBorrow());
                        binding.txtInterestRate.setText(borrower.getInterestRate());
                        binding.txtMonthlyInterest.setText(borrower.getMonthlyInterest());
                        binding.txtLoanDuration.setText(borrower.getLoanDuration());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);
                        String loanStartDate = dateFormat.format(new Date(borrower.getLoanStartDate().getTime()));
                        binding.txtLoanStartDate.setText(loanStartDate);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("BorrowerProfileActivity", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    public void onCollectInterestButtonClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        String currentMonthYear = currentMonth + "-" + currentYear;

        if (borrower != null && borrower.getInterestPaid() != null && borrower.getInterestPaid().containsKey(String.valueOf(currentYear))) {
            Map<String, Map<String, Object>> yearMap = borrower.getInterestPaid().get(String.valueOf(currentYear));
            if (yearMap != null && yearMap.containsKey(String.valueOf(currentMonth))) {
                Toast.makeText(this, "Interest for the current month has already been collected", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_collect_interest, null);
        DatePicker datePicker = dialogView.findViewById(R.id.loanStartDate);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount);
        Button buttonCollectInterest = dialogView.findViewById(R.id.buttonCollectInterest);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        editTextAmount.setText(borrower.getMonthlyInterest());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonCollectInterest.setOnClickListener(v -> {
            String date = dateFormat.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));
            String amount = editTextAmount.getText().toString().trim();

            if (amount.isEmpty()) {
                Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            long monthlyInterest = Long.parseLong(borrower.getMonthlyInterest());
            long collectedInterest = Long.parseLong(amount);
            long remainingInterest = monthlyInterest - collectedInterest;

            DatabaseReference borrowerRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Borrowers")
                    .child(borrowerId);

            borrowerRef.child("collectedInterest").setValue(amount);
            borrowerRef.child("remainingInterest").setValue(String.valueOf(remainingInterest));

            String[] parts = date.split("-");
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];

            DatabaseReference interestPaidRef = borrowerRef.child("interestPaid")
                    .child(year)
                    .child(month)
                    .child(day);

            interestPaidRef.child("date").setValue(date);
            interestPaidRef.child("collectedInterest").setValue(collectedInterest);
            interestPaidRef.child("remainingInterest").setValue(remainingInterest);

            if (borrower.getInterestPaid() == null) {
                borrower.setInterestPaid(new HashMap<>());
            }
            if (!borrower.getInterestPaid().containsKey(String.valueOf(currentYear))) {
                borrower.getInterestPaid().put(String.valueOf(currentYear), new HashMap<>());
            }
            borrower.getInterestPaid().get(String.valueOf(currentYear)).put(String.valueOf(currentMonth), new HashMap<>());

            Toast.makeText(this, "Interest collected successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void onViewInterestPaidDetailsButtonClick(View view) {
        Intent intent = new Intent(this, InterestPaidDetailsActivity.class);
        intent.putExtra("borrowerId", borrowerId);
        startActivity(intent);
    }

    public void onDeleteBorrowerButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this borrower?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteBorrower();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBorrower() {
        if (borrowerId != null) {
            DatabaseReference borrowerRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Borrowers")
                    .child(borrowerId);

            borrowerRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Borrower deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete borrower: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Borrower ID is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdateDetailsButtonClick(View view) {
        Intent intent = new Intent(this, UpdateDetailsActivity.class);
        intent.putExtra("borrowerId", borrowerId);
        startActivity(intent);
    }
}
