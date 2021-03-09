package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener {
    ListView chatListView;
    ArrayList<String> chatList;
    ArrayAdapter adapter;
    String userSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        userSelected = getIntent().getStringExtra("SelectedUserName");
        Toast.makeText(Chat.this,userSelected,Toast.LENGTH_LONG).show();
           findViewById(R.id.btnSend).setOnClickListener(this);
           chatListView = findViewById(R.id.chatList);
           chatList = new ArrayList<String>();
           adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,chatList);
           chatListView.setAdapter(adapter);
           try {
               ParseQuery<ParseObject> firstParseQuery= new ParseQuery<ParseObject>("Chat");
               ParseQuery<ParseObject> secondParseQuery= new ParseQuery<ParseObject>("Chat");

               firstParseQuery.whereEqualTo("WaSender",ParseUser.getCurrentUser().getUsername());
               firstParseQuery.whereEqualTo("waTarget",userSelected);

               secondParseQuery.whereEqualTo("WaSender",userSelected);
               secondParseQuery.whereEqualTo("waTarget",ParseUser.getCurrentUser().getUsername());

               ArrayList <ParseQuery<ParseObject>> allQuery = new ArrayList<>();
               allQuery.add(firstParseQuery);
               allQuery.add(secondParseQuery);
               ParseQuery<ParseObject> myQuery = ParseQuery.or(allQuery);
               myQuery.orderByAscending("createdAt");


               myQuery.findInBackground(new FindCallback<ParseObject>() {
                   @Override
                   public void done(List<ParseObject> objects, ParseException e) {
                       if (objects.size() > 0 && e == null) {

                           for (ParseObject chatObject : objects) {

                               String waMessage = chatObject.get("waMessage") + "";

                               if (chatObject.get("WaSender").equals(ParseUser.getCurrentUser().getUsername())) {

                                   waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                               }
                               if (chatObject.get("WaSender").equals(userSelected)) {

                                   waMessage = userSelected + ": " + waMessage;
                               }

                               chatList.add(waMessage);
                           }
                           adapter.notifyDataSetChanged();


                       }
                   }
               });



           }catch (Exception e){
               e.printStackTrace();
           }

    }

    @Override
    public void onClick(View v) {
         final EditText edtChat = findViewById(R.id.edtChat);
        ParseObject chat = new ParseObject("Chat");
        chat.put("WaSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTarget",userSelected);
        chat.put("waMessage",edtChat.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    chatList.add(ParseUser.getCurrentUser().getUsername()+": "+edtChat.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtChat.setText("");
                }
            }
        });
    }
}