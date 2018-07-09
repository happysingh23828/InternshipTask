package dynamicdrillers.happysingh.internshipdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageFullViewActivity extends AppCompatActivity {

    private  String imageLink;
    private ImageView flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_view);
        imageLink = getIntent().getStringExtra("flag");
        flag = (ImageView)findViewById(R.id.full_image);
        Picasso.get().load(imageLink).into(flag);

    }
}
