package com.pt_plus.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.pt_plus.MainActivity;
import com.pt_plus.Model.AddressModel;
import com.pt_plus.R;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Service.StoreService;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.PrefKeys;
import com.pt_plus.Utils.PrefUtils;
import com.pt_plus.cons.LocalCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentAddAddress extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private View appbar;
    private TextView txtappBarTitle;

    private EditText etDob, etMobileNumber;
    private AutoCompleteTextView etCountry, txtArea, etBlock;
    private GeneralService generalService;
    private JSONArray CountryJsonArr, cityJsonArr, blockJsonArr;
    private ArrayList<String> genterArr = null;
    private ArrayList<String> addressTypeArr = null;
    private Spinner spinnerGenter, spinnerAddressType;
    private EditText etFullName, etEmail, etZipCode, etStreetName;
    private Button btnSubmit;

    private String fromPage;


    private void initView(View view) {
        genterArr = new ArrayList<>(Arrays.asList(getString(R.string.Male), getString(R.string.Female)));
        addressTypeArr = new ArrayList<>(Arrays.asList(getString(R.string.Home_a), getString(R.string.Office), getString(R.string.Other)));
        etFullName = view.findViewById(R.id.et_full_name);
        etEmail = view.findViewById(R.id.et_email);
        etZipCode = view.findViewById(R.id.et_zipcode);
        etStreetName = view.findViewById(R.id.et_steet_name);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(_click);
        etCountry = view.findViewById(R.id.et_country);
        txtArea = view.findViewById(R.id.txt_area);
        etBlock = view.findViewById(R.id.et_block);
        etCountry.setOnClickListener(_click);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        etDob = view.findViewById(R.id.et_dob);
        etMobileNumber = view.findViewById(R.id.txt_mobile_number);
        etMobileNumber.addTextChangedListener(etMobileNUmberWatcher);
//        etDob.setOnClickListener(_click);

        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(false);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(getString(R.string.add_address));

        generalService = new GeneralService(getFragmentActivity(), _callback);
        generalService.getCountries(ServiceNames.REQUEST_ID_GET_COUNTRIES);
        Selection.setSelection(etMobileNumber.getText(), etMobileNumber.getText().length());


        spinnerGenter = view.findViewById(R.id.spinner_genter);
        ArrayAdapter<String> genterAdapter = new ArrayAdapter<String>
                (getFragmentActivity(), android.R.layout.simple_list_item_1, genterArr);
        spinnerGenter.setAdapter(genterAdapter);
        spinnerGenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerAddressType = view.findViewById(R.id.spinner_address_type);
        ArrayAdapter<String> AddressTYpeAdapter = new ArrayAdapter<String>
                (getFragmentActivity(), android.R.layout.simple_list_item_1, addressTypeArr);
        spinnerAddressType.setAdapter(AddressTYpeAdapter);

        spinnerAddressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (getFragmentActivity().isUserLoged()) {
            serProfile();
        }
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            fromPage = bundle.getString("FROM");
            try {
                selectedAddressModel = (AddressModel) bundle.getSerializable("addressModel");
                if (selectedAddressModel != null) {
                    etFullName.setText(selectedAddressModel.fullName);
                    etEmail.setText(selectedAddressModel.email);
                    etCountry.setText(selectedAddressModel.country);
                    etMobileNumber.setText(selectedAddressModel.mobilePrefix + " " + selectedAddressModel.mobile);
                    int indexSelected = 0;
                    for (int i = 0; i < genterArr.size(); i++) {
                        if (genterArr.get(i).equalsIgnoreCase(selectedAddressModel.gender)) {
                            indexSelected = i;
                        }
                    }
                    spinnerGenter.setSelection(indexSelected);
                    etZipCode.setText(selectedAddressModel.zipCOde);
                    etStreetName.setText(selectedAddressModel.streetName);
                    txtArea.setText(selectedAddressModel.Area);
                    etBlock.setText(selectedAddressModel.block);
                    indexSelected = 0;
                    for (int i = 0; i < addressTypeArr.size(); i++) {
                        if (addressTypeArr.get(i).equalsIgnoreCase(selectedAddressModel.AddressType)) {
                            indexSelected = i;
                        }
                    }
                    spinnerAddressType.setSelection(indexSelected);
                    btnSubmit.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        if (!getFragmentActivity().isEnglish()) {
            etFullName.setTextDirection(View.TEXT_DIRECTION_RTL);
            etEmail.setTextDirection(View.TEXT_DIRECTION_RTL);
        }
    }

    private void serProfile() {
        try {

            JSONObject jsonObject = new JSONObject(PrefUtils.getFromPrefs(getFragmentActivity(), PrefKeys.PREF_PROFILE, "").toString());
            etFullName.setText(AppUtils.getStringValueFromJson(jsonObject, "name"));
            etEmail.setText(AppUtils.getStringValueFromJson(jsonObject, "email"));
            etMobileNumber.setText(AppUtils.getStringValueFromJson(jsonObject, "phone"));
            if (AppUtils.getStringValueFromJson(jsonObject, "phone_code") != null && !AppUtils.getStringValueFromJson(jsonObject, "phone_code").equalsIgnoreCase("null")) {
                selectedContry.put("phone_code", AppUtils.getStringValueFromJson(jsonObject, "phone_code"));
            }


            AppLogger.log("profile json    " + jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final TextWatcher etMobileNUmberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (selectedContry != null) {
                if (!s.toString().startsWith("+" + AppUtils.getStringValueFromJson(selectedContry, "phone_code"))) {
                    etMobileNumber.setText("+" + AppUtils.getStringValueFromJson(selectedContry, "phone_code"));
                    Selection.setSelection(etMobileNumber.getText(), etMobileNumber.getText().length());

                }
            } else {
//                toastInFragment("Please select Country First");
            }

        }
    };

    private final CallBack _callback = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_GET_COUNTRIES) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null) {
                        ;
                        CountryJsonArr = jsonArray;

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                    (_activity,android.R.layout.select_dialog_item, arr);
                                (getFragmentActivity(), android.R.layout.simple_list_item_1, processJsonArrYToStringArray(CountryJsonArr, "name"));
                        etCountry.setThreshold(0);
                        etCountry.setAdapter(adapter);
                        etCountry.addTextChangedListener(etCountryTextWatcher);
                        etCountry.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

                                etCountry.showDropDown();
                                return false;
                            }
                        });
                        if (CountryJsonArr != null && CountryJsonArr.length() > 0) {
                            AppLogger.log("dfadfadfda            ");
                            etCountry.setText(CountryJsonArr.getJSONObject(0).getString("name"));
                            etCountry.setSelection(0);
                            etCountry.setTag(CountryJsonArr.getJSONObject(0).getLong("id"));
                            selectedContry = CountryJsonArr.getJSONObject(0);
                        }

                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_CITY) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null) {

                        cityJsonArr = jsonArray;

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getFragmentActivity(), android.R.layout.simple_list_item_1, processJsonArrYToStringArray(cityJsonArr, "name"));
                        txtArea.setThreshold(0);
                        txtArea.setAdapter(adapter);
                        txtArea.addTextChangedListener(etAreaTextWatcher);
                        txtArea.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

                                txtArea.showDropDown();
                                return false;
                            }
                        });
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_BLOCK) {
                    JSONArray jsonArray = jsonObject.has("result") ? jsonObject.getJSONArray("result") : null;
                    if (jsonArray != null) {

                        blockJsonArr = jsonArray;

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getFragmentActivity(), android.R.layout.simple_list_item_1, processJsonArrYToStringArray(blockJsonArr, "name"));
                        etBlock.setThreshold(0);
                        etBlock.setAdapter(adapter);
                        etBlock.addTextChangedListener(etBlockTextWatcher);
                        etBlock.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

                                etBlock.showDropDown();
                                return false;
                            }
                        });
                    }
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_ADD_ADDRESS) {

                    if (jsonObject.has("status")) {
                        if (AppUtils.getBooleanValueFromJson(jsonObject, "status")) {
                            toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                            goback();
                        } else {
                            if (jsonObject.has("errors")) {
                                JSONArray errorsArray = jsonObject.getJSONObject("errors").getJSONArray(jsonObject.getJSONObject("errors").keys().next());

                                if (errorsArray.length() > 0) {
                                    // Extract the first error message
                                    String firstErrorMessage = errorsArray.getString(0);
                                    toastInFragment(firstErrorMessage);
                                } else {
                                    System.out.println("No error messages found.");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private final TextWatcher etCountryTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            processEtCounrty(s.toString());
        }
    };
    private JSONObject selectedContry;

    private void processEtCounrty(String str) {
        try {
            if (CountryJsonArr != null && CountryJsonArr.length() > 0) {
                for (int i = 0; i < CountryJsonArr.length(); i++) {
                    JSONObject jsonObject = CountryJsonArr.getJSONObject(i);
                    if (AppUtils.getStringValueFromJson(jsonObject, "name").equalsIgnoreCase(str)) {
                        selectedContry = jsonObject;
                        generalService.getCity(ServiceNames.REQUEST_ID_GET_CITY, jsonObject.getLong("id"));
                        etCountry.setTag(jsonObject.getLong("id"));
                        etMobileNumber.setText("+" + AppUtils.getStringValueFromJson(jsonObject, "phone_code"));
                        if (selectedAddressModel != null) {
                            etMobileNumber.setText("+" + AppUtils.getStringValueFromJson(jsonObject, "phone_code") + selectedAddressModel.mobile);
                        }
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final TextWatcher etAreaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            processEtArea(s.toString());
        }
    };

    private JSONObject selectedCity;

    private void processEtArea(String str) {
        try {
            if (cityJsonArr != null && cityJsonArr.length() > 0) {
                for (int i = 0; i < cityJsonArr.length(); i++) {
                    JSONObject jsonObject = cityJsonArr.getJSONObject(i);
                    if (AppUtils.getStringValueFromJson(jsonObject, "name").equalsIgnoreCase(str)) {
                        txtArea.setTag(jsonObject.getLong("id"));
                        generalService.getBlock(ServiceNames.REQUEST_ID_GET_BLOCK, jsonObject.getLong("id"));
                        selectedCity = jsonObject;
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final TextWatcher etBlockTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            processEtBlock(s.toString());
        }
    };

    private JSONObject selectedBlcok;

    private void processEtBlock(String str) {
        try {
            if (blockJsonArr != null && blockJsonArr.length() > 0) {
                for (int i = 0; i < blockJsonArr.length(); i++) {
                    JSONObject jsonObject = blockJsonArr.getJSONObject(i);
                    if (AppUtils.getStringValueFromJson(jsonObject, "name").equalsIgnoreCase(str)) {
                        generalService.getBlock(ServiceNames.REQUEST_ID_GET_BLOCK, jsonObject.getLong("id"));
                        selectedBlcok = jsonObject;
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> processJsonArrYToStringArray(JSONArray jsonArray, String key) {
        ArrayList<String> resultArray = new ArrayList<>();
        try {

            if (jsonArray != null && jsonArray.length() > 0) {
                AppLogger.log("daasds " + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    AppLogger.log("daasds  " + AppUtils.getStringValueFromJson(jsonObject, key));
                    resultArray.add(AppUtils.getStringValueFromJson(jsonObject, key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.et_country: {
                    etCountry.showDropDown();

//                    showCountryPopup();
                    break;
                }
                case R.id.btn_submit: {
                    validateAndSumbitAddress();
                    break;
                }


            }
        }
    };

    private AddressModel selectedAddressModel = new AddressModel();

    private void validateAndSumbitAddress() {
        try {
            if (selectedAddressModel == null) {
                selectedAddressModel = new AddressModel();
            }
            boolean isValid = true;
            if (TextUtils.isEmpty(etFullName.getText().toString())) {
                etFullName.setError("This field is required");
                isValid = false;
            } else {
                etFullName.setError(null);
                selectedAddressModel.fullName = etFullName.getText().toString();
            }

            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                etEmail.setError("This field is required");
                isValid = false;
            } else {
                if (getFragmentActivity().isValidEmailId(etEmail.getText().toString())) {
                    etEmail.setError(null);
                    selectedAddressModel.email = etEmail.getText().toString();
                } else {
                    isValid = false;
                    etEmail.setError("InValid Email Address.");
                    toastInFragment(" InValid Email Address.");
                }

            }

            if (TextUtils.isEmpty(etCountry.getText().toString())) {
                etCountry.setError("This field is required");
                isValid = false;
            } else {
                etCountry.setError(null);
                selectedAddressModel.country = etCountry.getText().toString();
//                selectedAddressModel.countryId = Long.parseLong(etCountry.getTag().toString());
            }
            if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                etMobileNumber.setError("This field is required");
                isValid = false;
            } else {
                etMobileNumber.setError(null);
                selectedAddressModel.mobile = (etMobileNumber.getText().toString()).replace(AppUtils.getStringValueFromJson(selectedContry, "phone_code"), "");
                selectedAddressModel.mobilePrefix = "+" + AppUtils.getStringValueFromJson(selectedContry, "phone_code");
            }

            if (TextUtils.isEmpty(spinnerGenter.getSelectedItem().toString())) {
                toastInFragment("This field is required");
                isValid = false;
            } else {
                selectedAddressModel.gender = spinnerGenter.getSelectedItem().toString();
            }

            if (TextUtils.isEmpty(etZipCode.getText().toString())) {
//                etZipCode.setError("This field is required");
//                isValid = false;
            } else {
                etZipCode.setError(null);
                selectedAddressModel.zipCOde = etZipCode.getText().toString();
            }

            if (TextUtils.isEmpty(etStreetName.getText().toString())) {
                etStreetName.setError("This field is required");
                isValid = false;
            } else {
                etStreetName.setError(null);
                selectedAddressModel.streetName = etStreetName.getText().toString();
            }

            if (TextUtils.isEmpty(txtArea.getText().toString())) {
                txtArea.setError("This field is required");
                isValid = false;
            } else {
                txtArea.setError(null);
                selectedAddressModel.Area = txtArea.getText().toString();
//                selectedAddressModel.areaId = Long.parseLong(txtArea.getTag().toString());
            }

            if (TextUtils.isEmpty(etBlock.getText().toString())) {
                etBlock.setError("This field is required");
                isValid = false;
            } else {
                etBlock.setError(null);
                selectedAddressModel.block = etBlock.getText().toString();
//                selectedAddressModel.blockId = Long.parseLong(etBlock.getTag().toString());
            }

            if (TextUtils.isEmpty(spinnerAddressType.getSelectedItem().toString())) {
                toastInFragment("This field is required");
                isValid = false;
            } else {
                selectedAddressModel.AddressType = spinnerAddressType.getSelectedItem().toString();
            }

            if (isValid) {

                if (fromPage != null && fromPage.equalsIgnoreCase("CHECKOUT")) {
                    goback();
                    LocalCache.getCache().getSelectedOrder().addressModel = selectedAddressModel;
                } else {
                    saveAddress(selectedAddressModel);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAddress(AddressModel addressModel) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (addressModel != null) {
                jsonObject.put("name", addressModel.fullName);
                jsonObject.put("email", addressModel.email);
                jsonObject.put("phone_code", addressModel.mobilePrefix);
                jsonObject.put("phone", addressModel.mobile);
                jsonObject.put("country", addressModel.country);
                jsonObject.put("state", addressModel.Area);
                jsonObject.put("pincode", addressModel.zipCOde);
                jsonObject.put("street", addressModel.streetName);
                jsonObject.put("area", addressModel.Area);
                jsonObject.put("block", addressModel.block);
                jsonObject.put("building", addressModel.block);
                jsonObject.put("title", addressModel.AddressType);
                jsonObject.put("type", addressModel.AddressType);
                jsonObject.put("gender", addressModel.gender);

                generalService.addAddress(ServiceNames.REQUEST_ID_GET_ADD_ADDRESS, jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCountryPopup() {
        Dialog dialog = new Dialog(getFragmentActivity());
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout((int) (getFragmentActivity().getScreenWidth(getFragmentActivity()) * .95), ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setLayout(650, 800);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getFragmentActivity(), android.R.layout.simple_list_item_1, processJsonArrYToStringArray(CountryJsonArr, "name"));

        listView.setAdapter(adapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when item selected from list
                // set selected item on textView
                etCountry.setText(adapter.getItem(position));

                // Dismiss dialog
                dialog.dismiss();
            }
        });
    }

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }
}