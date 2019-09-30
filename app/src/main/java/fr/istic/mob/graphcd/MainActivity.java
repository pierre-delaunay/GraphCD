package fr.istic.mob.graphcd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.graphcd.model.Edge;
import fr.istic.mob.graphcd.model.Graph;
import fr.istic.mob.graphcd.model.Node;
import fr.istic.mob.graphcd.view.DrawableGraph;

public class MainActivity extends Activity implements View.OnTouchListener {
    private DrawableGraph drawableGraph;
    private Graph graph;
    private Context context;
    private List<Node> listNodes;
    private List<Edge> listEdges;
    private Node node;
    private AlertDialog dialogNode, dialogEdge;
    private Dialog dialogNodeColor;
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private boolean blockMoves, blockEdges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        listNodes = new ArrayList<>(); listEdges = new ArrayList<>();
        /*
        Node node1 = new Node(10,10);
        Node node2 = new Node(442,1150);
        listNodes.add(node1);
        listNodes.add(node2);
        listEdges.add(new Edge(node1, node2));
         */
        listNodes.add(new Node(550,550, "bluenode1", Color.BLUE, 50));
        listNodes.add(new Node(400,400,"bluenode2", Color.BLUE, 50));
        listNodes.add(new Node(490,1000,"blacknode1", Color.BLACK, 50));
        listNodes.add(new Node(190,200,"blacknode2", Color.BLACK, 50));
        listNodes.add(new Node(890,120, "n1", Color.BLACK, 50));
        listNodes.add(new Node(50,350, "n2", Color.CYAN, 50));
        listNodes.add(new Node(750,450, "nnnnnnnnnn3", Color.CYAN, 50));
        listNodes.add(new Node(390,650, "graynode21", Color.GRAY, 50));
        listNodes.add(new Node(320,900, "magentanode1", Color.MAGENTA, 50));

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

                if (node != null & (blockMoves)) {

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
        switch (item.getItemId()) {
            case R.id.graph_reinitialize:
                this.setTitle(getResources().getString(R.string.app_name));
                return true;
            case R.id.nodeModificationMode:
                blockMoves = false;
                blockEdges = false;
                item.setChecked(true);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.node_modification_mode));
                Toast.makeText(this, getResources().getString(R.string.node_edit_message), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edgeModificationMode:
                blockMoves = false;
                blockEdges = true;
                item.setChecked(true);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.edge_modification_mode));
                Toast.makeText(this, getResources().getString(R.string.edge_edit_message), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.moveMode:
                blockMoves = false;
                blockEdges = false;
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.move_mode));
                Toast.makeText(this, getResources().getString(R.string.move_mode_message), Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                return true;
            default:
                this.setTitle(getResources().getString(R.string.app_name));
                return super.onOptionsItemSelected(item);
        }

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
