package com.rubenwardy.minetestmodmanager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rubenwardy.minetestmodmanager.manager.Mod;
import com.rubenwardy.minetestmodmanager.manager.ModEventReceiver;
import com.rubenwardy.minetestmodmanager.manager.ModList;
import com.rubenwardy.minetestmodmanager.manager.ModManager;

/**
 * A mFragment representing a single Mod detail screen.
 * This mFragment is either contained in a {@link ModListActivity}
 * in two-pane mode (on tablets) or a {@link ModDetailActivity}
 * on handsets.
 */
public class ModDetailFragment extends Fragment {
    /**
     * The mFragment argument representing the item ID that this mFragment
     * represents.
     */
    public static final String ARG_MOD_LIST = "mod_list";
    public static final String ARG_MOD_NAME = "mod_name";

    private Mod mItem;

    /**
     * Mandatory empty constructor for the mFragment manager to instantiate the
     * mFragment (e.g. upon screen orientation changes).
     */
    public ModDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOD_NAME) &&
                getArguments().containsKey(ARG_MOD_LIST)) {
            // Load the dummy content specified by the mFragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String name = getArguments().getString(ARG_MOD_NAME);
            String listname = getArguments().getString(ARG_MOD_LIST);
            ModManager modman = new ModManager();
            ModList list = modman.get(listname);
            if (list == null) {
                mItem = new Mod(Mod.ModType.EMT_INVALID,
                        "", "invalid", "Invalid Path", "This mod location isn't loaded. Please try again.");
            } else {
                mItem = list.mods_map.get(name);
                if (mItem == null) {
                    list.valid = false;
                    mItem = new Mod(Mod.ModType.EMT_INVALID,
                            "", "invalid", "Invalid Mod", "There is no mod at this location!");
                }
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mod_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.mod_desc)).setText(mItem.desc);
        }

        Button btn = (Button) rootView.findViewById(R.id.uninstall);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                ModManager modman = new ModManager();
                if (modman.uninstallMod(mItem)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ModEventReceiver.PARAM_ACTION, ModEventReceiver.ACTION_UNINSTALL);
                    bundle.putString(ModEventReceiver.PARAM_DEST, mItem.listname);
                    bundle.putString(ModEventReceiver.PARAM_MODNAME, mItem.name);
                    Snackbar.make(view, "Uninstalled mod.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    ((ModEventReceiver) getActivity()).onModEvent(bundle);
                } else {
                    Snackbar.make(view, "Failed to uninstall mod for some reason.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return rootView;
    }
}
