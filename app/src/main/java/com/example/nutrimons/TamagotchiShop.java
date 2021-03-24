package com.example.nutrimons;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.ShopItem;
import com.example.nutrimons.database.TamagotchiPet;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TamagotchiShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TamagotchiShop extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AppDatabase mDb;
    private TamagotchiPet tama;
    private LinearLayout shopItemsArea;

    //Coins
    TextView coins;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TamagotchiShop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TamagotchiShop.
     */
    // TODO: Rename and change types and number of parameters
    public static TamagotchiShop newInstance(String param1, String param2) {
        TamagotchiShop fragment = new TamagotchiShop();
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
        View view = inflater.inflate(R.layout.fragment_tamagotchi_shop, container, false);

        mDb = AppDatabase.getInstance(getContext());
        tama = mDb.tamagotchiDao().findByUserId(mDb.tokenDao().getUserID());

        //setting coins
        coins = view.findViewById(R.id.tamaCoins);
        coins.setText(String.valueOf(tama.coins));
        mDb.tamagotchiDao().insert(tama);

        List<ShopItem> shopItems = mDb.shopItemDao().getAll();
        shopItemsArea = (LinearLayout) view.findViewById(R.id.shopItemsArea);
        shopItemsArea.addView(new TextView(getContext()));

        for(ShopItem si : shopItems)
        {
            TextView tv = new TextView(getContext());
            tv.setText(si.name);
            //tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            shopItemsArea.addView(tv);

            ImageButton ib = new ImageButton(getContext());
            ib.setImageBitmap(StringToBitMap(si.image));
            ib.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          try {
                                              tama.shopItems.add(String.valueOf(si.shopItemID));
                                              mDb.tamagotchiDao().insert(tama);
                                          } catch (SQLiteConstraintException e) {
                                              Toast.makeText(getContext(), "You already own this", Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  });
            shopItemsArea.addView(ib);

            shopItemsArea.addView(new TextView(getContext()));
        }

        /*for(ShopItem s : shopItems)
        {
            Log.d("name: ", s.name);
            Log.d("category: ", s.category);
            Log.d("cost: ", String.valueOf(s.cost));
        }*/

        return view;
    }

    public Bitmap StringToBitMap(String encodedString){ //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}