package com.youngguns.hybridmobs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    static final ForgeConfigSpec COMMON_SPEC;
    private static final Config.Common COMMON;
    public static int zomeletonSpawnWeight;
    public static List<String> dimensionBlacklist;

    private static class Common {
        private final ForgeConfigSpec.IntValue zomeletonSpawnWeight;
        private final ForgeConfigSpec.ConfigValue<List<String>> dimensionBlacklist;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common configuration settings").push("common");
            this.zomeletonSpawnWeight = builder
                    .comment("Zomeletons spawn weight", "Requires game restart")
                    .worldRestart()
                    .defineInRange("zomeletonSpawnWeight", 4, 0, 100);
            this.dimensionBlacklist = builder
                    .comment("Zomeletons will not spawn in the provided dimensions, using their registry name",
                            "Example - \"minecraft:overworld\", \"minecraft:the_nether\" ")
                    .define("dimensionBlacklist", new ArrayList<>());
            builder.pop();
        }
    }

    static {
        Pair<Config.Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Config.Common::new);
        COMMON_SPEC = commonPair.getRight();
        COMMON = commonPair.getLeft();
    }

    public static void bake(ForgeConfigSpec spec) {
        if (spec == COMMON_SPEC) {
            zomeletonSpawnWeight = COMMON.zomeletonSpawnWeight.get();
            dimensionBlacklist = COMMON.dimensionBlacklist.get();
        }
    }
}
