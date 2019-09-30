package fr.istic.mob.graphcd.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;
import android.widget.Toast;

import fr.istic.mob.graphcd.model.Edge;
import fr.istic.mob.graphcd.model.Graph;
import fr.istic.mob.graphcd.model.Node;

public class DrawableGraph extends Drawable {

    private Paint backgroundPaint, nodePaint, textPaint, edgePaint;
    private float height, width;
    private Graph graph;

    private Drawable mProxy;


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

    public DrawableGraph(Graph graph) {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStrokeWidth(10);
        edgePaint = new Paint();
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(15);

        textPaint = new Paint();
        this.graph = graph;

    }

    public Graph getGraph() {
        return this.graph;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        for (Node node : graph.getNodes()) {
            backgroundPaint.setStyle(Paint.Style.FILL);
            backgroundPaint.setColor(node.getColor());
            canvas.drawRoundRect(node.getRect(), 200,200, backgroundPaint);


            textPaint.setARGB(200, 255, 255, 255);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(60);
            canvas.drawText(node.getThumbnail(), node.getRect().centerX(), node.getRect().centerY(), textPaint);
        }



        for(Edge edge : graph.getEdges()) {
            edgePaint.setColor(Color.BLACK);
            canvas.drawPath(edge.getPath(), edgePaint);
        }
    }







}