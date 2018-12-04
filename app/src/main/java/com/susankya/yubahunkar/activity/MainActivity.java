package com.susankya.yubahunkar.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.fragment.EntertainmentFragment;
import com.susankya.yubahunkar.fragment.HomeFragment;
import com.susankya.yubahunkar.fragment.EconomicsFragment;
import com.susankya.yubahunkar.fragment.NewsFragment;
import com.susankya.yubahunkar.fragment.ThoughtFragment;
import com.susankya.yubahunkar.fragment.SportsFragment;
import com.susankya.yubahunkar.fragment.ValleyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setTabTextColors(getResources().getColor(android.R.color.white), getResources().getColor(android.R.color.white));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        tabLayout.setupWithViewPager(viewPager);

        if (getIntent().getBooleanExtra("isFromNotification", false)) {
            int id = getIntent().getIntExtra("post_id", 0);
            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    public static class TabsPagerAdapter extends FragmentStatePagerAdapter {

        private TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0: {
                    return new HomeFragment();
                }
                case 1: {
                    return new NewsFragment();
                }
                case 2: {
                    return new EconomicsFragment();
                }
                case 3: {
                    return new ThoughtFragment();
                }
                case 4: {
                    return new SportsFragment();
                }
                case 5: {
                    return new ValleyFragment();
                }
                case 6: {
                    return new EntertainmentFragment();
                }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 7;
        }

        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0: {
                    return "गृहपृष्ठ";
                }
                case 1: {
                    return "समाचार";
                }
                case 2: {
                    return "अर्थ / वाणिज्य";
                }
                case 3: {
                    return "विचार";
                }
                case 4: {
                    return "खेलकुद";
                }
                case 5: {
                    return "उपत्यका";
                }
                case 6: {
                    return "मनोरञ्जन";
                }
                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_home: {
                viewPager.setCurrentItem(0, true);
                break;
            }
            case R.id.nav_news: {
                viewPager.setCurrentItem(1, true);
                break;
            }
            case R.id.nav_economics: {
                viewPager.setCurrentItem(2, true);
                break;
            }
            case R.id.nav_thoughts: {
                viewPager.setCurrentItem(3, true);
                break;
            }
            case R.id.nav_sports: {
                viewPager.setCurrentItem(4, true);
                break;
            }
            case R.id.nav_valley: {
                viewPager.setCurrentItem(5, true);
                break;
            }
            case R.id.nav_entertainment: {
                viewPager.setCurrentItem(6, true);
                break;
            }
            case R.id.nav_others: {
                Intent intent = new Intent(getApplicationContext(), OthersActivity.class);
                intent.putExtra("key", "others");
                startActivity(intent);
                break;
            }
            case R.id.nav_saved_post: {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("key", "saved_post");
                startActivity(intent);
                break;
            }
            case R.id.nav_share: {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.susankya.yubahunkar");
                intent.setType("text/plain");
                startActivity(intent);

                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_about: {

                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("key", "about");
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
