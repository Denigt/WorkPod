package com.example.workpod.scale;

import android.widget.TextView;

public class Scale_TextView {
    //DECLARAMOS VARIABLES
    private TextView textView;
    private String widht;
    private String style;
    private int sizeLittle;
    private int sizeMiddle;
    private int sizeBig;
    private int widhtLittle;
    private int heightLittle;
    private int widhtMiddle;
    private int heightMiddle;
    private int widhtBig;
    private int heightBig;

    //CONSTRUCTORES
    public Scale_TextView(TextView textView, String widht, String style, int sizeLittle,int sizeMiddle, int sizeBig) {
        this.textView = textView;
        this.widht = widht;
        this.style = style;
        this.sizeLittle = sizeLittle;
        this.sizeMiddle=sizeMiddle;
        this.sizeBig = sizeBig;
        this.widhtBig=0;
        this.widhtLittle=0;
        this.widhtMiddle=0;
        this.heightBig=0;
        this.heightLittle=0;
        this.heightMiddle=0;
    }

    public Scale_TextView(TextView textView, String widht,String style, int sizeLittle, int sizeMiddle, int sizeBig, int widhtLittle, int heightLittle,
                          int widhtMiddle, int heightMiddle, int widhtBig, int heightBig) {
        this.textView = textView;
        this.style = style;
        this.sizeLittle = sizeLittle;
        this.sizeMiddle = sizeMiddle;
        this.sizeBig = sizeBig;
        this.widhtLittle = widhtLittle;
        this.heightLittle = heightLittle;
        this.widhtMiddle = widhtMiddle;
        this.heightMiddle = heightMiddle;
        this.widhtBig = widhtBig;
        this.heightBig = heightBig;
        this.widht=widht;
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

    public int getSizeMiddle() {
        return sizeMiddle;
    }

    public void setSizeMiddle(int sizeMiddle) {
        this.sizeMiddle = sizeMiddle;
    }

    public int getWidhtLittle() {
        return widhtLittle;
    }

    public void setWidhtLittle(int widhtLittle) {
        this.widhtLittle = widhtLittle;
    }

    public int getHeightLittle() {
        return heightLittle;
    }

    public void setHeightLittle(int heightLittle) {
        this.heightLittle = heightLittle;
    }

    public int getWidhtMiddle() {
        return widhtMiddle;
    }

    public void setWidhtMiddle(int widhtMiddle) {
        this.widhtMiddle = widhtMiddle;
    }

    public int getHeightMiddle() {
        return heightMiddle;
    }

    public void setHeightMiddle(int heightMiddle) {
        this.heightMiddle = heightMiddle;
    }

    public int getWidhtBig() {
        return widhtBig;
    }

    public void setWidhtBig(int widhtBig) {
        this.widhtBig = widhtBig;
    }

    public int getHeightBig() {
        return heightBig;
    }

    public void setHeightBig(int heightBig) {
        this.heightBig = heightBig;
    }
}
