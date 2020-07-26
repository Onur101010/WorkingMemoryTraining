package com.onuroapplications.sequenceBackwards;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.onuroapplications.memorytrainer.R;
import com.onuroapplications.sequence.Sequence;
import com.onuroapplications.sequence.SequenceAdapter;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class SequenceBackwards extends Sequence {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = "Sequence backwards";
        super.onCreate(savedInstanceState);
    }

    //am ende der Sequenz wird die Position ans Ende gesetzt, es wird also ab dem letzten Index getestet
    @Override
    protected void showAndStoreSequence() {
        if(copiedListe1.size() > 0) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, copiedListe1.size());
            String randomElem = copiedListe1.get(randomInt);
            text.setText(randomElem);
            sequenceOrderStore.add(randomElem);
            copiedListe1.remove(randomElem); //dieses Elem löschen damit jedes nur einmal kommt
        } else {
            tglBtn.setVisibility(View.INVISIBLE);
            text.setText("");
            text.setVisibility(View.INVISIBLE);
            counterText.setVisibility(View.VISIBLE);

            //this is a difference to sequence because we start from the backside
            sequenceOrderStorePositionCounter = sequenceOrderStore.size() - 1; //jump to last index
            initShuffledList();
        }
    }

    @Override
    protected void initShuffledList() {
        listView.setVisibility(View.VISIBLE);
        counterText.setText(answerNumber + ". backwards list elem.");

        Collections.shuffle(copiedListe2);
        arrayAdapter = new SequenceAdapter(
                SequenceBackwards.this, R.layout.list_item_sequence, copiedListe2);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //kleine Anpassungen weil counter rückwärts zählt
                if(sequenceOrderStorePositionCounter == 0) { //letztes element -> zusätzlich reset button aktivieren
                    TextView textView = ((TextView) view);
                    if (validateAnswer(textView, position)) {
                        tglBtn.setVisibility(View.INVISIBLE);
                        counterText.setVisibility(View.INVISIBLE);
                        restartBtn.setVisibility(View.VISIBLE);
                    }
                }

                if(sequenceOrderStorePositionCounter > 0) {
                    TextView textView = ((TextView) view);
                    validateAnswer(textView, position);
                }


            }
        });
    }

    //positioncounter zählt jetzt runter statt hoch
    @Override
    protected boolean validateAnswer(TextView textView, int position) {
        Log.i("testspeicher", "testspeicher hat wert " + sequenceOrderStore.get(sequenceOrderStorePositionCounter));
        Log.i("testspeicher", "geratener wert " + textView.getText());


        boolean test = sequenceOrderStore.get(sequenceOrderStorePositionCounter).equalsIgnoreCase((String) textView.getText());
        final int pos = position;
        final TextView tV = textView;

        if(!textView.getText().toString().equals("")) { //test only elements which are not done yet

            if(test) {

                answerNumber += 1;
                counterText.setText(answerNumber + ". backwards list elem.");
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
                sequenceOrderStorePositionCounter -= 1; //nur wenn Antwort richtig ist soll man nächste position anschauen, dieses mal runterzählen
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

    @Override
    protected void initListener() {
        super.initListener();
        counterText.setText(answerNumber + ". backwards list elem.");
    }
}
