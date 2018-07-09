package dynamicdrillers.happysingh.internshipdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import dmax.dialog.SpotsDialog;
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

    RecyclerView recyclerView;
    List<Worldpopulation> worldPopulationModelList;
    WorldPopulationListAdaptor worldPopulationListAdaptor;
    public static final int INTERNETCODE = 101;
    public static final int CONTACTCODE = 102;
    private Button extractContacts;
    FileWriter writer;
    File root ;
    File csvFIle;
    SpotsDialog spotsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getInternetPermissions();
        loadCountryList();
        enableRuntimePermissionForContacts();

        extractContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    spotsDialog.show();
                    saveContactsInZipFormat();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void saveContactsInZipFormat() throws IOException {



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

        spotsDialog.dismiss();
        Toast.makeText(this, "CSV Dowloaded", Toast.LENGTH_SHORT).show();


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

                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });


    }

    private void init() {

        recyclerView = (RecyclerView) findViewById(R.id.countryList_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        worldPopulationModelList = new ArrayList<>();
        extractContacts = (Button) findViewById(R.id.btn_extract_contacts);
        spotsDialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).build();
        root = Environment.getExternalStorageDirectory();
        csvFIle = new File(root, "contacts.csv");
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
}
