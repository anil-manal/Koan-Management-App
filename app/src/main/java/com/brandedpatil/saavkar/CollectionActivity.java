package com.brandedpatil.saavkar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements BorrowerListAdapter.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private BorrowerListAdapter adapter;
    private List<Borrower> collectedBorrowers;  // New list for collected borrowers
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample list of collected borrowers
        collectedBorrowers = new ArrayList<>();

        adapter = new BorrowerListAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("Borrowers");
            fetchBorrowersWithNextTenDays();
        } else {
            // Handle the case when the user is not logged in
            // You might want to redirect to the login screen
        }
    }

    @Override
    public void onItemClick(Borrower borrower) {
        // Start BorrowerProfileActivity and pass borrowerId
        Intent intent = new Intent(CollectionActivity.this, BorrowerProfileActivity.class);
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
                        if (collectionDay - currentDayOfMonth <= 5 && collectionDay >= currentDayOfMonth) {
                            adapter.add(borrower); // Add borrower to adapter
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CollectionActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(CollectionActivity.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(CollectionActivity.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(CollectionActivity.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(CollectionActivity.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(CollectionActivity.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
