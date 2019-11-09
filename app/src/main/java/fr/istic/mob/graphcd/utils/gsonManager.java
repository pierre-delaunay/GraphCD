package fr.istic.mob.graphcd.utils;

import android.os.Environment;
import android.util.Log;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;

import fr.istic.mob.graphcd.model.Graph;

/**
 * GSON Manager - Graph serialization/deserialization using GSON lib (Java Objects into JSON)
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class GsonManager {

    public static final String GRAPH_FILE_EXTENSION = ".gra";
    private static final String GRAPH_DIRECTORY_NAME = "Graphs";

    /**
     * Save current graph as JSON in external storage
     * @param graph Graph that will be saved in external storage
     */
    public static void saveCurrentGraph(Graph graph) {

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        File file, f;

        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), GRAPH_DIRECTORY_NAME);
        boolean isDirectoryCreated = file.exists() || file.mkdirs();

        if (isDirectoryCreated) {
            f = new File(file.getAbsolutePath() + File.separator + graph.getDescription() + now + GRAPH_FILE_EXTENSION);

            Gson gson = new Gson();
            String str = gson.toJson(graph);
            FileOutputStream fout;
            Log.i("Serialized graph", str);

            try {
                fout = new FileOutputStream(f);
                fout.write(str.getBytes());
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return a graph object from file (.graph file located in external storage)
     * @param path String file path
     * @return Graph graph object
     */
    public static Graph getExistingGraph(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();

            return gson.fromJson(json, Graph.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}