package com.example.flowusecases

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainViewModel : ViewModel() {

    val foodProducer: Flow<FoodOrder> = flow {

        (1..30).forEach {
            delay(2000)

            val random = (1..5).random()
            val item = getRandomFoodItem(random)

            emit(FoodOrder(orderNumber = it, foodItem = item))
        }
    }

    val pizzaProducer: Flow<FoodOrder> = flow {
        (1..30).forEach {
            delay(2000)
            emit(FoodOrder(orderNumber = it, foodItem = FoodItem.Pizza()))
        }
    }

    val toppingProducer: Flow<Topping> = flow {
        (1..30).forEach {
            delay(2000)

            val random = (1..5).random()
            val topping = getRandomTopping(random)

            emit(topping)
        }
    }

    private fun getRandomFoodItem(random: Int): FoodItem {
        return when (random) {
            1 -> FoodItem.Pizza()
            2 -> FoodItem.Sandwich()
            3 -> FoodItem.Burrito()
            4 -> FoodItem.Taco()
            else -> FoodItem.Burger()
        }
    }

    private fun getRandomTopping(random: Int): Topping {
        return when (random) {
            1 -> Topping.Onion()
            2 -> Topping.Tomato()
            3 -> Topping.Corn()
            4 -> Topping.Paneer()
            else -> Topping.None()
        }
    }

}