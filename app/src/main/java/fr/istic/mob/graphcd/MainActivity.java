package fr.istic.mob.graphcd;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import fr.istic.mob.graphcd.model.Edge;
import fr.istic.mob.graphcd.model.Graph;
import fr.istic.mob.graphcd.model.Loop;
import fr.istic.mob.graphcd.model.Node;
import fr.istic.mob.graphcd.utils.FileAdapter;
import fr.istic.mob.graphcd.utils.GsonManager;
import fr.istic.mob.graphcd.view.DrawableGraph;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends Activity implements View.OnTouchListener {
    private DrawableGraph drawableGraph;
    private static Graph graph;
    private Context context;
    private Node node, startingNode, endingNode, newNode;
    private Edge edge;
    private AlertDialog dialogNode, dialogEdge;
    private ImageView imageView;
    private float width, height;
    private boolean blockMoves, blockEditEdge, blockEditNode, allowNewNode, allowNewEdge;
    private enum EditMode {EDIT_NODE, EDIT_EDGE, NEW_NODE, NEW_EDGE, MOVE_ALL, INIT_MODE}
    private EditMode currentMode;
    private float[] startXY = new float[2];
    private float[] stopXY = new float[2];
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        this.height = context.getResources().getDisplayMetrics().heightPixels;
        this.width = context.getResources().getDisplayMetrics().widthPixels;

        setMode(EditMode.INIT_MODE);
        currentMode = EditMode.INIT_MODE;
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.drawableG);

        if (getIntent().getStringExtra("fileName") != null) {
            String path = getIntent().getStringExtra("fileName");
            graph = GsonManager.getExistingGraph(path);
        } else {
            graph = new Graph("myGraph", width, height);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.drawableG);
        imageView.setImageBitmap(bitmap);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(500, 500, conf);
        Canvas canvas = new Canvas(bmp);

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
        this.edge = getEdgeFromTouch(x, y);

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
                    this.newNode = new Node(x,y, "newNode", Color.BLACK, 50);

                    showInputNewNode(x,y);
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

                if (currentMode == EditMode.EDIT_EDGE) {
                    try {
                        edge = getEdgeFromTouch(x, y);
                        Objects.requireNonNull(edge);
                        showEdgeMenu(this);
                    } catch (Exception e) {
                        Log.v("MainActivity", "Edge detection issue");
                    }
                }

                break;
            // Fait suite à l'événement précédent et indique que l'utilisateur n'a pas relaché la pression sur l'écran et est en train de bouger
            case MotionEvent.ACTION_MOVE :
                if (currentMode == EditMode.MOVE_ALL && node != null) {
                    moveNodeTo(x, y, node);
                    drawableGraph.invalidateSelf();
                }

                if (currentMode == EditMode.MOVE_ALL && edge != null) {
                    moveEdgeTo(x, y, edge);
                    drawableGraph.invalidateSelf();
                }

                break;
             // Envoyé lorsque l'utilisateur cesse d'appuyer sur l'écran
             case MotionEvent.ACTION_UP:
                 stopXY[0] = x;
                 stopXY[1] = y;

                 if (currentMode == EditMode.NEW_EDGE) {
                    endingNode = getNodeFromTouch(stopXY[0], stopXY[1]);

                    // "Simple" edge, not a loop
                    if (endingNode != null && !startingNode.equals(endingNode)) {
                        Edge newEdge = new Edge(startingNode, endingNode, "newEdge", Edge.DEFAULT_EDGE_COLOR);
                        graph.addEdge(newEdge);
                        drawableGraph.invalidateSelf();
                    }
                    // Loop
                     try {
                         if (startingNode.equals(endingNode)) {
                             Loop newLoop = new Loop(startingNode, endingNode, "newLoop", Edge.DEFAULT_EDGE_COLOR);
                             graph.addEdge(newLoop);
                             drawableGraph.invalidateSelf();
                         }
                     } catch (Exception e) {
                         Log.i("MainActivity", "Loop problem");
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

    /**
     * Execute action after an item selection from the menu
     * @param item, selected item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.graph_reinitialize:
                this.setTitle(getResources().getString(R.string.app_name));
                showReinitializeConfirmation();
                return true;
            case R.id.save_graph:
                verifyStoragePermissions(this);
                GsonManager.saveCurrentGraph(graph);
                return true;
            case R.id.open_existing_graph:
                openExistingGraph();
                return true;
            case R.id.share_graph:
                sendEmail();
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
            e.printStackTrace();
            Log.v("MainActivity","Cannot delete node");
        }

        dialogNode.dismiss();
        drawableGraph.invalidateSelf();

    }

    /**
     * Move a node to a specified new location
     * @param newX, new X coord
     * @param newY, new Y coord
     * @param nodeToMove, the node to relocate
     */
    private void moveNodeTo(float newX, float newY, Node nodeToMove) {
        Objects.requireNonNull(nodeToMove);
        nodeToMove.setCoord(newX, newY);
    }

    /**
     * Move an edge to a specified new location
     * @param newX, new X coord
     * @param newY, new Y coord
     * @param edgeToMove, the edge to relocate
     */
    private void moveEdgeTo(float newX, float newY, Edge edgeToMove) {
        Objects.requireNonNull(edgeToMove);
        edgeToMove.setPath(newX, newY);
    }

    /**
     * Retrieve a node from a touch
     * @param x, coord X of the touch
     * @param y, coord Y of the touch
     * @return concerned Node
     */
    private Node getNodeFromTouch(float x, float y) {
        try {
            for (Node n : graph.getNodes()) {
                if (n.getRect().contains(x, y)) {
                    return n;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieve an edge from screen interaction
     * @param x, coord X of the touch
     * @param y, coord Y of the touch
     * @return concerned edge
     */
    private Edge getEdgeFromTouch(float x, float y) {
        try {
            for (Edge e : graph.getEdges()) {
                if (e.getRectThumbnail().contains(x, y)) {
                    return e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Display the node menu after a long click on a node
     * @param context Context
     */
    private void showNodeMenu(Context context) {
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
    }

    /**
     * Display the edge menu after a long click on existing edge
     * @param context Context
     */
    private void showEdgeMenu(Context context) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View view = layoutInflaterAndroid.inflate(R.layout.edge_options, null);
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
        dialogEdge = dlgBuild.create();
        dialogEdge.setTitle(getResources().getString(R.string.title_edge_menu));
        dialogEdge.show();
    }

    /**
     * Method called after a click on a "edit thumbnail" from the view
     * @param view View
     */
    public void editNodeThumbnail(View view) {
        showInputEditNodeThumbnail();
        dialogNode.dismiss();
    }

    /**
     * Display a user input to edit the thumbnail of a node
     */
    private void showInputEditNodeThumbnail() {
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
     * Display an input for the name of the new node
     * @param x, x coord of new node
     * @param y, y coord of new node
     */
    private void showInputNewNode(float x, float y) {
        final EditText textInput = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.new_node_title))
                .setMessage(getResources().getString(R.string.new_node_thumbnail))
                .setView(textInput)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String nodeName = textInput.getText().toString();
                        newNode.setThumbnail(nodeName);
                        graph.getNodes().add(newNode);
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
    public void showNodeColorMenu(View view) {
        int initColor = 0xff000000;
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                try {
                    node.setColor(color);
                    drawableGraph.invalidateSelf();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    public void showNodeSizeMenu(View v) {
        final AlertDialog.Builder d = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.node_size_numberpicker, null);
        d.setTitle(R.string.edit_size_node);
        d.setMessage(R.string.edit_size_message);
        d.setView(dialogView);
        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialog_number_picker);
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
    private void showReinitializeConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.reinitialize_title);
        builder.setMessage(R.string.confirm);

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                reinitialize();
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

    /**
     * Reinitiliaze current displayed graph
     */
    private void reinitialize() {
        graph = new Graph("myGraph", width, height);
        drawableGraph.setGraph(graph);
        drawableGraph.invalidateSelf();
    }

    /**
     * Set new mode for editing or adding new node/edge
     * @param mode, new mode
     */
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

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity, current activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Save current graph in external storage before sending it by email
     */
    private void sendEmail() {
        verifyStoragePermissions(this);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // Image naming and path to include sd card appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // Create bitmap screen capture
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            // Email intent
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imageFile);

            sharingIntent.setType("image/png");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_graph)));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called by the view in order to show the edit input
     * @param view View
     */
    public void editEdgeThumbnail(View view) {
        showInputEditEdgeThumbnail();
        dialogEdge.dismiss();
    }

    /**
     * Display an text input to the user
     */
    public void showInputEditEdgeThumbnail() {
        final EditText textInput = new EditText(this);
        textInput.setText(edge.getThumbnail());
        textInput.setHint(R.string.edit_thumbnail_hint);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.edit_thumbnail))
                .setMessage(getResources().getString(R.string.edit_thumbnail_message))
                .setView(textInput)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String edgeName = textInput.getText().toString();
                        edge.setThumbnail(edgeName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    /**
     * Display the edge color selection menu
     * @param view View
     */
    public void showEdgeColorMenu(View view) {
        int initColor = 0xff000000;
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                edge.setColor(color);
                drawableGraph.invalidateSelf();
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        dialog.show();
    }

    /**
     * Display the edit thickness menu
     * The user can pick a new thickness through the numberpicker
     * @param view View
     */
    public void showEdgeThicknessMenu(View view) {
        final AlertDialog.Builder d = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edge_thickness_numberpicker, null);
        d.setTitle(R.string.edit_thickness_edge);
        d.setMessage(R.string.edit_size_message);
        d.setView(dialogView);
        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(Edge.MAX_THICKNESS_VALUE);
        numberPicker.setMinValue(Edge.MIN_THICKNESS_VALUE);
        numberPicker.setWrapSelectorWheel(false);
        d.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("EditEdgeThickness", "onClick: " + numberPicker.getValue());
                edge.setThickness(numberPicker.getValue());
                drawableGraph.invalidateSelf();
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
     * Method called by the view in order to delete the selected edge
     * @param view View
     */
    public void deleteEdge(View view) {
        try { graph.deleteEdge(edge); }
        catch (Exception e) {
            e.printStackTrace();
            Log.v("MainActivity","Cannot delete edge");
        }
        dialogEdge.dismiss();
        drawableGraph.invalidateSelf();
    }

    /**
     * Handle click on selected file in order to open existing graph (from external storage)
     */
    private void openExistingGraph() {
        verifyStoragePermissions(this);
        ArrayList<String> filesList = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory() + File.separator + "DCIM/Graphs";
        File directory = new File(path);
        File[] files = directory.listFiles();

        try {
            for (File f : files) {
                if (f.isFile() && f.getPath().endsWith(GsonManager.GRAPH_FILE_EXTENSION)) {
                    filesList.add(path + File.separator + f.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final FileAdapter adapter = new FileAdapter(context, filesList);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.title_file));

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameFile = (String) adapter.getItem(which);
                try {
                    launchNewGraph(nameFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.show();
    }

    /**
     * Launch new graph
     * @param fileName, path of the graph located in external storage
     */
    private void launchNewGraph(String fileName) {
        // Previous version
        /*
        if (newGraph != null) {
            graph.clearNodes();
            graph.clearEdges();
            graph = newGraph;
            drawableGraph.setGraph(newGraph);
            drawableGraph.invalidateSelf();
        }
        */
        // New version - debugging Android Fatal Signal SIGSEGV
        Intent intent = getIntent();
        //Intent intent = new Intent(new Intent(getBaseContext(), MainActivity.class));
        finish();
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }
}
