package fr.istic.mob.graphcd.model;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Node model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Node {

    private float coordX, coordY;
    private String thumbnail;
    private int color;
    private RectF rect;
    private int size;
    private static int MIN_SIZE_NODE = 5;

    public Node(float coordX, float coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.thumbnail = "node";
        this.rect = new RectF(coordX-50, coordY, coordX + 150, coordY + 150);
        this.color = Color.RED;
    }

    public Node(float coordX, float coordY, String thumbnail, int color)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.thumbnail = thumbnail;
        this.rect = new RectF(coordX, coordY, coordX + 50, coordY + 50);
        this.color = color;
    }

    public Node(float coordX, float coordY, String thumbnail, int color, int size)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.thumbnail = thumbnail;
        this.color = color;
        this.setSize(size);
    }

    public void setSize(int size) {
        this.size = size;
        int thumbnailLength = this.getThumbnail().length();
        int coeffMult;

        if(this.getThumbnail().length() <= MIN_SIZE_NODE) {
            coeffMult = 8;
        }else if(this.getThumbnail().length() <= 10){
            coeffMult = 12;
        }else{
            coeffMult = 15;
        }

        this.rect = new RectF(this.coordX - (size + (thumbnailLength*coeffMult) ),
                this.coordY - size,
                this.coordX + ( size + (thumbnailLength*coeffMult) ),
                this.coordY + size);
    }

    public int getSize() {
        return size;
    }

    /**
     * Accessors and mutators
     */

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float coordY) {
        this.coordY = coordY;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
