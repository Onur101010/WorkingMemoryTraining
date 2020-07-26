package com.onuroapplications.runningMemorySpace;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.onuroapplications.memorytrainer.R;
import com.onuroapplications.sequence.Sequence;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class RunningMemory extends Sequence {

    private int randomStop; //random stopping point of shown sequence
    private int randomStopListSize; //how many elements the shown sequence has

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = "running memory span";
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initCounterAndSeqStore() {
        super.initCounterAndSeqStore();
        randomStopListSize = 0;
        //random stopping point of sequence is atleast n because you can't
        //guess more elements than the sequence will contain
        randomStop = ThreadLocalRandom.current().nextInt(n, testListe.size() + 1);
    }

    @Override
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
                randomStopListSize = 0; //the counter for the listlength of the sequence is also reset
                randomStop = ThreadLocalRandom.current().nextInt(n, testListe.size() + 1); //TODO further testing if interval is correct

                restartBtn.setVisibility(View.INVISIBLE);
                sequenceOrderStorePositionCounter = 0;
                answerNumber = 1;
                //counterText.setText("name the " + answerNumber + ". element"); TODO is it needed here?
                text.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                sequenceOrderStore.clear(); //sequenceOrderStore muss auch resettet werden da man von neu startet
                copiedListe1 = copyList(testListe);
                copiedListe2 = copyList(testListe);
                tglBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void showAndStoreSequence() {
        if(randomStop > 0) {
            Random random1 = new Random();
            int randomInt = random1.nextInt(copiedListe1.size());
            String randomElem = copiedListe1.get(randomInt);
            text.setText(randomElem);
            sequenceOrderStore.add(randomElem);
            copiedListe1.remove(randomElem); //dieses Elem löschen damit jedes nur einmal kommt

            //now the randomlistsize increments
            randomStopListSize = randomStopListSize + 1; //this will be the sequence length
            //and the randomStop decrements, it works as a counter
            randomStop = randomStop - 1;

        } else {
            tglBtn.setVisibility(View.INVISIBLE);
            text.setText("");
            text.setVisibility(View.INVISIBLE);
            counterText.setVisibility(View.VISIBLE);

            //If the counter reaches zero no element will be further added but the shuffled listview will be shown
            //before that we need to get the sequenceOrderStore position (the place where we start to test the answers) to the desired index.
            //That will be all elements of the sequence minus the desired n.
            //For example if the sequence will be 4 elements long and n is 2, you will have to guess the last 2 elements.
            sequenceOrderStorePositionCounter = randomStopListSize - n;
            //Also the counter showing which element of the sequence to guess needs to be updated because of the n.
            answerNumber = (randomStopListSize - n) + 1; //index starts at 1 (for user it is easier to understand)
            initShuffledList();
        }
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

        //do not reset before n is changed because it will init everything without the new n

        switch (id) {
            case R.id.nback1:
                //n = 1 is always possible because list has atleast 1 element
                item.setChecked(true);
                n = 1;
                reset();
                break;
            case R.id.nback2:
                if(isSmallerDataSet(2)) {
                    item.setChecked(true);
                    n = 2;
                    reset();
                    break;
                }
                Toast.makeText(getApplicationContext(), "n has to be smaller then the datalist size", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nback3:
                if(isSmallerDataSet(3)) {
                    item.setChecked(true);
                    n = 3;
                    reset();
                    break;
                }
                Toast.makeText(getApplicationContext(), "n has to be smaller then the datalist size", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nback4:
                if(isSmallerDataSet(4)) {
                    item.setChecked(true);
                    n = 4;
                    reset();
                    break;
                }
                Toast.makeText(getApplicationContext(), "n has to be smaller then the datalist size", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nback5:
                if(isSmallerDataSet(5)) {
                    item.setChecked(true);
                    n = 5;
                    reset();
                    break;
                }
                Toast.makeText(getApplicationContext(), "n has to be smaller then the datalist size", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean isSmallerDataSet(int i) {
        if(i < testListe.size()) {
            return true;
        }
        return false;
    }

    private void reset() {
        restartBtn.setVisibility(View.INVISIBLE);
        sequenceOrderStorePositionCounter = 0;
        answerNumber = 1;
        text.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        sequenceOrderStore.clear(); //sequenceOrderStore muss auch resettet werden da man von neu startet
        copiedListe1 = copyList(testListe);
        copiedListe2 = copyList(testListe);
        tglBtn.setVisibility(View.VISIBLE);

        text.setText("");
        counterText.setText("");
        initCounterAndSeqStore();
        initListener();
    }
}
