package com.brandedpatil.saavkar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.HashMap;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.brandedpatil.saavkar.databinding.ActivityAddBorrowerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBorrowerActivity extends AppCompatActivity {

    ActivityAddBorrowerBinding binding;
    String borrowerName, mobileNumber, amountToBorrow, interestRate, monthlyInterest, loanDuration;
    Date loanStartDate;
    int collectionDay; // New field for collection day
    FirebaseDatabase db;
    DatabaseReference reference;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBorrowerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();

        // Set click listener for the button
        binding.btnAddBorrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowerName = binding.borrowerName.getText().toString();
                mobileNumber = binding.mobileNumber.getText().toString();
                amountToBorrow = binding.amountToBorrow.getText().toString();
                interestRate = binding.interestRate.getText().toString();
                monthlyInterest = binding.monthlyInterest.getText().toString();
                loanDuration = binding.loanDuration.getText().toString();

                // Retrieve collection day from DatePicker
                collectionDay = binding.loanStartDate.getDayOfMonth();

                int day = binding.loanStartDate.getDayOfMonth();
                int month = binding.loanStartDate.getMonth();
                int year = binding.loanStartDate.getYear();

                // Format the date as dd/MM/yyyy
                String loanStartDateString = String.format("%02d/%02d/%04d", day, month + 1, year);

                // Convert loanStartDateString to Date object
                Date loanStartDate = null;
                try {
                    loanStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(loanStartDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!borrowerName.isEmpty() || !mobileNumber.isEmpty() || !amountToBorrow.isEmpty() || !interestRate.isEmpty() || !monthlyInterest.isEmpty() || loanDuration.isEmpty()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers").push();
                        String borrowerId = reference.getKey();

                        Borrower borrower = new Borrower(borrowerId, borrowerName, mobileNumber, amountToBorrow, interestRate, monthlyInterest, loanDuration, loanStartDate, "Active", 0, new HashMap<>());
                        borrower.setCollectionDay(collectionDay); // Set collection day
                        db = FirebaseDatabase.getInstance();

                        reference.setValue(borrower).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                binding.borrowerName.setText("");
                                binding.mobileNumber.setText("");
                                binding.amountToBorrow.setText("");
                                binding.interestRate.setText("");
                                binding.monthlyInterest.setText("");
                                binding.loanDuration.setText("");
                                binding.loanStartDate.updateDate(2022, 0, 1); // Default date
                                Toast.makeText(AddBorrowerActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(AddBorrowerActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
            startActivity(new Intent(AddBorrowerActivity.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(AddBorrowerActivity.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(AddBorrowerActivity.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(AddBorrowerActivity.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(AddBorrowerActivity.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
