package org.psygate.minecraft.vaultdoors;

import com.untamedears.citadel.Citadel;
import com.untamedears.citadel.entity.IReinforcement;
import com.untamedears.citadel.entity.PlayerReinforcement;
import com.untamedears.citadel.entity.PlayerState;
import com.untamedears.citadel.events.CreateReinforcementEvent;
import com.untamedears.citadel.events.PlayerDamageReinforcementEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultDoors
        extends JavaPlugin {

    public void onEnable() {
        final ItemStack vaultDoorPrototype = new ItemStack(Material.IRON_DOOR);
        ArrayList<String> lore = new ArrayList();
        lore.add("Vault Door");
        lore.add("");
        lore.add("An extremely durable door.");

        ItemMeta meta = vaultDoorPrototype.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName("Vault Door");
        vaultDoorPrototype.setItemMeta(meta);

        Map<Enchantment, Integer> map = new HashMap();
        map.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(4));
        vaultDoorPrototype.addUnsafeEnchantments(map);

        ShapedRecipe vaultdoorrecipe = new ShapedRecipe(vaultDoorPrototype);
        vaultdoorrecipe.shape(new String[]{".*.", ".*.", ".*."});
        vaultdoorrecipe.setIngredient('*', Material.DIAMOND);
        vaultdoorrecipe.setIngredient('.', Material.OBSIDIAN);

        getServer().addRecipe(vaultdoorrecipe);
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGH)
            public void placeDoor(BlockPlaceEvent ev) {

                if (vaultDoorPrototype.equals(ev.getItemInHand())) {
                    ev.getBlock().setMetadata("Vaultdoor", new VaultDoorMeta());
                }
            }

            @EventHandler
            public void reinforce(final CreateReinforcementEvent ev) {
                List<MetadataValue> valueList = ev.getBlock().getMetadata("Vaultdoor");

                boolean vaultDoorPrecondition = vaultDoorPrototype.equals(ev.getPlayer().getItemInHand());
                for (MetadataValue value : valueList) {
                    if (value instanceof VaultDoorMeta && ((VaultDoorMeta) value).asBoolean()) {
                        vaultDoorPrecondition = true;
                    }
                }

                if (vaultDoorPrecondition) { // Door was already placed - ctr
                    ev.getPlayer().sendMessage(ChatColor.GREEN + "Vaultdoor created. " + (ev.getReinforcement().getDurability() * 10) + " breaks.");
                    ev.getReinforcement().setDurability(ev.getReinforcement().getDurability() * 10);
                }
            }
        }, this);

//        getServer().getPluginManager().registerEvents(new Listener() {
//            @EventHandler
//            public void testBreak(AsyncPlayerChatEvent ev) {
//                if (ev.getMessage().equals("test")) {
//                    Block bb = ev.getPlayer().getTargetBlock(null, 5);
//                    if ((bb != null) && (bb.getType() != Material.AIR)) {
//                        VaultDoors.BreakTask task = new VaultDoors.BreakTask(VaultDoors.this, bb, ev.getPlayer());
//                        int id = VaultDoors.this.getServer().getScheduler().scheduleSyncRepeatingTask(VaultDoors.this, task, 1L, 1L);
//                        task.setId(id);
//                    }
//                }
//            }
//        }, this);
    }

    private class VaultDoorMeta implements MetadataValue {

        @Override
        public Object value() {
            return true;
        }

        @Override
        public void invalidate() {
        }

        @Override
        public Plugin getOwningPlugin() {
            return VaultDoors.this;
        }

        @Override
        public String asString() {
            return "true";
        }

        @Override
        public short asShort() {
            return 1;
        }

        @Override
        public long asLong() {
            return 1L;
        }

        @Override
        public int asInt() {
            return 1;
        }

        @Override
        public float asFloat() {
            return 1.0F;
        }

        @Override
        public double asDouble() {
            return 1.0D;
        }

        @Override
        public byte asByte() {
            return 1;
        }

        @Override
        public boolean asBoolean() {
            return true;
        }
    }
}

//    private class BreakTask
//            implements Runnable {
//
//        private int breaks = 0;
//        private long start = System.currentTimeMillis();
//        private int id;
//        private Block bb;
//        private Player player;
//
//        public BreakTask(Block bb, Player player) {
//            this.bb = bb;
//            this.player = player;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public void run() {
//            if ((this.bb != null) && (this.bb.getType() != Material.AIR)) {
//                BlockBreakEvent ev = new BlockBreakEvent(this.bb, this.player);
//                Bukkit.getPluginManager().callEvent(ev);
//                this.breaks += 1;
//            } else {
//                VaultDoors.this.getServer().getScheduler().cancelTask(this.id);
//                System.out.println("Breaks: " + this.breaks + " Time: " + (System.currentTimeMillis() - this.start));
//            }
//        }
//    }

