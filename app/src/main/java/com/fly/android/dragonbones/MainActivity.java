package com.fly.android.dragonbones;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.android.dragonbones.layer.ArmatureDrawable;
import org.android.dragonbones.layer.ArmatureNode;
import org.android.dragonbones.layer.SimpleImageCache;
import org.android.dragonbones.parser.Skeleton;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    private static void _log(String txt) {
        android.util.Log.e("dragonbones", txt);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        readSke();
//        useMatrix();
    }
    private void useMatrix() {
        Camera camera = new Camera();
        camera.save();
        camera.rotateY(60);
        Matrix matrix = new Matrix();
        camera.getMatrix(matrix);
        _log("y=60,matrix="+matrix);
        camera.restore();

        camera.rotateX(45);
        camera.getMatrix(matrix);
        _log("x=45,matrix="+matrix);
        camera.restore();

    }
    public static String readText(String path,int maxLen) {
        FileInputStream in = null;
        String r = null;
        do {
            try {
                File f = new File(path);
                if (!f.exists()) break;
                if (maxLen<=0) maxLen = (int)f.length();
                in = new FileInputStream(f);
                byte[] bin = new byte[maxLen];
                int ret = in.read(bin);
                r = new String(bin, 0, ret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (false);
        if (in!=null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return r;
    }
    private SimpleImageCache mImageCache;
    private void readSke() {
        mImageCache = new SimpleImageCache();
        mImageCache.setDir("/mnt/sdcard/ske");
        ArmatureDrawable ad = new ArmatureDrawable();
        try {
            JSONObject json = new JSONObject(readText("/mnt/sdcard/ske.json", 0));
            Skeleton ske = Skeleton.fromJson(json);
            ArmatureNode an = ArmatureNode.fromParser(ske, "Armatureblue", null, mImageCache);
            ad.mNode = an;
        } catch (Exception e) {
            e.printStackTrace();
        }

        View nodeView = findViewById(R.id.nodeView);
        nodeView.setBackgroundDrawable(ad);
        ad.play("default", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
