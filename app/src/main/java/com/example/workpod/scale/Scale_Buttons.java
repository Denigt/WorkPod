package com.example.workpod.scale;

import android.widget.Button;
import android.widget.TextView;

public class Scale_Buttons {
    //DECLARAMOS VARIABLES
    private Button button;
    private String width;
    private String heightString;
    private int widhtLittle;
    private int heightLittle;
    private int widhtMiddle;
    private int heightMiddle;
    private int widhtBig;
    private int heightBig;
    private String style;
    private int sizeLittle;
    private int sizeMiddle;
    private int sizeBig;

    //CONSTRUCTORES
    public Scale_Buttons(Button button, String widht, String style, int sizeLittle, int sizeMiddle, int sizeBig) {
        this.button = button;
        this.width = widht;
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

    public Scale_Buttons(Button button, String style, int sizeLittle, int sizeMiddle, int sizeBig, int widhtLittle, int heightLittle,
                          int widhtMiddle, int heightMiddle, int widhtBig, int heightBig) {
        this.button = button;
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
        this.width="";
    }

    //GETTERS Y SETTERS
    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public int getSizeMiddle() {
        return sizeMiddle;
    }

    public void setSizeMiddle(int sizeMiddle) {
        this.sizeMiddle = sizeMiddle;
    }

    public String getHeightString() {
        return heightString;
    }

    public void setHeightString(String heightString) {
        this.heightString = heightString;
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
