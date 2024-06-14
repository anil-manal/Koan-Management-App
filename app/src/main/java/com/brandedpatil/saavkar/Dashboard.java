package com.brandedpatil.saavkar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity implements BorrowerListAdapter.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private TextView txtTotalLoanAmount, txtTotalInterestForMonth, txtTotalCollectedInterest, txtTotalBorrowers;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerViewBorrowers;
    private BorrowerListAdapter adapter;
    private List<Borrower> borrowerList = new ArrayList<>();
    private DatabaseReference databaseReference; // Declare databaseReference as a class-level variable

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView); // Replace R.id.recyclerView with your RecyclerView id
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use this for activity, or getContext() for fragment
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BorrowerListAdapter(this, borrowerList, this);
        recyclerView.setAdapter(adapter);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Initialize TextViews for the new cards
        txtTotalLoanAmount = findViewById(R.id.txtTotalLoanAmount);
        txtTotalInterestForMonth = findViewById(R.id.txtTotalInterestForMonth);
        txtTotalCollectedInterest = findViewById(R.id.txtTotalCollectedInterest);
        txtTotalBorrowers = findViewById(R.id.txtTotalBorrowers);

        // Fetch and display data for the new cards
        fetchDashboardDataFromFirebase();
        fetchTotalLoanAmountFromFirebase();
        fetchTotalInterestForMonthFromFirebase();
        fetchTotalBorrowersFromFirebase();  // Added this line to fetch the total number of borrowers
        fetchTotalCollectedInterestForMonthFromFirebase(); // Added this line to fetch the total collected interest for the current month

        // Fetch and display borrowers with next ten days
        fetchBorrowersWithNextTenDays(); // Added this line to fetch and display borrowers with next ten days
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
            startActivity(new Intent(Dashboard.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(Dashboard.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(Dashboard.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(Dashboard.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(Dashboard.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchDashboardDataFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");

            // Use a query to filter borrowers with "Active" loan status
            Query activeBorrowersQuery = databaseReference.orderByChild("loanStatus").equalTo("Active");

            // Attach a listener to read the data from the specified Query
            activeBorrowersQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long totalLoanAmount = 0;
                        long totalInterestForMonth = 0;
                        long totalCollectedInterest = 0;
                        long totalBorrowers = dataSnapshot.getChildrenCount();

                        for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                            String amountBorrowedString = borrowerSnapshot.child("amountToBorrow").getValue(String.class);
                            String monthlyInterestString = borrowerSnapshot.child("monthlyInterest").getValue(String.class);

                            // Check if the strings obtained from the database are not empty or null
                            if (amountBorrowedString != null && !amountBorrowedString.isEmpty() &&
                                    monthlyInterestString != null && !monthlyInterestString.isEmpty()) {
                                long amountBorrowed = Long.parseLong(amountBorrowedString);
                                long monthlyInterest = Long.parseLong(monthlyInterestString);

                                // Assuming there's a field for collected interest in each borrower
                                totalCollectedInterest += /* Get collected interest from borrower */ 0;

                                // Display details or perform any action for the active borrower
                                txtTotalLoanAmount.setText("Total Loan Amount: ₹" + (totalLoanAmount + amountBorrowed));
                                txtTotalInterestForMonth.setText("Total Interest for the Month: ₹" + (totalInterestForMonth + monthlyInterest));
                                // You can continue displaying other details here
                            }
                        }

                        // Display the total number of borrowers
                        txtTotalBorrowers.setText(" " + totalBorrowers);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
    }

    private void fetchTotalLoanAmountFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long totalLoanAmount = 0;

                    for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                        String loanStatus = borrowerSnapshot.child("loanStatus").getValue(String.class);
                        if (loanStatus != null && loanStatus.equals("Active")) {
                            String amountBorrowedString = borrowerSnapshot.child("amountToBorrow").getValue(String.class);
                            if (amountBorrowedString != null && !amountBorrowedString.isEmpty()) {
                                long amountBorrowed = Long.parseLong(amountBorrowedString);
                                totalLoanAmount += amountBorrowed;
                            }
                        }
                    }

                    txtTotalLoanAmount.setText("₹" + totalLoanAmount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
    }


    private void fetchTotalInterestForMonthFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long totalInterestForMonth = 0;

                    for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                        String loanStatus = borrowerSnapshot.child("loanStatus").getValue(String.class);
                        if (loanStatus != null && loanStatus.equals("Active")) {
                            String monthlyInterestString = borrowerSnapshot.child("monthlyInterest").getValue(String.class);
                            if (monthlyInterestString != null && !monthlyInterestString.isEmpty()) {
                                long monthlyInterest = Long.parseLong(monthlyInterestString);
                                totalInterestForMonth += monthlyInterest;
                            }
                        }
                    }

                    txtTotalInterestForMonth.setText(" ₹" + totalInterestForMonth);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
    }


    private void fetchTotalBorrowersFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long totalBorrowers = 0;

                    for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                        String loanStatus = borrowerSnapshot.child("loanStatus").getValue(String.class);
                        if (loanStatus != null && loanStatus.equals("Active")) {
                            totalBorrowers++;
                        }
                    }

                    txtTotalBorrowers.setText("" + totalBorrowers);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
    }

    private void fetchTotalCollectedInterestForMonthFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");

            // Get the current month
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are zero-based in Calendar class

            // Use a query to filter borrowers with "Active" loan status
            Query activeBorrowersQuery = databaseReference.orderByChild("loanStatus").equalTo("Active");

            // Attach a listener to read the data from the specified Query
            activeBorrowersQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long totalCollectedInterestForMonth = 0;

                        for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot interestPaidSnapshot = borrowerSnapshot.child("interestPaid").child(String.valueOf(currentYear))
                                    .child(String.format("%02d", currentMonth)); // Format month to have leading zeros
                            if (interestPaidSnapshot.exists()) {
                                for (DataSnapshot daySnapshot : interestPaidSnapshot.getChildren()) {
                                    // Sum up collected interest for the current month
                                    long collectedInterest = daySnapshot.child("collectedInterest").getValue(Long.class);
                                    totalCollectedInterestForMonth += collectedInterest;
                                }
                            }
                        }

                        // Display total collected interest for the current month
                        txtTotalCollectedInterest.setText(" ₹" + totalCollectedInterestForMonth);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
    }

    @Override
    public void onItemClick(Borrower borrower) {
        // Start BorrowerProfileActivity and pass borrowerId
        Intent intent = new Intent(Dashboard.this, BorrowerProfileActivity.class);
        intent.putExtra("borrowerId", borrower.getBorrowerId());
        startActivity(intent);
    }

    private void fetchBorrowersWithNextTenDays() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear(); // Clear existing data in the adapter
                Calendar calendar = Calendar.getInstance();
                int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                    Borrower borrower = borrowerSnapshot.getValue(Borrower.class);
                    if (borrower != null) {
                        int collectionDay = borrower.getCollectionDay();
                        // Check if the collection day is within the next ten days
                        if (collectionDay - currentDayOfMonth <= 1 && collectionDay >= currentDayOfMonth) {
                            adapter.add(borrower); // Add borrower to adapter
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}