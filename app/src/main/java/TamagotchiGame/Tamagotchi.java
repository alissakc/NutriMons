package TamagotchiGame;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.example.nutrimons.Profile;
import com.example.nutrimons.R;
import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.TamagotchiPet;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

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

    // vars to go to store
    Button goToStore;

    //var for feeding pet
    ProgressBar healthBar;
    Button feedButton;
    int healthCounter;

    //var for giving pet water
    ProgressBar waterBar;
    Button waterButton;
    int waterCounter;




    //game stuff
    ImageButton TamagotchiPet;
    private float petX;
    private float petY;

    private int dirX = 1;
    private int dirY = 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    int counter = 0;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tamagotchi, container, false);

        mDb = AppDatabase.getInstance(getContext());
        Log.d("user", String.valueOf(mDb.tokenDao().getUserID()));
        TamagotchiPet tama = mDb.tamagotchiDao().findByUserId(mDb.tokenDao().getUserID());
        Log.d("health", String.valueOf(tama.healthLevel));
        //List<TamagotchiPet> allPet =  mDb.tamagotchiDao().getAll();

        //System.out.print(allPet);
        name = tama.petName;
        petName = view.findViewById(R.id.petName);
        petName.setText(name);
        petName.addTextChangedListener(new Tamagotchi.TextChangedListener<EditText>(petName, name));

        if (name == null)
        {
                name = "Name";
                TamagotchiPet tama1 = mDb.tamagotchiDao().findByUserId(mDb.tokenDao().getUserID());
                tama1.petName = name;
                mDb.tamagotchiDao().insert(tama1);
        }
        else if (name.isEmpty())
        {
            name = "Name";
            TamagotchiPet tama1 = mDb.tamagotchiDao().findByUserId(mDb.tokenDao().getUserID());
            tama1.petName = name;
            mDb.tamagotchiDao().insert(tama1);
        }


        goToStore = view.findViewById(R.id.storeButton);
        goToStore.setOnClickListener(this);


        //editing name
        petName = view.findViewById(R.id.petName);


        //feeding pet
        feedButton = view.findViewById(R.id.feedButton);
        healthBar = view.findViewById(R.id.healthBar);

        healthCounter = tama.healthLevel;
        healthBar.setProgress(healthCounter);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthCounter++;
                //healthCounter = 0;
                healthBar.setProgress(healthCounter);
                tama.healthLevel = healthCounter;
                mDb.tamagotchiDao().insert(tama);
            }
        });

        //giving water
        waterBar = view.findViewById(R.id.waterBar);
        waterButton = view.findViewById(R.id.waterButton);
        waterCounter = tama.waterLevel;
        waterBar.setProgress(waterCounter);
        waterButton.setOnClickListener(v -> {
            waterCounter++;
            //waterCounter = 0;
            waterBar.setProgress(waterCounter);
            tama.waterLevel = waterCounter;
            mDb.tamagotchiDao().insert(tama);
        });

        TamagotchiPet = view.findViewById(R.id.TamagotchiPet);
        LinearLayout ll_list = view.findViewById(R.id.petPlayZone);



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
                            /*
                            Random ran = new Random();

                            if(ran.nextInt() % 2 == 0)
                            {
                                changePos();
                            }
                            */


                            if (counter < 5)
                            {
                                System.out.println("STOP NOW");
                            }
                            else {

                                changePos();
                            }
                            if (counter >= 10 || counter < 0)
                            {
                                counter = 0;
                            }
                            counter++;


                            TamagotchiPet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TamagotchiPet.animate().rotation(TamagotchiPet.getRotation()-360).start();
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
    private void stopMovin()
    {

    }

    private void changePos() throws InterruptedException {
        int speed = 20;

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
            TamagotchiPet tama = mDb.tamagotchiDao().findByUserId(mDb.tokenDao().getUserID());
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