package com.fame.plumbum.lithicsin.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;
import com.fame.plumbum.lithicsin.activities.OrderDetails;
import com.fame.plumbum.lithicsin.adapters.OrderAdapter;
import com.fame.plumbum.lithicsin.interfaces.Get_order_details;
import com.fame.plumbum.lithicsin.interfaces.Load_more;
import com.fame.plumbum.lithicsin.model.Orders;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fame.plumbum.lithicsin.utils.Constants.BASE_URL_DEFAULT;

/**
 * Created by pankaj on 15/1/17.
 */

public class Home extends Fragment implements OnChartValueSelectedListener, Get_order_details, Load_more {

    View rootView;
    private PieChart mChart;
    RecyclerView list_orders;
    RecyclerView.Adapter adapter;
    List<Orders> dataList = new ArrayList<Orders>();
    int count = 5;
    int page = 0;

    protected List<String> mMonths = new ArrayList<>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        return rootView;
    }

    private void init() {
        list_orders = (RecyclerView) rootView.findViewById(R.id.list_orders);
        list_orders.setNestedScrollingEnabled(false);
        adapter = new OrderAdapter(getContext(), dataList, "MyOrders", this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        list_orders.setLayoutManager(mLayoutManager);
        list_orders.setAdapter(adapter);
        sendRequest(BASE_URL_DEFAULT + "getLastOrders.php");
        mChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        getPieData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private void getPieData() {
        final float[][] data = new float[1][12];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL_DEFAULT+"getMonthlyData.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jA = new JSONArray(response);
                            Log.e("LENGTHJA", jA.length()+ " " + jA.getString(0));
                            if (jA.length()>0) {
                                for (int i =0; i<jA.length(); i++) {
                                    data[0][mMonths.indexOf(jA.getString(i))]++;
                                    Log.e("LENGTHJA", data[0][mMonths.indexOf(jA.getString(i))]+"");
                                }
                                setData(data[0]);
                            }
                        } catch (JSONException e) {
                            Log.e("months data", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error receiving data!", Toast.LENGTH_SHORT).show();
                Log.e(getClass().getName(), error+"");
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                params.put("sku", sp.getString("sku", ""));
                return params;
            }};
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jA = new JSONArray(response);
                            if (jA.length()>0) {
                                TextView no_product = (TextView) rootView.findViewById(R.id.no_products);
                                no_product.setVisibility(View.GONE);
                                for (int i = 0; i < jA.length(); i++) {
                                    Orders order = new Orders();
                                    order.setName(jA.getJSONObject(i).getString("name"));
                                    order.setOrder_id(jA.getJSONObject(i).getString("increment_id"));
                                    order.setPrice(Double.parseDouble(jA.getJSONObject(i).getString("price_incl_tax")));
                                    order.setStatus(jA.getJSONObject(i).getString("status"));
                                    order.setThumbnail(jA.getJSONObject(i).getString("image"));
                                    dataList.add(order);
                                }
                                adapter.notifyDataSetChanged();
                                list_orders.setAdapter(adapter);
                                list_orders.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(getContext(), "No new products found!", Toast.LENGTH_SHORT).show();
                                page--;
                            }
                        } catch (JSONException e) {
                            Log.e("Orders Error", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error receiving order data!", Toast.LENGTH_SHORT).show();
                Log.e(getClass().getName(), error+"");
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                params.put("sku", sp.getString("sku", ""));
                params.put("count", count+"");
                params.put("page", page+"");
                return params;
            }};
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void setData(float[] range) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        Log.e("LENGTH", range.length+"");
        for (int i = 0; i < range.length; i++) {
            if (range[i]!=0) {
                entries.add(new PieEntry(range[i], mMonths.get(i)));
                Log.e("VALUES", i + "  " + range[i]);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Orders");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        return new SpannableString("Orders");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onInterfaceClick() {
        page++;
        sendRequest(BASE_URL_DEFAULT + "getLastOrders.php");
    }

    @Override
    public void onInterfaceClick(int position) {
        Intent intent = new Intent(getActivity(), OrderDetails.class);
        intent.putExtra("increment_id", dataList.get(position).getOrder_id());
        startActivity(intent);
    }
}
