package com.fame.plumbum.lithicsin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fame.plumbum.lithicsin.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pankaj on 12/1/17.
 */

public class MyAccount extends Fragment {
    View rootView;
    RelativeLayout contact, official, misc;


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
        official = (RelativeLayout) rootView.findViewById(R.id.rl_official_details);
        contact = (RelativeLayout) rootView.findViewById(R.id.rl_contact_info);
        misc = (RelativeLayout) rootView.findViewById(R.id.rl_miscellaneous);
        final LinearLayout ll_dialog_contact = (LinearLayout) getActivity().findViewById(R.id.ll_dialog_contact);
        final LinearLayout ll_dialog_official = (LinearLayout) getActivity().findViewById(R.id.ll_dialog_official);
        final LinearLayout ll_dialog_misc = (LinearLayout) getActivity().findViewById(R.id.ll_dialog_misc);
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

    private void useDialogOfficial() {
        MaterialEditText tin = (MaterialEditText) getActivity().findViewById(R.id.tin);
        MaterialEditText cst = (MaterialEditText) getActivity().findViewById(R.id.cst);
        Button permanent = (Button) getActivity().findViewById(R.id.permanent_address);
        final Button pickup = (Button) getActivity().findViewById(R.id.pickup_address);
        Button categories = (Button) getActivity().findViewById(R.id.product_categories);

        CheckBox pickup_checkbox = (CheckBox) getActivity().findViewById(R.id.checkbox_pickup);
        TextView heading = (TextView) getActivity().findViewById(R.id.heading_my_account_dialog);
        Spinner state_of_operation = (Spinner) getActivity().findViewById(R.id.state_of_operation);

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

            }
        });
        List<String> states = Arrays.asList("Choose your state", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        state_of_operation.setAdapter(adapter);
        state_of_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void useDialogContact() {
        MaterialEditText name_of_organization= (MaterialEditText) getActivity().findViewById(R.id.name_organization);
        MaterialEditText email_id = (MaterialEditText) getActivity().findViewById(R.id.email_id);
        MaterialEditText contact_name = (MaterialEditText) getActivity().findViewById(R.id.contact_name);
        MaterialEditText mobile_no = (MaterialEditText) getActivity().findViewById(R.id.mobile_no);
        TextView heading = (TextView) getActivity().findViewById(R.id.heading_my_account_dialog);
        Spinner state_of_operation = (Spinner) getActivity().findViewById(R.id.state_of_operation);


        List<String> states = Arrays.asList("Choose your state", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        state_of_operation.setAdapter(adapter);
        state_of_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void useDialogMisc() {



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