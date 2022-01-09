package net.creativityshark.pyrotastic.common.blocks.entities;

import net.creativityshark.pyrotastic.PyrotasticMod;
import net.creativityshark.pyrotastic.common.blocks.PyrotasticBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class FireworksCrateBlockEntity extends BlockEntity implements Inventory {

    private final DefaultedList<ItemStack> inventory;

    public FireworksCrateBlockEntity(BlockPos pos, BlockState state) {
        super(PyrotasticBlocks.FIREWORKS_CRATE_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(16, ItemStack.EMPTY);
    }

    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(getItems(), slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot <= this.inventory.size() && slot > 0) {
            this.inventory.set(slot, stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public boolean isFull() {
        int count = 0;
        boolean result = false;
        for (ItemStack itemStack : this.inventory) {
            count += itemStack.getCount();
            if (count >= this.size()) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
