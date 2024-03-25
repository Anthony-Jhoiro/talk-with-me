package fr.anthonyquere.talkwithme.minecraftmod.registries;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.NeighborHouseMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public class MenuRegistry {
  public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

  public static final RegistryObject<MenuType<NeighborHouseMenu>> NeighborHouseMenu = REGISTRY.register("neighbor_house_menu",
    () -> IForgeMenuType.create(NeighborHouseMenu::new)
  );



}
