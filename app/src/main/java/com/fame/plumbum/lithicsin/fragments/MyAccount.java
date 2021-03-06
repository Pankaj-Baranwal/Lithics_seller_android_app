package com.fame.plumbum.lithicsin.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;
import com.fame.plumbum.lithicsin.adapters.MiscQuesAdapter;
import com.fame.plumbum.lithicsin.model.DataSetMiscQues;
import com.fame.plumbum.lithicsin.utils.Constants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fame.plumbum.lithicsin.utils.Constants.BASE_URL_DEFAULT;

/**
 * Created by pankaj on 12/1/17.
 */

public class MyAccount extends Fragment {
    View rootView;
    RelativeLayout contact, official, misc;
    RecyclerView misc_list;
    RecyclerView.Adapter adapter;
    List<DataSetMiscQues> dataList = new ArrayList<>();
    SharedPreferences sp;
    String permanent_address_value , pickup_address_value, categories_value = "default_value" ;

    boolean pickupDifferent = true;

    TextView name, email_id, contact_person, mb_no, vat, cst, permanent_address, pickup_address, state_of_operation, categories;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_account, container, false);

        initCollapsingToolbar();
        try {
            Glide.with(getContext()).load(R.drawable.cover).into((ImageView) rootView.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();

    return rootView;
    }

    private void init() {
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        official = (RelativeLayout) rootView.findViewById(R.id.rl_official_details);
        contact = (RelativeLayout) rootView.findViewById(R.id.rl_contact_info);
        misc = (RelativeLayout) rootView.findViewById(R.id.rl_miscellaneous);
        misc_list = (RecyclerView) rootView.findViewById(R.id.list_misc_questions);
        adapter = new MiscQuesAdapter(dataList, "answers", getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        misc_list.setLayoutManager(mLayoutManager);
        misc_list.setAdapter(adapter);
        sendRequest(BASE_URL_DEFAULT + "get_registration_questions.php");
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useDialogContact();
            }
        });
        official.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useDialogOfficial();
            }
        });
        misc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useDialogMisc();
            }
        });
        name = (TextView) rootView.findViewById(R.id.name_value);
        email_id = (TextView) rootView.findViewById(R.id.email_value);
        contact_person = (TextView) rootView.findViewById(R.id.contact_person_value);
        mb_no = (TextView) rootView.findViewById(R.id.mobile_value);
        vat = (TextView) rootView.findViewById(R.id.vat_value);
        cst = (TextView) rootView.findViewById(R.id.cst_value);
        permanent_address = (TextView) rootView.findViewById(R.id.permanent_address_value);
        pickup_address = (TextView) rootView.findViewById(R.id.pickup_address_value);
        state_of_operation = (TextView) rootView.findViewById(R.id.state_value);
        categories = (TextView) rootView.findViewById(R.id.categories_value);
        getAllData();
    }

    private void getAllData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL_DEFAULT + "receiveProfile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("name")) {
                            try {
                                JSONArray jA = new JSONArray(response);
                                JSONObject jO = jA.getJSONObject(0);
                                name.setText(jO.getString("seller_name"));
                                if (!sp.contains("name")){
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("name", jO.getString("seller_name"));
                                    editor.apply();
                                }
                                email_id.setText(jO.getString("email_id"));
                                contact_person.setText(jO.getString("contact_person"));
                                mb_no.setText(jO.getString("mobile_number"));
                                vat.setText(jO.getString("vat"));
                                cst.setText(jO.getString("cst"));
                                permanent_address.setText(jO.getString("permanent"));
                                pickup_address.setText(jO.getString("pickup"));
                                state_of_operation.setText(jO.getString("state_operation"));
                                categories.setText(jO.getString("categories"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sp.getString("id", ""));
                return params;
            }
        };
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jA = new JSONArray(response);
                            for (int i =0; i<jA.length(); i++ ){
                                DataSetMiscQues dataset = new DataSetMiscQues();
                                dataset.setQues(jA.getJSONObject(i).getString("question"));
                                dataset.setAns(jA.getJSONObject(i).getString("answer_type"));
                                dataList.add(dataset);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void useDialogOfficial() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_official, null);
        final MaterialEditText vat = (MaterialEditText) mView.findViewById(R.id.tin);
        final MaterialEditText cst = (MaterialEditText) mView.findViewById(R.id.cst);
        final Button permanent = (Button) mView.findViewById(R.id.permanent_address);
        final Button pickup = (Button) mView.findViewById(R.id.pickup_address);
        final Button categories = (Button) mView.findViewById(R.id.product_categories);

        final LinearLayout permanent_wrapper = (LinearLayout) mView.findViewById(R.id.ll_permanent_wrapper);
        final LinearLayout pickup_wrapper = (LinearLayout) mView.findViewById(R.id.ll_pickup_wrapper);

        final MaterialEditText street1 = (MaterialEditText) mView.findViewById(R.id.street1);
        final MaterialEditText street2 = (MaterialEditText) mView.findViewById(R.id.street2);
        final MaterialEditText city = (MaterialEditText) mView.findViewById(R.id.city);
        final MaterialEditText pincode = (MaterialEditText) mView.findViewById(R.id.pincode);

        final MaterialEditText street1_pickup = (MaterialEditText) mView.findViewById(R.id.street1_pickup);
        final MaterialEditText street2_pickup = (MaterialEditText) mView.findViewById(R.id.street2_pickup);
        final MaterialEditText city_pickup = (MaterialEditText) mView.findViewById(R.id.city_pickup);
        final MaterialEditText pincode_pickup= (MaterialEditText) mView.findViewById(R.id.pincode_pickup);

        final CheckBox pickup_checkbox = (CheckBox) mView.findViewById(R.id.checkbox_pickup);
        final List<String> states = Arrays.asList(getResources().getStringArray(R.array.states));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner state_of_operation = (Spinner) mView.findViewById(R.id.state_of_operation);
        state_of_operation.setAdapter(adapter1);
        pickup_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    pickup.setClickable(false);
                    pickup.setBackgroundColor(0x886db096);
                    pickup_wrapper.setVisibility(View.GONE);
                    pickupDifferent = false;
                }else{
                    pickup.setClickable(true);
                    pickup.setBackgroundColor(0xff6db096);
                    pickupDifferent = true;
                }
            }
        });

        permanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permanent_wrapper.getVisibility()==View.GONE) {
                    permanent_wrapper.setVisibility(View.VISIBLE);
                    street1.requestFocus();
                }
                else
                    permanent_wrapper.setVisibility(View.GONE);
            }
        });
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickup_wrapper.getVisibility()==View.GONE) {
                    pickup_wrapper.setVisibility(View.VISIBLE);
                    street2.requestFocus();
                }
                else
                    pickup_wrapper.setVisibility(View.GONE);
            }
        });
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click words", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        permanent_address_value = street1.getText().toString() + "\n" + street2.getText().toString() + "\n" + city.getText().toString() + "\n" + pincode.getText().toString();
                        if (pickupDifferent)
                            pickup_address_value = street1_pickup.getText().toString() + "\n" + street2_pickup.getText().toString() + "\n" + city_pickup.getText().toString() + "\n" + pincode_pickup.getText().toString();
                        else{
                            pickup_address_value = permanent_address_value;
                        }
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL_DEFAULT + "saveOfficialDetails.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Notifications not working!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", sp.getString("id", ""));
                                params.put("vat", vat.getText().toString()+ "");
                                params.put("cst", cst.getText().toString()+ "");
                                params.put("permanent", permanent_address_value + "");
                                params.put("pickup", pickup_address_value + "");
                                params.put("state_operation", states.get(state_of_operation.getSelectedItemPosition()) + "");
                                params.put("categories", categories_value + "");
                                return params;
                            }
                        };
                        Singleton.getInstance().addToRequestQueue(stringRequest);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        alertDialogAndroid.show();
    }

    private void useDialogContact() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_contact, null);
        final MaterialEditText name_of_organization= (MaterialEditText) mView.findViewById(R.id.name_organization);
        final MaterialEditText email_id = (MaterialEditText) mView.findViewById(R.id.email_id);
        final MaterialEditText contact_name = (MaterialEditText) mView.findViewById(R.id.contact_name);
        final MaterialEditText mobile_no = (MaterialEditText) mView.findViewById(R.id.mobile_no);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL_DEFAULT + "updateSellerDetails.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contains("exists")){
                                            Toast.makeText(getContext(), "Already registered number!", Toast.LENGTH_SHORT).show();
                                        }else if (response.contains("id")){
                                            try {
                                                SharedPreferences.Editor editor = sp.edit();
                                                JSONArray resp = new JSONArray(response);
                                                JSONObject jo = resp.getJSONObject(0);
                                                editor.putString("id", jo.getString("id"));
                                                editor.apply();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Notifications not working!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", sp.getString("id", ""));
                                params.put("seller_name", name_of_organization.getText().toString()+"");
                                params.put("email_id", email_id.getText().toString()+"");
                                params.put("contact_person", contact_name.getText().toString()+"");
                                params.put("mobile_number", mobile_no.getText().toString()+"");
                                return params;
                            }
                        };
                        Singleton.getInstance().addToRequestQueue(stringRequest);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        alertDialogAndroid.show();
        alertDialogAndroid.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void useDialogMisc() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
//        ViewGroup parent = (ViewGroup) findViewById(R.id.rl_miscellaneous);

//        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_misc, parent, false);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_misc, null);
        final RecyclerView dialog_misc_list;
        RecyclerView.Adapter dialog_adapter;
        dialog_misc_list = (RecyclerView) mView.findViewById(R.id.list_misc_questions);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        dialog_misc_list.setLayoutManager(mLayoutManager);
        final List<DataSetMiscQues> dialogList = new ArrayList<>();
        dialog_adapter = new MiscQuesAdapter(dialogList, "questions", getContext());
        dialog_misc_list.setAdapter(dialog_adapter);
        for (int i =0; i<dataList.size(); i++ ){
            DataSetMiscQues dataset = new DataSetMiscQues();
            dataset.setQues(dataList.get(i).getQues());
            dataset.setType(dataList.get(i).getAns());
            dialogList.add(dataset);
        }
        dialog_adapter.notifyDataSetChanged();

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        List<String> data = new ArrayList<>();
                        for (int i =0; i<dialog_misc_list.getChildCount(); i++) {
                            View view = dialog_misc_list.getChildAt(i);

                            MaterialEditText ans_text = (MaterialEditText) view.findViewById(R.id.ans_text);
                            Spinner ans_binary = (Spinner) view.findViewById(R.id.ans_binary);
                            if (ans_binary.getVisibility()==View.VISIBLE){
                                if (ans_binary.getSelectedItemPosition()==1)
                                    data.add("Yes");
                                else if (ans_binary.getSelectedItemPosition()==2)
                                    data.add("No");
                                else
                                    data.add(" ");
                            }else
                                data.add(ans_text.getText().toString());
                            Log.e("DATA", data.get(i));
                        }
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        alertDialogAndroid.show();
        alertDialogAndroid.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        TextView title = (TextView) rootView.findViewById(R.id.title_collapsable);
        TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle_collapsable);
        title.setText("My Account");
        subtitle.setText("Manage your account here");
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.my_account));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}