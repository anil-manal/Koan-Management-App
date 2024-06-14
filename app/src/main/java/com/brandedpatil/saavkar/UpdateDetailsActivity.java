package com.brandedpatil.saavkar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.brandedpatil.saavkar.databinding.ActivityNotificationsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class UpdateDetailsActivity extends AppCompatActivity {

    private Spinner spinnerUpdateFields;
    private EditText editTextUpdatedValue;
    private Button btnUpdate;
    private String borrowerId;
    private String borrowerName;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth; // Add this line

    private ActivityNotificationsBinding binding;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> borrowerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Initialize TextViews for the new cards

        // Initialize views
        spinnerUpdateFields = findViewById(R.id.spinnerUpdateFields);
        editTextUpdatedValue = findViewById(R.id.editTextUpdatedValue);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance(); // Add this line

        // Get borrowerId and borrowerName from the Intent
        borrowerId = getIntent().getStringExtra("borrowerId");
        borrowerName = getIntent().getStringExtra("borrowerName");

        // Set the borrowerName to the TextView
        TextView txtBorrowerName = findViewById(R.id.txtBorrowerName);

        // Check for null to avoid NullPointerException
        if (txtBorrowerName != null && borrowerName != null) {
            txtBorrowerName.setText(borrowerName);
        }

        // Initialize Spinner with update fields
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.update_fields, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdateFields.setAdapter(adapter);

        // Set the onClickListener for the Update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateButtonClick(view);
            }
        });
    }

    // Add your logic to update the details based on the selected field
    // For example, update the details in the Firebase database
    public void onUpdateButtonClick(View view) {
        // Get the selected field from the spinner
        String selectedField = spinnerUpdateFields.getSelectedItem().toString();

        // Get the updated value from the EditText
        String updatedValue = editTextUpdatedValue.getText().toString().trim();

        // Check if the updated value is not empty
        if (updatedValue.isEmpty()) {
            Toast.makeText(this, "Please enter the updated value", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for null to avoid NullPointerException
        if (firebaseAuth.getCurrentUser() != null) { // Use firebaseAuth here
            // Update the details based on the selected field
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(firebaseAuth.getCurrentUser().getUid()) // Use firebaseAuth here
                    .child("Borrowers")
                    .child(borrowerId);

            // Update the database
            databaseReference.child(selectedField).setValue(updatedValue);

            // Display a success message
            Toast.makeText(this, "Details updated successfully", Toast.LENGTH_SHORT).show();

            // Finish the activity
            finish();
        } else {
            // Handle the case where the current user is null
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(UpdateDetailsActivity.this, Dashboard.class));
        } else if (itemId == R.id.nav_add_borrower) {
            startActivity(new Intent(UpdateDetailsActivity.this, AddBorrowerActivity.class));
        } else if (itemId == R.id.nav_borrower_list) {
            startActivity(new Intent(UpdateDetailsActivity.this, BorrowerListActivity.class));
        } else if (itemId == R.id.nav_collection) {
            startActivity(new Intent(UpdateDetailsActivity.this, CollectionActivity.class));
        } else if (itemId == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(UpdateDetailsActivity.this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
