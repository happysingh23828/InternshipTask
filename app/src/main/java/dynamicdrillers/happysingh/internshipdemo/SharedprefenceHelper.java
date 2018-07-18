package dynamicdrillers.happysingh.internshipdemo;

import android.content.Context;
import android.content.SharedPreferences;

import dynamicdrillers.happysingh.internshipdemo.models.User;

public class SharedprefenceHelper {

    private static Context mCtx;
    private static SharedprefenceHelper mInstance;
    public static final String SharedprefenceName = "USER_DATA";

    public SharedprefenceHelper( Context context) {

        mCtx = context;
    }

    public static synchronized SharedprefenceHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedprefenceHelper(context);
        }
        return mInstance;
    }


    public String  checkUserLoggedIn(){

        SharedPreferences sharedPreference = mCtx.getSharedPreferences(SharedprefenceName, Context.MODE_PRIVATE);
        return sharedPreference.getString("userflag","");

    }

    public void  setUserFlag(){
        SharedPreferences sharedPreference = mCtx.getSharedPreferences(SharedprefenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString("userflag","true");
        editor.apply();
        editor.commit();

    }

    public  void setUserDetails(User user){
        SharedPreferences sharedPreference = mCtx.getSharedPreferences(SharedprefenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString("username",user.getUsername());
        editor.putString("password",user.getPassword());
        editor.putString("id",user.getId());
        editor.apply();
        editor.commit();
    }
    public void removeUser(){
        SharedPreferences sharedPreference = mCtx.getSharedPreferences(SharedprefenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString("userflag","");
        editor.apply();
        editor.commit();
    }



}
