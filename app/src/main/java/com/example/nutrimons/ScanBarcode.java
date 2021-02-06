package com.example.nutrimons;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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

import android.os.Environment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.tbruyelle.rxpermissions3.RxPermissions;

import com.google.mlkit.vision.barcode.*;

import org.json.JSONArray;
import org.json.JSONException;
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
    Button button;
    ImageView bc;
    ImageCapture imageCapture;
    Executor executor = Executors.newSingleThreadExecutor();
    Context context;
    RequestQueue queue;

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
                        Manifest.permission.INTERNET/*,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE*/) // ask single or multiple permission once
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
                imageCapture.takePicture(ContextCompat.getMainExecutor((getContext())),
                        new ImageCapture.OnImageCapturedCallback() {
                            @Override
                            public void onCaptureSuccess(ImageProxy image) {
                                // insert your code here.
                                Bitmap bitmap = getBitmap(image); //show image taken in corner
                                bc.setImageBitmap(bitmap);

                                @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = image.getImage();
                                if(mediaImage != null) {
                                    InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                                    scanBarcode(inputImage);
                                }
                                //Toast.makeText(context, "Image taken", Toast.LENGTH_SHORT).show();
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
                        // [START_EXCLUDE]
                        // [START get_barcodes]
                        for (Barcode barcode: barcodes) {
                            //Rect bounds = barcode.getBoundingBox();
                            //Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();
                            //int valueType = barcode.getValueType();
                            TextView tv = view.findViewById(R.id.textView8);
                            tv.setText(rawValue);

                            queue.cancelAll(rawValue);
                            StringRequest strReq = callOFFapi(rawValue);
                            queue.add(strReq);
                            tv = view.findViewById(R.id.textView44);
                            tv.setText(strReq.toString());
                        }
                        // [END get_barcodes]
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Toast.makeText(context, "Error Processing Barcode", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private StringRequest callOFFapi(String barcodeString) //https://wiki.openfoodfacts.org/API
    {
        final String HEADER = "https://world.openfoodfacts.org/api/v0/product/";
        final String FOOTER = ".json";

        String searchURL = HEADER + barcodeString + FOOTER;

        return new StringRequest(Request.Method.GET, searchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response).getJSONObject("list");
                            int maxItems = result.getInt("end");
                            JSONArray resultList = result.getJSONArray("item");
                            Toast.makeText(context, resultList.toString(), Toast.LENGTH_SHORT).show();

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(context, "error with api response", Toast.LENGTH_SHORT).show();
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
        mPreviewView = view.findViewById(R.id.viewFinder);
        button = view.findViewById(R.id.camera_capture_button);
        bc = view.findViewById(R.id.imageView31);
        context = getContext();
        queue = Volley.newRequestQueue(getContext());

        return view;
    }
}