package com.manager.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.manager.myapplication.R;
import com.manager.myapplication.adapter.ChatAdapter;
import com.manager.myapplication.model.ChatMessage;
import com.manager.myapplication.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView imgsend;
    EditText edtMess;
    FirebaseFirestore db;
    ChatAdapter adapter;
    List<ChatMessage> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initControl();
        listenMess();
    }

    private void initControl() {
        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMesstoFire();
            }
        });
    }

    private void sendMesstoFire() {
        String str_mess = edtMess.getText().toString().trim();
        if(TextUtils.isEmpty(str_mess)){

        }else {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Utils.SENDID, Utils.user_current.getId());
            message.put(Utils.RECEIVEDID, Utils.ID_RECIVER);
            message.put(Utils.MESS, str_mess);
            message.put(Utils.DATETIME, new Date());
            db.collection(Utils.PATH_CHAT).add(message);
            edtMess.setText("");
        }
    }
    // Load du lieu so sanh de gui nguoc lai
    private  void listenMess(){
        db.collection(Utils.PATH_CHAT).whereEqualTo(Utils.SENDID, String.valueOf(Utils.user_current.getId()))
                .whereEqualTo(Utils.RECEIVEDID, Utils.ID_RECIVER)
                .addSnapshotListener(eventListener);
        db.collection(Utils.PATH_CHAT).whereEqualTo(Utils.SENDID, Utils.ID_RECIVER)
                .whereEqualTo(Utils.RECEIVEDID, String.valueOf(Utils.user_current.getId()))
                .addSnapshotListener(eventListener);
    }
    // xử lí dữ liệu từ firebase vào list message
    private  final EventListener<QuerySnapshot> eventListener = (value, error)->{
        if(error != null){
            return;
        }
        if(value != null){
            int count = list.size();
            for(DocumentChange documentChange :value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.sendid = documentChange.getDocument().getString(Utils.SENDID);
                    chatMessage.receviedid = documentChange.getDocument().getString(Utils.RECEIVEDID);
                    chatMessage.mess = documentChange.getDocument().getString(Utils.MESS);
                    chatMessage.dateOj = documentChange.getDocument().getDate(Utils.DATETIME);
                    chatMessage.datetime = format_date(documentChange.getDocument().getDate(Utils.DATETIME));
                    list.add(chatMessage);
                }
            }
            Collections.sort(list, (obj1, obj2)->obj1.datetime.compareTo(obj2.datetime));
            if(count == 0){
                adapter.notifyDataSetChanged();
            }else{
                adapter.notifyItemRangeInserted(list.size(), list.size());
                recyclerView.smoothScrollToPosition(list.size() - 1);
            }
        }
    };
    // chuyen dinh dang ngay gio
    private String format_date(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy- hh:mm a", Locale.getDefault()).format(date);
    }

    private void initView() {
        list = new ArrayList<>();
        // khoi tao firebase de truyen du lieu
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycle_chat);
        imgsend = findViewById(R.id.imgchat);
        edtMess = findViewById(R.id.edtinputchat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(getApplicationContext(), list, Utils.user_current.getId());
        recyclerView.setAdapter(adapter);
    }
}