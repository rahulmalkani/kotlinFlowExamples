package com.example.flowusecases

sealed class FoodItem(val name: String) {
    class Pizza() : FoodItem("Pizza")
    class Sandwich() : FoodItem("Sandwich")
    class Burger() : FoodItem("Burger")
    class Taco() : FoodItem("Taco")
    class Burrito() : FoodItem("Burrito")
}

sealed class Topping(val name: String) {
    class Onion() : Topping("Crispy Onions")
    class Tomato() : Topping("Red Tomatoes")
    class Corn() : Topping("Sweet Corns")
    class Paneer() : Topping("Tandoori Paneer")
    class None() : Topping("None")
}

data class FoodOrder(
    val foodItem: FoodItem,
    val orderNumber: Int,
    var topping: Topping = Topping.None()
)