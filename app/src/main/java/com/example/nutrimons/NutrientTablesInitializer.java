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
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class NutrientTablesInitializer {
    public NutrientTablesInitializer(AssetManager am, AppDatabase mDb) //from https://www.nal.usda.gov/sites/default/files/fnic_uploads/recommended_intakes_individuals.pdf
    //light manual processing required; '.xls workbook' file must be saved in format as in the /Assets folder's "recommended_intakes_individuals.xls"
    //trim everything except the actual tables; first sheet VitaminDRIs, second sheet ElementDRIs, third sheet McronutrientDRIs,
    //fourth sheet MacronutrientRanges acceptable ranges, fifth sheet VitaminULs, sixth sheet ElementULs
    {
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
                        String headerVal = sheet.getCell(j, 0).getContents();
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
                                values.add(sheet.getCell(j, k).getContents()); //assign cells (j,k)
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
                        String headerVal = sheet.getCell(0, j).getContents(); //set initial groupId age

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
                                values.add(sheet.getCell(0, j).getContents());
                                values.add(sex);
                                values.add(babyStatus);
                                for(int k = 1; k < sheet.getColumns(); ++k) //iterate over columns
                                {
                                    values.add(sheet.getCell(k, j).getContents()); //assign cells (j,k)
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
}
