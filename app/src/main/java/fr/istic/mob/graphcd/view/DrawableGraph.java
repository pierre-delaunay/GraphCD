package fr.istic.mob.graphcd.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import fr.istic.mob.graphcd.model.Edge;
import fr.istic.mob.graphcd.model.Graph;
import fr.istic.mob.graphcd.model.Node;

/**
 * DrawableGraph
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class DrawableGraph extends Drawable {

    private Paint backgroundPaint, nodeTextPaint, edgePaint, edgeTextPaint, edgeRectPaint;
    private Graph graph;
    private Drawable mProxy;
    private Path pathToPaint;


    /**
     * Constructor 1
     * @param graph with nodes/edges
     */
    public DrawableGraph(Graph graph) {
        edgePaint = new Paint();
        nodeTextPaint = new Paint();
        edgeTextPaint = new Paint();
        edgeRectPaint = new Paint();
        this.graph = graph;

    }

    /**
     * Accessors and mutators
     */

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * Methods from abstract class Drawable
     */

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mProxy != null) {
            mProxy.setColorFilter(colorFilter);
        }
    }

    @Override
    public int getOpacity() {
        return mProxy != null ? mProxy.getOpacity() : PixelFormat.TRANSPARENT;
    }


    @Override
    public void setAlpha(int alpha) {
        if (mProxy != null) {
            mProxy.setAlpha(alpha);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStrokeWidth(10);

        // Draw edges
        for(Edge edge : graph.getEdges()) {
            pathToPaint = new Path();
            pathToPaint = edge.getPath();

            edgePaint.setStyle(Paint.Style.STROKE);
            edgePaint.setStrokeWidth(edge.getThickness());
            edgePaint.setColor(edge.getColor());
            canvas.drawPath(pathToPaint, edgePaint);

            edgeTextPaint.setStyle(Paint.Style.FILL);
            edgeTextPaint.setColor(Color.BLACK);
            edgeTextPaint.setTextAlign(Paint.Align.CENTER);
            edgeTextPaint.setTextSize(50);
            canvas.drawText(edge.getThumbnail(), edge.getRectThumbnail().centerX(), edge.getRectThumbnail().centerY(), edgeTextPaint);

            edgeRectPaint.setColor(Color.RED);
            // debug loop
            //canvas.drawRoundRect(edge.getRectThumbnail(),50,60, edgeRectPaint);
        }

        // Draw nodes
        for (Node node : graph.getNodes()) {
            backgroundPaint.setStyle(Paint.Style.FILL);
            backgroundPaint.setColor(node.getColor());
            canvas.drawRoundRect(node.getRect(), 200,200, backgroundPaint);

            nodeTextPaint.setARGB(200, 255, 255, 255);
            nodeTextPaint.setTextAlign(Paint.Align.CENTER);
            nodeTextPaint.setTextSize(60);
            canvas.drawText(node.getThumbnail(), node.getRect().centerX(), node.getRect().centerY(), nodeTextPaint);
        }
    }
}