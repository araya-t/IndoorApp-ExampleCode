package com.estimote.indoorapp.Controller.Parka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.estimote.indoorapp.Adapter.CsvRowListAdapter;
import com.estimote.indoorapp.Manager.HttpManager;
import com.estimote.indoorapp.Model.Parka.CsvRow;
import com.estimote.indoorapp.Model.dao.CarPosition;
import com.estimote.indoorapp.Model.dao.Token;
import com.estimote.indoorapp.R;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowCsvDataActivity extends AppCompatActivity {
    ListView listViewCsvRow;
    CsvRowListAdapter listAdapter;
    List<CsvRow> csvRows;
    int rowToTrigger;
    boolean is_triggered = false;
    boolean is_stop_engine = false;
    int floor_id = 5018;
    DecimalFormat dcm = new DecimalFormat("0.000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_csv_data);

        initInstances();

        Log.d("ShowCsvDataActivity", "After initInstances()");

        Intent intent = getIntent();
        Bundle extra = intent.getBundleExtra("extra");
        csvRows = (List<CsvRow>) extra.getSerializable("csvRows");
        rowToTrigger = intent.getIntExtra("rowToTrigger", 1);

        Log.d("ShowCsvDataActivity", "csvRows size = " + csvRows.size());
        Log.d("ShowCsvDataActivity", "rowToTrigger = " + rowToTrigger);

//        Call<CarPosition> callParka = HttpManager.getInstance()
//                .getServiceParka()
//                .sendXYPosition("XxOwrl57E9BKjtenCkhDi3TloSvQqcRU",
//                        "ezViZI1-f7E:APA91bGrozOqklQ61YzSLyFEG5Ec4FWcfK8g_LsbCCwL7Mb7s-rQ3t0AeWa2IK_evyhg95bb4jeSJTXzNtCd9G-yqnlg2RRQmCtCLAV1hVswWjaKzuuR-cKeec6LDDXC5wqaf1dUQxLm",
//                        1537958432,
//                        5018,
//                        5.8,
//                        2.5);
//        callParka.enqueue(new Callback<CarPosition>() {
//            @Override
//            public void onResponse(Call<CarPosition> call, Response<CarPosition> response) {
//                CarPosition obj = response.body();
//                if (response.isSuccessful() && obj != null){
//                    Log.d("Passsss","message is: "+obj);
//                    Toast.makeText(ShowCsvDataActivity.this,"Wpwwwww : "+obj.getPositionId(),Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CarPosition> call, Throwable t) {
//                Toast.makeText(ShowCsvDataActivity.this, "Nooooo :"+t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

        if (csvRows.size() != 0) {
            int i = 0;
            while (i < csvRows.size()) {
                Log.d("ShowCsvDataActivity", "csvRows size = " + csvRows.size());
                Log.d("ShowCsvDataActivity"
                        , "index " + i + " | in loop to get element from csvRows --> " + csvRows);

                //if value of 'csvRows.get(i).getCountRow()' == true --> send trigger to GMS
                if (csvRows.get(i).getCountRow() == rowToTrigger && !is_triggered) {
                    System.out.println("{10}getCountRow(" + csvRows.get(i).getCountRow() + ") = rowToTrigger");
                    is_triggered = true;

                    //set data to send
                    String is_available = "False";
                    Call<Void> callGMS = HttpManager.getInstance()
                            .getServiceGMS().changeStatus
                                    (328, is_available);

                    //call GMS
                    callGMS.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("responseSuccess"," send trigger to GMS SUCCESS");
                                Toast.makeText(ShowCsvDataActivity.this
                                        , "Send trigger to GMS ==> SUCCESS", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ShowCsvDataActivity.this
                                    , "Send trigger to GMS ==> FAILED", Toast.LENGTH_SHORT).show();

                        }
                    });
                }


        /** --------------------------------------------------------------------------------------- **/

                //if value of 'csvRows.get(i).is_stop_engine()' == true --> send data to App Server
                if (csvRows.get(i).is_stop_engine() == true && !is_stop_engine) {
                    System.out.println("stop engine = true | " + csvRows.get(i).is_stop_engine());
                    is_stop_engine = true;

                    Log.d("sendDataToAppServer","-----------------------------------------");

                    String  userToken = "XxOwrl57E9BKjtenCkhDi3TloSvQqcRU";
                    String  fcmToken = "eCY8Rx4iA4U:APA91bELzl7MWVm6uanqjcSnxS5uUeoNuTmLSeetSof3Wkp3E60KorFuQO8QZ0iOLIflfBUzCTyI_7zEOqsuDZCczdL_el3u4n0Xx2FqhZ4gwZ-ylFiiDm63RP20OO1cfpArGaQvcVQo";
                    long timestampLong1000 = csvRows.get(i).getTimeStampLong() / 1000L;
                    double timestampDouble = Double.parseDouble(""+timestampLong1000);
                    double x_position = csvRows.get(i).getX_position();
                    String x_positionStr = dcm.format(x_position);
                    double x_positionDouble = Double.parseDouble(x_positionStr);

                    double y_position = csvRows.get(i).getY_position();
                    String y_positionStr = dcm.format(y_position);
                    double y_positionDouble = Double.parseDouble(y_positionStr);


//                    float x_position = Float.parseFloat(""+csvRows.get(i).getX_position());
//                    float y_position = Float.parseFloat(""+csvRows.get(i).getY_position());

                    Log.d("sendDataToAppServer","timestampLong1000 = " + timestampLong1000
                                                    + ", x_position = " + x_position
                                                    + ", y_position = " + y_position);

                    //set data to send

//                    Call<CarPosition> callParka = HttpManager.getInstance()
//                            .getServiceParka().sendXYPosition
//                                            ( "XxOwrl57E9BKjtenCkhDi3TloSvQqcRU"
//                                            , "ezViZI1-f7E:APA91bGrozOqklQ61YzSLyFEG5Ec4FWcfK8g_LsbCCwL7Mb7s-rQ3t0AeWa2IK_evyhg95bb4jeSJTXzNtCd9G-yqnlg2RRQmCtCLAV1hVswWjaKzuuR-cKeec6LDDXC5wqaf1dUQxLm"
//                                            , 1537958432
//                                            , 5018
//                                            , 6
//                                            , 4);

                    Call<CarPosition> callParka = HttpManager.getInstance()
                            .getServiceParka()
                            .sendXYPosition(
                                    userToken, //pass
                                    6,
                                    4,
                                    floor_id, //pass
                                    fcmToken, // pass
                                    1537958432);

//                    Call<CarPosition> callParka = HttpManager.getInstance()
//                            .getServiceParka()
//                            .sendXYPosition(
//                                    userToken
//                                    ,x_position
//                                    ,y_position
//                                    ,floor_id
//                                    ,fcmToken
//                                    ,timestampLong1000);

                    Log.d("sendDataToAppServer","callParka = " + callParka);

                    //call Parka
                    callParka.enqueue(new Callback<CarPosition>() {
                        @Override
                        public void onResponse(retrofit2.Call<CarPosition> call, Response<CarPosition> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ShowCsvDataActivity.this
                                        , "Send data to 'Parka' ==> SUCCESS", Toast.LENGTH_SHORT).show();
                                Log.d("responseSuccess", "Send data to 'Parka' ==> SUCCESS");
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<CarPosition> call, Throwable t) {
                            Toast.makeText(ShowCsvDataActivity.this
                                    , "Send data to 'Parka' ==> FAILED", Toast.LENGTH_SHORT).show();
                            Log.d("responseSuccess","Failed Error message is " + t);
                        }
                    });

                    Log.d("sendDataToAppServer"," After callParka.enqueue");

                }

                Log.d("readfromcsvfile",
                        String.format("row %s|%s: %s, %s, %s, %s, %s, %s, %s ,%s",
                                i + 1
                                , csvRows.get(i).getCountRow()
                                , csvRows.get(i).getMillisec()
                                , csvRows.get(i).getTimeStamp()
                                , csvRows.get(i).getAcce_x()
                                , csvRows.get(i).getAcce_y()
                                , csvRows.get(i).getAcce_z()
                                , csvRows.get(i).is_stop_engine()
                                , csvRows.get(i).getX_position()
                                , csvRows.get(i).getY_position()));
                i++;
            }
        }

        if (csvRows != null) {
            listAdapter.setCsvRows(csvRows);
//            listAdapter.notifyDataSetChanged();
        }

    }

    private void initInstances() {

        listViewCsvRow = (ListView) findViewById(R.id.listView);
        listAdapter = new CsvRowListAdapter();
        listViewCsvRow.setAdapter(listAdapter);

    }
}
