package dynamicdrillers.happysingh.internshipdemo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;

import dmax.dialog.SpotsDialog;
import dynamicdrillers.happysingh.internshipdemo.App;
import dynamicdrillers.happysingh.internshipdemo.R;
import dynamicdrillers.happysingh.internshipdemo.SharedprefenceHelper;
import dynamicdrillers.happysingh.internshipdemo.adaptors.WorldPopulationListAdaptor;
import dynamicdrillers.happysingh.internshipdemo.models.WorldPopulationModel;
import dynamicdrillers.happysingh.internshipdemo.models.Worldpopulation;
import dynamicdrillers.happysingh.internshipdemo.retrofit.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private static final int BUFFER = 2048 ;
    RecyclerView recyclerView;
    List<Worldpopulation> worldPopulationModelList;
    WorldPopulationListAdaptor worldPopulationListAdaptor;
    public static final int INTERNETCODE = 101;
    public static final int CONTACTCODE = 102;
    private Button extractContacts,logout;
    FileWriter writer;
    File root;
    File csvFIle;
    SpotsDialog spotsDialog;
    LinearLayout linearLayout;
    SharedprefenceHelper sharedprefenceHelper;

    @Inject
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplication()).getNetworkComponent().inject(this);

        init();
        getInternetPermissions();
        loadCountryListWithDagger();
        enableRuntimePermissionForContacts();
        isUSerLoggedIn();

        extractContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotsDialog.show();
                downloadZipWithRxJava();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {

        sharedprefenceHelper.removeUser();
        Toast.makeText(this, "User Logout Successfuly..", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

    private void downloadZipWithRxJava() {

        Observable<File> callFileObservable = Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return saveContactsInZipFormat();
            }
        });

        callFileObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {

                       Snackbar.make(linearLayout,"Zip Has Been Dowloaded ..... ",Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private File saveContactsInZipFormat() throws IOException {

        writer = new FileWriter(csvFIle);
        writer.append("Name");
        writer.append(",");
        writer.append("Phone Number\n");

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String name, phonenumber;

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            writer.write(String.format("%s\t", name));
            writer.append(",");
            writer.write(String.format("%s\n", phonenumber));
        }
            writer.flush();
        writer.close();
        cursor.close();
        zip(new String[]{csvFIle.getPath()},"/Contacts.zip");
        spotsDialog.dismiss();
        return csvFIle;
    }

    private void getInternetPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNETCODE);
        }

    }

    public void enableRuntimePermissionForContacts() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CONTACTCODE);

        }
    }

    private void loadCountryList() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.androidbegin.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Observable<WorldPopulationModel> observable = apiInterface.getCountryList().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<WorldPopulationModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WorldPopulationModel worldPopulationModel) {

                for (int i = 0; i < worldPopulationModel.getWorldpopulation().size(); i++) {

                    Worldpopulation worldpopulation = new Worldpopulation(
                            worldPopulationModel.getWorldpopulation().get(i).getRank()
                            , worldPopulationModel.getWorldpopulation().get(i).getCountry()
                            , worldPopulationModel.getWorldpopulation().get(i).getPopulation()
                            , worldPopulationModel.getWorldpopulation().get(i).getFlag());

                    worldPopulationModelList.add(worldpopulation);
                }


                worldPopulationListAdaptor = new WorldPopulationListAdaptor(worldPopulationModelList, MainActivity.this);
                recyclerView.setAdapter(worldPopulationListAdaptor);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    private  void loadCountryListWithDagger(){

        Observable<WorldPopulationModel> observable = retrofit.create(ApiInterface.class).getCountryList().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<WorldPopulationModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WorldPopulationModel worldPopulationModel) {

                for (int i = 0; i < worldPopulationModel.getWorldpopulation().size(); i++) {

                    Worldpopulation worldpopulation = new Worldpopulation(
                            worldPopulationModel.getWorldpopulation().get(i).getRank()
                            , worldPopulationModel.getWorldpopulation().get(i).getCountry()
                            , worldPopulationModel.getWorldpopulation().get(i).getPopulation()
                            , worldPopulationModel.getWorldpopulation().get(i).getFlag());

                    worldPopulationModelList.add(worldpopulation);
                }


                worldPopulationListAdaptor = new WorldPopulationListAdaptor(worldPopulationModelList, MainActivity.this);
                recyclerView.setAdapter(worldPopulationListAdaptor);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });



    }

    public void zip(String[] _files, String zipFileName) {

        
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void init() {

        recyclerView = (RecyclerView) findViewById(R.id.countryList_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        worldPopulationModelList = new ArrayList<>();
        extractContacts = (Button) findViewById(R.id.btn_extract_contacts);
        spotsDialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).build();
        root = Environment.getExternalStorageDirectory();
        csvFIle = new File(root, "contacts.csv");
        linearLayout = (LinearLayout)findViewById(R.id.linearlayoutmain);
        sharedprefenceHelper = SharedprefenceHelper.getInstance(this);
        logout = findViewById(R.id.btn_logout);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case INTERNETCODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisson Granted", Toast.LENGTH_SHORT).show();
                }
                break;

            case CONTACTCODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisson Granted", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;

        }


    }

    public  void   isUSerLoggedIn(){

        if(sharedprefenceHelper.checkUserLoggedIn().equals("")){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }
}
