recipe = """
{
    "conditions": [
    {
      "type": "config",
      "enabled": "quark"
    }
    ],
    "result": {
        "item": "quark:candle",
        "data": META
    },
    "ingredients": [
        {
            "item": "betterwithmods:candle",
            "data": META
        }
    ],
    "type": "forge:ore_shapeless"
}

"""

bwm_recipe = """
{
    "conditions": [
    {
      "type": "config",
      "enabled": "quark"
    }
    ],
    "result": {
        "item": "betterwithmods:candle",
        "data": META
    },
    "ingredients": [
        {
            "item": "quark:candle",
            "data": META
        }
    ],
    "type": "forge:ore_shapeless"
}

"""


for i in range(0, 16):
    s = str(i)
    with open('quark_candle_' + s + '.json', 'w+') as f:
        f.write(recipe.replace('META', s, 2))
    with open('candle_' + s + '.json', 'w+') as f:
        f.write(bwm_recipe.replace('META', s, 2))
# class Ingredient(object):
#     pass
#
#
# class ItemStack(Ingredient):
#     def __init__(self, item, data):
#         self.item = item
#         self.data = data
#
#
# class Recipe(object):
#     def __init__(self, type):
#         self.type = type
#
#
# class ShapelessRecipe(Recipe):
#     def __init__(self, result, ingredients):
#         super().__init__("forge:ore_shapeless")
#         self.result = result
#         self.ingredients = ingredients
#
#
# bwm_candle = ItemStack("betterwithmods:candle", 0)
# quark_candle = ItemStack("quark:candle", 0)
# recipe = ShapelessRecipe(bwm_candle, [quark_candle])
# import json
#
# print(json.dumps(recipe))

#
