package com.samsunguet.sev_user.mycloud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.samsunguet.sev_user.mycloud.IntroApp.example.introApp.Intro;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static String URL_DEFAULT = "http://107.113.190.176";
    ListView listview;
    FloatingActionButton fab;
    ImageView imgHamburgerButton, imgSearchButton, imgMoreButtonInActivity;
    PullToRefreshView mPullToRefreshView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", false);
        boolean isLogin = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isLogIn", false);

        //REGISTERY
        listview = (ListView) findViewById(R.id.listview);
        fab = (FloatingActionButton) findViewById(R.id.floatButton);
        imgHamburgerButton = (ImageView) findViewById(R.id.imgHamburgerButton);
        imgSearchButton = (ImageView) findViewById(R.id.imgSearchButton);
        imgMoreButtonInActivity = (ImageView) findViewById(R.id.imgMoreButtonInActivity);

//CHECK IS FIRST RUN TO SHOW INTRO APP   & SHOW LOGIN
        if (isFirstRun==false) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).commit();
            Intent intro = new Intent(getBaseContext(), Intro.class);
            startActivity(intro);
            finish();
        } else if (!isLogin) {
            Intent login = new Intent(getBaseContext(), Login.class);
            startActivity(login);
            finish();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogIn", false).commit();

        //attach to listview
        fab.attachToListView(listview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(MainActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.custom_add_file_dialog))
                        .setExpanded(true)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final DialogPlus dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.imgFolderInDialog:
                                        Toast.makeText(MainActivity.this, "Click folder", Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.imgUploadFileInDialog:
//                                        Toast.makeText(MainActivity.this, "Click Upload", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                                        // This works if you defined the intent filter
                                        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                                        // Set these depending on your use case. These are the defaults.
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                                        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                                        // Configure initial directory by specifying a String.
                                        // You could specify a String like "/storage/emulated/0/", but that can
                                        // dangerous. Always use Android's API calls to get paths to the SD-card or
                                        // internal memory.
                                        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                                        startActivityForResult(i, 1);
                                        break;
                                    case R.id.imgUploadPhotoInDialog:
                                        Toast.makeText(MainActivity.this, "Click Camera", Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.imgSyncContact:
                                        Toast.makeText(MainActivity.this, "Click Sync Contact", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }

                        })
                        .create();
                dialog.show();

            }
        });


        //ADD ITEM TO DRAWER
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home");
        item1.withBadge("19").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));

        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Offline File");
        item2.withIcon(R.drawable.icon_photo);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Setting");
        item3.withIcon(R.drawable.icon_setting);
        item3.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
                return true;
            }
        });

        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withName("About");
        item4.withIcon(R.drawable.icon_about);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.icon_minion))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4
                )
                .build();


        //SET ACTION
        imgHamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                finish();
            }
        });

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setRefreshStyle(PullToRefreshView.STYLE_SUN);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }
//SET ACTION PULL TO REFRESH


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                // Do something with the URI
            }
        }
    }

}
