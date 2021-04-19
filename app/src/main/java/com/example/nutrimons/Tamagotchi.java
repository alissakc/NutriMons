package com.example.nutrimons;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.ShopItem;
import com.example.nutrimons.database.TamagotchiPet;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tamagotchi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tamagotchi extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private AppDatabase mDb;

    //screen size
    private int screenWidth;
    private int screenHeight;

    //linearlayout size
    private int llWidth;
    private int llHeight;


    //Edit pet name
    EditText petName;
    String name;

    //Edit level
    TextView levelingView;

    // vars to go to store
    Button goToStore;

    //var for feeding pet
    ProgressBar healthBar;
    Button feedButton;

    //var for giving pet water
    ProgressBar waterBar;
    Button waterButton;
    int waterCounter;

    //var for changing animal
    Button changeAnimal;
    private int currentImage;
    //int[] images = {R.drawable.tamagotchi_pig, R.drawable.tamagotchi_big, R.drawable.tama__pic};

    List<Bitmap> imagesPet = new ArrayList<Bitmap>();



    Button hatButt;


    //pet movement
    ImageButton TamagotchiPet;
    private float petX;
    private float petY;
    int speed = 20;
    private int dirX = 1;
    private int dirY = 1;

    //shop
    int coins;
    private Handler handler = new Handler();
    private Timer timer = new Timer();


    int counterForStopping = 0;

    public Tamagotchi() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tamagotchi.
     */
    // TODO: Rename and change types and number of parameters
    public static Tamagotchi newInstance(String param1, String param2) {
        Tamagotchi fragment = new Tamagotchi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tamagotchi, container, false);
        // Inflate the layout for this fragment

        //get DB and pet
        mDb = AppDatabase.getInstance(getContext());
        TamagotchiPet tama = BAMM.getCurrentTamagotchi();


        List<ShopItem> shopList = mDb.shopItemDao().getAll();
        for(ShopItem si : shopList)
        {
            if(si.owned==1)
            {
                if(si.category.equals("pets"))
                {
                    ImageButton ib = new ImageButton(getContext());
                    imagesPet.add(BAMM.StringToBitMap(si.image));
                }
            }
            System.out.println("IMAGEPET"+imagesPet);
        }


        name = tama.petName;
        if (name == null)
        {
            name = "Name";
            TamagotchiPet tama1 = BAMM.getCurrentTamagotchi();
            tama1.petName = name;
            mDb.tamagotchiDao().insert(tama1);
        }
        else if (name.isEmpty())
        {
            name = "Name";
            TamagotchiPet tama1 = BAMM.getCurrentTamagotchi();
            tama1.petName = name;
            mDb.tamagotchiDao().insert(tama1);
        }
        petName = view.findViewById(R.id.petName);
        petName.setText(name);
        petName.addTextChangedListener(new Tamagotchi.TextChangedListener<EditText>(petName, name));


        goToStore = view.findViewById(R.id.storeButton);
        goToStore.setOnClickListener(this);


        //Editing name
        petName = view.findViewById(R.id.petName);

        //Level Number
        levelingView = view.findViewById(R.id.levelNum);
        levelingView.setText(String.valueOf(tama.level));


        //feeding pet
        feedButton = view.findViewById(R.id.feedButton);
        healthBar = view.findViewById(R.id.healthBar);
        healthBar.setProgress(tama.healthLevel);
        feedButton.setOnClickListener(v -> {
            healthBar.setProgress(++tama.healthLevel);

            if (tama.healthLevel > 20)
            {
                tama.healthLevel = 20;
            }
            mDb.tamagotchiDao().insert(tama);
        });

        //giving water
        waterBar = view.findViewById(R.id.waterBar);
        waterButton = view.findViewById(R.id.waterButton);
        waterBar.setProgress(tama.waterLevel);
        waterButton.setOnClickListener(v -> {
            waterBar.setProgress(++tama.waterLevel);
            if (tama.waterLevel > 20)
            {
                tama.waterLevel = 20;
            }
            mDb.tamagotchiDao().insert(tama);
        });


        hatButt = view.findViewById(R.id.hatsButt);
        /*hatButt.setOnClickListener(v -> {


        });
        */


        TamagotchiPet = view.findViewById(R.id.TamagotchiPet);
        LinearLayout ll_list = view.findViewById(R.id.petPlayZone);

        //Changing animal
        changeAnimal = view.findViewById(R.id.petButton);
        changeAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImage++;
                currentImage = currentImage % imagesPet.size();
                TamagotchiPet.setImageBitmap((imagesPet.get(currentImage)));
            }
        });

        //time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        //Get current time(String) then convert to milliseconds(long)
        String currentDateTime = sdf.format(c.getTime());
        Long currTimeConvert = testingTime(currentDateTime).getTime();
        System.out.println("CURRENT: "+currTimeConvert);


        //Get last logged on
        String lastLogIn = mDb.tamagotchiDao().getLastLoggedIn();
        Long lastLogInConvert = testingTime(currentDateTime).getTime();;
        if (lastLogIn != null) {
            lastLogInConvert = testingTime(lastLogIn).getTime();
            //System.out.println("LAST: " + lastLogInConvert);
        }



        int difference = (int)(currTimeConvert-lastLogInConvert)/1000;
        Log.d("TWO NUM", String.valueOf(currTimeConvert+" "+lastLogInConvert));
        Log.d("DIFFERENCE", String.valueOf(difference));


        //Lowering the health by time
        int lowerHealthWater = difference/60;

        tama.healthLevel -= lowerHealthWater;
        if (tama.healthLevel < 0)
        {
            tama.healthLevel = 0;
        }

        tama.waterLevel -= lowerHealthWater;
        if (tama.waterLevel < 0)
        {
            tama.waterLevel = 0;
        }
        mDb.tamagotchiDao().insert(tama);
        healthBar.setProgress(tama.healthLevel);
        waterBar.setProgress(tama.waterLevel);


        //Get Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //get linear layout size
        ll_list.post(new Runnable() {
            public void run() {
                llWidth = ll_list.getWidth();
                llHeight = ll_list.getHeight();
            }
        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (counterForStopping > 10) {
                                speed = 0;
                            }
                            else {
                                speed = 20;
                                changePos();
                            }
                            if (counterForStopping >= 50 || counterForStopping < 0)
                            {
                                counterForStopping = 0;
                            }
                            counterForStopping++;


                            TamagotchiPet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TamagotchiPet.animate().rotation(TamagotchiPet.getRotation()-360).start();
                                    coins = tama.coins;
                                    coins++;
                                    tama.coins++;
                                    tama.totalClicks++;
                                    levelByClicking(tama, view);
                                    mDb.tamagotchiDao().insert(tama);
                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        },0,100);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first
        System.out.println("STOP BITCHES");
        TamagotchiPet tama = BAMM.getCurrentTamagotchi();
        //update last logged on
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        //Get current time(String) then convert to milliseconds(long)
        String currentDateTime = sdf.format(c.getTime());
        tama.lastLoggedIn = currentDateTime;
        mDb.tamagotchiDao().insert(tama);
    }

    private Date testingTime(String dtStart) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void levelByClicking(TamagotchiPet tama, View view)
    {
        if (tama.totalClicks%10==0)
        {
            tama.level++;
        }
        mDb.tamagotchiDao().insert(tama);
        levelingView = view.findViewById(R.id.levelNum);
        levelingView.setText(String.valueOf(tama.level));
    }


    private void changePos() throws InterruptedException {


        if(TamagotchiPet.getY() < 0 ) {
            dirY = 1;
        }
        else if(TamagotchiPet.getY() + TamagotchiPet.getHeight() >= llHeight) {
            dirY = -1;
        }
        if(TamagotchiPet.getX() < 0) {
            dirX = 1;
        }
        else if(TamagotchiPet.getX() + TamagotchiPet.getWidth() >= llWidth) {
            dirX = -1;
        }
        double randomChange = Math.random();
        if(randomChange < .1) {

            double randomDir = Math.random();
            if (randomDir < .20)
                dirX = 1;
            else if (randomDir < .40)
                dirY = 1;
            else if (randomDir < .60)
                dirX = -1;
            else if (randomDir < .80)
                dirY = -1;


        }
        petY += dirY * speed;
        petX += dirX * speed;

        TamagotchiPet.setX(petX);
        TamagotchiPet.setY(petY);
    }


    public class TextChangedListener<T> implements TextWatcher { //https://stackoverflow.com/questions/11134144/android-edittext-onchange-listener
        private T target;
        private EditText et;
        private String str;

        public TextChangedListener(T target, String str) {
            this.target = target;
            this.str = str;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}


        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }

        public /*abstract*/ void onTextChanged(T target, Editable s)
        {
            //target.setText(s); //infinite loop since text now changed
            //also set the view fields database values then replace str with a db insert
            et = (EditText) target;
            str = et.getText().toString();
            name = str;
            TamagotchiPet tama = BAMM.getCurrentTamagotchi();
            tama.petName = name;
            mDb.tamagotchiDao().insert(tama);
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.storeButton):
                Navigation.findNavController(view).navigate(R.id.action_nav_tamagotchi_to_nav_tamagotchiShop);
                break;
        }
    }
}