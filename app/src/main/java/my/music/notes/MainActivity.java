package my.music.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> exampleItems;

    private RecyclerView recyclerView;
    private ExampleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button btnInsert;
    private Button btnRemove;
    private TextView txtInsert;
    private TextView txtRemove;
    @Override
    protected void onResume() {
        super.onResume();

        createExampleList();
        buildRecyclerView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        buildRecyclerView();

        setButtons();
    }

    private void setButtons() {
        txtInsert = findViewById(R.id.edittext_insert);
        txtRemove = findViewById(R.id.edittext_remove);

        btnInsert = findViewById(R.id.button_insert);
        btnRemove = findViewById(R.id.button_remove);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(txtInsert.getText().toString());
                insertItem(position);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(txtRemove.getText().toString());
                removeItem(position);
            }
        });
    }

    public void insertItem(int position){
        this.exampleItems.add(position, new ExampleItem(R.drawable.ic_android, "New item at posotion" + position, "This is line 2"));
        this.adapter.notifyItemInserted(position);

        clearFile();
        writeAllSongsToFile();
    }

    public void removeItem(int position){
        this.exampleItems.remove(position);
        this.adapter.notifyItemRemoved(position);

        clearFile();
        writeAllSongsToFile();
    }

    private void writeAllSongsToFile() {
        for (int index = 0; index < exampleItems.size(); index++){
            writeToFile(exampleItems.get(index).getText2(), this);
        }
    }

    private void clearFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // won't change in size

        layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnclickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                changeItem(position);
            }

            @Override
            public void OnDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void changeItem(int position){
        exampleItems.get(position).changeText1("Clicked");
        adapter.notifyItemChanged(position);
    }
    private void createExampleList() {
        exampleItems = new ArrayList<>();
        readFromFile(this);
//        exampleItems.add(new ExampleItem(R.drawable.ic_android, "Line 3", "Line 4"));
//        exampleItems.add(new ExampleItem(R.drawable.ic_android, "Line 5", "Line 6"));
    }
    private String readFromFile(Context context) {

        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                    exampleItems.add(new ExampleItem(R.drawable.ic_music, "Singer", receiveString));
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
