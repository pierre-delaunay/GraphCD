package fr.istic.mob.graphcd.model;

/**
 * Loop model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class Loop extends Edge {

    private float radius;

    /**
     * Constructor 1
     * @param startingNode
     * @param endingNode
     * @param thumbnail
     * @param color
     */
    public Loop(Node startingNode, Node endingNode, String thumbnail, int color) {
        super(startingNode, endingNode, thumbnail, color);
        initLoop();
    }

    /**
     * Accessors and mutators
     */

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Class methods
     */

    private void initLoop() {

    }
}
