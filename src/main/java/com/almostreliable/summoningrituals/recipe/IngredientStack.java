package com.almostreliable.summoningrituals.recipe;

import com.almostreliable.summoningrituals.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientStack(Ingredient ingredient, int count) {

    public static IngredientStack fromJson(JsonElement json) {
        if (json instanceof JsonObject jsonObject && jsonObject.has(Constants.COUNT)) {
            var ingredient = Ingredient.fromJson(jsonObject.get(Constants.INGREDIENT));
            var count = GsonHelper.getAsInt(jsonObject, Constants.COUNT);
            return new IngredientStack(ingredient, count);
        }
        return new IngredientStack(Ingredient.fromJson(json), 1);
    }

    public static IngredientStack fromNetwork(FriendlyByteBuf buffer) {
        var ingred = Ingredient.fromNetwork(buffer);
        var count = buffer.readVarInt();
        return new IngredientStack(ingred, count);
    }

    public JsonElement toJson() {
        if (count > 1) {
            var json = new JsonObject();
            json.add(Constants.INGREDIENT, ingredient.toJson());
            json.addProperty(Constants.COUNT, count);
            return json;
        }
        return ingredient.toJson();
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        ingredient.toNetwork(buffer);
        buffer.writeVarInt(count);
    }
}
