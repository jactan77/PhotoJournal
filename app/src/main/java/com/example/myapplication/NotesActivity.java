package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class NotesActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextInputEditText notesEditText;
    private String currentImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        db = new DatabaseHelper(this);
        notesEditText = findViewById(R.id.notesEditText);
        ImageView selectedImage = findViewById(R.id.selectedImage);

        // Get URI from intent
        currentImageUri = getIntent().getStringExtra("image_uri");
        if (currentImageUri != null) {
            try {
                Uri imageUri = Uri.parse(currentImageUri);
                selectedImage.setImageURI(imageUri);
                String existingNote = db.getNote(currentImageUri);
                notesEditText.setText(existingNote);
            } catch (Exception e) {
                Toast.makeText(this, "Error loading image: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_camera || itemId == R.id.navigation_dashboard) {
                saveNoteAndFinish();
                return true;
            } else if (itemId == R.id.navigation_notes) {
                Toast.makeText(this, "Already in Notes", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void saveNoteAndFinish() {
        if (notesEditText != null && currentImageUri != null) {
            String noteText = notesEditText.getText().toString();
            db.saveNote(currentImageUri, noteText);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        saveNoteAndFinish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        saveNoteAndFinish();
        super.onPause();
    }
}