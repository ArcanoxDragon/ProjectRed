package mrtjp.projectred.transmission;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.texture.SpriteRegistryHelper;
import codechicken.lib.util.SneakyUtils;
import codechicken.multipart.api.MultiPartType;
import codechicken.multipart.api.SimpleMultiPartType;
import codechicken.multipart.api.part.TMultiPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by covers1624 on 22/12/20.
 */
public class WireType implements SimpleMultiPartType.SimpleMultiPartTypeFactory<TMultiPart> {
    static ArrayList<WireType> registeredWireTypes = new ArrayList<>();

    public static WireType registerWireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, int thickness, String... textures) {
        return registerWireType(new WireType(modId, itemSupplier, partSupplier, partFactory, thickness, textures));
    }

    public static WireType registerWireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, EnumColour colour, int thickness, String... textures) {
        return registerWireType(new WireType(modId, itemSupplier, partSupplier, partFactory, colour, thickness, textures));
    }

    public static WireType registerWireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, int thickness, int itemColour, String... textures) {
        return registerWireType(new WireType(modId, itemSupplier, partSupplier, partFactory, thickness, itemColour, textures));
    }

    public static WireType registerWireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, EnumColour colour, int thickness, int itemColour, String... textures) {
        return registerWireType(new WireType(modId, itemSupplier, partSupplier, partFactory, colour, thickness, itemColour, textures));
    }

    private static WireType registerWireType(WireType wireType) {
        registeredWireTypes.add(wireType);
        return wireType;
    }

    private final String modId;
    private final Supplier<Supplier<? extends ItemPartWire>> itemSupplier;
    private final Supplier<Supplier<MultiPartType<?>>> partSupplier;
    private final Function<WireType, TWireCommons> partFactory;
    private final EnumColour colour;
    private final int thickness;
    private final int itemColour;
    private final List<String> textureNames;

    @OnlyIn(Dist.CLIENT)
    private List<TextureAtlasSprite> textures;
    private ItemPartWire item;
    private MultiPartType<?> partType;

    private WireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, int thickness, String... textures) {
        this(modId, itemSupplier, partSupplier, partFactory, null, thickness, textures);
    }

    private WireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, EnumColour colour, int thickness, String... textures) {
        this(modId, itemSupplier, partSupplier, partFactory, colour, thickness, 0xFFFFFF, textures);
    }

    private WireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, int thickness, int itemColour, String... textures) {
        this(modId, itemSupplier, partSupplier, partFactory, null, thickness, itemColour, textures);
    }

    private WireType(String modId, Supplier<Supplier<? extends ItemPartWire>> itemSupplier, Supplier<Supplier<MultiPartType<?>>> partSupplier, Function<WireType, TWireCommons> partFactory, EnumColour colour, int thickness, int itemColour, String... textures) {
        this.modId = modId;
        this.itemSupplier = itemSupplier;
        this.partSupplier = partSupplier;
        this.partFactory = partFactory;
        this.colour = colour;
        this.thickness = thickness;
        this.itemColour = itemColour;
        this.textureNames = ImmutableList.copyOf(textures);
    }

    public ItemPartWire getItem() {
        if (item == null) {
            item = itemSupplier.get().get();
        }
        return item;
    }

    public ItemStack makeStack() {
        return new ItemStack(getItem());
    }

    public MultiPartType<?> getPartType() {
        if (partType == null) {
            partType = partSupplier.get().get();
        }
        return partType;
    }

    public TWireCommons newPart() {
        return partFactory.apply(this);
    }

    @Override
    public TMultiPart create(boolean client) {
        return SneakyUtils.unsafeCast(newPart());
    }

    public EnumColour getColour() {
        return colour;
    }

    public int getColourIdx() {
        return colour == null ? -1 : colour.getWoolMeta();
    }

    public int getThickness() {
        return thickness;
    }

    public int getItemColour() {
        return itemColour;
    }

    @OnlyIn(Dist.CLIENT)
    public List<TextureAtlasSprite> getTextures() {
        return textures;
    }

    @OnlyIn(Dist.CLIENT)
    void registerTextures(SpriteRegistryHelper spriteHelper) {
        if (textureNames.isEmpty()) {
            return;
        }
        textures = new ArrayList<>(textureNames.size());
        for (int i = 0; i < textureNames.size(); i++) {
            textures.add(null);
        }
        spriteHelper.addIIconRegister(SpriteRegistryHelper.TEXTURES, registrar -> {
            for (int i = 0; i < textureNames.size(); i++) {
                int finalI = i;
                ResourceLocation tex = new ResourceLocation(this.modId, "block/" + textureNames.get(i));
                registrar.registerSprite(tex, sprite -> textures.set(finalI, sprite));
            }
        });
    }
}
