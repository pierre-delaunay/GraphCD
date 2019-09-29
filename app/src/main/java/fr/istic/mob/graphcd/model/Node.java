package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import android.graphics.RectF;

public class Node {

    private int id, size;
    private float coordX, coordY;
    private String thumbnail;
    private int color;

    private RectF rect;

    public Node(float coordX, float coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.thumbnail = "node";
        this.rect = new RectF(coordX-50, coordY, coordX + 150, coordY + 150);
        this.color = Color.RED;
    }

    public Node(float coordX, float coordY, String thumbnail)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.thumbnail = thumbnail;
        this.rect = new RectF(coordX, coordY, coordX + 50, coordY + 50);
    }

    private void resync() {
        //this.rect = new RectF(coordX, coordY, coordX + 50, coordY + 50);
    }

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        this.coordX = coordX;
        //this.resync();
    }

    public float getCoordY() {
        return coordY;

    }

    public void setCoordY(float coordY) {
        this.coordY = coordY;
        //this.resync();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }
}
