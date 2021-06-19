package com.example.workpod.scale;

import android.widget.TextView;

public class Scale_TextView {
    //DECLARAMOS VARIABLES
    private TextView textView;
    private String widht;
    private String style;
    private int sizeLittle;
    private int sizeBig;

    //CONSTRUCTOR
    public Scale_TextView(TextView textView, String widht, String style, int sizeLittle, int sizeBig) {
        this.textView = textView;
        this.widht = widht;
        this.style = style;
        this.sizeLittle = sizeLittle;
        this.sizeBig = sizeBig;
    }

    public int getSizeLittle() {
        return sizeLittle;
    }

    public void setSizeLittle(int sizeLittle) {
        this.sizeLittle = sizeLittle;
    }

    public int getSizeBig() {
        return sizeBig;
    }

    public void setSizeBig(int sizeBig) {
        this.sizeBig = sizeBig;
    }

    //GETTERS Y SETTERS
    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public String getWidht() {
        return widht;
    }

    public void setWidht(String widht) {
        this.widht = widht;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
