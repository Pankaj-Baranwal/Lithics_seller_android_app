package com.fame.plumbum.lithicsin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fame.plumbum.lithicsin.utils.Constants.BASE_URL_DEFAULT;

/**
 * Created by pankaj on 26/2/17.
 */

public class Registration extends AppCompatActivity {
    SharedPreferences sp;
    String token;
    RelativeLayout contact, official, misc;
    RecyclerView misc_list;
    RecyclerView.Adapter adapter;
    List<DataSetMiscQues> dataList = new ArrayList<>();
    Button submit;
    String permanent_address_value , pickup_address_value, categories_value  ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initCollapsingToolbar();
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        initFCM();
        submit = (Button) findViewById(R.id.submit_registration);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        official = (RelativeLayout) findViewById(R.id.rl_official_details);
        contact = (RelativeLayout) findViewById(R.id.rl_contact_info);
        misc = (RelativeLayout) findViewById(R.id.rl_miscellaneous);
        misc_list = (RecyclerView) findViewById(R.id.list_misc_questions);
        adapter = new MiscQuesAdapter(this, dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        misc_list.setLayoutManager(mLayoutManager);
        misc_list.setAdapter(adapter);
        sendRequest(BASE_URL_DEFAULT + "get_registration_questions.php");
//        final LinearLayout ll_dialog_contact = (LinearLayout) findViewById(R.id.ll_dialog_contact);
//        final LinearLayout ll_dialog_official = (LinearLayout) findViewById(R.id.ll_dialog_official);
//        final LinearLayout ll_dialog_misc = (LinearLayout) findViewById(R.id.ll_dialog_misc);
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
    }



    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL_DEFAULT + "get_registration_questions.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("Registration", response);
                            JSONArray jA = new JSONArray(response);
                            Log.e("SIZE", jA.length()+"");
                            for (int i =0; i<jA.length(); i++ ){
                                DataSetMiscQues dataset = new DataSetMiscQues();
                                dataset.setQues(jA.getJSONObject(i).getString("question"));
                                Log.e("DATA", jA.getJSONObject(i).getString("question"));
                                dataset.setAns(jA.getJSONObject(i).getString("answer_type"));
                                dataList.add(dataset);
                            }
//                            adapter.setDataList(dataList);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registration.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        });
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void useDialogOfficial() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_official, null);
        final MaterialEditText vat = (MaterialEditText) mView.findViewById(R.id.tin);
        final MaterialEditText cst = (MaterialEditText) mView.findViewById(R.id.cst);
        LinearLayout rll_enclosed = (LinearLayout) mView.findViewById(R.id.ll_wrapper);
        final Button permanent = (Button) mView.findViewById(R.id.permanent_address);
        final Button pickup = (Button) mView.findViewById(R.id.pickup_address);
        final Button categories = (Button) mView.findViewById(R.id.product_categories);
        ViewGroup mSceneRoot;
        TransitionSet mStaggeredTransition;
        mStaggeredTransition = new TransitionSet();
        Transition first = new ChangeBounds();
        first.addTarget(rll_enclosed);
        final CheckBox pickup_checkbox = (CheckBox) mView.findViewById(R.id.checkbox_pickup);
        List<String> states = Arrays.asList("Choose your state", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner state_of_operation = (Spinner) mView.findViewById(R.id.state_of_operation);
        pickup_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    pickup.setClickable(false);
                    pickup.setBackgroundColor(0x886db096);
                }else{
                    pickup.setClickable(true);
                    pickup.setBackgroundColor(0xff6db096);
                }
            }
        });

        permanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Registration.this, "Click words", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL_DEFAULT + "saveOfficialDetails.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(Registration.this, "Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Registration.this, "Notifications not working!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("vat", vat.getText().toString()+ "");
                                params.put("cst", cst.getText().toString()+ "");
                                params.put("permanent", permanent_address_value + "");
                                params.put("pickup", pickup_address_value + "");
                                params.put("state_operation", pickup_address_value + "");
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
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_contact, null);
        final MaterialEditText name_of_organization= (MaterialEditText) mView.findViewById(R.id.name_organization);
        final MaterialEditText email_id = (MaterialEditText) mView.findViewById(R.id.email_id);
        final MaterialEditText contact_name = (MaterialEditText) mView.findViewById(R.id.contact_name);
        final MaterialEditText mobile_no = (MaterialEditText) mView.findViewById(R.id.mobile_no);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL_DEFAULT + "saveSellerDetails.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(Registration.this, "Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Registration.this, "Notifications not working!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
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
        alertDialogAndroid.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        alertDialogAndroid.show();
    }

    private void useDialogMisc() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_contact, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
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

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        TextView title = (TextView) findViewById(R.id.title_collapsable);
        TextView subtitle = (TextView) findViewById(R.id.subtitle_collapsable);
        title.setText("Registration");
        subtitle.setText("Create new account");
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
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
                    collapsingToolbar.setTitle(getString(R.string.registration));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initFCM() {
        if (!sp.contains("token")){
            SharedPreferences.Editor editor = sp.edit();
            if (FirebaseInstanceId.getInstance()!=null){
                token = FirebaseInstanceId.getInstance().getToken();
                if (token != null) {
                    Log.e("TOKEN", token);
                    editor.putString("token", token);
                    editor.apply();
//                    sendFCM(sp.getString("uid", ""));
                }
            }
        }else {
            Log.e("TOKEN", sp.getString("token", ""));
//            sendFCM(sp.getString("uid", ""));
        }
    }

    private void sendFCM(final String uid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL_DEFAULT + "firebase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registration.this, "Notifications not working!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", uid);
                params.put("Token", sp.getString("token", ""));
                return params;
            }
        };
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }
}
