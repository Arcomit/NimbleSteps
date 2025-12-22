package mod.acomit.nimblesteps;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = NimbleStepsMod.MODID)
public class ServerConfig {
    protected static final ModConfigSpec SPEC;

    public static ModConfigSpec.BooleanValue ENABLE_OMNI_SPRINT;
    public static ModConfigSpec.BooleanValue ENABLE_CRAWL;
    public static ModConfigSpec.BooleanValue ENABLE_SLIDE_AND_EVADE;
    public static ModConfigSpec.IntValue SLIDE_AND_EVADE_COOLDOWN;
    public static ModConfigSpec.BooleanValue ENABLE_TAP_STRAFING;
    public static ModConfigSpec.BooleanValue ENABLE_LANDINH_ROLL;
    public static ModConfigSpec.IntValue LANDING_ROLL_WINDOW;
    public static ModConfigSpec.DoubleValue WALK_SPEED_MULTIPLIER;
    public static ModConfigSpec.DoubleValue SPRINT_SPEED_MULTIPLIER;
    public static ModConfigSpec.DoubleValue WALK_STEP_HEIGHT;
    public static ModConfigSpec.DoubleValue SPRINT_STEP_HEIGHT;
    public static ModConfigSpec.DoubleValue SAFE_FALL_HEIGHT;

    public static ModConfigSpec.BooleanValue ENABLE_SHALLOW_SWIMMING;
    public static ModConfigSpec.BooleanValue ENABLE_SWIMMING_BOOST;
    public static ModConfigSpec.IntValue SWMMING_BOOST_COOLDOWN;
    public static ModConfigSpec.BooleanValue ENABLE_FREESTYLE;

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
        // 滑铲和闪避功能
        ENABLE_SLIDE_AND_EVADE = builder
                .comment("Enable slide and evade (default: true)")
                .translation("nimblesteps.configuration.locomotion_settings.enable_slide_and_evade")
                .define("enableSlideAndEvade", true);
        // 滑铲和闪避冷却时间
        SLIDE_AND_EVADE_COOLDOWN = builder
                .comment("Slide and evade cooldown in ticks (default: 10)")
                .translation("nimblesteps.configuration.locomotion_settings.slide_and_evade_cooldown")
                .defineInRange("slideAndEvadeCooldown", 10, 0 , Integer.MAX_VALUE);
        // 滑铲空中变向功能
        ENABLE_TAP_STRAFING = builder
                .comment("Enable turning without slowdown while sliding in the air (default: true)")
                .translation("nimblesteps.configuration.locomotion_settings.enable_tap_strafing")
                .define("enableTapStrafing", true);
        // 着陆翻滚功能
        ENABLE_LANDINH_ROLL = builder
                .comment("Enable landing roll (default: true)")
                .translation("nimblesteps.configuration.locomotion_settings.enable_landing_roll")
                .define("enableLandingRoll", true);
        // 着陆翻滚窗口
        LANDING_ROLL_WINDOW = builder
                .comment("Landing roll window in ticks (default: 6)")
                .translation("nimblesteps.configuration.locomotion_settings.landing_roll_window")
                .defineInRange("landingRollWindow", 6, 0 , Integer.MAX_VALUE);
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
        // 安全掉落高度
        SAFE_FALL_HEIGHT = builder
                .comment("Safe fall height (default: 6.0)")
                .translation("nimblesteps.configuration.locomotion_settings.safe_fall_height")
                .defineInRange("safeFallHeight", 6.0, -3.0, Double.MAX_VALUE);

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
        // 水中推进冷却
        SWMMING_BOOST_COOLDOWN = builder
                .comment("Swimming boost cooldown in ticks (default: 10)")
                .translation("nimblesteps.configuration.swimming_boost_cooldown")
                .defineInRange("swimmingBoostCooldown", 10, 0 , Integer.MAX_VALUE);
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
    public static boolean enableSlideAndEvade;
    public static int slideAndEvadeCooldown;
    public static boolean enableTapStrafing;
    public static boolean enableLandingRoll;
    public static int landingRollWindow;
    public static double walkSpeedMultiplier;
    public static double sprintSpeedMultiplier;
    public static double walkStepHeight;
    public static double sprinStepHeight;
    public static double safeFallHeight;

    public static boolean enableShallowSwimming;
    public static boolean enableSwimmingBoost;
    public static int swimmingBoostCooldown;
    public static boolean enableFreestyle;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event instanceof ModConfigEvent.Unloading) return;
        if (event.getConfig().getSpec() != SPEC) return;
        enableOmniSprint = ENABLE_OMNI_SPRINT.get();
        enableCrawl = ENABLE_CRAWL.get();
        enableSlideAndEvade = ENABLE_SLIDE_AND_EVADE.get();
        slideAndEvadeCooldown = SLIDE_AND_EVADE_COOLDOWN.get();
        enableTapStrafing = ENABLE_TAP_STRAFING.get();
        enableLandingRoll = ENABLE_LANDINH_ROLL.get();
        landingRollWindow = LANDING_ROLL_WINDOW.get();
        walkSpeedMultiplier = WALK_SPEED_MULTIPLIER.get();
        sprintSpeedMultiplier = SPRINT_SPEED_MULTIPLIER.get();
        walkStepHeight = WALK_STEP_HEIGHT.get();
        sprinStepHeight = SPRINT_STEP_HEIGHT.get();
        safeFallHeight = SAFE_FALL_HEIGHT.get();

        enableShallowSwimming = ENABLE_SHALLOW_SWIMMING.get();
        enableSwimmingBoost = ENABLE_SWIMMING_BOOST.get();
        swimmingBoostCooldown = SWMMING_BOOST_COOLDOWN.get();
        enableFreestyle = ENABLE_FREESTYLE.get();
    }
}
