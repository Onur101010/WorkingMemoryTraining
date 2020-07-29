package com.onuroapplications.nback;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.onuroapplications.animation.AnimationManager;
import com.onuroapplications.memorytrainer.R;

import java.util.ArrayList;
import java.util.Random;

public class NBackActivity extends AppCompatActivity {
    private ArrayList<String> testListe;
    private ListView listView;
    private int rightCounter;
    private int falseCounter;
    private TextView rightCounterTextView;
    private TextView falseCounterTextView;
    private Button tglBtn;
    private Button endBtn;
    private Button elemBtn;
    private int elemBtnClickCounter;
    private Button posBtn;
    private int posBtnClickCounter;
    private ArrayList<StringInt> speicher = new ArrayList<StringInt>();
    private AnimationManager animationManager = AnimationManager.getInstance();;


    private int n = 1;
    private static final String TAG = "NBackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nback);
        getSupportActionBar().setTitle("N-Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //listview welches wir erstellt haben
        listView = findViewById(R.id.xmlListView);
        //richtig falsch counter und die Textviews
        rightCounter = 0;
        falseCounter = 0;
        rightCounterTextView = findViewById(R.id.richtig);
        falseCounterTextView = findViewById(R.id.falsch);
        //buttons zuweisen:
        tglBtn = findViewById(R.id.toggleBtn);
        endBtn = findViewById(R.id.endButton);
        posBtn = findViewById(R.id.positionBtn);
        posBtnClickCounter = 0;
        elemBtn = findViewById(R.id.elementBtn);
        elemBtnClickCounter = 0;

        animationManager.addAnimation(R.anim.fade_in, "fade_in", NBackActivity.this);

        testListe = (ArrayList<String>) getIntent().getSerializableExtra("dataset");


        //initialisier erstmal leere liste mit 5 Elementen als Startpunkt
        //TODO - magic number entfernen
        initEmptyArrayAdapter(5);

        //setze zufälligen wert an zufällige position
        initTglButton();

        //init posbutton
        initPosButton();

        //init elementbutton
        initElemButton();

        //init endbutton
        initEndButton();
    }



    private void initTglButton() {
        tglBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllElements();
                posBtn.setBackgroundColor(Color.parseColor("#ffd6d7d7"));
                elemBtn.setBackgroundColor(Color.parseColor("#ffd6d7d7"));

                //counter von antwort buttons resetten
                posBtnClickCounter = 0;
                elemBtnClickCounter = 0;

                //zufällige Position finden, dann zufälliges Element hereinsetzen
                final Random random = new Random();
                int randomIntPos = random.nextInt(5); //zufällige Zahl zwischen 0-4 bzw init
                Log.i(TAG, "es wird Position " + randomIntPos + " geupdatet");
                TextView randomIndexTV = getViewAt(randomIntPos);
                int randomIntElem = random.nextInt(testListe.size()); //zufällige Zahl von 0-length
                Log.i(TAG, "es wird Element  " + testListe.get(randomIntElem) + " hereingesetzt");
                randomIndexTV.setText(testListe.get(randomIntElem));
                animationManager.executeStoredAnimation("fade_in", randomIndexTV);


                //lade Position und Inhalt in Speicher (als Tupel)
                speicher.add(new StringInt(testListe.get(randomIntElem), randomIntPos));
                //löscht alles was vor dem getestetem element ist -> spart speicher
                for(int i = 0; i < (speicher.size() - 1) - n; i++){
                    speicher.remove(i);
                    //TODO - was wenn Bedingung noch negativ ist -> sollte nichts passieren: prüfen
                }
                //false counter aktualisieren falls was zutrifft man aber weiter

                //nur für console zum testen
                for(StringInt elem : speicher) {
                    String element = elem.getString();
                    int pos = elem.getInt();
                    Log.i(TAG, "Speicherelement: " + element + pos);
                }
            }
        });

    }


    private void initPosButton() {
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posBtnClickCounter == 0) {
                    if(speicher.size() >= 2 && speicher.size() == n + 1) { //speicher muss min 2 elemente haben und immer n + 1 sein konstant
                        int lastIndex = speicher.size() - 1; // current index
                        int currentPosition = speicher.get(lastIndex).getInt();
                        //because all other values are deleted this is current - n index:
                        int firstPosition = speicher.get(0).getInt();
                        Log.i(TAG, "onClick posBtn current= " + currentPosition);
                        Log.i(TAG, "onClick posBtn first= " + firstPosition);
                        if(currentPosition == firstPosition) {
                            Log.d(TAG, "onClick posBtn: same position -> right");
                            posBtn.setBackgroundColor(Color.parseColor("#81c784"));
                            rightCounter += 1;
                            rightCounterTextView.setText("" + rightCounter);
                            posBtnClickCounter += 1;
                            return;
                        } else {
                            Log.d(TAG, "onClick posBtn: not same position -> false");
                            posBtn.setBackgroundColor(Color.parseColor("#f44336"));
                            falseCounter += 1;
                            falseCounterTextView.setText("" + falseCounter);
                            posBtnClickCounter += 1;
                            return;
                        }
                    }
                    Log.d(TAG, "onClick posBtn: to few elements in list -> false");
                    posBtn.setBackgroundColor(Color.parseColor("#f44336"));
                    falseCounter += 1;
                    falseCounterTextView.setText("" + falseCounter);
                    posBtnClickCounter += 1;
                    return;
                }
                Log.d(TAG, "onClick posBtn: double action not allowed!");
            }
        });
    }

    private void initElemButton() {
        elemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elemBtnClickCounter == 0) {
                    if (speicher.size() >= 2 && speicher.size() == n + 1) { //speicher muss min 2 elemente haben und immer n + 1 sein konstant
                        int lastIndex = speicher.size() - 1; // current index
                        String currentPositionElement = speicher.get(lastIndex).getString();
                        //because all other values are deleted this is (current - n). index:
                        String firstPositionElement = speicher.get(0).getString();
                        Log.i(TAG, "onClick elemBtn current= " + currentPositionElement);
                        Log.i(TAG, "onClick elemBtn first= " + firstPositionElement);
                        if(currentPositionElement.equals(firstPositionElement)) {
                            Log.d(TAG, "onClick elemBtn: same position -> right");
                            elemBtn.setBackgroundColor(Color.parseColor("#81c784"));
                            rightCounter += 1;
                            rightCounterTextView.setText("" + rightCounter);
                            elemBtnClickCounter += 1;
                            return;
                        } else {
                            Log.d(TAG, "onClick elemBtn: not same value -> false");
                            elemBtn.setBackgroundColor(Color.parseColor("#f44336"));
                            falseCounter += 1;
                            falseCounterTextView.setText("" + falseCounter);
                            elemBtnClickCounter += 1;
                            return;
                        }
                    }
                    Log.d(TAG, "onClick elemBtn: to few elements in list -> false");
                    elemBtn.setBackgroundColor(Color.parseColor("#f44336"));
                    falseCounter += 1;
                    falseCounterTextView.setText("" + falseCounter);
                    elemBtnClickCounter += 1;
                    return;
                }

                Log.d(TAG, "onClick elemBtn: double action not allowed");
            }
        });
    }

    /**
     * Initializes the end game button
     */
    private void initEndButton() {
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

    }

    /**
     * End current game and reset all stats.
     */
    private void reset() {
        speicher.clear();
        clearAllElements();
        posBtn.setBackgroundColor(Color.parseColor("#ffd6d7d7"));
        elemBtn.setBackgroundColor(Color.parseColor("#ffd6d7d7"));
        rightCounter = 0;
        falseCounter = 0;
        rightCounterTextView.setText("" + rightCounter);
        falseCounterTextView.setText("" + falseCounter);
        Log.i(TAG, "Spiel wurde beendet");
        //TODO make toast!
    }

    /**
     * Initialize empty listview with capacity empty elements.
     * @param capacity
     */
    private void initEmptyArrayAdapter(int capacity) {
        //initialisier erstmal leere liste mit 5 Elementen als Startpunkt
        //TODO - magic number entfernen
        ArrayList<String> shoppingList = new ArrayList<>();
        initEmptyArrList(shoppingList, capacity);
        //Adapter zuweisen
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                NBackActivity.this, R.layout.list_item_nback, shoppingList);
        listView.setAdapter(arrayAdapter);
    }

    //alle elemente aus liste löschen
    private void clearAllElements() {
        for(int r = 0; r < listView.getChildCount(); r++) {
            //Log.i(TAG, "Anzahl Kinder ist: " + listView.getChildCount());
            getViewAt(r).setText("");
        }
    }

    /**
     * Initialize empty ArrayList with String type of capacity elements.
     * @param arrayList
     * @param capacity
     */
    private void initEmptyArrList(ArrayList<String> arrayList, int capacity) {
        for(int i = 0; i < capacity; i++) {
            arrayList.add("");
        }
    }

    //aus "How can I update..." stackoverflow
    private TextView getViewAt(int index){
        //getView auf richtiger Position
        View v = listView.getChildAt(index);

        //von dem View (Listenelement) noch unser Textview spezifisch rausholen
        TextView vTextView = v.findViewById(R.id.appTextView);
        return vTextView;
    }

    /**
     * Defines what menu layout is loaded at the top right
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.n_back_setn, menu);
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

        //end current game before setting n
        reset();
        switch (id) {
            case R.id.nback1:
                item.setChecked(true);
                this.n = 1;
                break;
            case R.id.nback2:
                item.setChecked(true);
                this.n = 2;
                break;
            case R.id.nback3:
                item.setChecked(true);
                this.n = 3;
                break;
            case R.id.nback4:
                item.setChecked(true);
                this.n = 4;
                break;
            case R.id.nback5:
                item.setChecked(true);
                this.n = 5;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //here some action can be done before leaving activity
        super.onBackPressed();
    }

    //oberes back button macht genau dasselbe wie onBackPressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
