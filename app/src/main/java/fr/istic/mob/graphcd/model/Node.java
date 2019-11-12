package fr.istic.mob.graphcd.model;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Node model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Node {

    private float coordX;
    private float coordY;
    private String thumbnail;
    private int color;
    private int size;
    private transient RectF rect;
    private transient PointF gravityCenter;
    private static int MIN_NODE_SIZE = 5;

    /**
     * Constructor 1
     * @param coordX, float x coord
     * @param coordY, float y coord
     * @param thumbnail, name of the node
     * @param color, int value of the node color
     * @param size, int
     */
    public Node(float coordX, float coordY, String thumbnail, int color, int size) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.thumbnail = thumbnail;
        this.color = color;
        this.gravityCenter = new PointF();
        this.setSize(size);
    }

    /**
     * Accessors and mutators
     */

    public void setSize(int size) {
        this.size = size;
        int thumbnailLength = this.getThumbnail().length();
        int coeffMult;

        if(this.getThumbnail().length() <= MIN_NODE_SIZE) {
            coeffMult = 8;
        } else if(this.getThumbnail().length() <= 10) {
            coeffMult = 12;
        } else {
            coeffMult = 15;
        }

        this.rect = new RectF(this.coordX - (size + (thumbnailLength*coeffMult) ),
                this.coordY - size,
                this.coordX + ( size + (thumbnailLength*coeffMult) ),
                this.coordY + size);

        this.gravityCenter.x = (this.coordX - (size + (thumbnailLength*coeffMult)) )
                + ( ((this.coordX + (size + (thumbnailLength*coeffMult)) )
                - (this.coordX - (size + (thumbnailLength*coeffMult))))/2 );
        this.gravityCenter.y = (this.coordY - size) + (((this.coordY+size)
                - (this.coordY-size))/2 );
    }

    public int getSize() {
        return size;
    }

    public float getCoordX() {
        return coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoord(float x, float y) {
        this.coordX = x;
        this.coordY = y;
        this.setSize(this.size);
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

    public PointF getGravityCenter() {
        return gravityCenter;
    }

    public void setGravityCenter(PointF gravityCenter) {
        this.gravityCenter = gravityCenter;
    }

    /**
     * GravityCenter was excluded from deserialization
     * We need to reinstanciate it in order to avoid NPE/Fatal signal
     */
    public void initAfterDeserialization() {

        int thumbnailLength = this.getThumbnail().length();
        int coeffMult;

        if(this.getThumbnail().length() <= MIN_NODE_SIZE) {
            coeffMult = 8;
        } else if(this.getThumbnail().length() <= 10) {
            coeffMult = 12;
        } else {
            coeffMult = 15;
        }

        this.gravityCenter = new PointF();

        this.rect = new RectF(this.coordX - (size + (thumbnailLength*coeffMult) ),
                this.coordY - size,
                this.coordX + ( size + (thumbnailLength*coeffMult) ),
                this.coordY + size);

        this.gravityCenter.x = (this.coordX - (size + (thumbnailLength*coeffMult)) )
                + ( ((this.coordX + (size + (thumbnailLength*coeffMult)) )
                - (this.coordX - (size + (thumbnailLength*coeffMult))))/2 );
        this.gravityCenter.y = (this.coordY - size) + (((this.coordY+size)
                - (this.coordY-size))/2 );
    }
}
