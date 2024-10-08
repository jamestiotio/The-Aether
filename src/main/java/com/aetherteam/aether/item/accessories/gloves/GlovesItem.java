package com.aetherteam.aether.item.accessories.gloves;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.item.accessories.AccessoryItem;
import com.aetherteam.aether.item.accessories.SlotIdentifierHolder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.UUID;

public class GlovesItem extends AccessoryItem implements SlotIdentifierHolder {
    protected final Holder<ArmorMaterial> material;
    protected final double damage;
    protected ResourceLocation GLOVES_TEXTURE;

    public GlovesItem(Holder<ArmorMaterial> material, double punchDamage, String glovesName, Holder<SoundEvent> glovesSound, Properties properties) {
        this(material, punchDamage, ResourceLocation.fromNamespaceAndPath(Aether.MODID, glovesName), glovesSound, properties);
    }

    public GlovesItem(Holder<ArmorMaterial> material, double punchDamage, ResourceLocation glovesName, Holder<SoundEvent> glovesSound, Properties properties) {
        super(glovesSound, properties);
        this.material = material;
        this.damage = punchDamage;
        this.setRenderTexture(glovesName.getNamespace(), glovesName.getPath());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Gloves Damage Bonus", this.damage, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

    /**
     * Warning for "deprecation" is suppressed because the method is fine to override.
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack item, ItemStack material) {
        return this.material.getRepairIngredient().test(material) || super.isValidRepairItem(item, material);
    }

    public Holder<ArmorMaterial> getMaterial() {
        return this.material;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setRenderTexture(String modId, String registryName) {
        this.GLOVES_TEXTURE = ResourceLocation.fromNamespaceAndPath(modId, "textures/models/accessory/gloves/" + registryName + "_accessory.png");
    }

    public ResourceLocation getGlovesTexture() {
        return this.GLOVES_TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    public ImmutableTriple<Float, Float, Float> getColors(ItemStack stack) {
        float red = 1.0F;
        float green = 1.0F;
        float blue = 1.0F;
        if (stack.getItem() instanceof LeatherGlovesItem leatherGlovesItem) {
            int i = leatherGlovesItem.getColor(stack);
            red = (float) (i >> 16 & 255) / 255.0F;
            green = (float) (i >> 8 & 255) / 255.0F;
            blue = (float) (i & 255) / 255.0F;
        }
        return new ImmutableTriple<>(red, green, blue);
    }

    /**
     * @return {@link GlovesItem}'s own identifier for its accessory slot,
     * using a static method as it is used in other conditions without access to an instance.
     */
    @Override
    public String getIdentifier() {
        return getIdentifierStatic();
    }

    /**
     * @return {@link GlovesItem}'s own identifier for its accessory slot.
     */
    public static String getIdentifierStatic() {
        return AetherConfig.COMMON.use_curios_menu.get() ? "hands" : "aether_gloves";
    }
}
