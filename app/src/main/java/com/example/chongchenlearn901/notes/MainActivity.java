package com.example.chongchenlearn901.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String SP_TAG = "com.example.chongchenlearn901.notes";
    public static final String TAG_NOTES = "notes";

    public static final String INTENT_INDEX = "index";
    public static ArrayList<String> notes;
    public static SharedPreferences sharedPreferences;
    public static ArrayAdapter<String> adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
        String spNotes = sharedPreferences.getString(TAG_NOTES, "");

        if(TextUtils.isEmpty(spNotes)){
            notes = new ArrayList<>();
        }else{
            try {
                notes = (ArrayList<String>) ObjectSerializer.deserialize(spNotes);
            } catch (Exception e) {
                Log.e(TAG, "deserialize notes had an error: ", e);
            }
        }
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
            intent.putExtra(INTENT_INDEX, position);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete Note")
                    .setMessage("Do you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        notes.remove(position);
                        saveNotes();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.menuAddNote:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
            default:
                result = false;
        }
        return result;
    }

    public static void saveNotes(){
        try {
            MainActivity.sharedPreferences.edit().putString(MainActivity.TAG_NOTES, ObjectSerializer.serialize(MainActivity.notes)).apply();
        } catch (Exception e) {
            Log.e(TAG, "saveNotes: ", e);
        }
    }
}
