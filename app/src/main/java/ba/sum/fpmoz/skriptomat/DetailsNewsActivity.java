package ba.sum.fpmoz.skriptomat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class DetailsNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_details_news);

        ImageView imageView = findViewById(R.id.imgUrlTv);
        TextView title_tv = findViewById(R.id.mTitle);
        TextView alias_tv = findViewById(R.id.mAlias);

        Bundle bundle= getIntent().getExtras();

        String mTitle = bundle.getString("title");
        String mAlias = bundle.getString("alias");
        title_tv.setText(mTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alias_tv.setText(Html.fromHtml(mAlias, Html.FROM_HTML_MODE_COMPACT));
        } else {
            alias_tv.setText(Html.fromHtml(mAlias));
        }
        // alias_tv.setText(mAlias);
        String imgUrl = bundle.getString("image");
        Picasso.get()
                .load(imgUrl)
                .error(R.drawable.home_img3)
                .into(imageView);
    }
}