package com.aetherteam.aether.integration.rei.categories.ban;

import com.aetherteam.aether.integration.rei.AetherREIServerPlugin;
import com.aetherteam.aether.recipe.recipes.ban.AbstractPlacementBanRecipe;
import com.aetherteam.aether.recipe.recipes.ban.BlockBanRecipe;
import com.aetherteam.aether.recipe.recipes.ban.ItemBanRecipe;
import com.aetherteam.nitrogen.integration.rei.REIUtils;
import com.aetherteam.nitrogen.recipe.BlockStateIngredient;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO: Implement DisplaySerializer within the future
public class PlacementBanRecipeDisplay<R extends AbstractPlacementBanRecipe<?, ?>> extends BasicDisplay {
    private final CategoryIdentifier<?> categoryIdentifier;

    private final Optional<BlockStateIngredient> bypassBlock;

    private final Optional<ResourceKey<Biome>> biomeKey;
    private final Optional<TagKey<Biome>> biomeTag;

    private final Optional<BlockStateIngredient> blockStateIngredient;

    protected PlacementBanRecipeDisplay(CategoryIdentifier<? extends PlacementBanRecipeDisplay<R>> categoryIdentifier, List<EntryIngredient> inputs, Optional<BlockStateIngredient> bypassBlock, Optional<ResourceKey<Biome>> biomeKey, Optional<TagKey<Biome>> biomeTag, Optional<BlockStateIngredient> blockStateIngredient, Optional<ResourceLocation> location){
        super(inputs, List.of(), location);
        this.bypassBlock = bypassBlock;
        this.biomeKey = biomeKey;
        this.biomeTag = biomeTag;
        this.blockStateIngredient = blockStateIngredient;
        this.categoryIdentifier = categoryIdentifier;
    }

    protected PlacementBanRecipeDisplay(R recipe, CategoryIdentifier<? extends PlacementBanRecipeDisplay<R>> categoryIdentifier, List<EntryIngredient> inputs) {
        this(categoryIdentifier,
                inputs,
                recipe.getBypassBlock(),
                recipe.getBiome().left(),
                recipe.getBiome().right(),
                Optional.ofNullable((recipe.getIngredient() instanceof BlockStateIngredient ingredient) ? ingredient : null),
                Optional.empty());
    }

    public static PlacementBanRecipeDisplay<ItemBanRecipe> ofItem(ItemBanRecipe recipe){
        List<EntryIngredient> list = new ArrayList<>();
        if (recipe.getBypassBlock().isPresent()) {
            list.addAll(REIUtils.toIngredientList(recipe.getBypassBlock().get().getPairs()));
        }

        list.add(EntryIngredients.ofIngredient(recipe.getIngredient()));

        return new PlacementBanRecipeDisplay<>(recipe, AetherREIServerPlugin.ITEM_PLACEMENT_BAN, list);
    }

    public static PlacementBanRecipeDisplay<BlockBanRecipe> ofBlock(BlockBanRecipe recipe){
        List<EntryIngredient> list = new ArrayList<>();
        if (recipe.getBypassBlock().isPresent()) {
            list.addAll(REIUtils.toIngredientList(recipe.getBypassBlock().get().getPairs()));
        }

        list.addAll(REIUtils.toIngredientList(recipe.getIngredient().getPairs()));

        return new PlacementBanRecipeDisplay<>(recipe, AetherREIServerPlugin.BLOCK_PLACEMENT_BAN, list);
    }

    public Optional<BlockStateIngredient> getBypassBlock() {
        return this.bypassBlock;
    }

    public Optional<ResourceKey<Biome>> getBiomeKey() {
        return this.biomeKey;
    }

    public Optional<TagKey<Biome>> getBiomeTag() {
        return this.biomeTag;
    }

    @Nullable
    public BlockStateIngredient getBlockStateIngredient() {
        return this.blockStateIngredient.orElse(null);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return this.categoryIdentifier;
    }
}
