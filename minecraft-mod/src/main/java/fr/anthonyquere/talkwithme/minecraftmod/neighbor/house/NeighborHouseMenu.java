package fr.anthonyquere.talkwithme.minecraftmod.neighbor.house;

import fr.anthonyquere.talkwithme.minecraftmod.registries.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NeighborHouseMenu extends ItemCombinerMenu {

  private final List<Block> requiredBlocks;
  private final Block resultBlock;

  private static final List<Integer> blockPositions = List.of(61, 79, 97);
  private static final Integer BLOCKS_Y_OFFSET = 44;


  public NeighborHouseMenu(int containerId, Inventory inventory, Player player, List<Block> requiredBlocks, Block resultBlock) {
    super(
      MenuRegistry.NeighborHouseMenu.get(),
      containerId,
      inventory,
      ContainerLevelAccess.create(inventory.player.level(), player.blockPosition())
    );

    this.requiredBlocks = requiredBlocks;
    this.resultBlock = resultBlock;
  }

  public NeighborHouseMenu(int containerId, Inventory inventory, FriendlyByteBuf ignored) {

    super(MenuRegistry.NeighborHouseMenu.get(),
      containerId,
      inventory,
      ContainerLevelAccess.create(inventory.player.level(), inventory.player.blockPosition()));

    requiredBlocks = List.of(Blocks.AIR, Blocks.AIR, Blocks.AIR);
    resultBlock = Blocks.AIR;
  }

  @Override
  protected boolean mayPickup(Player p_39798_, boolean p_39799_) {
    for (var i = 0; i < 3; i++) {
      if (!this.slots.get(i).hasItem()) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected void onTake(Player p_150601_, ItemStack p_150602_) {
    for (var i = 0; i < 3; i++) {
      var slot = this.slots.get(i);
      slot.remove(1);
      if (slot.getItem().isEmpty()) {
        slot.set(ItemStack.EMPTY);
      }
    }
  }

  @Override
  protected boolean isValidBlock(BlockState p_39788_) {
    return true;
  }

  @Override
  public void createResult() {
    for (var i = 0; i < 3; i++) {
      if (!this.slots.get(i).hasItem() ||
        !this.slots.get(i).getItem().getDescriptionId().equals(requiredBlocks.get(i).getDescriptionId())) {
        this.resultSlots.setItem(0, ItemStack.EMPTY);
        return;
      }
    }
    var stack = new ItemStack(resultBlock);
    stack.setCount(1);
    this.resultSlots.setItem(1, stack);
  }

  @Override
  protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
    var builder = ItemCombinerMenuSlotDefinition.create();

    for (var i = 0; i < 3; i++) {
      int finalI = i;
      builder = builder.withSlot(i, blockPositions.get(i), BLOCKS_Y_OFFSET, (itemStack) ->
        itemStack.getCount() >= 1 &&
          requiredBlocks.get(finalI).getDescriptionId().equals(itemStack.getItem().getDescriptionId()));
    }

    builder.withResultSlot(3, 133, BLOCKS_Y_OFFSET);

    return builder.build();
  }


}
