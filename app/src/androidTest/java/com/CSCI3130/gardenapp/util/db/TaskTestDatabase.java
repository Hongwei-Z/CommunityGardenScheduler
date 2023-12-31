package com.CSCI3130.gardenapp.util.db;

import androidx.annotation.NonNull;
import com.CSCI3130.gardenapp.db.TaskDatabase;
import com.CSCI3130.gardenapp.util.data.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.junit.Assert;

import java.util.Random;

import static org.junit.Assert.fail;

/**
 * Utility test wrapper for DatabaseTaskWriter to ensure tasks are written under a unique
 * test branch.
 *
 * @author Liam Hebert
 * @see TaskDatabase
 */
public class TaskTestDatabase extends TaskDatabase {
    private final String test;

    /**
     * Constructor that injects a modified database into DatabaseTaskWriter
     */
    public TaskTestDatabase() {
        super(FirebaseDatabase.getInstance().getReference());
        Random rand = new Random();
        test = "test-" + rand.nextInt();
        dbWrite = dbWrite.getRoot().child(test).child("Tasks");
        dbRead = dbWrite;
    }

    /**
     * Removes the autogenerated test tree in the database and all associated childs.
     * Should be used in the @After section of a test
     */
    public void clearDatabase() {
        dbWrite.getRoot().child(test).removeValue();
    }

    public void checkForTask(Task task) {
        final boolean[] flag = {false};
        dbRead.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Task got = child.getValue(Task.class);
                    Assert.assertEquals("\nExpected: " + task.toString() + "\nGot: " + got.toString(), task, got);
                }

                flag[0] = (dataSnapshot.getChildrenCount() == 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fail();
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        Assert.assertTrue(flag[0]);
    }

    public void ensureNoDatabaseActivity() {
        dbRead.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    fail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fail();
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
