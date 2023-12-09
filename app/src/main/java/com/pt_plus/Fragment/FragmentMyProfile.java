package com.pt_plus.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pt_plus.Activitys.SuperActivity;
import com.pt_plus.BuildConfig;
import com.pt_plus.Dialog.BottomSheetDilaog.ImagePickerBoottomSheetDilaog;
import com.pt_plus.Dialog.BottomSheetDilaog.LangugeChageBoottomSheetDilaog;
import com.pt_plus.MainActivity;
import com.pt_plus.R;
import com.pt_plus.Service.AuthService;
import com.pt_plus.Service.Base.CallBack;
import com.pt_plus.Service.Base.ServiceNames;
import com.pt_plus.Service.GeneralService;
import com.pt_plus.Utils.AppDateUtils;
import com.pt_plus.Utils.AppLogger;
import com.pt_plus.Utils.AppUtils;
import com.pt_plus.Utils.Callback.PopUpWithValueCallBack;
import com.pt_plus.Utils.PathUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FragmentMyProfile extends SuperFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initView(view);
        return view;
    }

    private ImageView imgBack;
    private View appbar;
    private TextView txtappBarTitle;
    private BottomSheetDialog bottomSheetDialog;
    private EditText etDob, etMobileNumber;
    private CardView cardViewChooseImage;
    private AuthService authService;
    private GeneralService generalService;
    private AutoCompleteTextView etCountry, txtArea;
    private Spinner spinnerGenter;
    private CardView cardViewBottomSheetDone;
    private CalendarView calendarView;
    private Button btnUpdate;
    private ArrayList<String> genterArr  =null;
    private EditText etName, etEamil;
private ImageView imgProfile;
    private void initView(View view) {
        genterArr   = new ArrayList<>(Arrays.asList(getString(R.string.Male), getString(R.string.Female)));
        imgProfile = view.findViewById(R.id.img_profile);
        etName = view.findViewById(R.id.et_name);
        etEamil = view.findViewById(R.id.et_email);
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(_click);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(_click);
        etDob = view.findViewById(R.id.et_dob);
        etDob.setOnClickListener(_click);
        txtArea = view.findViewById(R.id.txt_area);
        cardViewChooseImage = view.findViewById(R.id.carview_chooseImage);
        cardViewChooseImage.setOnClickListener(_click);

        getFragmentActivity().isDefaultAppBarShow(false);
        getFragmentActivity().setDrawerLock(true);
        getFragmentActivity().isDefaultBottomNavigationBarShow(true);
        appbar = view.findViewById(R.id.appbar);
        getFragmentActivity().setStatusBarHight(appbar);
        txtappBarTitle = view.findViewById(R.id.appbar_title);
        txtappBarTitle.setText(R.string.my_profile);

        bottomSheetDialog = new BottomSheetDialog(getFragmentActivity(), R.style.CustomBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.layout_checkout_shedule_bottomsheet);
        TextView txtBottomSheetTitle = bottomSheetDialog.findViewById(R.id.txt_bottosheettite);
        txtBottomSheetTitle.setText(R.string.choose_date_of_birth);
        calendarView = bottomSheetDialog.findViewById(R.id.calendarView);
        ImageView imgBottomSheetClose = bottomSheetDialog.findViewById(R.id.img_bottom_sheet_close);
        imgBottomSheetClose.setOnClickListener(_click);
        cardViewBottomSheetDone = bottomSheetDialog.findViewById(R.id.bottom_sheet_carview_done);
        cardViewBottomSheetDone.setOnClickListener(_click);
        requestPermissions(false);

        authService = new AuthService(getFragmentActivity(), callBack);
        etMobileNumber = view.findViewById(R.id.txt_mobile_number);
        etMobileNumber.addTextChangedListener(etMobileNUmberWatcher);
        generalService = new GeneralService(getFragmentActivity(), callBack);

        Selection.setSelection(etMobileNumber.getText(), etMobileNumber.getText().length());

        getFragmentActivity().showProgress(true);
        etCountry = view.findViewById(R.id.et_country);
        etCountry.setOnClickListener(_click);


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
        authService.getProfile(ServiceNames.REQUEST_ID_GET_PROFILE);
        if(!getFragmentActivity().isEnglish()){
            etName.setTextDirection(View.TEXT_DIRECTION_RTL);
            etEamil.setTextDirection(View.TEXT_DIRECTION_RTL);
            etCountry.setTextDirection(View.TEXT_DIRECTION_RTL);
            txtArea.setTextDirection(View.TEXT_DIRECTION_RTL);
            etMobileNumber.setTextDirection(View.TEXT_DIRECTION_RTL);
        }
    }

    private JSONObject selectedContry;
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

                    phoneCOde = "+" + AppUtils.getStringValueFromJson(selectedContry, "phone_code");
                    etMobileNumber.setText("+" + AppUtils.getStringValueFromJson(selectedContry, "phone_code"));
//                    if(phoneNumber !=  null && !phoneNumber.isEmpty()&& !phoneNumber.equalsIgnoreCase("")){
//                        etMobileNumber.setText("+" + AppUtils.getStringValueFromJson(selectedContry, "phone_code")+phoneNumber);
//                        phoneNumber ="";
//                    }
                    Selection.setSelection(etMobileNumber.getText(), etMobileNumber.getText().length());

                }
            } else {
                if(etCountry.getText() == null){
                    toastInFragment(getFragmentActivity().getString(R.string.plese_select_country_first));
                }


            }

        }
    };
    private String phoneCOde;

    private JSONArray CountryJsonArr, cityJsonArr;
    private final CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int serviceId, JSONObject jsonObject, int statusCode) {
            try {
                if (serviceId == ServiceNames.REQUEST_ID_UPDATE_PROFILE_PIC) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
                    authService.getProfile(ServiceNames.REQUEST_ID_GET_PROFILE);
                }
                if (serviceId == ServiceNames.REQUEST_ID_GET_PROFILE) {
                    setView(jsonObject);
                    etCountry.clearFocus();
                    txtArea.clearFocus();
                    generalService.getCountries(ServiceNames.REQUEST_ID_GET_COUNTRIES);
                }
                if (serviceId == ServiceNames.REQUEST_UPDATE_PROFIle) {
                    toastInFragment(AppUtils.getStringValueFromJson(jsonObject, "message"));
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

                    authService.getProfile(ServiceNames.REQUEST_ID_GET_PROFILE);
                } else if (serviceId == ServiceNames.REQUEST_ID_GET_COUNTRIES) {
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
                        setSelectedCountry();
                        etCountry.clearFocus();
                        txtArea.clearFocus();
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
                        setSelectedCity();
                        etCountry.clearFocus();
                        txtArea.clearFocus();
                    }
                }
                getFragmentActivity().showProgress(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int serviceId, String errorMessage, int statusCode) {

        }
    };

    private void setSelectedCity() {
        try {
            String city ="";
            if(txtArea.getText() != null && etCountry.getText().toString() != null){
                city = etCountry.getText().toString();
            }

            if(cityJsonArr != null && cityJsonArr.length() > 0){
                for (int i =0 ;i<cityJsonArr.length() ;i++){
                    JSONObject jsonObject = cityJsonArr.getJSONObject(i);
                    if(AppUtils.getStringValueFromJson(jsonObject,"name").equalsIgnoreCase(city)){
                        selectedCity = jsonObject;
                        AppLogger.log("selcted country            "+city);
                        txtArea.setSelection(i);

                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setSelectedCountry() {
        try {
            String country ="";
            if(etCountry.getText() != null && etCountry.getText().toString() != null){
                country = etCountry.getText().toString();
            }

            if(CountryJsonArr != null && CountryJsonArr.length() > 0){
                for (int i =0 ;i<CountryJsonArr.length() ;i++){
                    JSONObject jsonObject = CountryJsonArr.getJSONObject(i);
                    if(AppUtils.getStringValueFromJson(jsonObject,"name").equalsIgnoreCase(country)){
                        selectedContry = jsonObject;
                        AppLogger.log("selcted country            "+country);
                        etCountry.setSelection(i);
                        generalService.getCity(ServiceNames.REQUEST_ID_GET_CITY, jsonObject.getLong("id"));
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
String phoneNumber="";
    private void setView(JSONObject jsonObject) {
        try {
            etName.setText(AppUtils.getStringValueFromJson(jsonObject, "name"));
            etEamil.setText(AppUtils.getStringValueFromJson(jsonObject, "email"));
            etCountry.setText(AppUtils.getStringValueFromJson(jsonObject, "country"));
            phoneCOde = AppUtils.getStringValueFromJson(jsonObject, "phone_code");
            etMobileNumber.setText(phoneCOde +AppUtils.getStringValueFromJson(jsonObject, "phone"));
            phoneNumber = AppUtils.getStringValueFromJson(jsonObject, "phone");

            txtArea.setText(AppUtils.getStringValueFromJson(jsonObject, "area"));
            int indexSelected = 0;
            for (int i = 0; i < genterArr.size(); i++) {
                if (genterArr.get(i).equalsIgnoreCase(AppUtils.getStringValueFromJson(jsonObject, "gender"))) {
                    indexSelected = i;
                }
            }
            spinnerGenter.setSelection(indexSelected);
            etDob.setText(AppUtils.getStringValueFromJson(jsonObject, "dob"));
            Glide.with(getFragmentActivity())
                    .load(AppUtils.getStringValueFromJson(jsonObject,"profile_picture"))
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(imgProfile);

            etMobileNumber.setText(phoneCOde+jsonObject.getLong("phone"));
//            etMobileNumber.setText("ajmalfdfdasfds");
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

    private ArrayList<String> processJsonArrYToStringArray(JSONArray jsonArray, String key) {
        ArrayList<String> resultArray = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    resultArray.add(AppUtils.getStringValueFromJson(jsonObject, key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultArray;
    }

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

                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener _click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: {
                    goback();
                    break;
                }
                case R.id.et_dob: {
                    bottomSheetDialog.show();
                    break;
                }
                case R.id.img_bottom_sheet_close: {
                    bottomSheetDialog.dismiss();
                    break;
                }
                case R.id.carview_chooseImage: {

                    requestPermissions(true);
                    break;
                }
                case R.id.et_country: {
                    etCountry.showDropDown();

//                    showCountryPopup();
                    break;
                }
                case R.id.btn_update: {
                    validateAndSubmitUpdateProfile();
                    break;
                }
                case R.id.bottom_sheet_carview_done: {
                    Calendar selectedDate = calendarView.getFirstSelectedDate();

                    Date date = selectedDate.getTime();
                    etDob.setText(AppDateUtils.getInstance().convertDateToStr(date, "YYYY-MM-dd"));
                    bottomSheetDialog.dismiss();
                    break;
                }


            }
        }
    };

    private void validateAndSubmitUpdateProfile() {
        try {
            JSONObject jsonObject = new JSONObject();
            boolean isValid = true;
            if (TextUtils.isEmpty(etName.getText().toString())) {
                etName.setError("This field is required");
                isValid = false;
            } else {
                etName.setError(null);
                jsonObject.put("name", etName.getText().toString());
            }

            if (TextUtils.isEmpty(etEamil.getText().toString())) {
                etEamil.setError("This field is required");
                isValid = false;
            } else {
                etEamil.setError(null);
                jsonObject.put("email", etEamil.getText().toString());
            }

            if (TextUtils.isEmpty(phoneCOde)) {
                etMobileNumber.setError("This field is required");
                isValid = false;
            } else {
                etMobileNumber.setError(null);
                jsonObject.put("phone_code", phoneCOde);
            }
            if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                etMobileNumber.setError("This field is required");
                isValid = false;
            } else {
                etMobileNumber.setError(null);
                jsonObject.put("phone", (etMobileNumber.getText().toString().replace(phoneCOde, "")));
            }

            if (TextUtils.isEmpty(etCountry.getText().toString())) {
                etCountry.setError("This field is required");
                isValid = false;
            } else {
                etCountry.setError(null);
                jsonObject.put("country", etCountry.getText().toString());
            }

            if (TextUtils.isEmpty(txtArea.getText().toString())) {
                txtArea.setError("This field is required");
                isValid = false;
            } else {
                txtArea.setError(null);
                jsonObject.put("area", txtArea.getText().toString());
            }
            if (TextUtils.isEmpty(etDob.getText().toString())) {
                etDob.setError("This field is required");
                isValid = false;
            } else {
                etDob.setError(null);
                jsonObject.put("dob", etDob.getText().toString());
            }
            if (TextUtils.isEmpty(spinnerGenter.getSelectedItem().toString())) {
//                spinnerGenter.setError("This field is required");
                isValid = false;
            } else {
//                spinnerGenter.setError(null);
                jsonObject.put("gender", spinnerGenter.getSelectedItem().toString());
            }

            if (isValid) {
                authService.updateProfile(ServiceNames.REQUEST_UPDATE_PROFIle, jsonObject);
                getFragmentActivity().showProgress(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getFragmentActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        openCamera();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (which == 1) {
                    openImagesDocument();
                }
            }
        });
        builder.create().show();
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");  // 1
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);  // 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};  // 3
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), IMAGEPICK_GALLERY_REQUEST);  // 4
    }

    private void openCamera() throws IOException {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile(); // 1
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(getFragmentActivity(), BuildConfig.APPLICATION_ID.concat(".provider"), file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    String currentPhotoPath = "";

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        AppLogger.log("sda           " + currentPhotoPath);
        return file;
    }

    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;

    //
    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
//        UCrop.of(sourceUri, destinationUri)
////                .withMaxResultSize(300, 300)
////                .withAspectRatio(5f, 5f)
//                .start(getFragmentActivity());

        startActivityForResult(UCrop.of(sourceUri, destinationUri)

                .getIntent(getContext()), UCrop.REQUEST_CROP);
    }

    private void goback() {

        getFragmentActivity().getSupportFragmentManager().popBackStack();
    }

    private void requestPermissions(boolean isClick) {

        Dexter.withContext(getFragmentActivity())
                .withPermissions(Manifest.permission.CAMERA, permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


if(isClick){
    ImagePickerBoottomSheetDilaog dialogFragment = new ImagePickerBoottomSheetDilaog(getFragmentActivity(), new PopUpWithValueCallBack() {
        @Override
        public void onOK(int value) {
            if (value == 0) {
                try {
                    openCamera();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (value == 1) {
                openImagesDocument();
            }
        }

        @Override
        public void onCancel() {

        }
    });
//                    dialogFragment.setArguments(args);
    dialogFragment.show(getFragmentActivity().getSupportFragmentManager(), "My  Fragment");

}

//                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
//                            // do you work now
////                            Toast.makeText(getFragmentActivity(), "All the permissions are granted..", Toast.LENGTH_SHORT).show();
//                            if (isClick) {
////                                showImagePicDialog();
//
//
//                            }
//                        }
                        // check for permanent denial of any permission
//                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
//                            // permission is denied permanently, we will show user a dialog message.
//                            showSettingsDialog();
//                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Log.e("Dexter", "There was an error: " + error.toString());
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        AppLogger.log("dfdsafsdfsd         " + resultCode);
        AppLogger.log("croped uri   " + data);
        if (requestCode == IMAGE_PICKCAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(currentPhotoPath);
            openCropActivity(uri, uri);

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            AppLogger.log("croped uri   " + uri);
            File file = new File(uri.getPath());
            authService.updateProfilePIc(ServiceNames.REQUEST_ID_UPDATE_PROFILE_PIC, file);
            getFragmentActivity().showProgress(true);
        } else if (requestCode == IMAGEPICK_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri sourceUri = data.getData(); // 1
            File file = null; // 2
            try {
                file = getImageFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Uri destinationUri = Uri.fromFile(file);  // 3
            openCropActivity(sourceUri, destinationUri);  // 4
        }
    }


    public File getFile(Context context, Uri uri) throws IOException, URISyntaxException {
//        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + PathUtil.getPath(getFragmentActivity(), uri));
//        File destinationFilename = new File(PathUtil.getPath(getFragmentActivity(), uri) );
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


}