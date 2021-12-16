package com.pango.hsec.hsec.Facilito;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;

/**
 * Created by jcila on 4/05/2018.
 */

public class opcionfacilito extends AppCompatActivity implements IActivity {
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    RecyclerView recList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recList = (RecyclerView) findViewById(R.id.grid);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_opcionfacilito, null);
        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, false);
        popupWindow.showAtLocation(recList, Gravity.CENTER, 0, 0);
    }

    @Override
    public void success(String data, String Tipo) {

    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
}
