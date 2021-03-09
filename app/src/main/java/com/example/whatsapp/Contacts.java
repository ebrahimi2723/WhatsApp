package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Contacts extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayList<String> userList;
    ArrayAdapter arrayAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        listView = findViewById(R.id.list);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        userList = new ArrayList();
        arrayAdapter = new ArrayAdapter(Contacts.this, android.R.layout.simple_list_item_1,userList);
        listView.setOnItemClickListener(this);
       try {
           ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
           parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
           parseQuery.findInBackground(new FindCallback<ParseUser>(){

               @Override
               public void done(List<ParseUser> objects, ParseException e) {
                   if (objects.size()>0){
                       if (e==null){
                           for (ParseUser user:objects){
                               userList.add(user.getUsername());
                           }
                           listView.setAdapter(arrayAdapter);
                       }
                   }
               }
           });
         }catch (Exception e){
           e.printStackTrace();
       }



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",userList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>(){

                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size()>0){
                                if (e==null){
                                    for (ParseUser user:objects){
                                        userList.add(user.getUsername());
                                    }
                                    listView.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }else {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.LogOut){
            ParseUser.getCurrentUser().logOut();
            finish();
            startActivity(new Intent(Contacts.this,Login.class));
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Contacts.this,Chat.class);
        intent.putExtra("SelectedUserName",userList.get(position));
        startActivity(intent);

    }
}