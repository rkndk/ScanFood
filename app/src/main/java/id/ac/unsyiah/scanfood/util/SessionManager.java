package id.ac.unsyiah.scanfood.util;

/**
 * Created by include on 03/06/17.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.ac.unsyiah.scanfood.LoginActivity;
import id.ac.unsyiah.scanfood.MainActivity;
import id.ac.unsyiah.scanfood.PartnerActivity;
import id.ac.unsyiah.scanfood.model.Cart;
import id.ac.unsyiah.scanfood.model.Table;
import id.ac.unsyiah.scanfood.model.Transaction;
import id.ac.unsyiah.scanfood.model.User;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ScanFoodPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_SCAN = "IsScanned";
    private static final String IS_TRANSACTION = "IsTransaction";

    // user_session (make variable public to access from outside)
    public static final String KEY_USER = "user_session";
    public static final String KEY_CART = "cart_session";
    public static final String KEY_TRANSACTION = "transaction_session";
    public static final String KEY_SCAN = "scan_session";




    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(User user){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing user_session in pref
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);

        // Storing api_token in pref
        //editor.putString(KEY_USER, api_token);

        // commit changes
        editor.commit();

        // After login redirect user to Main Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * *//*
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // api_token
        user.put(KEY_USER, pref.getString(KEY_USER, null));

        // return user
        return user;
    }*/

     public User getUserDetails(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_USER, null);;
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Main Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }




    public void setScan(Table table){
        // Storing login value as TRUE
        editor.putBoolean(IS_SCAN, true);

        Gson gson = new Gson();
        String json = gson.toJson(table);
        editor.putString(KEY_SCAN, json);

        editor.commit();

        // After login redirect user to PartnerActivity
        Intent i = new Intent(_context, PartnerActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void updateScan(Table table){
        editor.putBoolean(IS_SCAN, true);

        Gson gson = new Gson();
        String json = gson.toJson(table);
        editor.putString(KEY_SCAN, json);

        editor.commit();
    }

    public Table getScan(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_SCAN, null);
        Table table = gson.fromJson(json, Table.class);
        return table;
    }

    public void clearScan(){

        clearCart();
        clearTransaction();

        editor.remove(IS_SCAN);
        editor.remove(KEY_SCAN);
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isScanned(){
        return pref.getBoolean(IS_SCAN, false);
    }

    public void checkScan(){
        if(this.isScanned()){
            Intent i = new Intent(_context, PartnerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }




    public ArrayList<Cart> getCart(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_CART, null);
        return gson.fromJson(json, new TypeToken<ArrayList<Cart>>(){}.getType());
    }

    public void addCart(Cart cart){
        ArrayList<Cart> listCart = getCart();
        if(listCart == null){
            listCart = new ArrayList<Cart>();
        }

        listCart.add(cart);

        Gson gson = new Gson();
        String json = gson.toJson(listCart);
        editor.putString(KEY_CART, json);
        editor.commit();
    }

    public void clearCart(){
        editor.remove(KEY_CART);
        editor.commit();
    }

    public void setCart(ArrayList<Cart> listCart){
        clearCart();
        Gson gson = new Gson();
        String json = gson.toJson(listCart);
        editor.putString(KEY_CART, json);
        editor.commit();
    }





    public Transaction getTransaction(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_TRANSACTION, null);
        return gson.fromJson(json, new TypeToken<Transaction>(){}.getType());
    }

    public void setTransaction(Transaction transaction){
        clearTransaction();

        // Storing transaction value as TRUE
        editor.putBoolean(IS_TRANSACTION, true);

        Gson gson = new Gson();
        String json = gson.toJson(transaction);
        editor.putString(KEY_TRANSACTION, json);
        editor.commit();
    }

    public void clearTransaction(){
        editor.remove(IS_TRANSACTION);
        editor.remove(KEY_TRANSACTION);
        editor.commit();
    }

    public boolean isTransaction(){
        return pref.getBoolean(IS_TRANSACTION, false);
    }
}