package com.vukihai.unisecchatbot.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vukihai.unisecchatbot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    PieChart pieChart;
    private RequestQueue mQueue;
    private ArrayList<String> major_name = new ArrayList<>();
    private ArrayList<Integer> major_quantity = new ArrayList<>();
    private ArrayList<String> uni_name = new ArrayList<>();
    private ArrayList<Integer> uni_quantity = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //statisticsViewModel = ViewModelProviders.of(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        /*statisticsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        pieChart = (PieChart) root.findViewById(R.id.piechart);

        Button butonParse = root.findViewById(R.id.btn_parse);
        mQueue = Volley.newRequestQueue(getContext());
        butonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();

            }
        });
        Button buttonDisplay = root.findViewById(R.id.btn_display);
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParseUni();
            }
        });
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        for(int i = 0; i < major_name.size(); i++){
            yvalues.add(new PieEntry(major_quantity.get(i), major_name.get(i)));
            Log.e("DVT", "name:" + major_name.get(i));
        }

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(20,15,20,10);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        /*yvalues.add(new PieEntry(34,"Haui"));
        yvalues.add(new PieEntry(38,"Hust"));
        yvalues.add(new PieEntry(40,"UET"));
        yvalues.add(new PieEntry(50,"BA"));*/
        Description description = new Description();
        description.setText("Biểu đồ tỉ lệ hỏi các ngành");
        description.setTextSize(9);
        pieChart.setDescription(description);
        PieDataSet dataSet = new PieDataSet(yvalues, "Major");
        dataSet.setSliceSpace(1f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.RED);
        pieChart.setData(data);
        jsonParse();
        return root;
    }
    private void jsonParse(){
        String url = "http://77c0975d.ngrok.io/major";
        major_name = new ArrayList<>();
        major_quantity = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String majorName = jsonObject.getString("name");
                                int quantity = jsonObject.getInt("quantity");
                                major_name.add(majorName);
                                major_quantity.add(quantity);
                                Log.e("DVT","name:" + majorName);
                                if(getActivity() != null)
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<PieEntry> yvalues = new ArrayList<>();
                                        for(int i = 0; i < major_name.size(); i++){
                                            yvalues.add(new PieEntry(major_quantity.get(i), major_name.get(i)));
                                            Log.e("DVT", "name:" + major_name.get(i));
                                        }

                /*pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);
                pieChart.setDrawHoleEnabled(false);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleRadius(61f);*/
                /*yvalues.add(new PieEntry(34,"Haui"));
                yvalues.add(new PieEntry(38,"Hust"));
                yvalues.add(new PieEntry(40,"UET"));
                yvalues.add(new PieEntry(50,"BA"));*/
                Description description = new Description();
                description.setText("Biểu đồ tỉ lệ hỏi các ngành đại học");
                description.setTextSize(9);
                                        pieChart.setDescription(description);
                                        PieDataSet dataSet = new PieDataSet(yvalues, "Major");
                                        dataSet.setSliceSpace(1f);
                                        dataSet.setSelectionShift(10f);
                                        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                        PieData data = new PieData(dataSet);
                                        data.setValueTextSize(10f);
                                        data.setValueTextColor(Color.RED);
                                        pieChart.setData(data);
                                        pieChart.invalidate();
                                    }
                                });
                                //mTextView.append(intentName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
    private void jsonParseUni(){
        String url = "http://77c0975d.ngrok.io/university";
        uni_name = new ArrayList<>();
        uni_quantity = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String uniName = jsonObject.getString("name");
                                int quantity = jsonObject.getInt("quantity");
                                uni_name.add(uniName);
                                uni_quantity.add(quantity);
                                Log.e("DVT","name:" + uniName);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<PieEntry> yvalues = new ArrayList<>();
                                        for(int i = 0; i < uni_name.size(); i++){
                                            yvalues.add(new PieEntry(uni_quantity.get(i), uni_name.get(i)));
                                            Log.e("DVT", "name:" + uni_name.get(i));
                                        }

                /*pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);
                pieChart.setDrawHoleEnabled(false);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleRadius(61f);*/
                /*yvalues.add(new PieEntry(34,"Haui"));
                yvalues.add(new PieEntry(38,"Hust"));
                yvalues.add(new PieEntry(40,"UET"));
                yvalues.add(new PieEntry(50,"BA"));*/
                Description description = new Description();
                description.setText("Biểu đồ tỉ lệ hỏi các trường đại học");
                description.setTextSize(9);
                                        pieChart.setDescription(description);
                                        PieDataSet dataSet = new PieDataSet(yvalues, "Uni");
                                        dataSet.setSliceSpace(1f);
                                        dataSet.setSelectionShift(10f);
                                        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                        PieData data = new PieData(dataSet);
                                        data.setValueTextSize(10f);
                                        data.setValueTextColor(Color.RED);
                                        pieChart.setData(data);
                                        pieChart.invalidate();
                                    }
                                });
                                //mTextView.append(intentName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}