package com.prototype_1.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
   static ArrayList<String> notes=new ArrayList<>();
   static ArrayAdapter arrayAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.addnote)
        {
            Intent intent=new Intent(MainActivity.this,NoteEditorActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getSharedPreferences("com.prototype_1.notes",Context.MODE_PRIVATE);
        HashSet<String> set= (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set==null){
        notes.add("Example Note");}
        else {
            notes=new ArrayList<>(set);
        }

        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,notes);

        ListView listView=findViewById(R.id.listview);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(MainActivity.this,NoteEditorActivity.class);
                intent.putExtra("plainId",position);
                startActivity(intent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.alert_dark_frame)
                        .setTitle("Are you sure?").
                        setMessage("Do you want to delete this note?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                       // SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.prototype_1.notes", Context.MODE_PRIVATE);
                        HashSet<String> set=new HashSet<>(MainActivity.notes);
                        sharedPreferences.edit().putStringSet("notes",set).apply();
                    }
                }).setNegativeButton("No",null).show();
                return true;
            }
        });

    }
}