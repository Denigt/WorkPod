package com.example.workpod.adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workpod.R;
import com.example.workpod.otherclass.LsV_Support;

import java.util.ArrayList;

public class Adaptador_LsV_Support extends BaseAdapter {

    Context context;
    ArrayList<LsV_Support> aLstSupport = new ArrayList<>();
    public TextView tVIcono;

    public Adaptador_LsV_Support() {
    }

    public Adaptador_LsV_Support(Context context, ArrayList<LsV_Support> aLstSupport) {
        this.context = context;
        this.aLstSupport = aLstSupport;
    }

    @Override
    public int getCount() {
        return aLstSupport.size();
    }

    @Override
    public Object getItem(int i) {
        return aLstSupport.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aLstSupport.get(i).getCodigo();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_support, null);
        ImageView iVIcono;
        tVIcono = (TextView) view.findViewById(R.id.iTVSupport);
        iVIcono = (ImageView) view.findViewById(R.id.iIVSupport);

        if (aLstSupport.get(i).getCodigo() == 1) {
            tVIcono.setText(Html.fromHtml("<a href=\"mailto:workpodtfg@gmail.com\">workpodtfg@gmail.com </a>"));
            tVIcono.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            tVIcono.setText(aLstSupport.get(i).getTexto());
        }
        iVIcono.setImageResource(aLstSupport.get(i).getIcono());


        return view;
    }
}
