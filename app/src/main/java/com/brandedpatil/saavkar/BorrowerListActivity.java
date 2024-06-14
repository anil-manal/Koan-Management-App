package com.brandedpatil.saavkar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class BorrowerListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ListView listView;
    private ArrayAdapter<Borrower> adapter;
    private ArrayList<Borrower> borrowerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_list);

        // Initialize your ListView and borrowerItems array
        listView = findViewById(R.id.listView);
        borrowerItems = new ArrayList<>();

        // Set up ArrayAdapter
        adapter = new BorrowerListAdapter();
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start BorrowerProfileActivity and pass borrowerId instead of borrowerName
                String borrowerId = borrowerItems.get(position).getBorrowerId();
                Log.d("BorrowerListActivity", "Clicked on: " + borrowerId);
                // Start BorrowerProfileActivity and pass borrowerId
                Intent intent = new Intent(BorrowerListActivity.this, BorrowerProfileActivity.class);
                intent.putExtra("borrowerId", borrowerId);
                startActivity(intent);
            }
        });

        // Fetch borrower items from Firebase
        fetchBorrowerItemsFromFirebase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
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
            startActivity(new Intent(BorrowerListActivity.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(BorrowerListActivity.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(BorrowerListActivity.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(BorrowerListActivity.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(BorrowerListActivity.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchBorrowerItemsFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Borrowers");

            // Attach a listener to read the data from the specified DatabaseReference
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Clear the existing list
                    borrowerItems.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Borrower borrower = snapshot.getValue(Borrower.class);
                        if (borrower != null) {
                            borrowerItems.add(borrower);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
    }

    // Inner class for custom ArrayAdapter
    private class BorrowerListAdapter extends ArrayAdapter<Borrower> {

        public BorrowerListAdapter() {
            super(BorrowerListActivity.this, R.layout.list_item_borrower, borrowerItems);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_item_borrower, parent, false);
            }

            Borrower currentBorrower = getItem(position);

            if (currentBorrower != null) {
                TextView borrowerNameTextView = itemView.findViewById(R.id.borrowerNameTextView);
                borrowerNameTextView.setText(currentBorrower.getBorrowerName());

                ImageView dotImageView = itemView.findViewById(R.id.dotImageView);
                int dotColor = currentBorrower.getLoanStatus().equals("Active") ? R.drawable.green_dot : R.drawable.red_dot;
                dotImageView.setImageResource(dotColor);
            }

            return itemView;
        }
    }
}
