package com.example.nutrimons;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.TextView;

import com.example.nutrimons.R;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.ElementDRIs;
import com.example.nutrimons.database.ElementULs;
import com.example.nutrimons.database.MacronutrientDRIs;
import com.example.nutrimons.database.MacronutrientRanges;
import com.example.nutrimons.database.VitaminDRIs;
import com.example.nutrimons.database.VitaminULs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

public class NutrientTablesApi {
    AppDatabase mDb;

    public NutrientTablesApi(AppDatabase db) { mDb = db; }

    public void Initialize(AssetManager am, AppDatabase db) //from https://www.nal.usda.gov/sites/default/files/fnic_uploads/recommended_intakes_individuals.pdf
    //light manual processing required; '.xls workbook' file must be saved in format as in the /Assets folder's "recommended_intakes_individuals.xls"
    //trim everything except the actual tables; first sheet VitaminDRIs, second sheet ElementDRIs, third sheet McronutrientDRIs,
    //fourth sheet MacronutrientRanges acceptable ranges, fifth sheet VitaminULs, sixth sheet ElementULs
    {
        mDb = db;
        try
        {
            //clear tables
            mDb.elementDRIsDAO().nukeTable();
            mDb.elementULsDAO().nukeTable();
            mDb.macronutrientRangesDAO().nukeTable();
            mDb.macronutrientDRIsDAO().nukeTable();
            mDb.vitaminDRIsDAO().nukeTable();
            mDb.vitaminULsDAO().nukeTable();

            InputStream is = am.open("recommended_intakes_individuals.xls"); //***jxl only accepts xls saved as workbooks
            Workbook wb = Workbook.getWorkbook(is);
            for(int i = 0; i < wb.getNumberOfSheets(); ++i) //iterate over sheets
            {
                Sheet sheet = wb.getSheet(i); //get sheet
                //Log.d("sheet: " + i, sheet.getName());

                if(sheet.getName().equals("MacronutrientRanges"))
                {
                    for(int j = 2; j < sheet.getColumns(); ++j) //iterate over columns
                    {
                        String headerVal = sheet.getCell(j, 0).getContents().replace("−", "-");
                        ArrayList<String> values = new ArrayList<>();
                        //Log.d("j: " + j, headerVal);

                        switch(headerVal)
                        {
                            case "Macronutrient":
                            case "":
                                break;
                            case "Children, 1-3 y":
                                values.add("1-3 y");
                                break;
                            case "Children, 4-18 y":
                                values.add("4-18 y");
                                break;
                            default: //refactor so default is to not add
                                values.add("Adult");
                        }

                        if(values.size() != 0)
                        {
                            for(int k = 1; k < sheet.getRows(); ++k) //iterate over rows
                            {
                                //Log.d("k: " + k, sheet.getCell(j, k).getContents());
                                String val = sheet.getCell(j, k).getContents().replaceAll("[^\\.\\-0123456789]","").replace("−", "-"); //assign cells (j,k), remove * and letters
                                if(val.contains("ND") || val.equals(""))
                                    values.add("0");
                                else
                                    values.add(val);
                            }
                            mDb.macronutrientRangesDAO().insert(new MacronutrientRanges(values));
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
                                        mDb.vitaminDRIsDAO().insert(new VitaminDRIs(values));
                                        break;
                                    case "ElementDRIs":
                                        mDb.elementDRIsDAO().insert(new ElementDRIs(values));
                                        break;
                                    case "MacronutrientDRIs":
                                        mDb.macronutrientDRIsDAO().insert(new MacronutrientDRIs(values));
                                        break;
                                    case "VitaminULs":
                                        mDb.vitaminULsDAO().insert(new VitaminULs(values));
                                        break;
                                    case "ElementULs":
                                        mDb.elementULsDAO().insert(new ElementULs(values));
                                        break;
                                }
                            }
                        }
                    }
                }
                /*for(int i = 0; i < sheet.getRows(); ++i) //iterate over rows and columns
                {
                    for(int j = 0; j < sheet.getColumns(); ++j)
                    {
                        Cell cell = sheet.getCell(j, i); //column, row
                        CellType type = cell.getType();
                        Log.d("cell: (" + j + "," + i + ")", cell.getContents() + " " + type); //output to log
                        exFile.append(cell.getContents()); //output to TextView
                    }
                }*/
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

            for(int i = 0; i < a.size(); ++i)
            {
                Log.d("Element DRI " + i, a.get(i).toString());
            }
            for(int i = 0; i < b.size(); ++i)
            {
                Log.d("Element UL " + i, b.get(i).toString());
            }
            for(int i = 0; i < c.size(); ++i)
            {
                Log.d("Macronutrient DRI " + i, c.get(i).toString());
            }
            for(int i = 0; i < d.size(); ++i)
            {
                Log.d("Macronutrient Range " + i, d.get(i).toString());
            }
            for(int i = 0; i < e.size(); ++i)
            {
                Log.d("Vitamin DRI " + i, e.get(i).toString());
            }
            for(int i = 0; i < f.size(); ++i)
            {
                Log.d("Vitamin UL " + i, f.get(i).toString());
            }
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
}
