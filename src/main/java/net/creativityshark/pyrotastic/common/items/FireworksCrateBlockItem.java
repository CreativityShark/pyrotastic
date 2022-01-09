package net.creativityshark.pyrotastic.common.items;

import net.creativityshark.pyrotastic.PyrotasticMod;
import net.minecraft.block.Block;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

//Most of this class is modified code from BundleItem.java

public class FireworksCrateBlockItem extends BlockItem {
    public FireworksCrateBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (itemStack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeFirstStack(stack).ifPresent((removedStack) -> addToBundle(stack, slot.insertStack(removedStack)));
            } else if (itemStack.getItem().canBeNested() && itemStack.isOf(Items.FIREWORK_ROCKET) && stack.getCount() == 1) {
                int i = (16 - getBundleOccupancy(stack)) / getItemOccupancy(itemStack);
                int j = addToBundle(stack, slot.takeStackRange(itemStack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (stack.isEmpty()) {
            return false;
        } else {
            if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
                if (otherStack.isEmpty()) {
                    removeFirstStack(stack).ifPresent((itemStack) -> {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    });
                } else {
                    int i = addToBundle(stack, otherStack);
                    if (i > 0) {
                        this.playInsertSound(player);
                        otherStack.decrement(i);
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isItemBarVisible(ItemStack stack) {
        return getBundleOccupancy(stack) > 0;
    }

    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * getBundleOccupancy(stack) / 16, 13);
    }

    public int getItemBarColor(ItemStack stack) {
        return MathHelper.packRgb(0.4F, 0.4F, 1.0F);
    }

    private static int addToBundle(ItemStack bundle, ItemStack stack) {
        if (bundle.getCount() == 1) {
            if (!stack.isEmpty() && stack.isOf(Items.FIREWORK_ROCKET) && stack.getItem().canBeNested()) {
                NbtCompound nbtCompound = bundle.getOrCreateSubNbt("BlockEntityTag");
                if (!nbtCompound.contains("Items")) {
                    nbtCompound.put("Items", new NbtList());
                }
                PyrotasticMod.LOGGER.info(nbtCompound);

                int i = getBundleOccupancy(bundle);
                int j = getItemOccupancy(stack);
                int k = Math.min(stack.getCount(), (16 - i) / j);
                if (k == 0) {
                    return 0;
                } else {
                    NbtList nbtList = nbtCompound.getList("Items", 10);
                    Optional<NbtCompound> optional = canMergeStack(stack, nbtList);
                    if (optional.isPresent()) {
                        NbtCompound nbtCompound2 = optional.get();
                        ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                        itemStack.increment(k);
                        itemStack.writeNbt(nbtCompound2);
                        nbtList.remove(nbtCompound2);
                        nbtList.add(0, nbtCompound2);
                    } else {
                        ItemStack nbtCompound2 = stack.copy();
                        nbtCompound2.setCount(k);
                        NbtCompound itemStack = new NbtCompound();
                        nbtCompound2.writeNbt(itemStack);
                        nbtList.add(0, itemStack);
                    }

                    return k;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private static Optional<NbtCompound> canMergeStack(ItemStack stack, NbtList items) {
        if (stack.isOf(Items.BUNDLE)) {
            return Optional.empty();
        } else {
            Stream<NbtElement> var10000 = items.stream();
            Objects.requireNonNull(NbtCompound.class);
            var10000 = var10000.filter(NbtCompound.class::isInstance);
            Objects.requireNonNull(NbtCompound.class);
            return var10000.map(NbtCompound.class::cast).filter((item) -> ItemStack.canCombine(ItemStack.fromNbt(item), stack)).findFirst();
        }
    }

    private static int getItemOccupancy(ItemStack stack) {
        if (stack.isOf(Items.BUNDLE)) {
            return 4 + getBundleOccupancy(stack);
        } else {
            if ((stack.isOf(Items.BEEHIVE) || stack.isOf(Items.BEE_NEST)) && stack.hasNbt()) {
                NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
                if (nbtCompound != null && !nbtCompound.getList("Bees", 10).isEmpty()) {
                    return 64;
                }
            }

            return 64 / stack.getMaxCount();
        }
    }

    private static int getBundleOccupancy(ItemStack stack) {
        return getBundledStacks(stack).mapToInt((itemStack) -> getItemOccupancy(itemStack) * itemStack.getCount()).sum();
    }

    private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateSubNbt("BlockEntityTag");
        assert nbtCompound != null;
        if (!nbtCompound.contains("Items")) {
            return Optional.empty();
        } else {
            NbtList nbtList = nbtCompound.getList("Items", 10);
            if (nbtList.isEmpty()) {
                return Optional.empty();
            } else {
                NbtCompound nbtCompound2 = nbtList.getCompound(0);
                ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                nbtList.remove(0);
                if (nbtList.isEmpty()) {
                    stack.removeSubNbt("Items");
                }

                return Optional.of(itemStack);
            }
        }
    }

    private static Stream<ItemStack> getBundledStacks(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateSubNbt("BlockEntityTag");
        if (nbtCompound == null) {
            return Stream.empty();
        } else {
            NbtList nbtList = nbtCompound.getList("Items", 10);
            Stream<NbtElement> var10000 = nbtList.stream();
            Objects.requireNonNull(NbtCompound.class);
            return var10000.map(NbtCompound.class::cast).map(ItemStack::fromNbt);
        }
    }

    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
        Stream<ItemStack> var10000 = getBundledStacks(stack);
        Objects.requireNonNull(defaultedList);
        var10000.forEach(defaultedList::add);
        return Optional.of(new BundleTooltipData(defaultedList, getBundleOccupancy(stack)));
    }

    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add((new TranslatableText("item.minecraft.bundle.fullness", getBundleOccupancy(stack), 16)).formatted(Formatting.GRAY));
    }

    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemUsage.spawnItemContents(entity, getBundledStacks(entity.getStack()));
    }

    @Override
    public boolean canBeNested() {
        return true;
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}


