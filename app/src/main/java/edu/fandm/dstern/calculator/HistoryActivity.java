package edu.fandm.dstern.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tianbai on 2/22/17.
 */

public class HistoryActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view for the history activity
        setContentView(R.layout.history_layout);

        Intent i = getIntent();
        // get the history data from intent
        ArrayList<String> history = i.getStringArrayListExtra("history");
        String[] historyArray = history.toArray(new String[0]);

        // set the views and put data into the views
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, historyArray);

        ListView listView = (ListView) findViewById(R.id.history_list);
        listView.setAdapter(adapter);



    }
}
