package com.example.chongchenlearn901.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {
    private static final String TAG = "NoteActivity";
    private EditText etNote;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        etNote = findViewById(R.id.etNote);
        Intent intent = getIntent();
        this.index = intent.getIntExtra(MainActivity.INTENT_INDEX, -1);
        if(this.index == -1){
            return;
        }

        etNote.setText(MainActivity.notes.get(index));

    }

    @Override
    protected void onPause() {
        super.onPause();

        String note = this.etNote.getText().toString();
        if(TextUtils.isEmpty(note) && index == -1){
            return;
        }
        if(this.index == -1){
            MainActivity.notes.add(note);
        }else{
            MainActivity.notes.set(index, note);
        }

        MainActivity.saveNotes();

        MainActivity.adapter.notifyDataSetChanged();
    }
}
