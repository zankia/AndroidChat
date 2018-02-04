package fr.zankia.android.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ValueEventListener, View.OnClickListener, MessageAdapter.Listener {

    private DatabaseReference mDatabaseReference;
    private MessageAdapter mMessageAdapter;
    private EditText mInputEditText;
    private RecyclerView mrecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!UserStorage.isUserLoggedIn(this)) {
            logout();
        }

        setContentView(R.layout.activity_main);

        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        mDatabaseReference.addValueEventListener(this);

        mrecyclerView = findViewById(R.id.recyclerView);
        this.mMessageAdapter = new MessageAdapter(
                this,
                new ArrayList<Message>(),
                UserStorage.getUserInfo(this)
        );
        mrecyclerView.setAdapter(mMessageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mrecyclerView.setLayoutManager(layoutManager);

        this.mInputEditText = findViewById(R.id.inputEditText);
        findViewById(R.id.inputButton).setOnClickListener(this);
    }

    private void logout() {
        Intent intent = new Intent(this, NamePickerActivity.class);
        this.startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                UserStorage.removeUserInfo(this);
                logout();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().goOffline();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().goOnline();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(MainActivity.class.getName(), "onDataChange: " + dataSnapshot);
        List<Message> items = new ArrayList<>();
        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Message message = postSnapshot.getValue(Message.class);
            message.setKey(postSnapshot.getKey());
            items.add(message);
        }
        mMessageAdapter.setData(items);
        mrecyclerView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(MainActivity.class.getName(), "onCancelled: " + databaseError);
    }

    @Override
    public void onClick(View view) {
        User user = UserStorage.getUserInfo(this);
        String message = mInputEditText.getText().toString();
        if(message.isEmpty() || user == null) {
            return;
        }
        DatabaseReference dbRef = mDatabaseReference.push();
        dbRef.setValue(new Message(
                message,
                user.getName(),
                user.getEmail(),
                System.currentTimeMillis()));
        mInputEditText.setText("");
    }

    @Override
    public void onItemLongClick(int position, final Message message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle(R.string.actions)
                .setItems(R.array.messageActions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                break;

                            case 1:
                                showInformations(message);
                                break;

                            case 2:
                                mDatabaseReference.child(message.getKey()).removeValue();
                                break;
                        }
                    }
                });
        alertBuilder.show();
    }

    private void showInformations(Message message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.details)
                .setMessage(
                        getString(R.string.username) + ": " + message.getUserName() + '\n'
                        + getString(R.string.userEmail) + ": " + message.getUserEmail() + '\n'
                        + getString(R.string.date) + ": " + new Date(message.getTimestamp())
                )
                .show();
    }
}
