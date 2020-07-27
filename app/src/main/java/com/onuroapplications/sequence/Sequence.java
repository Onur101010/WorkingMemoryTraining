package com.onuroapplications.sequence;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.onuroapplications.memorytrainer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The user has the task to guess the right order of the shown sequence.
 * This class provides the creation of the UI and the logic for this task.
 */
public class Sequence extends AppCompatActivity {

    protected ArrayList<String> testListe; //hauptliste, gearbeitet wird aber nur mit copies
    protected ArrayList<String> copiedListe1; //arbeitsliste 1 (wegen sequence anzeigen)
    protected ArrayList<String> copiedListe2; //arbeitsliste 2 (wegen zufällig in listview setzen)
    protected ArrayList<String> sequenceOrderStore; //hier wird die Reihenfolge reingespeichert

    protected Button tglBtn;
    protected Button restartBtn;
    protected TextView text;
    protected TextView counterText;
    protected ListView listView;
    protected ArrayAdapter<String> arrayAdapter;

    protected String title = "sequence";

    //n gets set by user
    //it can be adjusted to max length of the datalist if needed
    //this is for example the last n elements to be recalled
    //in this class though it does nothing yet
    protected int n = 1; //TODO is it needed to be assigned in this class??

    //Counter
    protected int answerNumber;
    protected int sequenceOrderStorePositionCounter; //gibt Kontrolle darüber wann welches Element aus Speicher gecheckt wird

    //handler for delayed operations
    protected final Handler handler = new Handler();

    //animations
    protected Animation slideLeft;
    protected Animation slideRight;

    protected Boolean alternateAnimation = true; //for alternating right and left animations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set different transition for activity change
        //inside your activity (if you did not enable transitions in your theme)
        //this needs to be done before setting content
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set exit transition to null disables flash in transition
        //(do same with other activity with enter transition)
        getWindow().setExitTransition(null);

        setContentView(R.layout.activity_sequence);

        setSupportActionBarTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        //testlist comes out of the array which was sent via intent from MainActivity
        testListe = (ArrayList<String>) getIntent().getSerializableExtra("dataset");
        //two copies of the list allow further modification of the list without changing the dataset
        copiedListe1 = copyList(testListe);
        copiedListe2 = copyList(testListe);

        initCounterAndSeqStore();

        initListener();
    }

    protected void setSupportActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    protected void initViews() {
        listView = findViewById(R.id.xmlListView); //hier kommt später die liste rein
        tglBtn = findViewById(R.id.tgl2);
        counterText = findViewById(R.id.iterationCounter);
        text = findViewById(R.id.textView);
        restartBtn = findViewById(R.id.restart);

        slideLeft = AnimationUtils.loadAnimation(Sequence.this, R.anim.slide_left);
        slideRight = AnimationUtils.loadAnimation(Sequence.this, R.anim.slide_right);

        restartBtn.setVisibility(View.INVISIBLE);
        text.setVisibility(View.VISIBLE);
        counterText.setVisibility(View.INVISIBLE);
    }


    protected void initCounterAndSeqStore() {
        //dient zum zufälligem hinzufügen in liste nacher bei initShuffleList
        sequenceOrderStore = new ArrayList<>();
        sequenceOrderStorePositionCounter = 0;
        answerNumber = 1;
    }

    protected void initListener() {
        tglBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAndStoreSequence();
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartBtn.setVisibility(View.INVISIBLE);
                sequenceOrderStorePositionCounter = 0;
                answerNumber = 1;
                counterText.setText("name the " + answerNumber + ". element");
                text.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                sequenceOrderStore.clear(); //sequenceOrderStore muss auch resettet werden da man von neu startet
                copiedListe1 = copyList(testListe);
                copiedListe2 = copyList(testListe);
                tglBtn.setVisibility(View.VISIBLE);
            }
        });

    }


    /**
     * Updates the value of {@link Sequence#n} to the amount of
     * elements in the given list, if it was a higher number than that.
     */
    protected int cutToTestListSize(int x) {
        if(x > testListe.size()) {
            x = testListe.size();
        }
        return x;
    }

    /**
     * Shows an unique random list element in the textView and
     * decrements {@link Sequence#copiedListe1}
     * in every iteration as long it is not zero.
     * The shown sequence of elements in the textView is also stored in {@link Sequence#sequenceOrderStore}.
     * If counter is zero the textView disappears and {@link Sequence#initShuffledList()}
     * is called.
     */
    protected void showAndStoreSequence() {
        if(copiedListe1.size() > 0) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, copiedListe1.size());
            String randomElem = copiedListe1.get(randomInt);
            //TODO create method for setting text and do anim simultaneously
            //setTextAndAnim(randomElem, text, slideLeft);
            setTextAndAnim(randomElem, text, alternateAnimation);
            sequenceOrderStore.add(randomElem);
            copiedListe1.remove(randomElem); //dieses Elem löschen damit jedes nur einmal kommt
        } else {
            tglBtn.setVisibility(View.INVISIBLE);
            text.setText("");
            text.setVisibility(View.INVISIBLE);
            counterText.setVisibility(View.VISIBLE);

            initShuffledList();
        }
    }

    private void setTextAndAnim(String text, TextView v, Animation anim){
        v.setText(text);
        v.startAnimation(anim);
    }

    //sets right and left animation alternately and text to the TextView
    private void setTextAndAnim(String text, TextView v, Boolean chooseSlideRight) {
        v.setText(text);
        if(chooseSlideRight) {
            v.startAnimation(slideRight);
            alternateAnimation = false;
        } else {
            v.startAnimation(slideLeft);
            alternateAnimation = true;
        }
    }

    /**
     * Initializes a listView with randomly arranged elements.
     * Provides an OnItemClickListener to check if a clicked item is the
     * correct element in the current sequence.
     */
    protected void initShuffledList() {

        listView.setVisibility(View.VISIBLE);
        counterText.setText("name the " + answerNumber + ". element");

        Collections.shuffle(copiedListe2);
        arrayAdapter = new SequenceAdapter(
                Sequence.this, R.layout.list_item_sequence, copiedListe2);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sequenceOrderStorePositionCounter < sequenceOrderStore.size() - 1) { //es kann nicht mehr schritte geben als elemente
                    TextView textView = ((TextView) view);
                    validateAnswer(textView, position);
                } else if(sequenceOrderStorePositionCounter == sequenceOrderStore.size() - 1) { //letztes element -> zusätzlich reset button aktivieren
                    TextView textView = ((TextView) view);
                    if (validateAnswer(textView, position)) {
                        tglBtn.setVisibility(View.INVISIBLE);
                        counterText.setVisibility(View.INVISIBLE);
                        restartBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }



    /**
     * Checks if a enabled TextView has the same value as the stored value in {@link Sequence#sequenceOrderStore}
     * and updates counters and background colors.
     * @param textView, position
     * @return true if values match, false else
     */
    protected boolean validateAnswer(TextView textView, int position) {
        Log.i("testspeicher", "testspeicher hat wert " + sequenceOrderStore.get(sequenceOrderStorePositionCounter));
        Log.i("testspeicher", "geratener wert " + textView.getText());


            boolean test = sequenceOrderStore.get(sequenceOrderStorePositionCounter).equalsIgnoreCase((String) textView.getText());
            final int pos = position;
            final TextView tV = textView;

            if(!textView.getText().toString().equals("")) { //test only elements which are not done yet

                if(test) {

                    answerNumber += 1;
                    counterText.setText("name the " + answerNumber + ". element");
                    //Toast.makeText(getApplicationContext(), "richtig", Toast.LENGTH_SHORT).show();
                    textView.setBackgroundColor(Color.parseColor("#81c784"));
                    //the handling of the view gets delayed, otherwise color gets dropped immediately
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            copiedListe2.remove(pos);
                            copiedListe2.add(pos, "");
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }, 600);
                    sequenceOrderStorePositionCounter += 1; //nur wenn Antwort richtig ist soll man nächste position anschauen
                    return true;

                } else if(!test) {

                        textView.setBackgroundColor(Color.parseColor("#f44336"));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //back to normal color (textView needs to be final thats why different var is used)
                                tV.setBackgroundColor(Color.parseColor("#4dd0e1"));
                            }
                        }, 600);

                    return false;
                }
            }

        return false;
    }



    /**
     * copies the given list
     * @param list
     * @return the copied list
     */
    protected ArrayList<String> copyList(ArrayList<String> list) {
        ArrayList<String> copyList = new ArrayList<>(list.size());
        for(int i = 0; i < list.size(); i++) {
            copyList.add(list.get(i));
        }
        return copyList;
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
