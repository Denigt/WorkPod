package com.example.workpod.scale;

import android.widget.Button;

public class Scale_Buttons {
    //DECLARAMOS VARIABLES
    private Button button;
    private String width;
    private String style;
    private int sizeLittle;
    private int sizeBig;

    //CONSTRUCTOR
    public Scale_Buttons(Button button, String widht, String style, int sizeLittle, int sizeBig) {
        this.button = button;
        this.width = widht;
        this.style = style;
        this.sizeLittle = sizeLittle;
        this.sizeBig = sizeBig;
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
}
