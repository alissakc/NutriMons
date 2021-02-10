import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NutrimonsDatabase";
    private static final int DATABASE_Version = 1;    // Database Version

    private static final String USER_TABLE = "User";   // Table Name
    private static final String UID="_id";     // Column I (Primary Key)
    private static final String USER_EMAIL = "Email";    //Column II
    private static final String USER_PASSWORD= "Password";    // Column III
    private static final String CREATE_USER_TABLE = "CREATE TABLE "+USER_TABLE+
            " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER_EMAIL+
            " VARCHAR(255) ,"+ USER_PASSWORD+" VARCHAR(225));";

    //Search SecretKeyFactory for password hashing if needed


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER_TABLE);

        } catch (Exception e) {
           System.out.println("Errors");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
    }

    public boolean insertUser(String user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(USER_EMAIL, user.getEmail());
        //values.put(USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(USER_TABLE, null, values);
        db.close();

        return true;
    }
}

