package com.brandedpatil.saavkar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class InterestPaidDetailsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private ListView listView;
    private InterestPaidDetailsAdapter adapter;
    private ArrayList<InterestPaidDetails> interestDetailsList;

    private String borrowerId; // Add a variable to hold the borrower ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_paid_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        listView = findViewById(R.id.listView);
        interestDetailsList = new ArrayList<>();
        adapter = new InterestPaidDetailsAdapter(this, interestDetailsList); // Corrected constructor call
        listView.setAdapter(adapter);

        // Retrieve the borrower ID from the intent
        borrowerId = getIntent().getStringExtra("borrowerId");

        // Fetch and display interest paid details for the specific borrower
        fetchInterestPaidDetailsForBorrower(borrowerId);
    }

    private void fetchInterestPaidDetailsForBorrower(String borrowerId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference borrowerRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(currentUser.getUid())
                    .child("Borrowers")
                    .child(borrowerId) // Add this to fetch details for the specific borrower
                    .child("interestPaid");

            borrowerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    interestDetailsList.clear();
                    for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                        String year = yearSnapshot.getKey();
                        for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                            String month = monthSnapshot.getKey();
                            for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                                String day = daySnapshot.getKey();
                                Map<String, Object> interestDetails = (Map<String, Object>) daySnapshot.getValue();
                                String date = day + "-" + month + "-" + year;
                                double amountCollected = Double.parseDouble(String.valueOf(interestDetails.get("collectedInterest")));
                                double remainingInterest = Double.parseDouble(String.valueOf(interestDetails.get("remainingInterest")));
                                interestDetailsList.add(new InterestPaidDetails(date, amountCollected, remainingInterest));
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
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
            startActivity(new Intent(InterestPaidDetailsActivity.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(InterestPaidDetailsActivity.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(InterestPaidDetailsActivity.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(InterestPaidDetailsActivity.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut(); // Initialize FirebaseAuth instance here
            startActivity(new Intent(InterestPaidDetailsActivity.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
