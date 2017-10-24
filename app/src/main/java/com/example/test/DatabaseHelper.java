package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuexiao on 4/23/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private int alarmNum = 0;
    public static final int LISTNUM = 40;

    private static final String DATABASE_NAME = "MyRecipe";
    private static final int DATABASE_VERSION = 1;

    static String create_alarm = "create table alarm(_id integer,time text,loop integer(1),date1 integer(1),date2 integer(1),date3 integer(1),date4 integer(1),date5 integer(1),date6 integer(1),date7 integer(1),title varchar(50),ringtone varchar(50),volume integer,account_id varchar)";
    static String create_recipe = "create table recipe(_id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(50),description vachar(50)," +
            "step text,material varchar(4),nutrition varchar(4),cook_time integer(4),kind text," +
            "times varchar(4),ingredients varchar,types varchar,favourite integer)";

    static String create_account = "create table account(account_id varchar,password varchar,gender integer(1),age integer,height integer," +
            "weight integer,aim integer,expected_weight integer,duration integer,occupation integer,type integer,frequency integer," +
            "exercise_duration integer,exercise_time integer,favourite text,shoppingcart text)";
    static String create_plan = "create table plan(account_id varchar,code integer,recipe_id integer,quantity float)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_alarm);
        db.execSQL(create_recipe);
        db.execSQL(create_account);
        db.execSQL(create_plan);
        addAllRecipe(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(String account_id, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (checkUserExist(account_id)) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("account_id", account_id);
            contentValues.put("password", password);
            db.insert("account", null, contentValues);

            return true;
        } else {
            return false;
        }
    }

    public void UserInfoStep1(String account_id, int gender, int age, int height, int weight) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        String whereClause = "account_id=?";
        String[] whereArgs = {account_id};

        values.put("gender", gender);
        values.put("age", age);
        values.put("height", height);
        values.put("weight", weight);
        db.update("account", values, whereClause, whereArgs);

    }

    public void UserInfoStep2(String account_id, int aim, int expected_weight, int duration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String whereClause = "account_id=?";
        String[] whereArgs = {account_id};

        values.put("aim", aim);
        values.put("expected_weight", expected_weight);
        values.put("duration", duration);
        db.update("account", values, whereClause, whereArgs);

    }

    public void UserInfoStep3(String account_id, int occupation, int type, int frequency, int exercise_duration, int exercise_time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String whereClause = "account_id=?";
        String[] whereArgs = {account_id};

        values.put("occupation", occupation);
        values.put("type", type);
        values.put("frequency", frequency);
        values.put("exercise_duration", exercise_duration);
        values.put("exercise_time", exercise_time);
        db.update("account", values, whereClause, whereArgs);
        db.close();

    }

    public int[] getPersonalParas(String account_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});


        if (cursor.moveToFirst()) {

            int gender = cursor.getInt(cursor.getColumnIndex("gender"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            int height = cursor.getInt(cursor.getColumnIndex("height"));
            int weight = cursor.getInt(cursor.getColumnIndex("weight"));
            int aim = cursor.getInt(cursor.getColumnIndex("aim"));
            int expected_weight = cursor.getInt(cursor.getColumnIndex("expected_weight"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            int occupation = cursor.getInt(cursor.getColumnIndex("occupation"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            int frequency = cursor.getInt(cursor.getColumnIndex("frequency"));
            int exercise_duration = cursor.getInt(cursor.getColumnIndex("exercise_duration"));
            int exercise_time = cursor.getInt(cursor.getColumnIndex("exercise_time"));


            return new int[]{gender, age, height, weight, aim, expected_weight, duration, occupation, type, frequency, exercise_duration, exercise_duration, exercise_time};
        }

        return null;

    }

    //新写
    public void resetPassword(String account_id, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        db.update("account", values, "account_id=?", new String[]{account_id});
        db.close();
    }

    //新写
    public boolean checkUserExist(String account_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});
        if (cursor.moveToFirst()) {
            return false;
        }
        return true;
    }

    //新写
    public boolean checkUserPassword(String account_id, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});
        if (cursor.moveToFirst()) {
            if (password.equals(cursor.getString(1))) {
                return true;
            }

        }
        return false;


    }

    //新写
    public String[] checkFavorite(int recipe_id, String account_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});
        String favourite="";
        if (cursor.moveToFirst()) {
            favourite = cursor.getString(cursor.getColumnIndex("favourite"));

           try {
                String favourites[] = favourite.split(",");
                for (String s : favourites) {

                    if (s.equals(String.valueOf(recipe_id))) {
                        return favourites;
                    }
                }


            } catch (Exception E) {
                return null;
            }


        }
        return null;
    }

    //新写
    public void deleteFavorite(int recipe_id, String account_id, String[] favorites) {

        SQLiteDatabase db = this.getWritableDatabase();

        String updateFavourite = "";

        for (String s : favorites) {
            if (!s.equals(String.valueOf(recipe_id))) {
                updateFavourite = updateFavourite + s + ",";
            }

        }


        ContentValues values = new ContentValues();
        String whereClause = "account_id=?";
        String[] whereArgs = {account_id};

        values.put("favourite", updateFavourite);
        db.update("account", values, whereClause, whereArgs);

        db.close();

    }

    //新写
    public void addFavorite(int recipe_id, String account_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});
        String favourite = "";
        if (cursor.moveToFirst()) {
            favourite = cursor.getString(cursor.getColumnIndex("favourite"));
            if (favourite==null){
                favourite="";
            }
            favourite = favourite + String.valueOf(recipe_id) + ",";
        }
        ContentValues values = new ContentValues();
        String whereClause = "account_id=?";
        String[] whereArgs = {account_id};

        values.put("favourite", favourite);
        db.update("account", values, whereClause, whereArgs);

        cursor.close();
        db.close();
    }

    //新写
    public int changeTotalFavorite(int _id, int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{String.valueOf(_id)});
        int num = 0;
        if (cursor.moveToFirst()) {
            num = cursor.getInt(cursor.getColumnIndex("favourite"));
            num = num + i;
            ContentValues values = new ContentValues();
            String whereClause = "_id=?";
            String[] whereArgs = {String.valueOf(_id)};
            values.put("favourite", num);
            db.update("recipe", values, whereClause, whereArgs);


        }
        cursor.close();
        db.close();


        return num;
    }

    //新写
    public String[] getFavorite(String account_id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});
        if (cursor.moveToFirst()) {
            String sFavorite = cursor.getString(cursor.getColumnIndex("favourite"));
            System.out.println("\n\n\n"+sFavorite+"\n\n\n");

            try {
                String[] sFavorites = sFavorite.split(",");
                for (String s:sFavorites){
                    System.out.println("getFavorite---------"+s);
                }
                return sFavorites;


            } catch (Exception e) {
                return null;
            }
        }
        return null;


    }

    public void addPlan(String account_id, int day, int time, int recipe_id, float quantity) {

        SQLiteDatabase db = this.getWritableDatabase();

        int code = (day - 1) * 4 + time;

        ContentValues contentValues = new ContentValues();
        contentValues.put("account_id", account_id);
        contentValues.put("code", code);
        contentValues.put("recipe_id", recipe_id);
        contentValues.put("quantity", quantity);
        db.insert("plan", null, contentValues);
        db.close();

    }


    //recipe_id:quantity,recipe_id:quantity
    public Map<Integer, Float> readCartRecipe(String account_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from account where account_id=?", new String[]{account_id});
        Map<Integer, Float> map = new HashMap<>();
        if (cursor.moveToFirst()) {
            String s;
            s = cursor.getString(cursor.getColumnIndex("shoppingcart"));
            if (s != null) {
                for (String info : s.split(",")) {
                    String infos[] = info.split(":");
                    map.put(Integer.parseInt(infos[0].trim()), Float.parseFloat(infos[1].trim()));
                }
                return map;
            }
        }
        return map;
    }

    //写入数据库
    public void updateCartRecipe(int day, int time, String account_id) {
        Map<Integer, Float> map = readCartRecipe(account_id);
        int code = (day - 1) * 4 + time;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from plan where account_id=? and code=?", new String[]{account_id, Integer.toString(code)});
        while (cursor.moveToNext()) {
            if (map.containsKey(cursor.getInt(cursor.getColumnIndex("recipe_id")))) {
                map.put(cursor.getInt(cursor.getColumnIndex("recipe_id")), map.get(cursor.getInt(cursor.getColumnIndex("recipe_id"))) + cursor.getFloat(cursor.getColumnIndex("quantity")));
            } else {
                map.put(cursor.getInt(cursor.getColumnIndex("recipe_id")), cursor.getFloat(cursor.getColumnIndex("quantity")));
            }
        }
        String s = "";
        for (Map.Entry entry : map.entrySet()) {
            s = s + entry.getKey() + ":" + entry.getValue() + ",";
        }
        ContentValues values = new ContentValues();
        String whereClause = "account_id=?";
        String[] whereArgs = new String[]{account_id};
        values.put("shoppingcart", s);
        db.update("account", values, whereClause, whereArgs);

        cursor.close();
        db.close();
    }


    public Map<String, Material> material(String account_id) {
        Map<String, Material> materialMap = new HashMap<>();
        Map<Integer, Float> recipeMap = readCartRecipe(account_id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        for (Map.Entry entry : recipeMap.entrySet()) {
            float times = (float) entry.getValue();
            cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString((int) entry.getKey())});
            if (cursor.moveToFirst()) {
                String s_material = cursor.getString(cursor.getColumnIndex("material")).split(";")[1];
                String arr_material[] = s_material.split("\\|");
                for (String s : arr_material) {
                    String attributes[] = s.split(":");
                    String name = attributes[0].trim();
                    Float quantity = Float.valueOf(attributes[1].trim()) * times;
                    String unit = attributes[2].trim();
                    if (materialMap.containsKey(name)) {
                        Material material = materialMap.get(name);
                        material.setQuantity(quantity);
                        materialMap.put(name, material);
                    } else {
                        materialMap.put(name, new Material(quantity, unit));
                    }
                }
            }
        }
        return materialMap;
    }

    //改
    public List<Map<String, Object>> searchPlanbyCode(String account_id, int code) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from plan where account_id=? and code=?", new String[]{account_id, Integer.toString(code)});
        String name, quantity, energy;

        while (cursor.moveToNext()) {

            Map<String, Object> map = new HashMap<String, Object>();
            name = this.getName(cursor.getInt(cursor.getColumnIndex("recipe_id")));
            quantity = String.valueOf(cursor.getFloat(cursor.getColumnIndex("quantity")) * 100.0);
            energy = String.valueOf(cursor.getFloat(cursor.getColumnIndex("quantity")) * getNutrition(cursor.getInt(cursor.getColumnIndex("recipe_id")))[0]);

            map.put("name", name);
            map.put("quantity", quantity);
            map.put("energy", energy);

            list.add(map);
        }

        return list;
    }

    public void addAlarm(String time, boolean[] bool, String title, String ringtone, int volume, String account_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor c = db.query("alarm", null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("select * from alarm where account_id=?", new String[]{account_id});
        alarmNum = cursor.getCount();
        ContentValues contentValues = new ContentValues();

        contentValues.put("_id", alarmNum);
        contentValues.put("time", time);
        String propertityName[] = {"loop", "date1", "date2", "date3", "date4", "date5", "date6", "date7"};

        for (int i = 0; i < 8; i++) {
            if (bool[i] == true) {
                contentValues.put(propertityName[i], 1);

            } else {
                contentValues.put(propertityName[i], 0);
            }
        }
        contentValues.put("title", title);
        contentValues.put("ringtone", ringtone);
        contentValues.put("volume", volume);
        contentValues.put("account_id", account_id);

        db.insert("alarm", null, contentValues);
        cursor.close();
        db.close();
    }

    public void deleteAlarm(String account_id, int _id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from alarm where account_id=?", new String[]{account_id});
        alarmNum = cursor.getCount();
        String whereClause = "account_id=? and _id=?";
        String[] whereArgs = {account_id, Integer.toString(_id)};
        db.delete("alarm", whereClause, whereArgs);


        for (int i = 0; i < alarmNum - _id - 1; i++) {
            ContentValues values = new ContentValues();
            whereClause = "account_id=? and _id=?";
            whereArgs = new String[]{account_id, String.valueOf(_id + i + 1)};
            values.put("_id", _id + i);
            db.update("alarm", values, whereClause, whereArgs);
        }
//        alarmNum -= 1;//？？？需要吗
    }

    public List<Map<String, Object>> readAllAlarm(String account_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        List list = new ArrayList<Map<String, Object>>();
        // Cursor cursor = db.query("alarm", null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("select * from alarm where account_id=?", new String[]{account_id});

        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();

            int id = cursor.getInt(0);
            String time = cursor.getString(1);
            boolean loop = cursor.getInt(2) == 1;
            boolean date1 = cursor.getInt(3) == 1;
            boolean date2 = cursor.getInt(4) == 1;
            boolean date3 = cursor.getInt(5) == 1;
            boolean date4 = cursor.getInt(6) == 1;
            boolean date5 = cursor.getInt(7) == 1;
            boolean date6 = cursor.getInt(8) == 1;
            boolean date7 = cursor.getInt(9) == 1;
            String title = cursor.getString(10);
            String ringtone = cursor.getString(11);
            int volume = cursor.getInt(12);

            map.put("_id", id);
            map.put("time", time);
            map.put("loop", loop);
            map.put("date1", date1);
            map.put("date2", date2);
            map.put("date3", date3);
            map.put("date4", date4);
            map.put("date5", date5);
            map.put("date6", date6);
            map.put("date7", date7);
            map.put("title", title);
            map.put("ringtone", ringtone);
            map.put("volume", volume);
            list.add(map);
        }

        return list;
    }

    public void updateAlarm(int id, String time, boolean[] bool, String title, String ringtone, int volume, String account_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String whereClause = "account_id=? and _id=?";
        String[] whereArgs = {account_id, String.valueOf(id)};
        String flags[] = {"loop", "date1", "date2", "date3", "date4", "date5", "date6", "date7"};
        for (int i = 0; i < bool.length; i++) {
            if (bool[i] == true) {
                values.put(flags[i], 1);
            } else {
                values.put(flags[i], 0);
            }

        }
        values.put("time", time);
        values.put("title", title);
        values.put("ringtone", ringtone);
        values.put("volume", volume);
        db.update("alarm", values, whereClause, whereArgs);
    }

    public void addRecipe(SQLiteDatabase db, String name, String description, String step, String material, String nutrition,
                          int cook_time, String kind, String times, String ingredients, String type, int favourite) {

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("description", description);
        values.put("step", step);
        values.put("material", material);
        values.put("nutrition", nutrition);
        values.put("cook_time", cook_time);
        values.put("kind", kind);
        values.put("times", times);
        values.put("ingredients", ingredients);
        values.put("types", type);
        values.put("favourite", favourite);

        db.insert("recipe", null, values);
    }

    public void addAllRecipe(SQLiteDatabase db) {

        addRecipe(db, "Spinach Chicken Parmesan", "A family favorite. A classic dish with a Popeye-esque twist; so eat yer spinach (with chicken parmesan) and enjoy it, too!", "1. Preheat oven to 350 degrees F (175 degrees C). | 2. In a small bowl combine cheese and seasoning. Roll chicken pieces in cheese mixture to coat lightly. Set remaining cheese mixture aside. Arrange coated chicken pieces in an 8x8x2 inch baking dish. | 3. In a small saucepan, saute green onion in butter/margarine until tender. Stir in flour, then add milk all at once. Simmer, stirring, until bubbly. Stir in drained spinach and pimiento and mix together. Spoon spinach mixture over chicken and sprinkle with remaining cheese mixture. Bake uncovered for 30 to 35 minutes or until tender and chicken juices run clear.", "all-purpose flour: 30g | skim milk: 1/2 cup | frozen chopped spinach, thawed and drained: 1/2 (10 ounce) package | chopped pimento peppers: 30g | butter: 30g | chopped green onions: 1/4 cup | skinless, boneless chicken breasts: 6 | Italian seasoning: 1g | grated Parmesan cheese: 1/3 cup ; all-purpose flour: 30:g | milk: 0.5:cup | spinach: 10:ounce | pepper: 30:g | butter: 30:g | onion: 1:  | chicken breasts: 6:  | parmesan cheese: 0.33:cup ", "186, 4.8, 3.6, 30.8, 78, 185", 30, "0110", "0100", "100000", "111", 0);

        addRecipe(db, "Wine-braised Beef Brisket", "This is yummy the day you make it, but is even more delicious the next day.", "1. Preheat oven to 350 degrees F (175 degrees C). | 2. Mix thyme, salt, and black pepper in a small bowl and rub the mixture over both sides of brisket. | 3. Heat olive oil in a roasting pan over medium-high heat; place brisket in the hot oil and brown on both sides, 3 to 4 minutes per side. Remove brisket from pan and set aside. | 4. Place red onion slices into the hot roasting pan and cook and stir until onion is slightly softened, about 2 minutes. Stir in beef broth, tomato sauce, and wine. | 5. Place the brisket back into the roasting pan and cover pan with foil. | 6. Roast the brisket in the preheated oven for 1 hour; remove foil and baste brisket with pan juices. Place foil back over roasting pan and roast brisket until very tender and pan sauce has thickened, 1 1/2 to 2 more hours.", "dried thyme: 3g | red onion, sliced: 1 | salt: 3g | beef broth: 1 (14.5 ounce) can | ground black pepper: 0.75g | tomato sauce: 1 (8 ounce) can | beef brisket: 1 (3 pound) | red wine: 1/2 cup | olive oil: 30g ; thyme: 3:g | onion: 1:  | beef broth: 14.5:ounce | tomato sauce: 8:ounce | beef brisket: 3:pound | red wine: 0.5:cup | olive oil: 30:g ", "327, 25.1, 3.3, 18.4, 69, 649", 150, "0110", "0001", "010000", "001", 0);

        addRecipe(db, "Penne with Chicken and Asparagus", "A light but super-tasty pasta dish, with fresh asparagus cooked in broth with sauteed garlic and seasoned chicken.", "1. Bring a large pot of lightly salted water to boil. Add pasta, and cook until al dente, about 8 to 10 minutes. Drain, and set aside. | 2. Warm 3 tablespoons olive oil in a large skillet over medium-high heat. Stir in chicken, and season with salt, pepper, and garlic powder. Cook until chicken is cooked through and browned, about 5 minutes. Remove chicken to paper towels. | 3. Pour chicken broth into the skillet. Then stir in asparagus, garlic, and a pinch more garlic powder, salt, and pepper. Cover, and steam until the asparagus is just tender, about 5 to 10 minutes. Return chicken to the skillet, and warm through. | 4. Stir chicken mixture into pasta, and mix well. Let sit about 5 minutes. Drizzle with 2 tablespoons olive oil, stir again, then sprinkle with Parmesan cheese.", "dried penne pasta: 1 (16 ounce) package | low-sodium chicken broth: 1/2 cup | olive oil, divided: 75g | slender asparagus spears, trimmed, cut on diagonal into 1-inch: 1 bunch | skinless, boneless chicken breast halves - cut into cubes: 2 | clove garlic, thinly sliced: 1 | salt and pepper to taste | Parmesan cheese: 1/4 cup | garlic powder to taste ; pasta: 16:ounce | chicken broth: 0.5:cup | olive oil: 75:g | asparagus: 1:bunch | chicken breast: 2:  | garlic: 1:  | parmesan cheese: 0.25:cup  ", "332, 10.9, 43.3, 16.7, 20, 69", 20, "0110", "0100", "100001", "001", 0);

        addRecipe(db, "Baked Honey Mustard Chicken", "Quick and easy to prepare, and the kids love it too!", "1. Preheat oven to 350 degrees F (175 degrees C). | 2. Sprinkle chicken breasts with salt and pepper to taste, and place in a lightly greased 9x13 inch baking dish. In a small bowl, combine the honey, mustard, basil, paprika, and parsley. Mix well. Pour 1/2 of this mixture over the chicken, and brush to cover. | 3. Bake in the preheated oven for 30 minutes. Turn chicken pieces over and brush with the remaining 1/2 of the honey mustard mixture. Bake for an additional 10 to 15 minutes, or until chicken is no longer pink and juices run clear. Let cool 10 minutes before serving.", "skinless, boneless chicken breast halves: 6 | dried basil: 3g | salt and pepper to taste | paprika: 3g | honey: 1/2 cup | dried parsley: 1.5g | prepared mustard: 1/2 cup ; chicken breast: 6:  | pepper: 3:g | honey: 0.5:cup | mustard: 0.5:cup ", "232, 3.7, 24.8, 25.6, 67, 296", 45, "0110", "0010", "100000", "110", 0);

        addRecipe(db, "Tomato and Garlic Pasta", "There is nothing nicer than the flavor of fresh tomatoes. You can use canned, but the trouble you take to prepare this dish is worth it. You prepare the sauce while the pasta is cooking, no long hours of waiting. Great if you want meatless pasta.", "1. Place tomatoes in a kettle, and cover with cold water. Bring just to the boil. Pour off water, and cover again with cold water. Peel. Cut into small pieces. | 2. Cook the pasta in a large pot of boiling salted water until al dente. | 3. In a large skillet or saute pan, saute the garlic in enough olive oil to cover the bottom of the pan. The garlic should just become opaque, not brown. Stir in the tomato paste. Immediately stir in the tomatoes, and salt and pepper. Reduce heat, and simmer until the pasta is ready; add the basil. | 4. Drain the pasta, but do not rinse in cold water. Toss with a couple of tablespoons of olive oil, and then mix into the sauce. Reduce the heat as low as possible. Keep warm, uncovered, for about 10 minutes when it is ready to serve. Garnish generously with fresh Parmesan cheese. | 5. VARIATIONS: Saute fresh quartered mushrooms with the garlic, or add shoestring zucchini along with the tomato.", "angel hair pasta: 1 (8 ounce) package | tomato paste: 30g | tomatoes: 2 pounds | salt to taste | crushed garlic: 4 cloves | ground black pepper to taste | olive oil: 15g | grated Parmesan cheese: 1/4 cup | chopped fresh basil: 15g ; pasta: 8:ounce | tomato paste: 30:g | tomato: 2:  | garlic: 0.5:  | olive oil: 15:g | parmesan cheese: 0.25:cup | basil: 15:g ", "260, 6.8, 41.9, 10.3, 4, 236", 25, "0110", "0100", "000001", "001", 0);

        addRecipe(db, "Simple Garlic Shrimp", "If you like shrimp and LOVE garlic, I hope you give this fast and delicious recipe a try soon. Enjoy!", "1. Heat olive oil in a heavy skillet over high heat until it just begins to smoke. Place shrimp in an even layer on the bottom of the pan and cook for 1 minute without stirring. | 2. Season shrimp with salt; cook and stir until shrimp begin to turn pink, about 1 minute. | 3. Stir in garlic and red pepper flakes; cook and stir 1 minute. Stir in lemon juice, caper brine, 1 1/2 teaspoon cold butter, and half the parsley. | 4. Cook until butter has melted, about 1 minute, then turn heat to low and stir in 1 1/2 tablespoon cold butter. Cook and stir until all butter has melted to form a thick sauce and shrimp are pink and opaque, about 2 to 3 minutes. | 5. Remove shrimp with a slotted spoon and transfer to a bowl; continue to cook butter sauce, adding water 1 teaspoon at a time if too thick, about 2 minutes. Season with salt to taste. | 6. Serve shrimp topped with the pan sauce. Garnish with remaining flat-leaf parsley.", "olive oil: 22.5g | caper brine: 15g | shrimp, peeled and deveined: 1 pound | cold butter: 4.5g | salt to taste | chopped Italian flat leaf parsley, divided: 1/3 cup | garlic, finely minced: 6 cloves | cold butter: 22.5g | red pepper flakes: 0.75g | water, as needed | lemon juice: 45g ; olive oil: 22.5:g | shrimp: 1:pound | butter: 28:g | garlic: 0.5:  | lemon juice: 45:g ", "196, 12, 2.9, 19.1, 188, 244", 10, "0110", "1000", "000100", "001", 0);

        addRecipe(db, "Tortellini Bacon Broccoli Salad", "This is an excellent pasta salad that is great to make for the week ahead or to bring to potlucks! Make it -- you won't be disappointed. You may frown at the coleslaw dressing, but trust me, it's amazing in this recipe! This recipe is highly requested when served", "1.Cook the tortellini according to the package directions, drain, rinse with cold water, and refrigerate until cool, about 30 minutes. | 2.Place the bacon in a large, deep skillet, and cook over medium-high heat, turning occasionally, until evenly browned, about 10 minutes. Drain the bacon slices on a paper towel-lined plate. Chop the bacon into 1/2-inch pieces while still a little warm. | 3.Place the tortellini, bacon, broccoli, grape tomatoes, and green onions into a salad bowl. Pour the dressing over the ingredients, and toss lightly to coat. Chill in refrigerator before serving.", "refrigerated three-cheese tortellini: 2 (9 ounce) packages | grape tomatoes, halved: 1 pint | bacon: 1 pound | green onions, finely chopped: 2 | bottled coleslaw dressing: 1 cup | chopped broccoli: 4 cups ; tortellini: 9:ounce | tomato: 2:  | bacon: 1:pound | onion: 2:  | bottled coleslaw: 1:cup | broccoli: 2:  ", "349, 18.2, 33.6, 13.9, 47, 736", 15, "0110", "1000", "001001", "001", 0);

        addRecipe(db, "Fragola Pazzo (Crazy Strawberry)", "This is an Italian dessert that goes great with pasta. As the name suggests, it's a crazy combination but surprisingly excellent. Someone always asks me for the recipe when I make it. Be sure to use a good quality balsamic vinegar with this dish or you will not have as good results.", "1.Mix balsamic vinegar, sugar, and black pepper together in a bowl. Add strawberries and stir to coat; marinate in refrigerator until chilled, about 10 minutes. Grate chocolate over top as a garnish.", "balsamic vinegar: 1/4 cup | fresh strawberries, hulled and quartered: 2 pints | unsweetened chocolate, grated: 1 (1 ounce) square, or to taste | white sugar: 30g | freshly ground black pepper: 3g ; balsamic vinegar: 0.25:cup | strawberry: 2:pound | chocolate: 1:ounce | white sugar: 30:g ", "85, 2.8, 16.5, 1.5, 0, 5", 10, "0001", "1000", "000010", "011", 0);

        addRecipe(db, "Fresh Strawberry Sauce", "This is an excellent sauce for pies and cakes - especially cheesecakes!", "1.Combine strawberries, sugar, 2 tablespoons water, and balsamic vinegar in a saucepan and bring to simmer over medium heat. | 2.Reduce heat to medium-low, cover, and simmer for 15 minutes. | 3.Whisk 2 tablespoons water and cornstarch in a small bowl. | Whisk cornstarch mixture into strawberry mixture. Cook, stirring constantly, until mixture thickens, 1 to 2 minutes. Remove from heat. | 5.Transfer mixture to a blender and puree until smooth.", "fresh strawberries, hulled: 1 pint | balsamic vinegar: 1.5g | white sugar: 1/4 cup, or more to taste | water: 30g | cornstarch: 3g ; strawberry: 1:pound | balsamic vinegar: 0.25:cup | white sugar: 15:g | cornstarch: 3:g ", "53, 0.2, 13.4, 0.4, 0, 1", 25, "0001", "0100", "000010", "011", 0);

        addRecipe(db, "Asian Avocado", "Here is a very different and simple way of enjoying avocados. Use low-sodium soy sauce if desired, but full-flavored is best.", "1.Stir together garlic, ginger, and soy sauce; set aside for five minutes to allow the flavors to blend. Cut the avocado in half, and discard the pit; divide the sauce between the avocado halves. Eat with a spoon!", "avocado: 1 | minced fresh ginger rootb: 1.5g | minced garlic: 1.5g | soy sauce: 3g ; avocado: 1:  ", "164, 14.7, 9.1, 2.2, 0, 157", 15, "0001", "1000", "000010", "111", 0);

        addRecipe(db, "Grilled Salmon", "A simple soy sauce and brown sugar marinade, with hints of lemon and garlic, are the perfect salty-sweet complement to rich salmon fillets. Even my 9 year old loves this recipe!", "1 Season salmon fillets with lemon pepper, garlic powder, and salt. | 2 In a small bowl, stir together soy sauce, brown sugar, water, and vegetable oil until sugar is dissolved. Place fish in a large resealable plastic bag with the soy sauce mixture, seal, and turn to coat. Refrigerate for at least 2 hours. | 3 Preheat grill for medium heat. | 4 Lightly oil grill grate. Place salmon on the preheated grill, and discard marinade. Cook salmon for 6 to 8 minutes per side, or until the fish flakes easily with a fork.", "salmon fillets1: 1/2 pounds | lemon pepper to taste | garlic powder to taste | salt to taste | soy sauce: 1/3 cup | brown sugar: 1/3 cup | water: 1/3 cup | vegetable oil: 1/4 cup ; salmon: 0.5:pound | soy sauce: 0.5:cup | brown sugar: 0.5:cup | vegetable oil: 0.25:cup ", "318, 20.1, 13.2, 20.5, 56, 1092", 16, "0110", "0100", "000100", "100", 0);

        addRecipe(db, "Rockin' Osters Rockefeller", "This is a slight variation on the classic dish Oysters Rockefeller. Serve this delicious dish and watch your guests cry, 'I love you!!!'", "1 Clean oysters, and place in a large stockpot. Pour in beer and enough water to cover oysters; add 2 cloves garlic, seasoned salt, and peppercorns. Bring to a boil. Remove from heat, drain, and cool. | 2 Once oysters are cooled, break off and discard the top shell. Arrange the oysters on a baking sheet. Preheat oven to 425 degrees F (220 degrees C.) | 3 Melt butter in a saucepan over medium heat. Cook onion and garlic in butter until soft. Reduce heat to low, and stir in spinach, Monterey Jack, fontina, and mozzarella. Cook until cheese melts, stirring frequently. Stir in the milk, and season with salt and pepper. Spoon sauce over each oyster, just filling the shell. Sprinkle with bread crumbs. | 4 Bake until golden and bubbly, approximately 8 to 10 minutes.", "fresh, unopened oysters:48 | beer: 1 1/2 cups | cloves garlic:2 | seasoned salt to taste | black peppercorns: 7 | butter: 1/2 cup | onion, chopped: 1 | clove garlic, crushed: 1 | frozen chopped spinach, thawed and drained: 1 (10 ounce) package | Monterey Jack cheese, shredded: 8 ounces | fontina cheese, shredded: 8 ounces | mozzarella cheese, shredded: 8 ounces | milk: 1/2 cup | salt, or to taste: 30g | ground black pepper: 15g | fine bread crumbs: 30g ; oyster: 48:  | beer: 1.5:cup | garlic: 3:  | butter: 50:g | onion: 1  | spinach: 10:ounce | Monterey Jack cheese: 8:ounce | fontina cheese: 8:ounce | mozzarella cheese: 8:ounce | milk: 0.5:cup | pepper: 30:g ", "248, 17.4, 5.3, 16.4, 66, 652", 30, "0110", "0100", "000100", "101", 0);

        addRecipe(db, "Cioppino", "A wonderful seafood stew! Serve with a loaf of warm, crusty bread for sopping up the delicious broth!", "1.Over medium-low heat melt butter in a large stockpot, add onions, garlic and parsley. Cook slowly, stirring occasionally until onions are soft. | 2.Add tomatoes to the pot (break them into chunks as you add them). Add chicken broth, bay leaves, basil, thyme, oregano, water and wine. Mix well. Cover and simmer 30 minutes. | 3.Stir in the shrimp, scallops, clams, mussels and crabmeat. Stir in fish, if desired. Bring to boil. Lower heat, cover and simmer 5 to 7 minutes until clams open. Ladle soup into bowls and serve with warm, crusty bread!", "butter: 3/4 cup | onions, chopped: 2 | cloves garlic, minced: 2 | fresh parsley, chopped: 1 bunch | stewed tomatoes: 2 (14.5 ounce) cans | chicken broth: 2 (14.5 ounce) cans | bay leaves: 2 | dried basil: 15g | dried thyme: 7.5g | dried oregano: 7.5g | water: 1 cup | white wine: 1 1/2 cups | large shrimp - peeled and deveined: 1 1/2 pounds | bay scallops: 1 1/2 pounds | small clams: 18 | mussels, cleaned and debearded: 18 | crabmeat: 1 1/2 cups | cod fillets, cubed: 1 1/2 pounds ; butter: 50:g | onion: 2:  | garlic: 2:  | tomato: 3:  | chicken broth: 2.5:cup | basil: 15:g | thyme: 7.5:g | white wine: 1.5:cup | shrimp: 1.5:pound | bay scallops: 1.5:pound | clam: 18:  | crabmeat: 1.5:cup | mussel: 18:  | cod: 1.5:pound ", "318, 12.9, 9.3, 34.9, 164, 755", 55, "0110", "0001", "000100", "110", 0);

        addRecipe(db, "Fish Chowder", "The fishermen of Bodega Bay, California shared this favorite, quick and easy recipe with my sister during a Fish Festival. It is one of the best chowders I've had, and my kids love it too! We top with bacon bits and a few shakes of hot sauce for a little spice. Enjoy!", "1.In a large stockpot, melt 2 tablespoons butter over medium heat. Saute onions, mushrooms and celery in butter until tender. | 2.Add chicken stock and potatoes; simmer for 10 minutes. | 3.Add fish, and simmer another 10 minutes. | 4.Season to taste with Old Bay seasoning, salt and pepper. Mix together clam juice and flour until smooth; stir into soup. Remove from heat, and stir in evaporated milk. Serve.", "butter: 30g | chopped onion: 2 cups | fresh mushrooms, sliced: 4 | stalk celery, chopped: 1 | chicken stock: 4 cups | diced potatoes: 4 cups | cod, diced into 1/2 inch cubes: 2 pounds | Old Bay Seasoning TM: 2g, or to taste | salt to taste | ground black pepper to taste | clam juice: 1 cup | all-purpose flour: 1/2 cup | evaporated milk: 2 (12 fluid ounce) cans ; butter: 30:g | onion: 1:  | mushroom: 4:  | chicken broth: 4:cup | potato: 3:  | cod: 2:pound | all-purpose flour: 50:g | milk: 3:cup ", "356, 11.3, 33.7, 29.8, 77, 618", 60, "0110", "0001", "000100", "110", 0);

        addRecipe(db, "Fish Tacos", "I'm from San Diego and these taste just like home! We live in the south now, and nobody has heard of these! Serve with homemade pico de gallo, and lime wedges to squeeze on top!", "1.To make beer batter: In a large bowl, combine flour, cornstarch, baking powder, and salt. Blend egg and beer, then quickly stir into the flour mixture (don't worry about a few lumps). | 2.To make white sauce: In a medium bowl, mix together yogurt and mayonnaise. Gradually stir in fresh lime juice until consistency is slightly runny. Season with jalapeno, capers, oregano, cumin, dill, and cayenne. | 3.Heat oil in deep-fryer to 375 degrees F (190 degrees C). | 4.Dust fish pieces lightly with flour. Dip into beer batter, and fry until crisp and golden brown. Drain on paper towels. Lightly fry tortillas; not too crisp. To serve, place fried fish in a tortilla, and top with shredded cabbage, and white sauce.", "all-purpose flour: 1 cup | cornstarch: 30g | baking powder: 15g | salt: 7.5g | egg: 1 | beer: 1 cup | plain yogurt: 1/2 cup | mayonnaise: 1/2 cup | lime, juiced: 1 | jalapeno pepper, minced: 1 | minced capers: 15g | dried oregano: 7.5g | ground cumin: 7.5g | dried dill weed: 7.5g | ground cayenne pepper: 15g | oil for frying: 1 quart | cod fillets, cut into 2 to 3 ounce portions: 1 pound | corn tortillas: 1 (12 ounce) package | medium head cabbage, finely shredded: 1/2 ; all-purpose flour: 50:g | cornstarch: 30:g | backing powder: 15:g | egg: 1:  | yogurt: 0.5:cup | mayonnaise: 0.5:cup | pepper: 15:g | cod: 1:pound | corn tortillas: 12:ounce ", "409, 18.8, 43, 17.3, 54, 407", 60, "0110", "0001", "000100", "010", 0);

        addRecipe(db, "pork afritada", "Pork, liver, and potatoes are cooked in a tomato base to make a stew elegant enough for special occasions.", "1.Place the pork in a large pot; pour enough water into the pot to cover the pork. Stir the soy sauce and lemon juice into the water. Bring the mixture to a boil for 5 minutes. Remove the meat and set aside. Discard the liquid. | 2.Refill the pot with fresh water; add the pork liver and bring to a boil for about 5 minutes. Remove the liver and allow to cool; cut into bite sized pieces. Set aside. | 3.Heat 3 tablespoons olive oil in a large skillet over medium-high heat; fry the potatoes in the hot oil until golden brown and cooked through, 7 to 10 minutes. Remove the potatoes to a plate lined with paper towels. Add 2 more tablespoons olive oil to the skillet and allow to get hot. Cook and stir the onion and garlic in the hot oil until fragrant, 3 to 5 minutes. Add the pork and pork liver to the skillet; cover and cook for 5 minutes. Stir the tomatoes into the mixture; cook together, stirring occasionally, another 5 minutes. Return the potatoes to the skillet with the green bell pepper. Season with salt and pepper. Cook and stir another 5 minutes.", "boneless pork, cut into bite-sized pieces: 2 1/4 pounds | soy sauce: 30g | lemon, juiced: 1/2 | pork liver: 2 pounds | olive oil: 45g | potatoes, quartered: 2 | olive oil: 30g | onion, chopped: 1 | cloves garlic, minced: 2 | large tomatoes, diced: 2 | green bell pepper, cut into chunks: 1 | salt and ground black pepper to taste ; pork: 2.5:pound | soy sauce: 1:cup | lemon juice: 20:g | pork liver: 2:pound | olive oil: 75:g | potato: 2:  | onion: 1:  | garlic: 2:  | tomato: 3:  | pepper: 30g | ", "324, 16.9, 14.1, 28.6, 189, 235", 60, "0110", "0001", "001000", "100", 0);

        addRecipe(db, "Almond Pork", "This pork recipe is a staple in our house. It is easy to prepare and is quite tasty! I like to serve this dish with steamed broccoli with hollandaise sauce.", "1.With a meat mallet, pound the tenderloin slices to 1/4-inch thick. Combine tarragon and garlic powder in a small bowl, and sprinkle the mixture over the cutlets. Rub the seasoning into the meat, cover, and refrigerate for 1 hour to blend the flavors. | 2.Melt 1 tablespoon of butter in a skillet over medium heat until the foam disappears. Brown half of the tenderloin pieces in the butter, 3 to 4 minutes per side. Do not crowd pan. Repeat with the remainder of the pork pieces and 1 more tablespoon of butter. Set the pork pieces aside on a warm platter. | 3.Melt remaining 2 tablespoons of butter in the skillet over medium heat; stir in the flour until smooth and the mixture starts to fry. Gradually whisk in wine, stirring constantly until the flour mixture and wine are blended; stir in chicken bouillon granules and Dijon mustard. Allow the sauce to boil for 1 minute; whisk in the cream. Bring the mixture almost to the boiling point, and stir in the almonds and green onions. Pour sauce over the pork, and serve.", "pork tenderloin, trimmed and cut into 1/2-inch thick medallions: 1 pound | dried tarragon, crumbled: 15g | garlic powder: 15g | butter, divided: 60g | all-purpose flour: 15g | white wine: 3/4 cup | chicken bouillon granules: 15g | Dijon mustard: 15g | heavy cream: 1/4 cup | toasted slivered almonds: 1/2 cup | sliced green onions: 1/4 cup ; pork: 1:pound | garlic: 1:  | butter: 60:g | all-purpose flour: 15:g | white wine: 0.5:cup | mustard: 0.5:cup | heavy cream: 0.25:cup | onion: 0.5:  | almond: 30:g ", "417, 28.3, 7.8, 24.3, 114, 323", 110, "0110", "0001", "001000", "100", 0);

        addRecipe(db, "Tender Pork Spare Ribs", "I saw a Celebrity Chef use this braising method for baby back ribs, so I decided to give it a try for pork spare ribs. I changed the seasonings and increased the cooking time and I have been very pleased with them every time. They're really tender and the meat is so flavorful that you don't have to add BBQ sauce unless you want to. My neighbor is the one who told me about the fajita seasoning, and she was right on!", "1.Mix the brown sugar, fajita seasoning, and paprika in a bowl. Rub both sides of the pork spareribs with the brown sugar mixture. Place the spareribs in a 9x13-inch baking pan; cover and refrigerate overnight. | 2.Preheat an oven to 250 degrees F (120 degrees C). Whisk together the beer, garlic, honey, Worcestershire sauce, and mustard in a bowl. Set aside. | 3.Tear off 2 large sheets of heavy duty aluminum foil and lay them shiny-side down. Place a rack of spareribs on each sheet, meaty-side up. Tear off 2 more sheets of foil and place them on top of the ribs, shiny-side up. Begin tightly folding the edges of the foil together to create a sealed packet. Just before sealing completely, divide the beer mixture evenly into each packet. Complete the seal. Place the packets side-by-side on an 11x14-inch baking sheet. | 4.Bake in the preheated oven until the ribs are very tender, 3 hours and 30 minutes to 4 hours. Carefully open each packet, and drain the drippings into a saucepan. You may only need the drippings from one packet. Set ribs aside. Simmer the drippings over medium-high heat until the sauce begins to thicken, about 5 minutes. Brush the thickened sauce over the ribs. | 5.Preheat the oven's broiler and set the oven rack about 6 inches from the heat source. | 6.Place the ribs back into the oven and broil until the sauce is lightly caramelized, 5 to 7 minutes.", "brown sugar: 1 cup | fajita seasoning (such as Fiesta?): 1/2 cup | Hungarian sweet paprika: 30g | racks pork spareribs, fat trimmed: 2 | beer: 1 cup | cloves garlic, minced: 3 | honey: 15g |Worcestershire sauce: 45g | prepared brown mustard: 15g ; brown sugar: 1:cup | pepper: 30:g | pork: 3:pound | beer: 1:cup | garlic: 3:  | honey: 0.5:cup | mustard: 0.5:cup ", "943, 60.6, 37.4, 58.4, 240, 701", 980, "0110", "0001", "001000", "100", 0);

        addRecipe(db, "Stuffed Pork Tenderlion", "I came up with this spinach and mushroom stuffed pork tenderloin one day when looking for something new to do with pork tenderloin. This dish is very easy to make and assemble and is so pretty to serve to family and guests. I like to serve this with a nice salad and roasted potatoes.", "1.Preheat oven to 350 degrees F (175 degrees C). | 2.Heat 1 teaspoon olive oil in a skillet over medium heat; cook and stir mushrooms, shallot, thyme, garlic powder, sage, black pepper, and salt in the hot oil until liquid has evaporated and mushrooms and shallots are softened, 5 to 10 minutes. Add parsley; cook and stir for 1 minute. Mix in spinach; cook and stir until spinach is wilted, about 5 minutes. Stir in mustard. Remove from heat. | 3.Place pork tenderloin on a work surface; lay prosciutto atop tenderloin. Spread mushroom-spinach mixture over prosciutto, leaving 1/2-inch border on all sides. Tightly roll tenderloin around the filling and tie together with kitchen string to keep closed. | 4.Heat 2 tablespoons olive oil in a large skillet over medium heat; place rolled tenderloin in the hot oil. Sear until all sides are golden brown, about 10 minutes. Transfer seared tenderloin to a 9x13-inch casserole dish. | 5.Bake in the preheated oven until pork is no longer pink in the center, 25 to 30 minutes. An instant-read thermometer inserted into the center should read at least 160 degrees F (71 degrees C).", "extra-virgin olive oil, or as needed: 15g | white mushrooms, minced: 10 | shallot, minced: 1 | dried thyme: 7.5g | garlic powder: 7.5g | dried sage: 7.5g | ground black pepper: 7.5g | salt: 3.75g | chopped fresh parsley: 1/4 cup | fresh spinach: 2 cups | Dijon mustard: 15g | pork tenderloin, butterflied and pounded flat: 1 (2 pound) | slices prosciutto: 4 | extra-virgin olive oil: 30g ; olive oil: 15:g | mushroom: 15:  | thyme: 7.5:g | garlic: 0.5:  | pepper: 7.5:g | spinach: 5:ounce | mustard: 0.5:cup | pork: 2:pound ", "296, 16.2, 4.2, 32.1, 89, 440", 35, "0110", "0010", "001000", "100", 0);

        addRecipe(db, "Barbecued Pork Kebabs", "This pork kebab recipe is great for parties outside, especially picnics.", "1.Whisk the sugar, soy sauce, onion, garlic and black pepper together in a large bowl. Add the pork and toss to coat. Cover and refrigerate at least 2 hours. Overnight is best if possible. | 2.Preheat an outdoor grill for high heat, and lightly oil the grate. | 3.Thread the pork onto the soaked skewers. Cook on the preheated grill until the pork is no longer pink in the center, 3 to 5 minutes per side.", "white sugar: 1 cup | soy sauce: 1 cup | onion, diced: 1 | cloves garlic, chopped: 5 | ground black pepper: 15g | boneless pork loin, cut into 1 1/2-inch cubes: 1 (4 pound) | bamboo skewers, soaked in water for 30 minutes: 10 ; white sugar: 30:g | soy sauce: 1:cup | onion: 1:  | garlic: 5:  | pepper: 15:g | pork: 4:pound", "369, 15.8, 24.7, 31.1, 88, 1508", 15, "0110", "1000", "001000", "100", 0);

        addRecipe(db, "BLT salad", "This recipe is reminiscent of the classic BLT or bacon, lettuce, and tomato sandwich. It's a great summertime salad!", "1.Place bacon in a large, deep skillet. Cook over medium high heat, turning frequently, until evenly browned. Drain, crumble and set aside. | 2.In a blender or food processor, combine mayonnaise, milk, garlic powder and black pepper. Blend until smooth. Season the dressing with salt. | 3.Combine lettuce, tomatoes, bacon and croutons in a large salad bowl. Toss with dressing, and serve immediately.", "bacon: 1 pound | mayonnaise: 3/4 cup | milk: 1/4 cup | garlic powder: 15g | ground black pepper: 2g | salt to taste | romaine lettuce - rinsed, dried and shredded: 1 head | large tomatoes, chopped: 2 | seasoned croutons: 2 cups ; bacon: 1:pound | mayonnaise: 0.75:cup | milk: 0.25:cup | garlic: 0.5:  | pepper: 2:g | tomato: 3:  ", "421, 35.1, 14.8, 12.5, 40, 907", 10, "1110", "1000", "000010", "100", 0);

        addRecipe(db, "Roasted Brussels Sprouts", "This recipe is from my mother. It may sound strange, but these are really good and very easy to make. The Brussels sprouts should be brown with a bit of black on the outside when done. Any leftovers can be reheated or even just eaten cold from the fridge. I don't know how, but they taste sweet and salty at the same time!", "1.Preheat oven to 400 degrees F (205 degrees C). | 2.Place trimmed Brussels sprouts, olive oil, kosher salt, and pepper in a large resealable plastic bag. Seal tightly, and shake to coat. Pour onto a baking sheet, and place on center oven rack. | 3.Roast in the preheated oven for 30 to 45 minutes, shaking pan every 5 to 7 minutes for even browning. Reduce heat when necessary to prevent burning. Brussels sprouts should be darkest brown, almost black, when done. Adjust seasoning with kosher salt, if necessary. Serve immediately.", "Brussels sprouts, ends trimmed and yellow leaves removed: 1 1/2 pounds | olive oil: 45g | kosher salt: 15g | freshly ground black pepper: 7.5g ; brussels sprouts: 1.5:pound | olive oil: 45:g | pepper: 7.5:g ", "104, 7.3, 10, 2.9, 0, 344", 50, "0110", "0001", "000010", "011", 0);

        addRecipe(db, "Spinach and Orzo Salad", "A light, easy-to-make salad that's pleasing to the palate.", "1.Bring a large pot of lightly salted water to a boil. Add orzo and cook for 8 to 10 minutes or until al dente; drain and rinse with cold water. Transfer to a large bowl and stir in spinach, feta, onion, pine nuts, basil and white pepper. Toss with olive oil and balsamic vinegar. Refrigerate and serve cold.", "uncooked orzo pasta: 1 (16 ounce) package | baby spinach leaves, finely chopped: 1 (10 ounce) package | crumbled feta cheese: 1/2 pound | red onion, finely chopped: 1/2 | pine nuts: 3/4 cup | dried basil: 7.5g | ground white pepper: 3.75g | olive oil: 1/2 cup | balsamic vinegar: 1/2 cup ; pasta: 16:ounce | spinach: 10:ounce | onion: 0.5:  | basil: 7.5:g | pine nuts: 30:g | pepper: 4:g | olive oil: 30:g | balsamic vinegar: 0.5: cup ", "490, 26.9, 49, 15.8, 25, 349", 80, "0110", "0001", "000010", "010", 0);

        addRecipe(db, "Sweet Potato Casserole", "Mmm! Sweet potatoes topped with creamy toasted marshmallows.", "1.Preheat oven to 350 degrees F (175 degrees C). | 2.Place sweet potatoes in a large saucepan with enough water to cover. Bring to a boil, and cook until tender, about 15 minutes. Remove from heat, drain, and mash. | 3.Place mashed sweet potatoes in large bowl, and use an electric mixer to blend with the margarine, brown sugar, orange juice, and cinnamon. Spread evenly into a 9x13 inch baking dish. Top with miniature marshmallows. | 4.Bake for 25 to 30 minutes in the preheated oven, or until heated through, and marshmallows are puffed and golden brown.", "sweet potatoes, sliced: 5 | reduced fat margarine: 1/4 cup | packed brown sugar: 1/2 cup | orange juice: 45g | ground cinnamon: 1 pinch | miniature marshmallows: 1 (10.5 ounce) package ; potato: 5:  | brown sugar: 0.5:cup | orange juice: 45:g | marshmallows: 10.5: ounce ", "267, 2.6, 60.4, 2, 0, 143", 60, "0111", "0001", "000010", "011", 0);

        addRecipe(db, "Honey Mustard Tofu", "I have always made this recipe with chicken breasts until my daughter became a vegetarian and asked me to adapt the recipe for tofu.", "1.Melt butter in a medium skillet over medium-high heat. Dredge tofu slices in flour and place in hot butter. Brown slightly, then turn over and brown the other side. Add water, wine and bouillon cubes; simmer for 10 minutes, or until bouillon completely dissolves. Stir in mustard and honey. Simmer until thickened.", "butter: 45g | firm tofu, sliced into 1/4 inch slices: 1 pound | whole wheat flour: 2 cups | water: 1 cup | dry white wine: 1/4 cup | vegetable bouillon: 2 cubes | prepared mustard: 60g | honey: 1/4 cup ; butter: 45:g | tofu: 1:pound | whole wheat flour: 2:cup | white wine: 0.25:cup | mustard: 2:cup | honey: 0.25:cup ", "532, 20.3, 67.1, 27, 23, 262", 25, "0110", "0100", "000010", "100", 0);

        addRecipe(db, "Pumpkin Pancakes", "These are good any season but taste best on cold winter mornings. You can use canned or cooked fresh pumpkin.", "1.In a bowl, mix together the milk, pumpkin, egg, oil and vinegar. Combine the flour, brown sugar, baking powder, baking soda, allspice, cinnamon, ginger and salt in a separate bowl. Stir into the pumpkin mixture just enough to combine. | 2.Heat a lightly oiled griddle or frying pan over medium high heat. Pour or scoop the batter onto the griddle, using approximately 1/4 cup for each pancake. Brown on both sides and serve hot.", "milk: 1 1/2 cups | pumpkin puree: 1 cup | egg: 1 | vegetable oil: 30g | vinegar: 30g | all-purpose flour: 2 cups | brown sugar: 45g | baking powder: 30g | baking soda: 15g | ground allspice: 15g | ground cinnamon: 15g | ground ginger: 7.5g | salt: 7.5g ; milk : 1.5:cup | pumpkin puree: 1:cup | egg: 1:  | vegetable oil: 1:cup | all-purpose flour: 2:cup | brown sugar: 1.5:cup | baking powder: 30:g ", "278, 7.2, 45.8, 7.9, 36, 608", 40, "1111", "0010", "000010", "010", 0);

        addRecipe(db, "Pretzel Turtles", "Quick and easy Turtles? candies! Mini pretzels, caramel covered chocolate candies, and pecans make up this delicious treat.", "1.Preheat oven to 300 degrees F (150 degrees C). | 2.Arrange the pretzels in a single layer on a parchment lined cookie sheet. Place one chocolate covered caramel candy on each pretzel. | 3.Bake for 4 minutes. While the candy is warm, press a pecan half onto each candy covered pretzel. Cool completely before storing in an airtight container.", "small mini pretzels: 20 | pecan halves: 20 | chocolate covered caramel candies: 20 ; pretzels: 20:  | pecan halves: 20:  | chocolate: 10:ounce ", "83, 2.2, 14.1, 1.7, 1, 263", 14, "0001", "1000", "000001", "011", 0);

        addRecipe(db, "Playgroup Granola Bars", "My girlfriend brought these granola bars over for a playgroup one morning and ever since they've been a staple! My son requests them almost daily so I usually triple the recipe and make 2 trays so we have plenty on hand.", "1.Preheat the oven to 350 degrees F (175 degrees C). Generously grease a 9x13 inch baking pan. | 2.In a large bowl, mix together the oats, brown sugar, wheat germ, cinnamon, flour, raisins and salt. Make a well in the center, and pour in the honey, egg, oil and vanilla. Mix well using your hands. Pat the mixture evenly into the prepared pan. | 3.Bake for 30 to 35 minutes in the preheated oven, until the bars begin to turn golden at the edges. Cool for 5 minutes, then cut into bars while still warm. Do not allow the bars to cool completely before cutting, or they will be too hard to cut.", "rolled oats: 2 cups | packed brown sugar: 3/4 cup | wheat germ: 1/2 cup | ground cinnamon: 12g | all-purpose flour: 1 cup | raisins (optional): 3/4 cup | salt: 12g | honey: 1/2 cup | egg, beaten: 1 | vegetable oil: 1/2 cup | vanilla extract: 30g ; oat: 2:cup | brown sugar: 0.75:cup | ground cinnamon: 12:g | all-purpose flour: 30:g | honey: 0.5:cup | egg: 1:  | vegetable oil: 0.5:cup ", "161, 5.5, 26.6, 2.4, 8, 79", 50, "0001", "0001", "000001", "010", 0);

        addRecipe(db, "Mint Juleps", "Sit on the front porch in your rocking chair on a sultry afternoon and sip on one or two of these. The proper way to serve a mint julep is in a frozen silver goblet, but you can use glasses instead--just use the most elegant ones you have! You can make the syrup ahead of time and store it in the refrigerator for whenever the julep mood strikes you.", "1.Combine water, sugar and chopped mint leaves in a small saucepan. Bring to a boil over high heat until the sugar is completely dissolved. Allow syrup to cool, approximately 1 hour. Pour syrup through a strainer to remove mint leaves | 2.Fill eight cups or frozen goblets with crushed ice and pour 4 ounces of bourbon and 1/4 cup mint syrup in each. (Proportions can be adjusted depending on each person's sweet tooth). Top each cup with a mint sprig and a straw. Trim straws to just barely protrude from the top of the cups. Serve juleps on a silver platter.", "Water: 2 cups | white sugar: 2 cups | roughly chopped fresh mint leaves: 1/2 cup | Kentucky bourbon: 32 fluid ounces | sprigs fresh mint leaves for garnish: 8 ; white sugar: 60:g | mint leaves: 100:g | bourbon: 32:ounce ", "95, 2.2, 14.1, 1.7, 0, 263", 25, "0001", "0100", "000010", "010", 0);

        addRecipe(db, "Salted Caramel Custard", "These easy 'pots de crèmes' are just sweet enough, just salty enough, and just drop-dead gorgeous enough to be my favorite dessert.", "1.Preheat oven to 300 degrees F (150 degrees C). Place 6 (6.5 ounce) ramekins in a baking dish. | 2.Place sugar evenly in a heavy-bottomed saucepan over medium heat. Without stirring, allow sugar to slowly melt around the edges of the pan, about 5 minutes, adjusting heat as necessary. When edges start to bubble, shake and swirl pan to dissolve remaining sugar but without stirring it. Keep pan moving until you have a clear, dark caramel, about 10 minutes. When sugar is completely dissolved, whisk in cream; caramel will cool into a lump; this is normal. Keep stirring over medium heat until caramel melts and mixture comes up to temperature and sugar has dissolved. Remove from heat | 3.Stir in kosher salt, vanilla, and cold milk; mix well. | 4.Place egg yolks in a large mixing bowl. Whisk in a ladleful of caramel/milk mixture to the yolks. Add 2 more ladlefuls, one at a time. Then mix in the remainder of the caramel/milk mixture to the yolks, whisking until thoroughly blended. | 5.Divide custard mixture evenly among the ramekins. Carefully pour into the baking dish enough water so that it comes halfway up the sides of the filled ramekins. | 6.Bake in preheated oven until custard is set and the top of custard jiggles evenly across the surface, 45 to 60 minutes (depending on the size of your ramekins and how full they are). If the center seems looser than the edges, continue to bake another minute or so. | 7.Transfer ramekins to a cooling rack; cool to room temperature. Cover ramekins with plastic wrap and refrigerate at least 1 hour until they are very cold. | 8.Serve topped with a pinch of light, flaky sea salt.", "large egg yolks: 9 | white sugar: 2/3 cup | heavy cream: 2 cups | kosher salt: 7.5g | vanilla extract: 30g | whole milk: 1 cup | Flaky sea salt (such as Maldon?), to garnish ; egg: 9:  | white sugar: 50:g | heavy cream: 2:cup | milk: 1:cup | vanilla extract: 30:g ", "467, 37.3, 27.3, 6.9, 420, 272", 130, "0001", "0001", "100000", "010", 0);

        addRecipe(db, "Chicken Enchiladas", "A great way to use leftover chicken. Even kids love these!", "1.Preheat oven to 350 degrees F (175 degrees C). Lightly grease a large baking dish. | 2.In a medium saucepan over medium heat, melt the butter and saute the green onion until tender (about 3 to 4 minutes). Add the garlic powder, then stir in the green chiles, cream of mushroom soup and sour cream. Mix well. Reserve 3/4 of this sauce and set aside. To the remaining 1/4 of the sauce in the saucepan, add the chicken and 1/2 cup of shredded Cheddar cheese. Stir together. | 3.Fill each flour tortilla with the chicken mixture and roll up. Place seam side down in the prepared baking dish. | 4.In a small bowl combine the reserved 3/4 of the sauce with the milk. Spoon this mixture over the rolled tortillas and top with the remaining 1/2 cup of shredded Cheddar cheese. Bake in the preheated oven for 30 to 35 minutes, or until cheese is bubbly.", "butter: 15g | chopped green onions: 1/2 cup | garlic powder: 7.5g | diced green chiles: 1 (4 ounce) can | condensed cream of mushroom soup: 1 (10.75 ounce) can | sour cream: 1/2 cup | cubed cooked chicken breast meat: 1 1/2 cups | shredded Cheddar cheese, divided: 1 cup | flour tortillas: 6 (12 inch) | milk: 1/4 cup ; butter: 15:g | onion: 1:  | garlic: 1:  | mushroom: 8:  | chicken breast: 1:  | milk: 0.25:cup ", "619, 27.1, 66.8, 26.1, 60, 1459", 45, "0110", "0001", "100001", "100", 0);

        addRecipe(db, "American Lasagna", "Making this lasagna a day ahead and refrigerating overnight allows the spices to meld, and gives it exceptional flavor.", "1.In a skillet over medium heat, brown ground beef, onion and garlic; drain fat. Mix in basil, oregano, brown sugar, 1 1/2 teaspoons salt, diced tomatoes and tomato paste. Simmer for 30 to 45 minutes, stirring occasionally. | 2.Preheat oven to 375 degrees F (190 degrees C). Bring a large pot of lightly salted water to a boil. Add lasagna noodles, and cook for 5 to 8 minutes, or until al dente; drain. Lay noodles flat on towels, and blot dry. | 3.In a medium bowl, mix together eggs, ricotta, Parmesan cheese, parsley and 1 teaspoon salt. | 4.Layer 1/3 of the lasagna noodles in the bottom of a 9x13 inch baking dish. Cover noodles with 1/2 ricotta mixture, 1/2 of the mozzarella cheese and 1/3 of the sauce. Repeat. Top with remaining noodles and sauce. Sprinkle additional Parmesan cheese over the top. | 5.Bake in the preheated oven 30 minutes. Let stand 10 minutes before serving.", "lean ground beef: 1 1/2 pounds | onion, chopped: 1 | cloves garlic, minced: 2 | chopped fresh basil: 15g | dried oregano: 5g | brown sugar: 30g | salt: 4.5g | diced tomatoes: 1 (29 ounce) can | tomato paste: 2 (6 ounce) cans | dry lasagna noodles: 12 | eggs, beaten: 2 | part-skim ricotta cheese: 1 pint | grated Parmesan cheese: 1/2 cup | dried parsley: 30g | salt: 3g | mozzarella cheese, shredded: 1 pound | grated Parmesan cheese: 30g ; beef: 1.5:pound | onion: 1:  | garlic: 2:  | basil: 15:g | brown sugar: 1:cup | tomato: 2:  | tomato paste: 50:g | lasagna noodles: 12:  | egg: 2:  | parmesan cheese: 1:cup | mozzarella cheese: 9:ounce ", "664, 29.5, 48.3, 50.9, 168, 1900", 120, "0110", "0001", "010001", "100", 0);

        addRecipe(db, "Buffalo Style Chicken Pizza", "Pizza with a little kick of buffalo wing flavor! Have your pizza and wings together!", "1.Preheat oven to 425 degrees F (220 degrees C). | 2.In a medium bowl combine the cubed chicken, melted butter and hot sauce. Mix well. Spread whole bottle of salad dressing over crust, then top with chicken mixture and sprinkle with shredded cheese. | 3.Bake in preheated oven until crust is golden brown and cheese is bubbly, about 5 to 10 minutes. Let set a few minutes before slicing, and serve.", "skinless, boneless chicken breast halves - cooked and cubed: 3 | butter, melted: 30g | hot sauce: 1 (2 ounce) bottle | blue cheese salad dressing: 1 (8 ounce) bottle | prepared pizza crust: 1 (16 inch) | shredded mozzarella cheese: 1 (8 ounce) package ; chicken breast: 3:  | butter: 30:g | pizza crust: 1:  | mozzarella cheese: 8:ounce ", "785, 40.7, 66.6, 37.1, 83, 1840", 55, "0110", "0001", "100001", "100", 0);

        addRecipe(db, "Tuna Noodle Casserole from Scratch", "No canned soup mix in this recipe! Mushrooms, onions, celery, and peas all go into this comfort casserole.", "1.Preheat oven to 375 degrees F (190 degrees C). Butter a medium baking dish with 1 tablespoon butter. | 2.Bring a large pot of lightly salted water to a boil. Add egg noodles, cook for 8 to 10 minutes, until al dente, and drain. | 3.Melt 1 tablespoon butter in a skillet over medium-low heat. Stir in the onion, celery, and garlic, and cook 5 minutes, until tender. Increase heat to medium-high, and mix in mushrooms. Continue to cook and stir 5 minutes, or until most of the liquid has evaporated. | 4.Melt 4 tablespoons butter in a medium saucepan, and whisk in flour until smooth. Gradually whisk in milk, and continue cooking 5 minutes, until sauce is smooth and slightly thickened. Season with salt and pepper. Stir in tuna, peas, mushroom mixture, and cooked noodles. Transfer to the baking dish. Melt remaining 2 tablespoons butter in a small bowl, mix with bread crumbs, and sprinkle over the casserole. Top with cheese. | 5.Bake 25 minutes in the preheated oven, or until bubbly and lightly browned.", "butter, divided: 1/2 cup | uncooked medium egg noodles: 1 (8 ounce) package | medium onion, finely chopped: 1/2 | stalk celery, finely chopped: 1 | clove garlic, minced: 1 | button mushrooms, sliced: 8 ounces | all-purpose flour: 1/4 cup | milk: 2 cups | salt and pepper to taste | tuna, drained and flaked: 2 (6 ounce) cans | frozen peas, thawed: 1 cup | bread crumbs: 45g | butter, melted: 30g | shredded Cheddar cheese : 1 cup ; butter: 60:g | egg noodles: 8:ounce | onion: 0.5:  | garlic: 1:  | mushroom: 10:  | all-purpose flour: 15:g | milk: 2:cup | tuna: 6:ounce ", "562, 31.3, 39.9, 30.8, 125, 793", 75, "0110", "0001", "000111", "100", 0);

        addRecipe(db, "Paleo Cauliflower Rice", "This paleo recipe is so quick, easy, and delicious and really a great substitute for rice if you're trying to eat low-carb. Your whole family will love it!", "1.Place cauliflower chunks in a food processor and pulse until broken down into rice-size pieces. | 2.Heat olive oil in a skillet over medium heat; add cauliflower 'rice', salt, and pepper. Cover skillet and cook until heated through, 3 to 5 minutes. Remove lid and fluff 'rice' with a fork.", "large head cauliflower, cut into large chunks: 1 | extra-virgin olive oil: 30g | salt and ground black pepper to taste ; cauliflower: 1:  | olive oil: 30:g ", "113, 7, 11.1, 4.2, 0, 160", 15, "0110", "1000", "000011", "011", 0);

        addRecipe(db, "Flatlander Chili", "Easy to make, great anytime, and always a favorite.", "1.Place ground beef in a large, deep skillet. Cook over medium-high heat until evenly brown. Drain, crumble, and set aside. | 2.Add all ingredients to a large kettle. Bring to boil. Reduce heat and simmer for 1 to 1 1/2 hours, stirring occasionally.", "lean ground beef: 2 pounds | tomato juice: 1 (46 fluid ounce) can | tomato sauce: 1 (29 ounce) can | chopped onion: 1 1/2 cups | chopped celery：1/2 cup | chopped green bell pepper: 1/4 cup | chili powder: 1/4 cup | ground cumin: 6g | garlic powder: 7.5g | salt: 3g | ground black pepper: 1.5g | dried oregano: 1.5g | white sugar: 1.5g | ground cayenne pepper: 1g | canned red beans, drained and rinsed: 2 cups ; beef: 2:pound | tomato sauce: 29:ounce | tomato: 3:  | onion: 2:  | pepper: 15:g | garlic: 1:  | white sugar: 1.5:g ", "347, 19.9, 22.6, 21.4, 68, 1246", 105, "0110", "0001", "010000", "100", 0);

        addRecipe(db, "Beef Wellington", "This is a very easy recipe that I learned when I was living in England. Note that Beef Wellington should always be served with the center slightly pink. Enjoy!", "1.Preheat oven to 425 degrees F (220 degrees C). Place beef in a small baking dish, and spread with 2 tablespoons softened butter. Bake for 10 to 15 minutes, or until browned. Remove from pan, and allow to cool completely. Reserve pan juices. | 2.Melt 2 tablespoons butter in a skillet over medium heat. Saute onion and mushrooms in butter for 5 minutes. Remove from heat, and let cool. | 3.Mix together pate and 2 tablespoons softened butter, and season with salt and pepper. Spread pate over beef. Top with onion and mushroom mixture. | 4.Roll out the puff pastry dough, and place beef in the center. Fold up, and seal all the edges, making sure the seams are not too thick. Place beef in a 9x13 inch baking dish, cut a few slits in the top of the dough, and brush with egg yolk. | 5.Bake at 450 degrees F (230 degrees C) for 10 minutes, then reduce heat to 425 degrees F (220 degrees C) for 10 to 15 more minutes, or until pastry is a rich, golden brown. Set aside, and keep warm. | 6.Place all reserved juices in a small saucepan over high heat. Stir in beef stock and red wine; boil for 10 to 15 minutes, or until slightly reduced. Strain, and serve with beef.", "beef tenderloin: 2 1/2 pounds | butter, softened: 30g | butter: 30g | onion, chopped: 1 | sliced fresh mushrooms: 1/2 cup | liver pate: 2 ounces | butter, softened: 30g | salt and pepper to taste | frozen puff pastry, thawed: 1 (17.5 ounce) package | egg yolk, beaten: 1 | beef broth: 1 (10.5 ounce) can | red wine : 30g ; beef: 2.5:pound | butter: 90:g | onion: 1:  | mushroom: 5:  | egg: 1:  | beef broth: 10.5:ounce | red wine: 1:cup ", "744, 57.2, 29.6, 26.2, 131, 434", 60, "0110", "0001", "010000", "100", 0);

        addRecipe(db, "Burgundy Beef Stew", "This is a great comfort food for those cold winter days, and it's great tasting. This is my own recipe. I like to serve it in big pre-warmed soup bowls with salad and French bread.", "1.Cook bacon in a large skillet over medium-high heat until evenly browned, about 10 minutes. Remove bacon to a paper towel, retaining drippings in the skillet. Pour olive oil into reserved bacon drippings. | 2.Pour flour into a large sealable plastic bag; add the beef, seal, and shake to coat meat with flour. Cook and stir beef in the bacon drippings mixture until browned on all sides, 7 to 10 minutes. | 3.Crumble the bacon and add to the skillet. Pour beef stock and Burgundy wine over the beef mixture; bring to a boil, reduce heat to medium-low, and simmer until the beef is tender, about 1 hour. | 4.Stir carrots, potatoes, mushrooms, garlic, onion, marjoram, thyme, seasoned salt, salt, and black pepper into the beef mixture; continue cooking at a simmer until the vegetables are tender, 20 to 30 minutes.", "slice bacon: 1 | olive oil: 30g | all-purpose flour: 1/4 cup | London broil-cut beef, cut into chunks: 1 1/2 pounds | low-sodium beef stock: 2 cups | Burgundy wine: 1 cup | carrots, cut into chunks: 4 | potatoes, cut into chunks: 1 pound | mushrooms, sliced: 1/2 pound | garlic cloves, minced: 2 | onion, cut into chunks: 1 | dried marjoram: 3g | ground thyme: 2.2g | seasoned salt: 1.5g | salt: 1.35g | ground black pepper, or to taste: 1 pinch ; bacon: 1:pound | olive oil: 30:g | all-purpose flour: 15:g | beef: 1.5:pound | potato: 2:  | mushroom: 10:  | garlic: 2:  | onion: 1:  | thyme: 2.2:g | pepper: 15:g | Burgundy wine: 1:cup ", "472, 15.1, 43.3, 29.9, 55, 395", 120, "0110", "0001", "010000", "100", 0);

        addRecipe(db, "Greek-Style Beef Pita", "Stir-fried beef is stuffed into pitas and topped with your family's favorite veggies like cucumbers and olives.", "1.Stack beef steaks; cut lengthwise in half, then crosswise into 1-inch wide strips. Combine beef and lemon pepper in medium bowl. | 2.Heat 2 teaspoons oil in large nonstick skillet over medium-high heat until hot. Add 1/2 of beef; stir-fry 1 to 3 minutes or until outside surface of beef is no longer pink. (Do not overcook.) Remove from skillet. Repeat with remaining beef, adding remaining 1 teaspoon oil to skillet, if necessary. | 3.Spread hummus evenly in pita pockets. Fill with equal amounts of beef and toppings, as desired.", "beef sirloin tip steaks, cut 1/8 to 1/4-inch thick: 1 pound | lemon pepper: 30g | vegetable oil, divided: 15g | plain or seasoned hummus: 3/4 cup | whole wheat pita breads, cut crosswise in half: 4 ; beef: 1:pound | pepper: 30:g | vegetable oil: 0.5:cup | whole wheat pita bread: 4:  ", "414, 15.6, 41.5, 29.3, 49, 887", 20, "0010", "0100", "010000", "100", 0);

        addRecipe(db, "Pineapple Teriyaki Burgers", "Fresh ginger is a key ingredient for these tasty burgers.", "1.Preheat the oven's broiler and set the oven rack about 6 inches from the heat source. Spray a broiling rack with cooking spray. | 2.Place pineapple slices on a baking sheet and broil just until lightly browned, about 1 minute per side. Set the pineapple slices aside. | 3.Thoroughly mix ground beef, bread crumbs, water chestnuts, teriyaki sauce, egg, minced ginger, Asian seasoning blend, onion powder, and garlic powder in a bowl; divide in half and form 2 large patties. Place the patties onto the prepared broiling rack. | 4.Broil patties until burgers are browned and no longer pink inside, 5 to 8 minutes per side. An instant-read meat thermometer inserted into the middle of a burger should read at least 160 degrees F (70 degrees C). | 5.Place half the lettuce onto the bottom half of each bun; top with a burger and a slice of pineapple. Place bun tops on sandwiches and serve.", "cooking spray | slices canned pineapple, drained: 2 | ground beef: 3/4 pound | plain bread crumbs: 1/2 cup | sliced water chestnuts, drained and chopped: 1/2 (8 ounce) can | teriyaki sauce: 1/4 cup | egg: 1 | fresh ginger, minced: 1 (1 inch) piece | Asian seasoning blend: 4.5g | onion powder: 1.5g | garlic powder: 0.75g | shredded lettuce, divided: 1/2 cup | hamburger buns, split: 2 ; beef: 0.75:pound | pineapple: 2:  | egg: 1:  | teriyaki sauce: 0.25:cup ", "675, 26.3, 65.3, 41.9, 196, 2650", 45, "0110", "0010", "010010", "100", 0);

    }

    //for fragmentcooksteps
    public String getStep(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String stepToDisplay = "";
        String step = "";
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            step = cursor.getString(cursor.getColumnIndex("step"));
        }

        String[] steps = step.split("\\|");
        for (int i = 0; i < steps.length; i++) {
            stepToDisplay = stepToDisplay + steps[i] + "\n";
        }
        return stepToDisplay;
    }

    //for fragmentingredient
    public String getMaterial(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String materialToDisplay = "";
        String raw_material;
        String material = "";
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            raw_material = cursor.getString(cursor.getColumnIndex("material"));
            material = raw_material.split(";")[0];
        }

        String[] materials = material.split("\\|");

        for (int i = 0; i < materials.length; i++) {
            materialToDisplay = materialToDisplay + materials[i] + "\n";
        }
        return materialToDisplay;
    }

    //for fragmentnutrition
    public float[] getNutrition(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        String nutrition = "";
        float[] nutritionList = new float[6];

        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            nutrition = cursor.getString(cursor.getColumnIndex("nutrition"));

            nutrition.replace(" ", "");
            String[] nutritions = nutrition.split(",");

            for (int i = 0; i < nutritions.length; i++) {
                nutritionList[i] = Float.parseFloat(nutritions[i]);

            }


        }


        return nutritionList;
    }

    public int getCooktime(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        int cooktimeToDisplay = 0;
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            cooktimeToDisplay = cursor.getInt(cursor.getColumnIndex("cook_time"));
        }

        return cooktimeToDisplay;
    }

    public String getFat(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        String nutritionToDisplay = "";
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            nutritionToDisplay = cursor.getString(cursor.getColumnIndex("nutrition"));
        }
        String[] nutritions = nutritionToDisplay.split(",");
        String fat = nutritions[1];

        return fat;
    }

    public String getName(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        String nameToDisplay = "";
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            nameToDisplay = cursor.getString(cursor.getColumnIndex("name"));
        }

        return nameToDisplay;
    }

    public boolean[] checkKind(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        boolean[] boolKind = new boolean[4];
        String kindStr = "";
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            kindStr = cursor.getString(cursor.getColumnIndex("kind"));
            char[] kind = kindStr.toCharArray();
            for (int i = 0; i < 4; i++) {
                boolKind[i] = (kind[i] == '1');
            }
        }

        return boolKind;
    }

    public boolean[] checkTimes(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();

        boolean[] boolTimes = new boolean[4];
        String timesStr;
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            timesStr = cursor.getString(cursor.getColumnIndex("times"));
            char[] times = timesStr.toCharArray();
            for (int i = 0; i < 4; i++) {
                boolTimes[i] = (times[i] == '1');
            }

        }

        return boolTimes;
    }

    public boolean[] checkIngredients(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        boolean[] boolIngredients = new boolean[6];
        String ingredientsStr = "";
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            ingredientsStr = cursor.getString(cursor.getColumnIndex("ingredients"));
            char[] ingredients = ingredientsStr.toCharArray();

            for (int i = 0; i < 6; i++) {
                boolIngredients[i] = (ingredients[i] == '1');
            }
        }

        return boolIngredients;
    }

    public boolean[] checkType(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();


        boolean[] boolType = new boolean[3];
        String typeStr;
        Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(_id)});
        if (cursor.moveToFirst()) {
            typeStr = cursor.getString(cursor.getColumnIndex("types"));
            char[] type = typeStr.toCharArray();

            for (int i = 0; i < 3; i++) {
                boolType[i] = (type[i] == '1');
            }
        }

        return boolType;
    }

    public int[] searchKind(int[] list, String kind) {

        char[] ckind = kind.toCharArray();
        boolean[] real = new boolean[4];
        for (int i = 0; i < 4; i++) {
            real[i] = (ckind[i] == '1');
        }
        int[] a = new int[LISTNUM];
        int num = 0;
        for (int i = 0; i < list.length; i++) {
            boolean boolkind[] = checkKind(list[i]);
            boolean bool = (real[0] & boolkind[0]) |
                    (real[1] & boolkind[1]) |
                    (real[2] & boolkind[2]) |
                    (real[3] & boolkind[3]);

            if (bool) {
                a[num] = list[i];
                num++;

            }
        }
        return a;
    }

    public int[] searchTimes(int[] list, String times) {
        char[] ctimes = times.toCharArray();
        boolean[] real = new boolean[4];
        for (int i = 0; i < 4; i++) {
            real[i] = (ctimes[i] == '1');
        }
        int[] a = new int[LISTNUM];
        int num = 0;
        for (int i = 0; i < list.length; i++) {
            boolean boolkind[] = checkTimes(list[i]);
            boolean bool = (real[0] & boolkind[0]) |
                    (real[1] & boolkind[1]) |
                    (real[2] & boolkind[2]) |
                    (real[3] & boolkind[3]);
            if (bool) {
                a[num] = list[i];
                num++;
            }
        }
        return a;
    }

    public int[] searchIngredients(int[] list, String ingredients) {
        char[] cingredients = ingredients.toCharArray();
        boolean[] real = new boolean[6];
        for (int i = 0; i < 6; i++) {
            real[i] = (cingredients[i] == '1');
        }
        int[] a = new int[LISTNUM];
        int num = 0;
        for (int i = 0; i < list.length; i++) {
            boolean boolkind[] = checkIngredients(list[i]);
            boolean bool = (real[0] & boolkind[0]) |
                    (real[1] & boolkind[1]) |
                    (real[2] & boolkind[2]) |
                    (real[3] & boolkind[3]) |
                    (real[4] & boolkind[4]) |
                    (real[5] & boolkind[5]);

            if (bool) {
                a[num] = list[i];
                num++;

            }
        }
        return a;
    }

    public int[] searchTypes(int[] list, String types) {
        char[] ctypes = types.toCharArray();
        boolean[] real = new boolean[3];
        for (int i = 0; i < 3; i++) {
            real[i] = (ctypes[i] == '1');
        }
        int[] a = new int[LISTNUM];
        int num = 0;
        for (int i = 0; i < list.length; i++) {
            boolean boolkind[] = checkType(list[i]);
            boolean bool = (real[0] & boolkind[0]) |
                    (real[1] & boolkind[1]) |
                    (real[2] & boolkind[2]);

            if (bool) {
                a[num] = list[i];
                num++;

            }
        }
        return a;
    }

    public List<Map<String, Object>> readRecipe(int[] idList) {
        SQLiteDatabase db = this.getReadableDatabase();

        List list = new ArrayList<>();
        for (int i = 0; i < idList.length; i++) {
            Cursor cursor = db.rawQuery("select * from recipe where _id=?", new String[]{Integer.toString(idList[i])});
            if (cursor.moveToFirst()) {
                Map<String, Object> map = new HashMap<String, Object>();
                int _id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String step = cursor.getString(3);
                String material = cursor.getString(4);
                String nutrition = cursor.getString(5);
                int cook_time = cursor.getInt(6);
                String kind = cursor.getString(7);
                String times = cursor.getString(8);
                String ingredients = cursor.getString(9);
                String type = cursor.getString(10);
                int favourite = cursor.getInt(11);

                map.put("_id", _id);
                map.put("name", name);
                map.put("description", description);
                map.put("step", step);
                map.put("material", material);
                map.put("nutrition", nutrition);
                map.put("cook_time", cook_time);
                map.put("kind", kind);
                map.put("times", times);
                map.put("ingredients", ingredients);
                map.put("types", type);
                map.put("favourite", favourite);
                list.add(map);

            }
        }
        return list;
    }

    public int[] searchRecipeName(String searchKey) {
        SQLiteDatabase db = this.getReadableDatabase();


        int[] list = new int[LISTNUM];
        Cursor cursor = db.query("recipe", null, null, null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            if (compare(name, searchKey)) {
                list[i] = cursor.getInt(0);
                i++;
            }

        }
        return list;
    }

    public boolean compare(String name, String searchKey) {
        String[] nameSplited = name.split(" ");
        for (int i = 0; i < nameSplited.length; i++) {
            if (nameSplited[i].equalsIgnoreCase(searchKey)) return true;
        }
        return false;
    }
}
