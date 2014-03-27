package org.djodjo.jjson.exampleapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    private FragmentPutter putter = new FragmentPutter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
       putter.put(position, R.id.container);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_go) {
            ((BlankFragment)getFragmentManager().findFragmentById(R.id.container)).execGo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used for non argument Fragments
     */
    public class FragmentPutter {

        public static final String JSON_PARSE = "json_parse";
        public static final String JSON_POJO = "json_pojo";
        public static final String JSON_WRAPPERS = "json_wrappers";
        public static final String JSON_SCHEMA = "json_schema";
        public static final String SCHEMA_FORM_BUILDER = "schema_form_builder";
        public static final String JSON_FORM_FITTER = "json_form_fitter";
        //form2json
        public static final String JSONFY = "jsonfy";


        HashMap<String, Class> fragments;

        public FragmentPutter() {
            fragments = new HashMap<String, Class>();
            fragments.put(JSON_PARSE, JsonParseFragment.class);
            fragments.put(JSON_POJO, JsonPojoFragment.class);
            fragments.put(JSON_WRAPPERS, JsonWrappersFragment.class);
            fragments.put(JSON_SCHEMA, JsonSchemaFragment.class);
            fragments.put(SCHEMA_FORM_BUILDER, SchemaFormBuilderFragment.class);
            fragments.put(JSON_FORM_FITTER, JsonFormFitterFragment.class);
            fragments.put(JSONFY, JsonfyFragment.class);
        }

        public Fragment put(int i, int container) {
            String tag = "";
            switch (i) {
                case 0: tag = JSON_PARSE;  break;
                case 1: tag = JSON_POJO; break;
                case 2: tag = JSON_WRAPPERS; break;
                case 3: tag = JSON_SCHEMA; break;
                case 4: tag = SCHEMA_FORM_BUILDER; break;
                case 5: tag = JSON_FORM_FITTER; break;
                case 6: tag = JSONFY; break;
            }

            return put(tag, container);
        }

        public Fragment put(String tag, int container) {
            FragmentManager fragmentManager = getFragmentManager();

            Fragment fragment= fragmentManager.findFragmentByTag(tag);
            if(fragment == null) try {
                fragment = (Fragment)fragments.get(tag).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            fragmentManager.beginTransaction()
                    .replace(container, fragment)
                    .commit();

            return fragment;
        }
    }


}
