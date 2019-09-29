package fr.istic.mob.graphcd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.graphcd.model.Edge;
import fr.istic.mob.graphcd.model.Graph;
import fr.istic.mob.graphcd.model.Node;
import fr.istic.mob.graphcd.view.DrawableGraph;

import static java.security.AccessController.getContext;

public class MainActivity extends Activity implements View.OnTouchListener {
    DrawableGraph drawableGraph;
    Graph graph;
    private Context context;
    private List<Node> listNodes;
    private List<Edge> listEdges;
    private Node node;
    private AlertDialog dialogNode, dialogEdge;
    private Dialog dialogNodeColor;
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private boolean editNode = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        listNodes = new ArrayList<>(); listEdges = new ArrayList<>();

        Node node1 = new Node(10,10);
        Node node2 = new Node(442,1150);
        listNodes.add(node1);
        listNodes.add(node2);
        listNodes.add(new Node(550,550));
        listNodes.add(new Node(400,400));
        listNodes.add(new Node(490,750));
        listNodes.add(new Node(190,200));
        listNodes.add(new Node(890,20));
        listNodes.add(new Node(50,350));
        listNodes.add(new Node(1000,10));

        listEdges.add(new Edge(node1, node2));

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.drawableG);

        graph = new Graph("My graph", listNodes, listEdges);

        bitmap = BitmapFactory.decodeResource(getResources(), R.id.drawableG);
        imageView.setImageBitmap(bitmap);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(500, 500, conf); // this creates a MUTABLE bitmap
        canvas = new Canvas(bmp);

        drawableGraph = new DrawableGraph(graph);
        imageView.setImageDrawable(drawableGraph);
        imageView.setClickable(true);
        imageView.setLongClickable(true);

        imageView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        this.node = getNodeFromTouch(x, y);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                showNodeMenu(this);

                break;

            case MotionEvent.ACTION_MOVE :

                if (node != null & (editNode)) {

                    //modeNodeTo(x, y, node);
                    //view.invalidate();
                }

                break;

        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method called by the view in order to delete the selected node
     * @param view
     */
    public void deleteNode(View view) {

        try { graph.deleteNode(node); }
        catch (Exception e) {
            Log.v("MainActivity","Cannot delete node");
        }

        dialogNode.dismiss();
        drawableGraph.invalidateSelf();

    }

    /**
     * Move a node to a specified new location
     * @param newX, new X coord
     * @param newY, new Y coord
     * @param node, the node to relocate
     */
    public void modeNodeTo(float newX, float newY, Node node) {
        node.setRect(new RectF(newX-50, newY, newX+150, newY+150 ));

        //this.invalidate();
    }

    /**
     * Retrieve a node from a touch
     * @param x, coord X of the touch
     * @param y, coord Y of the touch
     * @return concerned Node
     */
    private Node getNodeFromTouch(float x, float y) {
        try {
            for (Node node : this.graph.getNodes()) {
                if (node.getRect().contains(x, y)) {
                    return node;
                }
            }
        } catch (Exception e) {
            Log.v("DrawableGraph","Can't reach node");
        }
        return null;
    }

    /**
     * Display the node menu after a long click on a node
     * @pre editNode = true
     * @param context
     * @return boolean
     */
    private boolean showNodeMenu(Context context) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View view = layoutInflaterAndroid.inflate(R.layout.node_options, null);
        AlertDialog.Builder dlgBuild = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                        }
                        return false;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        dlgBuild.setView(view);
        dialogNode = dlgBuild.create();
        dialogNode.setTitle(getResources().getString(R.string.title_node_menu));
        dialogNode.show();
        return false;
    }

    public void editThumbnail(View view)
    {
        showInputEditThumbnail();
        dialogNode.dismiss();
    }

    /**
     * Display a user input to edit the thumbnail of a node
     */
    private void showInputEditThumbnail()
    {
        final EditText textInput = new EditText(this);
        textInput.setText(node.getThumbnail());
        textInput.setHint(R.string.edit_thumbnail_hint);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.edit_thumbnail))
                .setMessage(getResources().getString(R.string.edit_thumbnail_message))
                .setView(textInput)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String nodeName = textInput.getText().toString();
                        node.setThumbnail(nodeName);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    /**
     * Display the color menu in order to edit the color of a node
     * @param view
     */
    public void showColorMenu(View view)
    {
        dialogNode.dismiss();
        dialogNodeColor = new Dialog(context);
        dialogNodeColor.setTitle(getResources().getString(R.string.color));
        dialogNodeColor.setContentView(R.layout.colors_options);
        dialogNodeColor.show();
    }

    /**
     * After a color has been selected from the view, a new color will be apply to the node
     * @param view
     */
    public void colorClick(View view)
    {

    }

    /**
     * Display the node size menu
     * @param v
     */
    public void showSizeMenu(View v)
    {
        final AlertDialog.Builder d = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.size_options, null);
        d.setTitle(R.string.edit_size_node);
        d.setMessage(R.string.edit_size_message);
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d("d", "onValueChange: ");
            }
        });
        d.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("d", "onClick: " + numberPicker.getValue());
            }
        });
        d.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }

}
