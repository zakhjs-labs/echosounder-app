package com.example.tcpclientserver;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final long DELAY_TIMER = 1000;
    private EditText e1;
    private TextView textView;
    private GraphView gvGraph;
    private BarGraphSeries series;
    //private LineGraphSeries series;
    private String lastDistValues = "";
    private Handler handler;
    private Runnable timer;
    private int xLastValue = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);
       gvGraph = findViewById(R.id.gv_graph);
       textView = findViewById(R.id.textView);
       series = new BarGraphSeries();
       //series = new LineGraphSeries();
       gvGraph.addSeries(series);

       gvGraph.getViewport().setMinX(0);
       gvGraph.getViewport().setMaxX(70);
       gvGraph.getViewport().setMinY(-500);
       gvGraph.getViewport().setMaxY(0);
       gvGraph.getViewport().setYAxisBoundsManual(true);
       gvGraph.getViewport().setXAxisBoundsManual(true);



       Thread myThread = new Thread(new MyServerThread());
       myThread.start();
    }

    class MyServerThread implements Runnable
    {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        String message;
        Handler h = new Handler();

        @Override
        public void run() {
            try{
                ss = new ServerSocket(7801);
                while (true){

                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    bufferedReader = new BufferedReader(isr);
                    lastDistValues = bufferedReader.readLine();
                    //startTimer();
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            if(lastDistValues != null){
                                xLastValue++;
                                int dist = Integer.parseInt(lastDistValues)*(-1);

                                series.appendData(new DataPoint(xLastValue, dist), true, 150);
                                textView.setText("График глубины в см: "+ lastDistValues);

                            }
                        }
                    });
                }

            }catch (IOException e){
                e.printStackTrace();
            }


        }
    }









}
