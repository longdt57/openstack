package com.samsunguet.sev_user.mycloud;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.samsunguet.sev_user.mycloud.IntroApp.example.introApp.Intro;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);
        Boolean isLogin = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isLogIn",false);
        if(isFirstRun){
            System.out.println("DA CHAY VAO DAY NHE");
            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isFirstRun",false).commit();
            Intent intro = new Intent(getBaseContext(),Intro.class);
            startActivity(intro);
            finish();
        }

        else if(!isLogin){
            Intent login = new Intent(getBaseContext(),Login.class);
            startActivity(login);
            finish();
        }

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home");
        item1.withBadge("19").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));

        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Offline File");
        item2.withIcon(R.drawable.icon_photo);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Setting");
        item3.withIcon(R.drawable.icon_setting);
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
//        new DrawerBuilder().withActivity(this).build();
    }
}
