package com.susankya.yubahunkar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.adapter.OthersAdapter;
import com.susankya.yubahunkar.model.OthersModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OthersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new OthersAdapter(this, OtherListItems()));
    }

    private List<OthersModel> OtherListItems() {

        List<OthersModel> list = new ArrayList<>();

        list.add(new OthersModel("कोसेली", 22));
        list.add(new OthersModel("विश्व", 23));
        list.add(new OthersModel("ब्लग", 25));
        list.add(new OthersModel("जीवनशैली", 29));
        list.add(new OthersModel("साहित्य / विविध", 30));
        list.add(new OthersModel("विज्ञान र प्रविधि", 31));
        list.add(new OthersModel("स्वास्थ्य", 32));
        list.add(new OthersModel("भिडियो", 33));
        list.add(new OthersModel("पाठक मञ्च", 34));
        list.add(new OthersModel("कुराकानी", 35));
        list.add(new OthersModel("कला", 36));
        list.add(new OthersModel("प्रवास", 26));
        list.add(new OthersModel("फिचर", 27));
        list.add(new OthersModel("फोटोफिचर", 28));

        return list;
    }
}
