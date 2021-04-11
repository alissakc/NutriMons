package com.example.nutrimons;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nutrimons.database.AppDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonObject;
import com.google.mlkit.vision.common.InputImage;
import com.tbruyelle.rxpermissions3.RxPermissions;

import com.google.mlkit.vision.barcode.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanBarcode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanBarcode extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PreviewView mPreviewView;
    View view;
    Button button, barcodeRetakeButton, barcodeAcceptButton;
    ImageView bc;
    ImageCapture imageCapture;
    Context context;
    RequestQueue queue;
    AppDatabase mDb;
    com.example.nutrimons.database.Meal food;
    int errorCount = 0;

    final String HEADER = "https://world.openfoodfacts.org/api/v0/product/", FOOTER = ".json";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanBarcode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanBarcode.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanBarcode newInstance(String param1, String param2) {
        ScanBarcode fragment = new ScanBarcode();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan_barcode);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        start();
    }

    private void start()
    {
        //get camera permissions
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                        // Set up the listener for take photo button
                        startCamera();
                    } else {
                        Toast.makeText(getContext(), "Permissions needed for this function", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_to_nav_home);
                    }
                });
    }

    private void startCamera() { //https://akhilbattula.medium.com/android-camerax-java-example-aeee884f9102

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        imageCapture = builder
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(errorCount++ > 2)
                    Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_to_nav_addMeal);
                imageCapture.takePicture(ContextCompat.getMainExecutor((getContext())),
                        new ImageCapture.OnImageCapturedCallback() {
                            @Override
                            public void onCaptureSuccess(ImageProxy image) {
                                // insert your code here.
                                Bitmap bitmap = getBitmap(image); //show image taken up top
                                bc.setImageBitmap(bitmap);

                                @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = image.getImage();
                                if(mediaImage != null) {
                                    InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                                    scanBarcode(inputImage);
                                }
                                mediaImage.close();
                            }
                            @Override
                            public void onError(ImageCaptureException error) {
                                // insert your code here.
                                Toast.makeText(context, "Error taking picture", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private Bitmap getBitmap(ImageProxy image) { //https://stackoverflow.com/questions/61693512/converting-a-captured-image-to-a-bitmap
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }

    private void scanBarcode(InputImage inputImage) //modified from https://github.com/googlesamples/mlkit/blob/a815d463bd79a8ec277f9dcf64bd35de0cc396b7/android/android-snippets/app/src/main/java/com/google/example/mlkit/BarcodeScanningActivity.java#L45-L50
    {
        //set barcode scanner options
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_UPC_A, //food barcode formats https://www.barcodefaq.com/best-to-use/
                                Barcode.FORMAT_UPC_E,
                                Barcode.FORMAT_EAN_8,
                                Barcode.FORMAT_EAN_13)
                        .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(options); //make barcode client

        Task<List<Barcode>> result = scanner.process(inputImage) //process barcode
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        for (Barcode barcode: barcodes) {
                            String rawValue = barcode.getRawValue();
                            View br = view.findViewById(R.id.barcodeResult);
                            View vf = view.findViewById(R.id.viewFinder);

                            if(rawValue != null)
                                errorCount = 0;
                            else
                                Log.d("barcode", "null");

                            queue.cancelAll(rawValue);

                            JsonRequest JReq = callOFFapi(rawValue);
                            queue.add(JReq);
                            br.setVisibility(View.VISIBLE);
                            vf.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error Processing Barcode. Try adding manually", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_to_nav_addMeal);
                    }
                });
    }

    private JsonRequest callOFFapi(String barcodeString) //https://wiki.openfoodfacts.org/API ; also has an app: https://github.com/openfoodfacts/openfoodfacts-androidapp
    {
        String searchURL = HEADER + barcodeString + FOOTER;

        return new JsonObjectRequest(Request.Method.GET, searchURL, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            TextView tv = view.findViewById(R.id.barcodeApiResults);
                            tv.setText(response.toString()); //ref: https://world.openfoodfacts.org/api/v0/product/0075270410521.json

                            JSONObject product = response.getJSONObject("product");
                            food.mealName = product.getString("product_name");
                            food.servingSize = product.getString("serving_size");
                            food.servingsEaten = 1;

                            JSONObject nutriments = product.getJSONObject("nutriments");
                            HashMap<String, String> nutrientsOfInterest = generateNutrientsOfInterest();
                            for(int i = 0; i < nutriments.names().length(); ++i)
                            {
                                String key = nutriments.names().getString(i);
                                if(nutrientsOfInterest.containsKey(key))
                                {
                                    food.setFieldFromString(nutrientsOfInterest.get(key), convertOFFResponseUnits(key, nutriments.get(key)));
                                }
                            }

                            TextView body = view.findViewById(R.id.barcodeApiResults);
                            body.setText(food.toTextViewString());

                            // catch for the JSON parsing error
                        } catch (Exception e/*JSONException e*/) {
                            Toast.makeText(context, "error with api response", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(context, "api not responding", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override //change http header per OFF api READ operations request
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("UserAgent: NutriMons - Android - Version 0.0 - https://github.com/alissakc/NutriMons", "CSULB CECS 491: BAMM");

                return params;
            }
        };
    }

    private void setContentView(int activity_dashboard) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_scan_barcode, container, false);
        mDb = AppDatabase.getInstance(getContext());
        mPreviewView = view.findViewById(R.id.viewFinder);
        food = new com.example.nutrimons.database.Meal();
        button = view.findViewById(R.id.camera_capture_button);
        barcodeRetakeButton = view.findViewById(R.id.barcodeRetake);
        barcodeRetakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_self);
            }
        });
        barcodeAcceptButton = view.findViewById(R.id.barcodeAccept);
        barcodeAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AddMeal.food = food;
                    Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_to_nav_addMeal);
                }
                catch(SQLiteConstraintException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Barcode already scanned", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigate(R.id.action_nav_scanBarcode_self);
                }
            }
        });
        bc = view.findViewById(R.id.barcodePreview);
        context = getContext();
        queue = Volley.newRequestQueue(getContext());

        return view;
    }

    private HashMap<String, String> generateNutrientsOfInterest()
    {
        HashMap<String, String> nutrientsOfInterest = new HashMap<>();

        nutrientsOfInterest.put("energy-kcal", "calories"); //kcal
        //nutrientsOfInterest.put("Water", "water"); //doesn't seem to have
        nutrientsOfInterest.put("proteins", "protein"); //g
        nutrientsOfInterest.put("carbohydrates", "carbohydrate"); //g
        nutrientsOfInterest.put("sugars", "sugar"); //g
        nutrientsOfInterest.put("fiber", "fiber"); //g
        nutrientsOfInterest.put("cholesterol", "cholesterol"); //mg
        nutrientsOfInterest.put("saturated-fat", "saturatedFat"); //g
        nutrientsOfInterest.put("monounsaturated-fat", "monounsaturatedFat"); //g
        nutrientsOfInterest.put("polyunsaturated-fat", "polyunsaturatedFat"); //g
        nutrientsOfInterest.put("trans-fat", "transFat"); //g

        nutrientsOfInterest.put("vitamin-a", "vitaminA"); //µg
        nutrientsOfInterest.put("vitamin-c", "vitaminC"); //mg
        nutrientsOfInterest.put("vitamin-d", "vitaminD"); //µg
        //nutrientsOfInterest.put("vitamin-e", "vitaminE");
        //nutrientsOfInterest.put("vitamin-k", "vitaminK");
        //nutrientsOfInterest.put("thiamin", "thiamin");
        //nutrientsOfInterest.put("riboflavin", "riboflavin");
        //nutrientsOfInterest.put("niacin", "niacin");
        //nutrientsOfInterest.put("vitamin-b-6", "vitaminB6");
        //nutrientsOfInterest.put("folate", "folate");
        //nutrientsOfInterest.put("vitamin-b-12", "vitaminB12");
        //nutrientsOfInterest.put("pantothenic-acid", "pantothenicAcid");
        //nutrientsOfInterest.put("biotin", "biotin");
        //nutrientsOfInterest.put("choline", "choline");
        //nutrientsOfInterest.put("carotenoids", "carotenoids");

        nutrientsOfInterest.put("sodium", "sodium"); //g
        nutrientsOfInterest.put("potassium", "potassium"); //g
        nutrientsOfInterest.put("calcium", "calcium"); //mg
        nutrientsOfInterest.put("iron", "iron"); //mg
        //nutrientsOfInterest.put("chromium", "chromium");
        //nutrientsOfInterest.put("copper", "copper");
        //nutrientsOfInterest.put("fluoride", "fluoride");
        //nutrientsOfInterest.put("iodine", "iodine");
        //nutrientsOfInterest.put("magnesium", "magnesium");
        //nutrientsOfInterest.put("manganese", "manganese");
        //nutrientsOfInterest.put("molybdenum", "molybdenum");
        //nutrientsOfInterest.put("phosphorous", "phosphorous");
        //nutrientsOfInterest.put("selenium", "selenium");
        //nutrientsOfInterest.put("zinc", "zinc");
        //nutrientsOfInterest.put("chloride", "chloride");
        //nutrientsOfInterest.put("arsenic", "arsenic");
        //nutrientsOfInterest.put("boron", "boron");
        //nutrientsOfInterest.put("nickel", "nickel");
        //nutrientsOfInterest.put("silicon", "silicon");
        //nutrientsOfInterest.put("vanadium", "vanadium");

        return nutrientsOfInterest;
    }

    private float convertOFFResponseUnits(String nutrient, Object value)
    {
        if(value.getClass() == Integer.class)
        {
            switch(nutrient) //all values from these names in json are in kcl or grams
            {
                //already in correct units, just round to ints
                case "energy-kcal":
                case "proteins":
                case "carbohydrates":
                case "sugars":
                case "fiber":
                case "saturated-fat":
                case "monounsaturated-fat":
                case "polyunsaturated-fat":
                case "trans-fat":
                case "sodium":
                    return ((Integer) value).floatValue();
                //1000 off since we use mg
                case "cholesterol":
                case "vitamin-c":
                case "potassium":
                case "calcium":
                case "iron":
                    return ((Integer) value).floatValue() * 1000;
                //1000000 off since we use µg
                case "vitamin-a":
                case "vitamin-d":
                    return ((Integer) value).floatValue() * 1000000;
                default:
                    return -1;
            }
        }
        else if(value.getClass() == Double.class)
        {
            switch(nutrient) //all values from these names in json are in kcl or grams
            {
                //already in correct units, just round to ints
                case "energy-kcal":
                case "proteins":
                case "carbohydrates":
                case "sugars":
                case "fiber":
                case "saturated-fat":
                case "monounsaturated-fat":
                case "polyunsaturated-fat":
                case "trans-fat":
                case "sodium":
                case "potassium":
                    return ((Double) value).floatValue();
                //1000 off since we use mg
                case "cholesterol":
                case "vitamin-c":
                case "calcium":
                case "iron":
                    return ((Double) value).floatValue() * 1000;
                //1000000 off since we use µg
                case "vitamin-a":
                case "vitamin-d":
                    return ((Double) value).floatValue() * 1000000;
                default:
                    return -1;
            }
        }
        else
            return -2;
    }
}