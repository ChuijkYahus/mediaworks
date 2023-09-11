package io.github.artynova.mediaworks.macula;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.util.NbtHelpers;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.NotNull;

public class TextVisage extends Visage {
    public static final Vec2f UNBOUNDED_DIMENSIONS = new Vec2f(-1, -1);
    public static final String TEXT_TAG = "text";
    public static final String ORIGIN_TAG = "origin";
    public static final String DIMENSIONS_TAG = "dims";
    public static VisageType<TextVisage> TYPE = new VisageType<>() {
        @Override
        public TextVisage deserializeData(NbtCompound compound) {
            Text text = Text.Serializer.fromJson(compound.getString(TEXT_TAG));
            Vec2f origin = NbtHelpers.deserializeVec2(compound.getList(ORIGIN_TAG, NbtElement.FLOAT_TYPE));
            Vec2f dimensions = NbtHelpers.deserializeVec2(compound.getList(DIMENSIONS_TAG, NbtElement.FLOAT_TYPE));
            return new TextVisage(text, origin, dimensions);
        }

        @Override
        public @NotNull NbtCompound serializeData(TextVisage visage) {
            NbtCompound compound = new NbtCompound();
            compound.put(TEXT_TAG, NbtString.of(Text.Serializer.toJson(visage.getText())));
            compound.put(ORIGIN_TAG, NbtHelpers.serializeVec2(visage.getOrigin()));
            compound.put(DIMENSIONS_TAG, NbtHelpers.serializeVec2(visage.getDimensions()));
            return compound;
        }
    };

    private final Text text;
    private final Vec2f origin;
    private final Vec2f dimensions;

    public TextVisage(Text text, Vec2f origin, Vec2f dimensions) {
        super(TYPE);
        this.text = text;
        this.origin = origin;
        if ((dimensions.x < 0 && dimensions.x != -1) || (dimensions.y < 0 && dimensions.y != -1)) {
            throw new IllegalArgumentException("Illegal TextVisage sizes: " + dimensions);
        }
        this.dimensions = dimensions;
    }

    public TextVisage(Text text, Vec2f origin) {
        this(text, origin, UNBOUNDED_DIMENSIONS);
    }

    public static Text captureText(Iota iota) {
        return iota.display();
    }

    public Text getText() {
        return text;
    }

    public Vec2f getOrigin() {
        return origin;
    }

    public Vec2f getDimensions() {
        return dimensions;
    }

    public boolean isBounded() {
        return !UNBOUNDED_DIMENSIONS.equals(getDimensions());
    }
}
