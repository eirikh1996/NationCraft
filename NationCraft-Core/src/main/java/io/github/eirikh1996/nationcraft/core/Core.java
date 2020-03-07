package io.github.eirikh1996.nationcraft.core;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;

public class Core {
    private static NationCraftMain main;
    public static void initialize(NationCraftMain main) {
        Core.main = main;
    }

    public static NationCraftMain getMain() {
        return main;
    }
}
