package com.fly.android.dragonbones;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.android.dragonbones.layer.ArmatureDrawable;

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
                int nTimes = 4;
                ++mSkeIdx;
                mSkeIdx %= nTimes*SKES.length;
                if (mSkeIdx % nTimes == 0) {
                    readSke(SKES[mSkeIdx/nTimes]);
                }
                mAd.play(null, false);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        readSke("/mnt/sdcard/ske.json");
//        useMatrix();
    }
    private static final String[] SKES = {"/mnt/sdcard/ske/blue_ske.json","/mnt/sdcard/ske/green_ske.json","/mnt/sdcard/ske/red_ske.json","/mnt/sdcard/ske/yellow_ske.json"};
    private int mSkeIdx = 0;
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
    private ArmatureDrawable mAd = new ArmatureDrawable();
    private void readSke(String path) {
        mAd.setScale(0.8f);
        mAd.enableSimpleImageCache("/mnt/sdcard/ske", true);
//        mAd.loadAnimation("/mnt/sdcard/ske.json", "Armatureblue");
        mAd.loadAnimation(path, null);

        View nodeView = findViewById(R.id.nodeView);
        nodeView.setBackgroundDrawable(mAd);
//        mAd.play("default", false);
        mAd.play(null, false);
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
