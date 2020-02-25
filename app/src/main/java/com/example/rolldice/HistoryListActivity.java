package com.example.rolldice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryListActivity extends AppCompatActivity {

    private ArrayList<Roll> results;
    private RollAdapter arrayAdapter;
    private ListView listView;

    private void setupOnItemClickEvent()
    {
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Roll roll = results.get(position);
            String text;
            if(roll.getScore() / roll.getDices().size() > 3){
                text = "That was Super Amazing Roll";
            }
            else
                text = "That was pretty lame Roll";

            Toast.makeText(this, text,
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshListView()
    {
        //Create new adapter
        arrayAdapter = new RollAdapter(this,results);

        //Set new adapter
        listView.setAdapter(arrayAdapter);
    }

    private void setupListView()
    {
        listView = findViewById(R.id.listView);

        setupOnItemClickEvent();

        //Get rolls from intent
        results = (ArrayList<Roll>) getIntent().getSerializableExtra("rolls");

        refreshListView();
    }

    private void close()
    {
        Intent data = new Intent();
        data.putExtra("rolls",results);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        setupListView();
        findViewById(R.id.btnClose).setOnClickListener((View v)->close());
        findViewById(R.id.btnClearHistory).setOnClickListener((View v)->clearHistory());
    }

    private void clearHistory() {
        this.results.clear();
        refreshListView();
    }

}


class RollAdapter extends ArrayAdapter<Roll> {

    private ArrayList<Roll> rolls;

    public RollAdapter(@NonNull Context context, @NonNull ArrayList<Roll> rolls) {
        super(context, 0, rolls);
        this.rolls = rolls;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if(view == null)
        {
            //Obtain inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_view_record,null);
        }

        Roll roll = rolls.get(position);

        TextView txtScore = view.findViewById(R.id.txtScore);
        txtScore.setText("Score: " + roll.getScore());


        LinearLayout dicesLayout = view.findViewById(R.id.layDicesListItem);
        List<Dice> dices = roll.getDices();
        dicesLayout.removeAllViews();
        for (Dice dice :dices) {
            DiceView dw = new DiceView(getContext(),dice);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(5,5,5,5);
            dw.setLayoutParams(params);
            dicesLayout.addView(dw);
        }

        return view;
    }
}
