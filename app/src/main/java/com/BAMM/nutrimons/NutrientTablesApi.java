package com.BAMM.nutrimons;

import android.content.res.AssetManager;
import android.util.Log;

import com.BAMM.nutrimons.database.AppDatabase;
import com.BAMM.nutrimons.database.ElementDRIs;
import com.BAMM.nutrimons.database.ElementULs;
import com.BAMM.nutrimons.database.MacronutrientDRIs;
import com.BAMM.nutrimons.database.MacronutrientRanges;
import com.BAMM.nutrimons.database.User;
import com.BAMM.nutrimons.database.VitaminDRIs;
import com.BAMM.nutrimons.database.VitaminULs;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class NutrientTablesApi {
    private static AppDatabase mDb;
    private static DecimalFormat df = new DecimalFormat("0.0");

    public NutrientTablesApi(AppDatabase db) { mDb = db; }

    public void Initialize(AssetManager am) //from https://www.nal.usda.gov/sites/default/files/fnic_uploads/recommended_intakes_individuals.pdf
    //light manual processing required; '.xls workbook' file must be saved in format as in the /Assets folder's "recommended_intakes_individuals.xls"
    //trim everything except the actual tables; first sheet VitaminDRIs, second sheet ElementDRIs, third sheet McronutrientDRIs,
    //fourth sheet MacronutrientRanges acceptable ranges, fifth sheet VitaminULs, sixth sheet ElementULs
    {
        try
        {
            InputStream is = am.open("recommended_intakes_individuals.xls"); //***jxl only accepts xls saved as workbooks
            Workbook wb = Workbook.getWorkbook(is);
            for(int i = 0; i < wb.getNumberOfSheets(); ++i) //iterate over sheets
            {
                Sheet sheet = wb.getSheet(i); //get sheet

                if(sheet.getName().equals("MacronutrientRanges"))
                {
                    for(int j = 2; j < sheet.getColumns(); ++j) //iterate over columns
                    {
                        String headerVal = sheet.getCell(j, 0).getContents().replace("−", "-");
                        ArrayList<String> values = new ArrayList<>();

                        switch(headerVal)
                        {
                            case "Macronutrient":
                            case "":
                                break;
                            case "Children, 1-3 y":
                                headerVal = "1-3 y";
                                values.add(headerVal);
                                break;
                            case "Children, 4-18 y":
                                headerVal = "4-18 y";
                                values.add(headerVal);
                                break;
                            default: //refactor so default is to not add
                                headerVal = "Adult";
                                values.add(headerVal);
                        }

                        if(values.size() != 0)
                        {
                            for(int k = 1; k < sheet.getRows(); ++k) //iterate over rows
                            {
                                String val = sheet.getCell(j, k).getContents().replaceAll("[^\\.\\-0123456789]","").replace("−", "-"); //assign cells (j,k), remove * and letters
                                if(val.contains("ND") || val.equals(""))
                                    values.add("0");
                                else
                                    values.add(val);
                            }

                            //checks each row, verifying integrity of db tables
                            try { mDb.macronutrientRangesDAO().findByGroup(headerVal).toString(); } //if found, don't repopulate/
                            catch (NullPointerException e) { mDb.macronutrientRangesDAO().insert(new MacronutrientRanges(values)); } //if not populated, populate

                        }
                    }
                }
                else
                {
                    String sex = "N/A", babyStatus = "N/A"; //groupId vals
                    for(int j = 0; j < sheet.getRows(); ++j) //iterate over rows
                    {
                        String headerVal = sheet.getCell(0, j).getContents().replace("−", "-"); //set initial groupId age

                        switch(headerVal)
                        {
                            case "Life Stage":
                            case "Group":
                            case "Infants":
                            case "Children":
                                break;
                            case "Males":
                                sex = "Male";
                                break;
                            case "Females":
                                sex = "Female";
                                break;
                            case "Pregnancy":
                                babyStatus = "Pregnant";
                                break;
                            case "Lactation":
                                babyStatus = "Lactating";
                                break;
                            default: //refactor so default is to not add
                            {
                                ArrayList<String> values = new ArrayList<>();
                                values.add(headerVal);
                                values.add(sex);
                                values.add(babyStatus);
                                for(int k = 1; k < sheet.getColumns(); ++k) //iterate over columns
                                {
                                    String val = sheet.getCell(k, j).getContents().replaceAll("[^\\.0123456789]","").replace("−", "-"); //assign cells (k,j), remove * and letters
                                    if(val.contains("ND") || val.equals(""))
                                        values.add("0");
                                    else
                                        values.add(val);
                                }
                                switch(sheet.getName())
                                {
                                    case "VitaminDRIs":
                                        try { mDb.vitaminDRIsDAO().findByGroup(headerVal, sex, babyStatus).toString(); }
                                        catch(NullPointerException e) { mDb.vitaminDRIsDAO().insert(new VitaminDRIs(values)); }
                                        break;
                                    case "ElementDRIs":
                                        try { mDb.elementDRIsDAO().findByGroup(headerVal, sex, babyStatus).toString(); }
                                        catch(NullPointerException e) { mDb.elementDRIsDAO().insert(new ElementDRIs(values)); }
                                        break;
                                    case "MacronutrientDRIs":
                                        try { mDb.macronutrientDRIsDAO().findByGroup(headerVal, sex, babyStatus).toString(); }
                                        catch(NullPointerException e) { mDb.macronutrientDRIsDAO().insert(new MacronutrientDRIs(values)); }
                                        break;
                                    case "VitaminULs":
                                        try { mDb.vitaminULsDAO().findByGroup(headerVal, sex, babyStatus).toString(); }
                                        catch(NullPointerException e) { mDb.vitaminULsDAO().insert(new VitaminULs(values)); }
                                        break;
                                    case "ElementULs":
                                        try { mDb.elementULsDAO().findByGroup(headerVal, sex, babyStatus).toString(); }
                                        catch(NullPointerException e) { mDb.elementULsDAO().insert(new ElementULs(values)); }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log.d("error", "excel processing; " + e.toString());
        }
        finally
        {
            List<ElementDRIs> a = mDb.elementDRIsDAO().getAll();
            List<ElementULs> b = mDb.elementULsDAO().getAll();
            List<MacronutrientDRIs> c = mDb.macronutrientDRIsDAO().getAll();
            List<MacronutrientRanges> d = mDb.macronutrientRangesDAO().getAll();
            List<VitaminDRIs> e = mDb.vitaminDRIsDAO().getAll();
            List<VitaminULs> f = mDb.vitaminULsDAO().getAll();
        }
    }

    public Hashtable<String, Hashtable<String, Float>> getTablesByGroup(String age, String sex, String babyStatus)
    //returns an arraylist of 6 string arraylists of the nutrient table rows
    // corresponding to that group
    {
        Hashtable<String, Hashtable<String, Float>> nutrientTables = new Hashtable();
        float ageNum = Float.parseFloat(age);
        Log.d("float age", String.valueOf(ageNum));
        String ageGroup1 = "";
        String ageGroup2 = "";

        if(ageNum >= 0 && ageNum < .5)
        {
            ageGroup1 = "0 to 6 mo";
            ageGroup2 = "1-3 y";
            sex = "N/A";
        }
        else if(ageNum >= .5 && ageNum < 1)
        {
            ageGroup1 = "6 to 12 mo";
            ageGroup2 = "1-3 y";
            sex = "N/A";
        }
        else if(ageNum >= 1 && ageNum <= 3)
        {
            ageGroup1 = "1-3 y";
            ageGroup2 = "1-3 y";
            sex = "N/A";
        }
        else if(ageNum >= 4 && ageNum <= 8)
        {
            ageGroup1 = "4-8 y";
            ageGroup2 = "4-18 y";
            sex = "N/A";
        }
        else if(ageNum >= 9 && ageNum <= 13)
        {
            ageGroup1 = "9-13 y";
            ageGroup2 = "4-18 y";
        }
        else if(ageNum >= 14 && ageNum <= 18)
        {
            ageGroup1 = "14-18 y";
            ageGroup2 = "4-18 y";
        }
        else if(ageNum >= 19 && ageNum <= 30)
        {
            ageGroup1 = "19-30 y";
            ageGroup2 = "Adult";
        }
        else if(ageNum >= 31 && ageNum <= 50)
        {
            ageGroup1 = "31-50 y";
            ageGroup2 = "Adult";
        }
        else if(ageNum >= 51 && ageNum <= 70)
        {
            ageGroup1 = "51-70 y";
            ageGroup2 = "Adult";
        }
        else if(ageNum > 70)
        {
            ageGroup1 = "> 70 y";
            ageGroup2 = "Adult";
        }
        Log.d("group", ageGroup1 + " " + ageGroup2 + " " + sex + " " + babyStatus);

        nutrientTables.put("elementDRIs", mDb.elementDRIsDAO().findByGroup(ageGroup1, sex, babyStatus).toHashTable());
        nutrientTables.put("elementULs", mDb.elementULsDAO().findByGroup(ageGroup1, sex, babyStatus).toHashTable());
        nutrientTables.put("vitaminDRIs", mDb.vitaminDRIsDAO().findByGroup(ageGroup1, sex, babyStatus).toHashTable());
        nutrientTables.put("vitaminULs", mDb.vitaminULsDAO().findByGroup(ageGroup1, sex, babyStatus).toHashTable());
        nutrientTables.put("nutrientDRIs", mDb.macronutrientDRIsDAO().findByGroup(ageGroup1, sex, babyStatus).toHashTable());
        nutrientTables.put("nutrientRanges", mDb.macronutrientRangesDAO().findByGroup(ageGroup2).toHashTable());

        return nutrientTables;
    }

    public void updateUserNutrients(int userID)
    {
        User u = mDb.userDao().findByUserID(userID);
        Log.d("age, sex", u.age + " " + u.sex);
        Hashtable<String, Hashtable<String, Float>> hash = getTablesByGroup(u.age, u.sex, "N/A");
        
        u.calories = String.valueOf(calculateCalories(u));
        u.water = String.valueOf(hash.get("nutrientDRIs").get("water"));
        u.proteinDRI = String.valueOf(hash.get("nutrientDRIs").get("protein")); //grams returned
        u.proteinUL = df.format((hash.get("nutrientRanges").get("proteinMax")) / 100 * Float.parseFloat(u.calories) / 4); //percentage returned
        u.carbsDRI = String.valueOf(hash.get("nutrientDRIs").get("carbohydrate"));
        u.carbsUL = df.format((hash.get("nutrientRanges").get("carbohydrateMax")) / 100 * Float.parseFloat(u.calories) / 4);
        u.sugarDRI = "0.0";
        u.sugarUL = df.format(Float.parseFloat(u.calories) * .25 / 4);
        u.fiberDRI = String.valueOf(hash.get("nutrientDRIs").get("fiber"));
        u.fatsDRI = String.valueOf(hash.get("nutrientDRIs").get("fat"));
        u.fatsUL = df.format((hash.get("nutrientRanges").get("fatMax")) / 100 * Float.parseFloat(u.calories) / 9);
        u.cholesterolDRI = "0.0";
        u.cholesterolUL = "0.0";
        u.saturatedFatsDRI = "0.0";
        u.saturatedFatsUL = "0.0";
        u.unsaturatedFatsDRI = String.valueOf(hash.get("nutrientDRIs").get("linoleicAcid") + hash.get("nutrientDRIs").get("alphaLinoleicAcid"));
        u.unsaturatedFatsUL = df.format(((hash.get("nutrientRanges").get("linoleicAcidMax") + hash.get("nutrientRanges").get("alphaLinoleicAcidMax")))/ 100 * Float.parseFloat(u.calories) / 9);
        u.transFatsDRI = "0.0";
        u.transFatsUL = "0.0";

        u.vitaminADRI = String.valueOf(hash.get("vitaminDRIs").get("vitaminA"));
        u.vitaminAUL = String.valueOf(hash.get("vitaminULs").get("vitaminA"));
        u.vitaminCDRI = String.valueOf(hash.get("vitaminDRIs").get("vitaminC"));
        u.vitaminCUL = String.valueOf(hash.get("vitaminULs").get("vitaminC"));
        u.vitaminDDRI = String.valueOf(hash.get("vitaminDRIs").get("vitaminD"));
        u.vitaminDUL = String.valueOf(hash.get("vitaminULs").get("vitaminD"));

        u.sodiumDRI = String.valueOf(hash.get("elementDRIs").get("sodium"));
        u.sodiumUL = String.valueOf(hash.get("elementULs").get("sodium"));
        u.potassiumDRI = String.valueOf(hash.get("elementDRIs").get("potassium"));
        u.calciumDRI = String.valueOf(hash.get("elementDRIs").get("calcium"));
        u.calciumUL = String.valueOf(hash.get("elementULs").get("calcium"));
        u.ironDRI = String.valueOf(hash.get("elementDRIs").get("iron"));
        u.ironUL = String.valueOf(hash.get("elementULs").get("iron"));

        mDb.userDao().insert(u);
        Log.d("user", u.age + " " + u.toString());
    }

    private int calculateCalories(User u)
    {
        int focusMod = 0; //maintain weight/default
        double al = 1.0; //BMR

        //modify with focus modifier
        switch(u.profileFocus)
        {
            case "Lose Weight":
                focusMod = -500;
                break;
            case "Gain Muscle":
                focusMod = 500;
                break;
        }

        //modify with activity modifier https://www.k-state.edu/paccats/Contents/PA/PDF/Physical%20Activity%20and%20Controlling%20Weight.pdf
        switch(u.activityLevel)
        {
            case "Sedentary":
                al = 1.2;
                break;
            case "Lightly Active":
                al = 1.375;
                break;
            case "Moderately Active":
                al = 1.55;
                break;
            case "Very Active":
                al = 1.725;
                break;
            case "Extremely Active":
                al = 1.9;
                break;
        }

        if(u.sex.equals("Male")) //Mifflin-St Jeor Equation https://en.wikipedia.org/wiki/Basal_metabolic_rate
            return (int)((10 * Float.parseFloat(u.weight) / 2.2 /*pounds*/ + 6.25 * 2.54 * Float.parseFloat(u.height) /*inches*/ - 5 * Float.parseFloat(u.age) + 5) * al) + focusMod;
        else
            return (int)((10 * Float.parseFloat(u.weight) / 2.2 /*pounds*/ + 6.25 * 2.54 * Float.parseFloat(u.height) /*inches*/ - 5 * Float.parseFloat(u.age) - 161) * al) + focusMod;
    }
}
