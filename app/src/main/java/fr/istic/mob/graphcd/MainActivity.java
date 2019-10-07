package fr.istic.mob.graphcd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import fr.istic.mob.graphcd.model.Edge;
import fr.istic.mob.graphcd.model.Graph;
import fr.istic.mob.graphcd.model.Node;
import fr.istic.mob.graphcd.view.DrawableGraph;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends Activity implements View.OnTouchListener {
    private DrawableGraph drawableGraph;
    private Graph graph;
    private Context context;
    private Node node, startingNode, endingNode;
    private AlertDialog dialogNode, dialogEdge;
    private Dialog dialogNodeColor;
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private boolean blockMoves, blockEditEdge, blockEditNode, allowNewNode, allowNewEdge;
    private enum EditMode {EDIT_NODE, EDIT_EDGE, NEW_NODE, NEW_EDGE, MOVE_ALL, INIT_MODE}
    private EditMode currentMode;
    private float[] startXY = new float[2];
    private float[] stopXY = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        setMode(EditMode.INIT_MODE);
        currentMode = EditMode.INIT_MODE;

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.drawableG);

        this.graph = new Graph("My graph");

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
            // L'utilisateur vient d'appuyer sur l'écran. C'est la première valeur récupérée suite à une action sur l'écran
            case MotionEvent.ACTION_DOWN:
                startXY[0] = x;
                startXY[1] = y;

                if (currentMode == EditMode.EDIT_NODE ) {
                    // Click on a existing node
                    showNodeMenu(this);
                }

                if (currentMode == EditMode.NEW_NODE){
                    // Add new node on touch location
                    this.graph.getNodes().add(new Node(x,y, "new node", Color.BLACK, 50));
                    drawableGraph.invalidateSelf();
                }

                // Add new edge
                if (currentMode == EditMode.NEW_EDGE) {

                    try {
                        startingNode = getNodeFromTouch(startXY[0], startXY[1]);
                    } catch (Exception e) {
                        Log.v("MainActivity", "Starting edge issue");
                    }
                }

                break;
            // Fait suite à l'événement précédent et indique que l'utilisateur n'a pas relaché la pression sur l'écran et est en train de bouger
            case MotionEvent.ACTION_MOVE :
                if (currentMode == EditMode.MOVE_ALL)
                {
                    moveNodeTo(x, y, node);
                    Log.v("Action_Move", "node x " +node.getCoordX() + "node y" +node.getCoordY()
                            + "startingnode x " + graph.getEdges().get(0).getStartingNode().getCoordX());

                }

                break;
             // Envoyé lorsque l'utilisateur cesse d'appuyer sur l'écran
             case MotionEvent.ACTION_UP:
                 stopXY[0] = x;
                 stopXY[1] = y;

                 if (currentMode == EditMode.NEW_EDGE) {
                    endingNode = getNodeFromTouch(stopXY[0], stopXY[1]);
                    if (endingNode != null) {
                        Edge newEdge = new Edge(startingNode, endingNode, "newEdge", Color.BLACK);
                        this.graph.addEdge(newEdge);
                        drawableGraph.invalidateSelf();
                    }
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
                reinitialize();
                return true;
            case R.id.newNodeMode:
                setMode(EditMode.NEW_NODE);
                item.setChecked(true);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.new_node_mode));
                return true;
            case R.id.newEdgeMode:
                setMode(EditMode.NEW_EDGE);
                item.setChecked(true);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.new_edge_mode));
                return true;
            case R.id.nodeModificationMode:
                setMode(EditMode.EDIT_NODE);
                item.setChecked(true);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.node_modification_mode));
                Toast.makeText(this, getResources().getString(R.string.node_edit_message), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edgeModificationMode:
                setMode(EditMode.EDIT_EDGE);
                item.setChecked(true);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.edge_modification_mode));
                Toast.makeText(this, getResources().getString(R.string.edge_edit_message), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.moveMode:
                setMode(EditMode.MOVE_ALL);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.move_mode));
                Toast.makeText(this, getResources().getString(R.string.move_mode_message), Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                return true;
            default:
                setMode(EditMode.INIT_MODE);
                this.setTitle(getResources().getString(R.string.app_name));
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Method called by the view in order to delete the selected node
     * @param view View
     */
    public void deleteNode(View view) {

        try { graph.deleteNode(node); }
        catch (Exception e) {
            Log.v("MainActivity","Cannot delete node");
        }

        dialogNode.dismiss();
        //drawableGraph.invalidateSelf();

    }

    /**
     * Move a node to a specified new location
     * @param newX, new X coord
     * @param newY, new Y coord
     * @param nodeToMove, the node to relocate
     */
    private void moveNodeTo(float newX, float newY, Node nodeToMove) {
        nodeToMove.setCoord(newX, newY);
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
     * @param context Context
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

    /**
     * Method called after a click on a "edit thumbnail" from the view
     * @param view View
     */
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
                        node.setSize(node.getSize());

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
     * @param view View
     */
    public void showColorMenu(View view)
    {
        int initColor = 0xff000000;
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                node.setColor(color);
                drawableGraph.invalidateSelf();
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                //
            }
        });
        dialog.show();
    }

    /**
     * Display the node size menu
     * @param v View
     */
    public void showSizeMenu(View v)
    {
        final AlertDialog.Builder d = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.node_size_numberpicker, null);
        d.setTitle(R.string.edit_size_node);
        d.setMessage(R.string.edit_size_message);
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(5);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d("EditNodeSize", "onValueChange: ");
            }
        });
        d.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("EditNodeSize", "onClick: " + numberPicker.getValue());
                node.setSize(numberPicker.getValue());
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

    /**
     * Display a confirmation message box before the reinitialization
     */
    private void reinitialize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.reinitialize_title);
        builder.setMessage(R.string.confirm);

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setMode(EditMode mode) {
        switch (mode) {
            case INIT_MODE:
                blockMoves = true; blockEditEdge = true; blockEditNode = true;
                allowNewNode = false; allowNewEdge = false;
                currentMode = EditMode.INIT_MODE;
                break;
            case EDIT_NODE:
                blockMoves = true; blockEditEdge = true; blockEditNode = false;
                allowNewNode = false; allowNewEdge = false;
                currentMode = EditMode.EDIT_NODE;
                break;
            case NEW_NODE:
                blockMoves = true; blockEditEdge = true; blockEditNode = true;
                allowNewNode = true; allowNewEdge = false;
                currentMode = EditMode.NEW_NODE;
                break;
            case EDIT_EDGE:
                blockMoves = false; blockEditEdge = false; blockEditNode = false;
                allowNewNode = false; allowNewEdge = false;
                currentMode = EditMode.EDIT_EDGE;
                break;
            case NEW_EDGE:
                blockMoves = true; blockEditEdge = true; blockEditNode = true;
                allowNewNode = false; allowNewEdge = true;
                currentMode = EditMode.NEW_EDGE;
                break;
            case MOVE_ALL:
                blockMoves = false; blockEditEdge = true; blockEditNode = true;
                allowNewNode = false; allowNewEdge = false;
                currentMode = EditMode.MOVE_ALL;
                break;
        }

    }

}
