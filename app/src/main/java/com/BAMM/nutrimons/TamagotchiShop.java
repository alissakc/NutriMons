package com.BAMM.nutrimons;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BAMM.nutrimons.database.AppDatabase;
import com.BAMM.nutrimons.database.ShopItem;
import com.BAMM.nutrimons.database.TamagotchiPet;
import com.BAMM.nutrimons.database.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TamagotchiShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TamagotchiShop extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AppDatabase mDb;
    private TamagotchiPet tama;

 
    private User user;
    private LinearLayout shopItemsArea;

    Button hatsButt, petsButt, backButt, allButt;
    //Coins
    TextView coins;

    private View view;

    private List<ShopItem> shopItems;

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
        view = inflater.inflate(R.layout.fragment_tamagotchi_shop, container, false);

        mDb = AppDatabase.getInstance(getContext());
        tama = BAMM.getCurrentTamagotchi();
        user = BAMM.getCurrentUser();

        //setting coins
        coins = view.findViewById(R.id.tamaCoins);
        coins.setText(String.valueOf(user.nutriCoins));
        //coins.setText(String.valueOf(tama.coins));
        //mDb.tamagotchiDao().insert(tama);


        //shopItemsArea = (LinearLayout) view.findViewById(R.id.shopItemsArea);
        //shopItemsArea.addView(new TextView(getContext()));


        hatsButt = view.findViewById(R.id.hatsButt);
        hatsButt.setOnClickListener(this);

        allButt = view.findViewById(R.id.allButt);
        allButt.setOnClickListener(this);
        //List<ShopItem> shopItems = mDb.shopItemDao().getAll();
        //shopItemsArea = (LinearLayout) view.findViewById(R.id.shopItemsArea);
        //shopItemsArea.addView(new TextView(getContext()));


        //shopItemsArea = (LinearLayout) view.findViewById(R.id.shopItemsArea);
        //shopItemsArea.addView(new TextView(getContext()));
        petsButt = view.findViewById(R.id.petsButt);
        petsButt.setOnClickListener(this);


        backButt = view.findViewById(R.id.backToTamagotchi);
        backButt.setOnClickListener(this);

        shopItems = mDb.shopItemDao().getAll();
        shopItems.clear();
        List<ShopItem> petList= mDb.shopItemDao().getShopItemByCategory("pets");
        List<ShopItem> hatsList= mDb.shopItemDao().getShopItemByCategory("hats");
        shopItems.addAll(petList);
        shopItems.addAll(hatsList);
        populateShop();

        /*for(ShopItem si : shopItems)
        {
            TextView tv = new TextView(getContext());
            tv.setText(si.name);
            //tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            shopItemsArea.addView(tv);

            ImageButton ib = new ImageButton(getContext());
            ib.setImageBitmap(StringToBitMap(si.image));
            ib.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            ib.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          //Log.d("clicked", String.valueOf(si.shopItemID));
                                          try {
                                              if(tama.shopItems.contains(String.valueOf(si.shopItemID)))
                                                  Toast.makeText(getContext(), "You already own this", Toast.LENGTH_SHORT).show();
                                              else
                                              {
                                                  tama.shopItems.add(String.valueOf(si.shopItemID));
                                                  mDb.tamagotchiDao().insert(tama);
                                              }
                                          } catch (SQLiteConstraintException e) {
                                              Toast.makeText(getContext(), "You already own this", Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  });
            shopItemsArea.addView(ib);

            shopItemsArea.addView(new TextView(getContext()));
        }
           */
        /*for(ShopItem s : shopItems)
        {
            Log.d("name: ", s.name);
            Log.d("category: ", s.category);
            Log.d("cost: ", String.valueOf(s.cost));
        }*/

        return view;
    }

    @Override
    public void onClick(View v)
    {
        Button b = (Button) v;
        switch(b.getText().toString()) {
            case "Hats":
                shopItems = mDb.shopItemDao().getShopItemByCategory("hats");
                break;
            case "Toys":
                shopItems = mDb.shopItemDao().getShopItemByCategory("toys");
                break;
            case "Pets":
                shopItems = mDb.shopItemDao().getShopItemByCategory("pets");
                break;
            case "Back":
                Navigation.findNavController(view).navigate(R.id.action_nav_tamagotchiShop_to_nav_tamagotchi);
                break;
            case "All":
                //shopItems = mDb.shopItemDao().getAll();
                shopItems.clear();
                List<ShopItem> petList= mDb.shopItemDao().getShopItemByCategory("pets");
                List<ShopItem> hatsList= mDb.shopItemDao().getShopItemByCategory("hats");
                shopItems.addAll(petList);
                shopItems.addAll(hatsList);
                break;
            //default:
                //shopItems = mDb.shopItemDao().getAll();
        }
        populateShop();
    }

    private void populateShop()
    {
        LinearLayout shopItemsArea = (LinearLayout) view.findViewById(R.id.shopItemsArea);
        //shopItemsArea.addView(new TextView(getContext()));
        shopItemsArea.removeAllViews();
        int i = 0;
        for(ShopItem si : shopItems)
        {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);
            rowLayout.setPadding(20,10, 20, 10);
            if(i++ % 2 == 0)
                rowLayout.setBackgroundColor(Color.parseColor("#22222222"));
            else
                rowLayout.setBackgroundColor(Color.parseColor("#11111111"));

            //rowLayout.addView(createTvLabel(si.name, 18, "#660000ff"));

            ImageView iv = new ImageView(getContext());
            iv.setImageBitmap(BAMM.StringToBitMap(si.image));
            iv.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            rowLayout.addView(iv);

            //rowLayout.addView(createTvLabel(si.category, 14, "#660000ff"));

            rowLayout.addView(createTvLabel(String.valueOf(si.cost) + " coins", 14, "#660000ff"));

            View ownedView;
            if(si.owned == 0)
            {
                ownedView = createTvLabel("Buy", 14, "#66ff00ff");
                rowLayout.addView(ownedView);
            }
            else
            {
                ownedView = createTvLabel("Sold!", 14, "#44000000");
                rowLayout.addView(ownedView);
            }
            ownedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d("clicked", String.valueOf(si.shopItemID));
                    try {
                        if(si.owned==1)//tama.shopItems.contains(String.valueOf(si.shopItemID)))
                        {
                            Toast.makeText(getContext(), "You already own this", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(user.nutriCoins>=si.cost) {

                                user.nutriCoins-=si.cost;
                                mDb.userDao().insert(user);
                                coins.setText(String.valueOf(user.nutriCoins));
                                si.owned = 1;
                                System.out.println(si.name);
                                switch (si.name) {

                                    case "Propellor Hat":
                                        ShopItem temp = mDb.shopItemDao().getShopItemByName("PigWithPropellor");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        temp = mDb.shopItemDao().getShopItemByName("CatWithPropellor");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        temp = mDb.shopItemDao().getShopItemByName("PandaWithPropellor");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        break;
                                    case "Red Hat":
                                        temp = mDb.shopItemDao().getShopItemByName("PigWithRedHat");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        temp = mDb.shopItemDao().getShopItemByName("CatWithRedHat");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        temp = mDb.shopItemDao().getShopItemByName("PandaWithRedHat");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        break;
                                    case "Winter Hat":
                                        temp = mDb.shopItemDao().getShopItemByName("PigWithWinter");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        temp = mDb.shopItemDao().getShopItemByName("CatWithWinter");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        temp = mDb.shopItemDao().getShopItemByName("PandaWithWinter");
                                        temp.owned = 1;
                                        mDb.shopItemDao().insert(temp);
                                        break;
                                }
                                mDb.shopItemDao().insert(si);
                                populateShop();
                            }
                            else
                                Toast.makeText(getContext(), "YOU NEED MONEY", Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLiteConstraintException e) {
                        Toast.makeText(getContext(), "You already own this", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            shopItemsArea.addView(rowLayout);
        }
    }

    private TextView createTvLabel(String label, float size, String color)
    {
        TextView tv = new TextView(getContext());
        tv.setText(label);
        tv.setWidth((int)(size * 12));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextSize(size);
        tv.setBackgroundColor(Color.parseColor(color));
        return tv;
    }
}