package TamagotchiGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutrimons.R;

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
    //onRun runGame = new onRun();

    //screen size
    private int screenWidth;
    private int screenHeight;

    // vars
    Button goToStore;
    TextView feedText;
    Button feedButton;
    int feedCounter;

    ImageView TamagotchiPet;
    private float petX;
    private float petY;

    private int dirX = 1;
    private int dirY = 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    public Tamagotchi() {
        // Required empty public constructor
        //runGame.run();
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

        goToStore = view.findViewById(R.id.storeButton);
        goToStore.setOnClickListener(this);
        feedText = (TextView) view.findViewById(R.id.feedNum);
        feedButton = view.findViewById(R.id.feedButton);
        feedButton.setOnClickListener(this);
        TamagotchiPet = (ImageView) view.findViewById(R.id.TamagotchiPet);

        //Get Screen Size
        //WindowManager wm = (WindowManager) Context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        //Display disp = wm.getDefaultDisplay();
        //Point size = new Point();
        //disp.getSize(size);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        System.out.println(screenHeight + " " + screenWidth);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePos();
                    }
                });
            }
        },0,20);



        return view;
    }
    public void changePos() {
        int speed = 20;

        if(TamagotchiPet.getY() < 0 ) {
            //petX = (float)Math.floor(Math.random() * (screenWidth - TamagotchiPet.getWidth()));
            //petY = screenHeight + 100.0f;
            dirY = 1;
        }
        else if(TamagotchiPet.getY() + TamagotchiPet.getHeight() >= screenHeight) {
            dirY = -1;
        }
        if(TamagotchiPet.getX() < 0) {
            dirX = 1;
        }
        else if(TamagotchiPet.getX() + TamagotchiPet.getWidth() >= screenWidth) {
            dirX = -1;
        }
        petY += dirY * speed;
        petX += dirX * speed;
        TamagotchiPet.setX(petX);
        TamagotchiPet.setY(petY);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.storeButton):
                Navigation.findNavController(view).navigate(R.id.action_nav_tamagotchi_to_nav_tamagotchiShop);
                break;
            case(R.id.feedButton):
                TextView text=(TextView) feedText;
                feedCounter++;
                text.setText("YOURE FAT" + " " + feedCounter);


        }
    }
}