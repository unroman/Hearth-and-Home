package com.crispytwig.hearth_and_home.registry.forge;

import com.crispytwig.hearth_and_home.mixin.forge.FireBlockAccessor;
import com.crispytwig.hearth_and_home.registry.ModRegistry;
import com.crispytwig.hearth_and_home.Mod;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.Supplier;

public class ModRegistryImpl {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Mod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mod.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Mod.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Mod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Mod.MOD_ID);

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        return SOUND_EVENTS.register(name, soundEvent);
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, category).sized(width, height).build(name));
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> registerBlockEntityType(String name, Supplier<T> blockEntity) {
        return BLOCK_ENTITY_TYPES.register(name, blockEntity);
    }

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(ModRegistry.BlockEntitySupplier<T> blockEntity, Block... validBlocks) {
        return BlockEntityType.Builder.of(blockEntity::create, validBlocks).build(null);
    }

    public static CreativeModeTab registerCreativeModeTab(ResourceLocation name, Supplier<ItemStack> icon) {
        return new CreativeModeTab(name.getNamespace()) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider) {
        EntityRenderers.register(type.get(), renderProvider);
    }

    public static <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<T> renderProvider) {
        BlockEntityRenderers.register(type.get(), renderProvider);
    }

    public static <T extends Block> void setFlammable(Block fireBlock, Supplier<T> block, int encouragement, int flammability) {
        ((FireBlockAccessor)fireBlock).invokeSetFlammable(block.get(), encouragement, flammability);
    }

    public static boolean isForge() {
        return true;
    }
}
