package crazypants.enderzoo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import crazypants.enderzoo.EnderZoo;
import crazypants.enderzoo.Log;

public final class Config {

  public static class Section {
    public final String name;
    public final String lang;

    public Section(String name, String lang) {
      this.name = name;
      this.lang = lang;
      register();
    }

    private void register() {
      sections.add(this);
    }

    public String lc() {
      return name.toLowerCase();
    }
  }

  public static final List<Section> sections;

  static {
    sections = new ArrayList<Section>();
  }

  public static Configuration config;

  public static File configDirectory;

  public static final Section sectionEnderminy = new Section("Enderminy", "enderminy");
  public static boolean enderminyEnabled = true;
  public static boolean enderminyAttacksPlayerOnSight = false;
  public static boolean enderminyAttacksCreepers = true;
  public static int enderminyAttackDamage = 10;
  public static int enderminyHealth = 20;
  public static boolean enderminyGroupAgro = true;
  public static int enderminyMaxGroupSize = 3;
  public static boolean enderminySpawnInLitAreas = false;
  public static boolean enderminySpawnOnlyOnGrass = true;
  public static int enderminyMinSpawnY = 0;

  public static final Section sectionConCreeper = new Section("Concussion Creeper", "concussionCreeper");
  public static boolean concussionCreeperEnabled = true;
  public static int concussionCreeperMaxTeleportRange = 32;
  public static int concussionCreeperConfusionDuration = 100;
  public static int concussionCreeperExplosionRange = 5;

  public static final Section sectionFallenKnight = new Section("Fallen Knight", "fallenKnight");
  public static boolean fallenKnightEnabled = true;
  public static double fallenKnightBaseDamage = 4.0;
  public static double fallenKnightHealth = 12;
  public static double fallenKnightFollowRange = 40.0;
  public static double fallenKnightChargeSpeed = 1.2;
  public static int fallenKnightRangedMinAttackPause = 20;
  public static int fallenKnightRangedMaxAttackPause = 60;
  public static float fallenKnightRangedMaxRange = 15;
  public static float fallenKnightChancePerArmorPiece = 0.7f;
  public static float fallenKnightChancePerArmorPieceHard = 0.9f;
  public static float fallenKnightRangedRatio = 0.25f;
  public static float fallenKnightChanceMounted = 0.75f;
  public static float fallenKnightChanceArmorUpgradeHard = 0.4f;
  public static float fallenKnightChanceArmorUpgrade = 0.2f;
  public static boolean fallKnightMountedArchesMaintainDistance = true;
  public static boolean fallenKnightArchersSwitchToMelee = true;

  public static final Section sectionFallenMount = new Section("Fallen Mount", "fallenMount");
  public static boolean fallenMountEnabled = true;
  public static double fallenMountChargeSpeed = 2.5f;
  public static double fallenMountBaseAttackDamage = 4;
  public static boolean fallenMountShadedByRider = true;
  public static float fallenMountChanceArmored = 0.5f;
  public static float fallenMountChanceArmoredHard = 0.9f;
  public static float fallenMountChanceArmorUpgrade = 0.01f;
  public static float fallenMountChanceArmorUpgradeHard = 0.05f;
  public static double fallenMountHealth = 30;

  public static final Section sectionWitherWitch = new Section("Wither Witch", "witherWitch");
  public static boolean witherWitchEnabled = true;
  public static double witherWitchHealth = 30;
  public static int witherWitchMinCats = 1;
  public static int witherWitchMaxCats = 2;

  public static final Section sectionWitherCat = new Section("Wither Cat", "witherCat");
  public static boolean witherCatEnabled = true;
  public static double witherCatHealth = 12;
  public static double witherCatAttackDamage = 3;
  public static double witherCatAngryHealth = 30;
  public static double witherCatAngryAttackDamage = 9;
  public static double witherCatAngryAttackDamageHardModifier = 2;

  public static final Section sectionEnchants = new Section("Enchantments", "enchantments");
  public static int enchantmentWitherArrowWeight = 2;
  public static int enchantmentWitherArrowDuration = 200;
  public static int enchantmentWitherArrowMinEnchantability = 20;
  public static int enchantmentWitherArrowMaxEnchantability = 50;

  public static int enchantmentWitherWeaponWeight = 2;
  public static int enchantmentWitherWeaponDuration = 200;
  public static int enchantmentWitherWeaponMinEnchantability = 20;
  public static int enchantmentWitherWeaponMaxEnchantability = 50;

  public static final Section sectionCharges = new Section("Charges", "charges");
  public static boolean confusingChargeEnabled = true;
  public static double confusingChargeRange = 6;
  public static int confusingChargeEffectDuration = 300;

  public static boolean enderChargeEnabled = true;
  public static double enderChargeRange = 6;
  public static int enderChargeMaxTeleportRange = 64;

  public static boolean concussionChargeEnabled = true;


  public static void load(FMLPreInitializationEvent event) {

    FMLCommonHandler.instance().bus().register(new Config());
    configDirectory = new File(event.getModConfigurationDirectory(), EnderZoo.MODID.toLowerCase());
    if(!configDirectory.exists()) {
      configDirectory.mkdir();
    }

    File configFile = new File(configDirectory, "EnderZoo.cfg");
    config = new Configuration(configFile);
    syncConfig();
  }

  public static void syncConfig() {
    try {
      Config.processConfig(config);
    } catch (Exception e) {
      Log.error("EnderZoo has a problem loading it's configuration");
      e.printStackTrace();
    } finally {
      if(config.hasChanged()) {
        config.save();
      }
    }
  }

  @SubscribeEvent
  public void onConfigChanged(OnConfigChangedEvent event) {
    if(event.modID.equals(EnderZoo.MODID)) {
      Log.info("Updating config...");
      syncConfig();
    }
  }

  public static void processConfig(Configuration config) {

    enderminyEnabled = config.getBoolean("enderminyEnabled", sectionEnderminy.name, enderminyEnabled, "Wether Enderminies are enabled");
    enderminyAttacksPlayerOnSight = config.getBoolean("enderminyAttacksPlayerOnSight", sectionEnderminy.name, enderminyAttacksPlayerOnSight,
        "When true an Enderminy will attack a player if it looks at them, otherwise they are neutral mobs.");
    enderminyAttacksCreepers = config.getBoolean("enderminyAttacksCreepers", sectionEnderminy.name, enderminyAttacksCreepers,
        "When true Enderminies will attack creepers");
    enderminyAttackDamage = config.get(sectionEnderminy.name, "enderminyAttackDamage", enderminyAttackDamage,
        "Attack damage of Enderminies. 7=Enderman damage, 3=Zombie damage").getInt(enderminyAttackDamage);
    enderminyHealth = config.get(sectionEnderminy.name, "enderminyHealth", enderminyHealth,
        "Health of Enderminies. 40=Enderman health, 20=Zombie health").getInt(enderminyHealth);
    enderminyGroupAgro = config.getBoolean("enderminyGroupAgro", sectionEnderminy.name, enderminyGroupAgro,
        "When true attacking one Enderminy will cause other Enderminies who witness the attack to attack the player as well");
    enderminyMaxGroupSize = config.get(sectionEnderminy.name, "enderminyMaxGroupSize", enderminyMaxGroupSize,
        "Maximum number of Enderminies that will spawn in a single group").getInt(enderminyMaxGroupSize);
    enderminySpawnInLitAreas = config.getBoolean("enderminySpawnInLitAreas", sectionEnderminy.name, enderminySpawnInLitAreas,
        "When true enderminies will spawn in well lit areas, when false they will only spawn in dark areas.");
    enderminySpawnOnlyOnGrass = config.getBoolean("enderminySpawnOnlyOnGrass", sectionEnderminy.name, enderminySpawnOnlyOnGrass,
        "When true enderminies will spawn only on grass blocks.");
    enderminyMinSpawnY = config.get(sectionEnderminy.name, "enderminyMinSpawnY", enderminyMinSpawnY,
        "The minimum Y level at which enderminies will spawn").getInt(enderminyMinSpawnY);

    concussionCreeperEnabled = config.getBoolean("concussionCreeperEnabled", sectionConCreeper.name, concussionCreeperEnabled,
        "Wether ConcussionCreepers are enabled");
    concussionCreeperMaxTeleportRange = config.get(sectionConCreeper.name, "concussionCreeperMaxTeleportRange", concussionCreeperMaxTeleportRange,
        "Sets the max range entites can be telported when the creeper explodes").getInt(concussionCreeperMaxTeleportRange);
    concussionCreeperConfusionDuration = config.get(sectionConCreeper.name, "concussionCreeperConfusionDuration", concussionCreeperConfusionDuration,
        "Sets the durtaion in ticks of the confusion effect applied on explosion").getInt(concussionCreeperConfusionDuration);
    concussionCreeperExplosionRange = config.get(sectionConCreeper.name, "concussionCreeperExplosionRange", concussionCreeperExplosionRange,
        "The range of the 'teleport explosion'").getInt(concussionCreeperExplosionRange);

    fallenKnightEnabled = config.getBoolean("fallenKnightEnabled", sectionFallenKnight.name, fallenKnightEnabled, "Wether Fallen Knights are enabled");
    fallenKnightBaseDamage = config.get(sectionFallenKnight.name, "fallenKnightBaseDamage", fallenKnightBaseDamage, "Base damage of a knight").getDouble(
        fallenKnightBaseDamage);
    fallenKnightHealth = config.get(sectionFallenKnight.name, "fallenKnightHealth", fallenKnightHealth, "Health of a knight").getDouble(
        fallenKnightHealth);
    fallenKnightFollowRange = config.get(sectionFallenKnight.name, "fallenKnightFollowRange", fallenKnightFollowRange, "Follow range of a knight").getDouble(
        fallenKnightFollowRange);
    fallenKnightChargeSpeed = config.get(sectionFallenKnight.name, "fallenKnightChargeSpeed", fallenKnightChargeSpeed,
        "The speed at which a knight will charge its target").getDouble(fallenKnightChargeSpeed);
    fallenKnightRangedMinAttackPause = config.get(sectionFallenKnight.name, "fallenKnightRangedMinAttackPause", fallenKnightRangedMinAttackPause,
        "The min number of ticks between ranged attacks").getInt(fallenKnightRangedMinAttackPause);
    fallenKnightRangedMaxAttackPause = config.get(sectionFallenKnight.name, "fallenKnightRangedMinAttackPause", fallenKnightRangedMinAttackPause,
        "The max number of ticks between ranged attacks").getInt(fallenKnightRangedMinAttackPause);
    fallenKnightRangedMaxRange = (float) config.get(sectionFallenKnight.name, "fallenKnightRangedMaxRange", fallenKnightRangedMaxRange,
        "The max attack range when using a bow").getDouble(fallenKnightRangedMaxRange);
    fallenKnightChancePerArmorPiece = (float) config.get(sectionFallenKnight.name, "fallenKnightChancePerArmorPiece", fallenKnightChancePerArmorPiece,
        "The chance each armor piece has of being added to a spawned knight").getDouble(fallenKnightChancePerArmorPiece);
    fallenKnightChancePerArmorPieceHard = (float) config.get(sectionFallenKnight.name, "fallenKnightChancePerArmorPieceHard",
        fallenKnightChancePerArmorPieceHard, "The chance each armor piece has of being added to a spawned knight when difficulty is set to hard").getDouble(
        fallenKnightChancePerArmorPieceHard);
    fallenKnightRangedRatio = (float) config.get(sectionFallenKnight.name, "fallenKnightRangedRatio", fallenKnightRangedRatio,
        "The precentage of spawned knoghts equipped with bows").getDouble(
        fallenKnightRangedRatio);
    fallenKnightChanceMounted = (float) config.get(sectionFallenKnight.name, "fallenKnightChanceMounted", fallenKnightChanceMounted,
        "The chance a spawned knight will be mounted").getDouble(
        fallenKnightChanceMounted);
    fallenKnightChanceArmorUpgradeHard = (float) config.get(sectionFallenKnight.name, "fallenKnightChanceArmorUpgradeHard", fallenKnightChanceArmorUpgradeHard,
        "The chance the type of armor equipped will be improved when dificult is hard")
        .getDouble(fallenKnightChanceArmorUpgradeHard);
    fallenKnightChanceArmorUpgrade = (float) config.get(sectionFallenKnight.name, "fallenKnightChanceArmorUpgrade", fallenKnightChanceArmorUpgrade,
        "The chance the type of armor equipped will be improved")
        .getDouble(fallenKnightChanceArmorUpgrade);
    fallKnightMountedArchesMaintainDistance = config.getBoolean("fallKnightMountedArchesMaintainDistance", sectionFallenKnight.name,
        fallKnightMountedArchesMaintainDistance, "When true mounted archer knigts will attempt to keep distance between themselves and their target");
    fallenKnightArchersSwitchToMelee = config
        .getBoolean("fallenKnightArchersSwitchToMelee", sectionFallenKnight.name, fallenKnightArchersSwitchToMelee,
            "When true archer knigts will switch to a sword when target is within melee range. "
                + "Doesn't apply to mounted archers if fallKnightMountedArchesMaintainDistance is true");

    fallenMountEnabled = config.getBoolean("fallenMountEnabled", sectionFallenMount.name, fallenMountEnabled, "If false fallen mounts will be disabled");
    fallenMountChargeSpeed = config.get(sectionFallenMount.name, "fallenMountChargeSpeed", fallenMountChargeSpeed,
        "he speed at which a mount will charge its target").getDouble(fallenMountChargeSpeed);
    fallenMountBaseAttackDamage = config.get(sectionFallenMount.name, "fallenMountBaseAttackDamage", fallenMountBaseAttackDamage,
        "Base attack damage of the mount").getDouble(fallenMountBaseAttackDamage);
    fallenMountHealth = config.get(sectionFallenMount.name, "fallenMountHealth", fallenMountHealth,
        "Base attack health of the mount").getDouble(fallenMountHealth);
    fallenMountShadedByRider = config.getBoolean("fallenMountShadedByRider", sectionFallenMount.name, fallenMountShadedByRider,
        "When true a mount will not burn in the sun unless its rider is");
    fallenMountChanceArmored = (float) config.get(sectionFallenMount.name, "fallenMountChanceArmored", fallenMountChanceArmored,
        "The chance a spawned mount will be armored").getDouble(fallenMountChanceArmored);
    fallenMountChanceArmoredHard = (float) config.get(sectionFallenMount.name, "fallenMountChanceArmoredHard", fallenMountChanceArmoredHard,
        "The chance a spawned mount will be armored when difficult is hard").getDouble(fallenMountChanceArmoredHard);
    fallenMountChanceArmorUpgrade = (float) config.get(sectionFallenMount.name, "fallenMountChanceArmorUpgrade", fallenMountChanceArmorUpgrade,
        "The chance a mount's armor will be upgraded").getDouble(fallenMountChanceArmorUpgrade);
    fallenMountChanceArmorUpgradeHard = (float) config.get(sectionFallenMount.name, "fallenMountChanceArmorUpgradeHard", fallenMountChanceArmorUpgradeHard,
        "The chance a mount's armor will be upgraded when difficulty is hard").getDouble(fallenMountChanceArmorUpgradeHard);

    witherWitchEnabled = config.getBoolean("witherWitchEnabled", sectionWitherWitch.name, witherWitchEnabled, "If false Wither Witches will be disabled");
    witherWitchHealth = config.get(sectionWitherWitch.name, "witherWitchHealth", witherWitchHealth,
        "Base attack damage of the mount").getDouble(witherWitchHealth);
    witherWitchMinCats = config.get(sectionWitherWitch.name, "witherWitchMinCats", witherWitchMinCats,
        "The minimum number of cats spawned with a Wither Witch").getInt(witherWitchMinCats);
    witherWitchMaxCats = config.get(sectionWitherWitch.name, "witherWitchMaxCats", witherWitchMaxCats,
        "The maximum number of cats spawned with a Wither Witch").getInt(witherWitchMaxCats);

    witherCatEnabled = config.getBoolean("witherCatEnabled", sectionWitherCat.name, witherCatEnabled, "If false Wither Cats will be disabled");
    witherCatHealth = config.get(sectionWitherCat.name, "witherCatHealth", witherCatHealth,
        "Base health of the wither cat").getDouble(witherCatHealth);
    witherCatAttackDamage = config.get(sectionWitherCat.name, "witherCatAttackDamage", witherCatAttackDamage,
        "Base attack damage of the wither cat").getDouble(witherCatAttackDamage);
    witherCatAngryAttackDamageHardModifier = config.get(sectionWitherCat.name, "witherCatAngryAttackDamageHardModifier",
        witherCatAngryAttackDamageHardModifier,
        "The increase to damage when playing on hard").getDouble(witherCatAngryAttackDamageHardModifier);

    enchantmentWitherArrowWeight = config.get(sectionEnchants.name, "enchantmentWitherArrowWeight", enchantmentWitherArrowWeight,
        "The weight (or chance of getting) the enchantment. eg sharpness=10, knockback = 5, fire aspect = 2, silk touch = 1").getInt(
        enchantmentWitherArrowWeight);
    enchantmentWitherArrowDuration = config.get(sectionEnchants.name, "enchantmentWitherArrowDuration", enchantmentWitherArrowDuration,
        "Duration of the wither effect in ticks").getInt(
        enchantmentWitherArrowDuration);
    enchantmentWitherArrowMinEnchantability = config.get(sectionEnchants.name, "enchantmentWitherArrowMinEnchantability",
        enchantmentWitherArrowMinEnchantability, "The minimum required enchantability level").getInt(enchantmentWitherArrowMinEnchantability);
    enchantmentWitherArrowMaxEnchantability = config.get(sectionEnchants.name, "enchantmentWitherArrowMaxEnchantability",
        enchantmentWitherArrowMaxEnchantability, "The maximum required level").getInt(enchantmentWitherArrowMaxEnchantability);

    enchantmentWitherWeaponWeight = config.get(sectionEnchants.name, "enchantmentWitherWeaponWeight", enchantmentWitherWeaponWeight,
        "The weight (or chance of getting) the enchantment. eg sharpness=10, knockback = 5, fire aspect = 2, silk touch = 1").getInt(
        enchantmentWitherWeaponWeight);
    enchantmentWitherWeaponDuration = config.get(sectionEnchants.name, "enchantmentWitherWeaponDuration", enchantmentWitherWeaponDuration,
        "Duration of the wither effect in ticks").getInt(
        enchantmentWitherWeaponDuration);
    enchantmentWitherWeaponMinEnchantability = config.get(sectionEnchants.name, "enchantmentWitherWeaponMinEnchantability",
        enchantmentWitherWeaponMinEnchantability, "The minimum required enchantability level").getInt(enchantmentWitherWeaponMinEnchantability);
    enchantmentWitherWeaponMaxEnchantability = config.get(sectionEnchants.name, "enchantmentWitherWeaponMaxEnchantability",
        enchantmentWitherWeaponMaxEnchantability, "The maximum required level").getInt(enchantmentWitherWeaponMaxEnchantability);
    
    confusingChargeEnabled = config.getBoolean("confusingChargeEnabled", sectionCharges.name, confusingChargeEnabled,
        "If false Confusing Charges will be disabled");
    confusingChargeRange = config.get(sectionCharges.name, "confusingChargeRange", confusingChargeRange,
        "The range of the confusion charges effect").getDouble(
        confusingChargeRange);
    confusingChargeEffectDuration = config.get(sectionCharges.name, "confusingChargeEffectDuration", confusingChargeEffectDuration,
        "Numer of ticks the confusion effect active. Scales with distance from the expolosion").getInt(
        confusingChargeEffectDuration);

    enderChargeEnabled = config.getBoolean("enderChargeEnabled", sectionCharges.name, enderChargeEnabled,
        "If false Ender Charges will be disabled");
    enderChargeRange = config.get(sectionCharges.name, "enderChargeRange", enderChargeRange,
        "The range of the ender charges effect").getDouble(enderChargeRange);
    enderChargeMaxTeleportRange = config.get(sectionCharges.name, "enderChargeMaxTeleportRange", enderChargeMaxTeleportRange,
        "The max range effected entities will be teleported. Distance is randomised").getInt(
        enderChargeMaxTeleportRange);

    concussionChargeEnabled = config.getBoolean("concussionChargeEnabled", sectionCharges.name, concussionChargeEnabled,
        "If false Concussion Charges will be disabled");

  }

  private Config() {
  }

}
