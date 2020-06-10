package com.example.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class edittaskdetails extends MainActivity {


    private Button backtodetail;
    private Button updatedetailinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittaskdetails);

        backtodetail = (Button)findViewById(R.id.backtodetailbutton);
        backtodetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Backtodetail();
            }
        });

        updatedetailinfo = (Button)findViewById(R.id.EditUpdate);
        updatedetailinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void Backtodetail(){
        Intent intent = new Intent(this, TaskDetailInfo.class);
        startActivity(intent);
    }

    public void openDialog(){
        UpdateDialog updateDialog = new UpdateDialog();
        updateDialog.show(getSupportFragmentManager(), "update dialog");
    }
}