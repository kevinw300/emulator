package server;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import client.Equip;
import client.IItem;
import client.MapleInventoryType;
import client.MapleWeaponType;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class MapleItemInformationProvider {

    private static MapleItemInformationProvider instance = null;
    protected MapleDataProvider itemData;
    protected MapleDataProvider equipData;
//    protected MapleData stringData;
    protected MapleData cashStringData;
    protected MapleData consumeStringData;
    protected MapleData eqpStringData;
    protected MapleData etcStringData;
    protected MapleData insStringData;
    protected MapleData petStringData;
    protected Map<Integer, MapleInventoryType> inventoryTypeCache = new HashMap<>();
    protected Map<Integer, Short> slotMaxCache = new HashMap<>();
    protected Map<Integer, MapleStatEffect> itemEffects = new HashMap<>();
    protected Map<Integer, Map<String, Integer>> equipStatsCache = new HashMap<>();
    protected Map<Integer, Equip> equipCache = new HashMap<>();
    protected Map<Integer, Double> priceCache = new HashMap<>();
    protected Map<Integer, Integer> wholePriceCache = new HashMap<>();
    protected Map<Integer, Integer> projectileWatkCache = new HashMap<>();
    protected Map<Integer, String> nameCache = new HashMap<>();
    protected Map<Integer, String> descCache = new HashMap<>();
    protected Map<Integer, String> msgCache = new HashMap<>();
    private static final Random rand = new Random();

    /**
     * Creates a new instance of MapleItemInformationProvider
     */
    protected MapleItemInformationProvider() {
        itemData = MapleDataProviderFactory.getDataProvider(new File("wz/Item.wz"));
        equipData = MapleDataProviderFactory.getDataProvider(new File("wz/Character.wz"));
//        stringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Item.img");
        cashStringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Cash.img");
        consumeStringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Consume.img");
        eqpStringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Eqp.img");
        etcStringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Etc.img");
        insStringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Ins.img");
        petStringData = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Pet.img");
    }

    public static MapleItemInformationProvider getInstance() {
        if (instance == null) {
            instance = new MapleItemInformationProvider();
        }
        return instance;
    }

    /* returns the inventory type for the specified item id */
    public MapleInventoryType getInventoryType(int itemId) {
        if (inventoryTypeCache.containsKey(itemId)) {
            return inventoryTypeCache.get(itemId);
        }
        MapleInventoryType ret;
        String idStr = "0" + String.valueOf(itemId);
        // first look in items...
        MapleDataDirectoryEntry root = itemData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            // we should have .img files here beginning with the first 4 IID
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
                    ret = MapleInventoryType.getByWZName(topDir.getName());
                    inventoryTypeCache.put(itemId, ret);
                    return ret;
                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
                    ret = MapleInventoryType.getByWZName(topDir.getName());
                    inventoryTypeCache.put(itemId, ret);
                    return ret;
                }
            }
        }
        // not found? maybe its equip...
        root = equipData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr + ".img")) {
                    ret = MapleInventoryType.EQUIP;
                    inventoryTypeCache.put(itemId, ret);
                    return ret;
                }
            }
        }
        ret = MapleInventoryType.UNDEFINED;
        inventoryTypeCache.put(itemId, ret);
        return ret;
    }

    protected MapleData getStringData(int itemId) {
        String cat = "null";
        MapleData theData;
        if (itemId >= 5010000) {
            theData = cashStringData;
        } else if (itemId >= 2000000 && itemId < 3000000) {
            theData = consumeStringData;
        } else if (itemId >= 1010000 && itemId < 1040000 || itemId >= 1122000 && itemId < 1123000) {
            theData = eqpStringData;
            cat = "Accessory";
        } else if (itemId >= 1000000 && itemId < 1010000) {
            theData = eqpStringData;
            cat = "Cap";
        } else if (itemId >= 1102000 && itemId < 1103000) {
            theData = eqpStringData;
            cat = "Cape";
        } else if (itemId >= 1040000 && itemId < 1050000) {
            theData = eqpStringData;
            cat = "Coat";
        } else if (itemId >= 20000 && itemId < 22000) {
            theData = eqpStringData;
            cat = "Face";
        } else if (itemId >= 1080000 && itemId < 1090000) {
            theData = eqpStringData;
            cat = "Glove";
        } else if (itemId >= 30000 && itemId < 32000) {
            theData = eqpStringData;
            cat = "Hair";
        } else if (itemId >= 1050000 && itemId < 1060000) {
            theData = eqpStringData;
            cat = "Longcoat";
        } else if (itemId >= 1060000 && itemId < 1070000) {
            theData = eqpStringData;
            cat = "Pants";
        } else if (itemId >= 1802000 && itemId < 1810000) {
            theData = eqpStringData;
            cat = "PetEquip";
        } else if (itemId >= 1112000 && itemId < 1120000) {
            theData = eqpStringData;
            cat = "Ring";
        } else if (itemId >= 1092000 && itemId < 1100000) {
            theData = eqpStringData;
            cat = "Shield";
        } else if (itemId >= 1070000 && itemId < 1080000) {
            theData = eqpStringData;
            cat = "Shoes";
        } else if (itemId >= 1900000 && itemId < 2000000) {
            theData = eqpStringData;
            cat = "Taming";
        } else if (itemId >= 1300000 && itemId < 1800000) {
            theData = eqpStringData;
            cat = "Weapon";
        } else if (itemId >= 4000000 && itemId < 5000000) {
            theData = etcStringData;
        } else if (itemId >= 3000000 && itemId < 4000000) {
            theData = insStringData;
        } else if (itemId >= 5000000 && itemId < 5010000) {
            theData = petStringData;
        } else {
            return null;
        }
        if (cat.equalsIgnoreCase("null")) {
            return theData.getChildByPath(String.valueOf(itemId));
        } else {
            return theData.getChildByPath(cat + "/" + itemId);
        }
    }
//    protected MapleData getStringData(int itemId) {
//        String cat;
//        if (itemId >= 5010000) {
//            cat = "Cash";
//        } else if (itemId >= 2000000 && itemId < 3000000) {
//            cat = "Con";
//        } else if (itemId >= 1010000 && itemId < 1040000 || itemId >= 1122000 && itemId < 1123000) {
//            cat = "Eqp/Accessory";
//        } else if (itemId >= 1000000 && itemId < 1010000) {
//            cat = "Eqp/Cap";
//        } else if (itemId >= 1102000 && itemId < 1103000) {
//            cat = "Eqp/Cape";
//        } else if (itemId >= 1040000 && itemId < 1050000) {
//            cat = "Eqp/Coat";
//        } else if (itemId >= 20000 && itemId < 22000) {
//            cat = "Eqp/Face";
//        } else if (itemId >= 1080000 && itemId < 1090000) {
//            cat = "Eqp/Glove";
//        } else if (itemId >= 30000 && itemId < 32000) {
//            cat = "Eqp/Hair";
//        } else if (itemId >= 1050000 && itemId < 1060000) {
//            cat = "Eqp/Longcoat";
//        } else if (itemId >= 1060000 && itemId < 1070000) {
//            cat = "Eqp/Pants";
//        } else if (itemId >= 1802000 && itemId < 1810000) {
//            cat = "Eqp/PetEquip";
//        } else if (itemId >= 1112000 && itemId < 1120000) {
//            cat = "Eqp/Ring";
//        } else if (itemId >= 1092000 && itemId < 1100000) {
//            cat = "Eqp/Shield";
//        } else if (itemId >= 1070000 && itemId < 1080000) {
//            cat = "Eqp/Shoes";
//        } else if (itemId >= 1900000 && itemId < 2000000) {
//            cat = "Eqp/Taming";
//        } else if (itemId >= 1300000 && itemId < 1800000) {
//            cat = "Eqp/Weapon";
//        } else if (itemId >= 4000000 && itemId < 5000000) {
//            cat = "Etc";
//        } else if (itemId >= 3000000 && itemId < 4000000) {
//            cat = "Ins";
//        } else if (itemId >= 5000000 && itemId < 5010000) {
//            cat = "Pet";
//        } else {
//            return null;
//        }
//        return stringData.getChildByPath(cat + "/" + itemId);
//    }

    protected MapleData getItemData(int itemId) {
        MapleData ret = null;
        String idStr = "0" + String.valueOf(itemId);
        MapleDataDirectoryEntry root = itemData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            // we should have .img files here beginning with the first 4 IID
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
                    ret = itemData.getData(topDir.getName() + "/" + iFile.getName());
                    if (ret == null) {
                        return null;
                    }
                    ret = ret.getChildByPath(idStr);
                    return ret;
                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
                    return itemData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        root = equipData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr + ".img")) {
                    return equipData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        return ret;
    }

    /**
     * returns the maximum of items in one slot
     */
    public short getSlotMax(int itemId) {
        if (slotMaxCache.containsKey(itemId)) {
            return slotMaxCache.get(itemId);
        }
        short ret = 0;
        MapleData item = getItemData(itemId);
        if (item != null) {
            MapleData smEntry = item.getChildByPath("info/slotMax");
            if (smEntry == null) {
                if (getInventoryType(itemId).getType() == MapleInventoryType.EQUIP.getType()) {
                    ret = 1;
                } else {
                    ret = 100;
                }
            } else {
                if (isThrowingStar(itemId)) {
                    ret = 1;
                } else if (MapleDataTool.getInt(smEntry) == 0) {
                    ret = 1;
                }
                ret = (short) MapleDataTool.getInt(smEntry);
            }
        }
        slotMaxCache.put(itemId, ret);
        return ret;
    }

    public int getWholePrice(int itemId) {
        if (wholePriceCache.containsKey(itemId)) {
            return wholePriceCache.get(itemId);
        }
        MapleData item = getItemData(itemId);
        if (item == null) {
            return -1;
        }

        int pEntry = 0;
        MapleData pData = item.getChildByPath("info/price");
        if (pData == null) {
            return -1;
        }
        pEntry = MapleDataTool.getInt(pData);

        wholePriceCache.put(itemId, pEntry);
        return pEntry;
    }

    public double getPrice(int itemId) {
        if (priceCache.containsKey(itemId)) {
            return priceCache.get(itemId);
        }
        MapleData item = getItemData(itemId);
        if (item == null) {
            return -1;
        }

        //TODO ULTRAHACK - prevent players gaining miriads of mesars with orbis/eos scrolls
        if (itemId == 4001019 || itemId == 4001020) {
            return 0;
        }

        double pEntry = 0.0;
        MapleData pData = item.getChildByPath("info/unitPrice");
        if (pData != null) {
            try {
                pEntry = MapleDataTool.getDouble(pData);
            } catch (Exception e) {
                pEntry = (double) MapleDataTool.getInt(pData);
            }
        } else {
            pData = item.getChildByPath("info/price");
            if (pData == null) {
                return -1;
            }
            pEntry = (double) MapleDataTool.getInt(pData);
        }

        priceCache.put(itemId, pEntry);
        return pEntry;
    }

    protected Map<String, Integer> getEquipStats(int itemId) {
        if (equipStatsCache.containsKey(itemId)) {
            return equipStatsCache.get(itemId);
        }
        Map<String, Integer> ret = new LinkedHashMap<String, Integer>();
        MapleData item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        MapleData info = item.getChildByPath("info");
        if (info == null) {
            return null;
        }
        for (MapleData data : info.getChildren()) {
            if (data.getName().startsWith("inc")) {
                ret.put(data.getName().substring(3), MapleDataTool.getIntConvert(data));
            }
        }
        ret.put("tuc", MapleDataTool.getInt("tuc", info, 0));
        ret.put("reqLevel", MapleDataTool.getInt("reqLevel", info, 0));
        ret.put("cursed", MapleDataTool.getInt("cursed", info, 0));
        ret.put("success", MapleDataTool.getInt("success", info, 0));
        equipStatsCache.put(itemId, ret);
        return ret;
    }

    public int getReqLevel(int itemId) {
        final Integer req = getEquipStats(itemId).get("reqLevel");
        return req == null ? 0 : req.intValue();
    }

    public MapleWeaponType getWeaponType(int itemId) {
        int cat = itemId / 10000;
        cat = cat % 100;
        switch (cat) {
            case 30:
                return MapleWeaponType.SWORD1H;
            case 31:
                return MapleWeaponType.AXE1H;
            case 32:
                return MapleWeaponType.BLUNT1H;
            case 33:
                return MapleWeaponType.DAGGER;
            case 37:
                return MapleWeaponType.WAND;
            case 38:
                return MapleWeaponType.STAFF;
            case 40:
                return MapleWeaponType.SWORD2H;
            case 41:
                return MapleWeaponType.AXE2H;
            case 42:
                return MapleWeaponType.BLUNT2H;
            case 43:
                return MapleWeaponType.SPEAR;
            case 44:
                return MapleWeaponType.POLE_ARM;
            case 45:
                return MapleWeaponType.BOW;
            case 47:
                return MapleWeaponType.CLAW;
            case 46:
                return MapleWeaponType.CROSSBOW;

        }
        return MapleWeaponType.NOT_A_WEAPON;
    }

    public boolean isShield(int itemId) {
        int cat = itemId / 10000;
        cat = cat % 100;;
        return cat == 9;
    }

    public boolean isEquip(int itemId) {
        return itemId / 1000000 == 1;
    }

    public IItem scrollEquipWithId(IItem equip, int scrollId, boolean usingWhiteScroll) {
        if (equip instanceof Equip) {
            Equip nEquip = (Equip) equip;
            Map<String, Integer> stats = this.getEquipStats(scrollId);
            if (nEquip.getUpgradeSlots() > 0 && Math.ceil(Math.random() * 100.0) <= stats.get("success")) {
                for (Entry<String, Integer> stat : stats.entrySet()) {
                    if (stat.getKey().equals("STR")) {
                        nEquip.setStr((short) (nEquip.getStr() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("DEX")) {
                        nEquip.setDex((short) (nEquip.getDex() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("INT")) {
                        nEquip.setInt((short) (nEquip.getInt() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("LUK")) {
                        nEquip.setLuk((short) (nEquip.getLuk() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("PAD")) {
                        nEquip.setWatk((short) (nEquip.getWatk() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("PDD")) {
                        nEquip.setWdef((short) (nEquip.getWdef() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("MAD")) {
                        nEquip.setMatk((short) (nEquip.getMatk() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("MDD")) {
                        nEquip.setMdef((short) (nEquip.getMdef() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("ACC")) {
                        nEquip.setAcc((short) (nEquip.getAcc() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("EVA")) {
                        nEquip.setAvoid((short) (nEquip.getAvoid() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("Speed")) {
                        nEquip.setSpeed((short) (nEquip.getSpeed() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("Jump")) {
                        nEquip.setJump((short) (nEquip.getJump() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("MHP")) {
                        nEquip.setHp((short) (nEquip.getHp() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("MMP")) {
                        nEquip.setMp((short) (nEquip.getMp() + stat.getValue().intValue()));
                    } else if (stat.getKey().equals("afterImage")) {
                    }
                }
                nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                nEquip.setLevel((byte) (nEquip.getLevel() + 1));
            } else {
                if (!usingWhiteScroll) {
                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                }
                if (Math.ceil(1.0 + Math.random() * 100.0) < stats.get("cursed")) {
                    // DESTROY :) (O.O!)
                    return null;
                }
            }
        }
        return equip;
    }

    public IItem getEquipById(int equipId) {
        if (equipCache.containsKey(equipId)) {
            return equipCache.get(equipId).copy();
        }
        Equip nEquip = new Equip(equipId, (byte) 0);
        nEquip.setQuantity((short) 1);
        Map<String, Integer> stats = this.getEquipStats(equipId);
        if (stats != null) {
            for (Entry<String, Integer> stat : stats.entrySet()) {
                if (stat.getKey().equals("STR")) {
                    nEquip.setStr((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("DEX")) {
                    nEquip.setDex((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("INT")) {
                    nEquip.setInt((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("LUK")) {
                    nEquip.setLuk((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("PAD")) {
                    nEquip.setWatk((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("PDD")) {
                    nEquip.setWdef((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("MAD")) {
                    nEquip.setMatk((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("MDD")) {
                    nEquip.setMdef((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("ACC")) {
                    nEquip.setAcc((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("EVA")) {
                    nEquip.setAvoid((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("Speed")) {
                    nEquip.setSpeed((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("Jump")) {
                    nEquip.setJump((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("MHP")) {
                    nEquip.setHp((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("MMP")) {
                    nEquip.setMp((short) stat.getValue().intValue());
                } else if (stat.getKey().equals("tuc")) {
                    nEquip.setUpgradeSlots((byte) stat.getValue().intValue());
                } else if (stat.getKey().equals("afterImage")) {
                }
            }
        }
        equipCache.put(equipId, nEquip);
        return nEquip.copy();
    }

    private short getRandStat(short defaultValue, int maxRange) {
        if (defaultValue == 0) {
            return 0;
        }

        // vary no more than ceil of 10% of stat
        int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);
        return (short) ((defaultValue - lMaxRange) + Math.floor(rand.nextDouble() * (lMaxRange * 2 + 1)));
    }

    public Equip randomizeStats(Equip equip) {
        equip.setStr(getRandStat(equip.getStr(), 5));
        equip.setDex(getRandStat(equip.getDex(), 5));
        equip.setInt(getRandStat(equip.getInt(), 5));
        equip.setLuk(getRandStat(equip.getLuk(), 5));
        equip.setMatk(getRandStat(equip.getMatk(), 5));
        equip.setWatk(getRandStat(equip.getWatk(), 5));
        equip.setAcc(getRandStat(equip.getAcc(), 5));
        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
        equip.setJump(getRandStat(equip.getJump(), 5));
        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
        equip.setWdef(getRandStat(equip.getWdef(), 10));
        equip.setMdef(getRandStat(equip.getMdef(), 10));
        equip.setHp(getRandStat(equip.getHp(), 10));
        equip.setMp(getRandStat(equip.getMp(), 10));
        return equip;
    }

    public MapleStatEffect getItemEffect(int itemId) {
        MapleStatEffect ret = itemEffects.get(Integer.valueOf(itemId));
        if (ret == null) {
            MapleData item = getItemData(itemId);
            if (item == null) {
                return null;
            }
            MapleData spec = item.getChildByPath("spec");
            ret = MapleStatEffect.loadItemEffectFromData(spec, itemId);
            itemEffects.put(Integer.valueOf(itemId), ret);
        }
        return ret;
    }

    public boolean isThrowingStar(int itemId) {
        return itemId >= 2070000 && itemId < 2080000;
    }

    public boolean isOverall(int itemId) {
        return itemId >= 1050000 && itemId < 1060000;
    }

    public boolean isArrowForCrossBow(int itemId) {
        return itemId >= 2061000 && itemId < 2062000;
    }

    public boolean isArrowForBow(int itemId) {
        return itemId >= 2060000 && itemId < 2061000;
    }

    public boolean isTwoHanded(int itemId) {
        switch (getWeaponType(itemId)) {
            case AXE2H:
                return true;
            case BLUNT2H:
                return true;
            case BOW:
                return true;
            case CLAW:
                return true;
            case CROSSBOW:
                return true;
            case POLE_ARM:
                return true;
            case SPEAR:
                return true;
            case SWORD2H:
                return true;
            default:
                return false;
        }
    }

    public int getWatkForProjectile(int itemId) {
        Integer atk = projectileWatkCache.get(itemId);
        if (atk != null) {
            return atk.intValue();
        }
        MapleData data = getItemData(itemId);
        atk = Integer.valueOf(MapleDataTool.getInt("info/incPAD", data, 0));
        projectileWatkCache.put(itemId, atk);
        return atk.intValue();
    }

    public boolean canScroll(int scrollid, int itemid) {
        int scrollCategoryQualifier = (scrollid / 100) % 100;
        int itemCategoryQualifier = (itemid / 10000) % 100;
        return scrollCategoryQualifier == itemCategoryQualifier;
    }

    public String getName(int itemId) {
        if (nameCache.containsKey(itemId)) {
            return nameCache.get(itemId);
        }
        MapleData strings = getStringData(itemId);
        if (strings == null) {
            return null;
        }
        String ret = MapleDataTool.getString("name", strings, null);
        nameCache.put(itemId, ret);
        return ret;
    }

    public String getDesc(int itemId) {
        if (descCache.containsKey(itemId)) {
            return descCache.get(itemId);
        }
        MapleData strings = getStringData(itemId);
        if (strings == null) {
            return null;
        }
        String ret = MapleDataTool.getString("desc", strings, null);
        descCache.put(itemId, ret);
        return ret;
    }

    public String getMsg(int itemId) {
        if (msgCache.containsKey(itemId)) {
            return msgCache.get(itemId);
        }
        MapleData strings = getStringData(itemId);
        if (strings == null) {
            return null;
        }
        String ret = MapleDataTool.getString("msg", strings, null);
        msgCache.put(itemId, ret);
        return ret;
    }
}
