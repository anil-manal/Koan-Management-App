package com.brandedpatil.saavkar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.IntentService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CollectionNotificationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "collection_notification_channel";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_notification);

        // Create notification channel
        createNotificationChannel();

        // Schedule background service
        scheduleNotificationService();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotificationService() {
        // Set the alarm to start the service at 9 AM every day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 31);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, CollectionNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static class CollectionNotificationService extends IntentService {

        public CollectionNotificationService() {
            super("CollectionNotificationService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            fetchBorrowersAndSendNotifications();
        }

        private void fetchBorrowersAndSendNotifications() {
            DatabaseReference borrowersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("8DTYNnAb0wgx0Pbmomvc1S4f6Dn1").child("Borrowers");

            borrowersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot borrowerSnapshot : dataSnapshot.getChildren()) {
                        Borrower borrower = borrowerSnapshot.getValue(Borrower.class);
                        if (borrower != null && isCollectionDay(borrower.getCollectionDay())) {
                            Log.d("CollectionNotification", "Sending notification for " + borrower.getBorrowerName());
                            sendNotification(borrower.getBorrowerName());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Log.e("CollectionNotification", "Failed to read value.", databaseError.toException());
                }
            });
        }

        private boolean isCollectionDay(int collectionDay) {
            Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("CollectionNotification", "Current day: " + currentDay + ", Collection day: " + collectionDay);
            return currentDay == collectionDay;
        }

        private void sendNotification(String borrowerName) {
            Intent intent = new Intent(getApplicationContext(), CollectionNotificationActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Loan Collection Reminder")
                    .setContentText("Today is the collection day for " + borrowerName)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
