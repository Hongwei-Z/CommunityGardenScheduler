package com.example.myfirstapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.os.Bundle;
import android.app.Dialog;

public class UpdateDialog extends AppCompatDialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle saved){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("TASK UPDATED").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();

    }
}
