package com.workpodapp.workpod.scale;

import android.widget.ImageView;

public class Scale_Image_View {
    //DECLARAMOS VARIABLES
    private ImageView iV;
    private int widhtLittle;
    private int heightLittle;
    private int widhtMiddle;
    private int heightMiddle;
    private int widhtBig;
    private int heightBig;
    private String widhtString;
    private String heightString;

    //CONSTRUCTOR
    public Scale_Image_View(ImageView iV, int widhtLittle, int heightLittle, int widhtMiddle, int heightMiddle, int widhtBig, int heightBig, String widhtString, String heightString) {
        this.iV = iV;
        this.widhtLittle = widhtLittle;
        this.heightLittle = heightLittle;
        this.widhtMiddle = widhtMiddle;
        this.heightMiddle = heightMiddle;
        this.widhtBig = widhtBig;
        this.heightBig = heightBig;
        this.widhtString = widhtString;
        this.heightString = heightString;
    }

    //GETTERS Y SETTERS
    public ImageView getiV() {
        return iV;
    }

    public void setiV(ImageView iV) {
        this.iV = iV;
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

    public String getWidhtString() {
        return widhtString;
    }

    public void setWidhtString(String widhtString) {
        this.widhtString = widhtString;
    }

    public String getHeightString() {
        return heightString;
    }

    public void setHeightString(String heightString) {
        this.heightString = heightString;
    }
}
