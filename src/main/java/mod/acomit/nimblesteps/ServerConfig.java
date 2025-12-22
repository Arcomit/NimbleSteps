package mod.acomit.nimblesteps;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class CommonConfig {
    protected static final ModConfigSpec SPEC;

    public static ModConfigSpec.BooleanValue ENABLE_OMNI_SPRINT;
    public static ModConfigSpec.BooleanValue ENABLE_CRAWL;
    public static ModConfigSpec.BooleanValue ENABLE_SHALLOW_SWIMMING;
    public static ModConfigSpec.BooleanValue ENABLE_SWIMMING_BOOST;
    public static ModConfigSpec.BooleanValue ENABLE_FREESTYLE;
    public static ModConfigSpec.DoubleValue WALK_SPEED_MULTIPLIER;
    public static ModConfigSpec.DoubleValue SPRINT_SPEED_MULTIPLIER;
    public static ModConfigSpec.DoubleValue WALK_STEP_HEIGHT;
    public static ModConfigSpec.DoubleValue SPRINT_STEP_HEIGHT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder
                .comment("Locomotion settings let you change all options related to movement on the ground.")
                .translation("nimblesteps.configuration.locomotion_settings")
                .push("locomotion_settings");

        // 全向疾跑功能
        ENABLE_OMNI_SPRINT = builder
                .comment("Enable omnidirectional sprinting (default: true)")
                .translation("nimblesteps.configuration.locomotion_settings.enable_omni_sprint")
                .define("enableOmniSprint", true);
        // 爬行功能
        ENABLE_CRAWL = builder
                .comment("Enable crawl (default: true)")
                .translation("nimblesteps.configuration.locomotion_settings.enable_crawl")
                .define("enableCrawl", true);
        // 行走速度系数
        WALK_SPEED_MULTIPLIER = builder
                .comment("Walking speed multiplier (default: 1.3 = 30% increase)")
                .translation("nimblesteps.configuration.locomotion_settings.walk_speed_multiplier")
                .defineInRange("walkSpeedMultiplier", 1.3, 1.0, 5.0);
        // 疾跑速度系数
        SPRINT_SPEED_MULTIPLIER = builder
                .comment("Sprinting speed multiplier (default: 1.3 = 30% increase)")
                .translation("nimblesteps.configuration.locomotion_settings.sprint_speed_multiplier")
                .defineInRange("sprintSpeedMultiplier", 1.3, 1.0, 5.0);
        // 行走步高
        WALK_STEP_HEIGHT = builder
                .comment("Walking step height in blocks (default: 0.6, vanilla: 0.6)")
                .translation("nimblesteps.configuration.locomotion_settings.walk_step_height")
                .defineInRange("walkStepHeight", 0.6, 0.6, 1.0);
        // 疾走步高
        SPRINT_STEP_HEIGHT = builder
                .comment("Sprinting step height in blocks (default: 1.0, vanilla: 0.6)")
                .translation("nimblesteps.configuration.locomotion_settings.sprint_step_height")
                .defineInRange("sprintStepHeight", 1.0, 0.6, 1.0);

        builder.pop();

        builder
                .comment("Swimming settings let you change all options related to movement on the water.")
                .translation("nimblesteps.configuration.swimming_settings")
                .push("swimming_settings");

        // 浅水游泳启用在水中疾跑时进入游泳状态
        ENABLE_SHALLOW_SWIMMING = builder
                .comment("Enables swimming while sprinting through water (default: true)")
                .translation("nimblesteps.configuration.swimming_settings.enable_shallow_swimming")
                .define("enableShallowSwimming", true);
        // 水中推进
        ENABLE_SWIMMING_BOOST = builder
                .comment("Enables speed boost while swimming (default: true)")
                .translation("nimblesteps.configuration.swimming_settings.enable_swimming_boost")
                .define("enableSwimmingBoost", true);
        // 自由泳
        ENABLE_FREESTYLE = builder
                .comment("Enables freestyle swimming (default: true)")
                .translation("nimblesteps.configuration.swimming_settings.enable_freestyle")
                .define("enableFreestyle", true);

        builder.pop();

        SPEC = builder.build();
    }

    public static boolean enableOmniSprint;
    public static boolean enableCrawl;
    public static boolean enableShallowSwimming;
    public static boolean enableSwimmingBoost;
    public static boolean enableFreestyle;
    public static double walkSpeedMultiplier;
    public static double sprintSpeedMultiplier;
    public static double walkStepHeight;
    public static double sprinStepHeight;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        enableOmniSprint = ENABLE_OMNI_SPRINT.get();
        enableCrawl = ENABLE_CRAWL.get();
        enableSwimmingBoost = ENABLE_SWIMMING_BOOST.get();
        enableFreestyle = ENABLE_FREESTYLE.get();
        enableShallowSwimming = ENABLE_SHALLOW_SWIMMING.get();
        walkSpeedMultiplier = WALK_SPEED_MULTIPLIER.get();
        sprintSpeedMultiplier = SPRINT_SPEED_MULTIPLIER.get();
        walkStepHeight = WALK_STEP_HEIGHT.get();
        sprinStepHeight = SPRINT_STEP_HEIGHT.get();
    }
}
