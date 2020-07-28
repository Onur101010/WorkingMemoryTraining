package com.onuroapplications.memorytrainer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onuroapplications.runningMemorySpace.RunningMemory;
import com.onuroapplications.sequence.Sequence;
import com.onuroapplications.nback.NBackActivity;
import com.onuroapplications.sequenceBackwards.SequenceBackwards;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> myDataset;
    private Button addBtn;
    private EditText editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn = (Button) findViewById(R.id.addButton);
        editText = (EditText) findViewById(R.id.editText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //attach the ItemTouchHelper for swipe deletion
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        myDataset = new ArrayList<>();

        //read saved Array if there is one
        if(InputOutput.read(InputOutput.INPUT_LIST,this) != null) {
            myDataset = InputOutput.read(InputOutput.INPUT_LIST,this);
        }

        //layoutmanager for recyclerview
        linLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linLayoutManager);

        //adapter for recyclerview
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding the text to the arraylist and informing adapter
                //TODO - add to stored Array
                String text = editText.getText().toString();
                if(!text.equals("")) {
                    myDataset.add(text);
                    mAdapter.notifyItemInserted(myDataset.size() - 1);
                    editText.setText("");
                }
            }
        });
    }

    //if app gets killed store the array with InputOutput
    @Override
    protected void onPause() {
        super.onPause();
        InputOutput.write(myDataset, InputOutput.INPUT_LIST, this);
    }

    /**
     * Sets up the ItemTouchHelper Callback which can be used in the recyclerView
     * for example for swipe deletion
     * @return the itemTouchHelper (simple) Callback
     */
    private ItemTouchHelper.SimpleCallback getItemTouchHelperCallback() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        myDataset.remove(viewHolder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };
        return itemTouchHelperCallback;
    }

    /**
     * Defines what menu layout is loaded at the top right
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_game, menu);
        return true;
    }

    /**
     * Defines what actions happen when an item is clicked.
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.n_back:
                if(myDataset.size() < 2) {
                    Toast.makeText(getApplicationContext(), "datalist size has to be atleast 2 elements", Toast.LENGTH_SHORT).show();
                    break;
                }
                openActivity(NBackActivity.class);
                break;
            case R.id.sequence:
                if(myDataset.size() < 2) {
                    Toast.makeText(getApplicationContext(), "datalist size has to be atleast 2 elements", Toast.LENGTH_SHORT).show();
                    break;
                }
                openActivity(Sequence.class);
                break;
            case R.id.sequenceBackwards:
                if(myDataset.size() < 2) {
                    Toast.makeText(getApplicationContext(), "datalist size has to be atleast 2 elements", Toast.LENGTH_SHORT).show();
                    break;
                }
                openActivity(SequenceBackwards.class);
                break;
            case R.id.runningMemory:
                if(myDataset.size() < 2) {
                    Toast.makeText(getApplicationContext(), "datalist size has to be atleast 2 elements", Toast.LENGTH_SHORT).show();
                    break;
                }
                openActivity(RunningMemory.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Opens the Activity for a game
     * and sends the array created by the user
     * to the new activity
     */
    private void openActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra("dataset", myDataset);
        startActivity(intent); //TODO send array
    }
}
