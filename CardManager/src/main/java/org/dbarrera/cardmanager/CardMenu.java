package org.dbarrera.cardmanager;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class CardMenu extends Activity implements View.OnClickListener {

    private ImageButton newCard, showAll, editCard, helpInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_menu);

        setupWidgets();
    }

    private void setupWidgets() {
        newCard = (ImageButton)findViewById(R.id.ib_new);
        newCard.setOnClickListener(this);
        helpInfo = (ImageButton)findViewById(R.id.ib_help);
        helpInfo.setOnClickListener(this);
        showAll = (ImageButton)findViewById(R.id.ib_all);
        showAll.setOnClickListener(this);
        editCard = (ImageButton)findViewById(R.id.ib_edit);
        editCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_new:
                Intent iNew = new Intent(this, CardNew.class);
                startActivity(iNew);
                break;
            case R.id.ib_help:
                Intent iHelp = new Intent(this, CardHelp.class);
                startActivity(iHelp);
                break;
            case R.id.ib_all:
                Intent iShow = new Intent(this, CardShow.class);
                startActivity(iShow);
                break;
            case R.id.ib_edit:
                Intent iEdit = new Intent(this, CardEdit.class);
                startActivity(iEdit);
                break;
        }

    }
}
