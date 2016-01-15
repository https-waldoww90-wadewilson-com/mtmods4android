package com.rubenwardy.minetestmodmanager.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A list of mods. May represent a folder in the file system or
 */
public class ModList {
    public enum ModListType {
        EMLT_STORE,
        EMLT_PATH
    }

    public final String uri;
    public final ModListType type;
    public boolean valid;
    public List<Mod> mods = new ArrayList<Mod>();
    public Map<String, Mod> mods_map = new HashMap<String, Mod>();

    public ModList(ModListType type, String uri) {
        this.type = type;
        this.uri = uri;
        this.valid = true;
    }

    public void add(Mod mod) {
        mods.add(mod);
        mods_map.put(mod.name, mod);
    }
}
