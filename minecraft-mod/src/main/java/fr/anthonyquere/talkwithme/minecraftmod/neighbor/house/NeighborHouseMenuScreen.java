package fr.anthonyquere.talkwithme.minecraftmod.neighbor.house;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.registries.MenuRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class NeighborHouseMenuScreen extends ItemCombinerScreen<NeighborHouseMenu> {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("voisins:textures/screens/neighbor_house_menu.png");

  public NeighborHouseMenuScreen(NeighborHouseMenu p_98901_, Inventory p_98902_, Component p_98903_) {
    super(p_98901_, p_98902_, p_98903_, RESOURCE_LOCATION);
    this.imageHeight = 166;
    this.imageWidth = 176;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
    guiGraphics.blit(RESOURCE_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
  }

  @Override
  protected void renderErrorIcon(GuiGraphics p_281990_, int p_266822_, int p_267045_) {

  }
}
