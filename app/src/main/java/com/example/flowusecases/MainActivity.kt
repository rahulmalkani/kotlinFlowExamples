package com.example.flowusecases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.flowusecases.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        openPizzaWithToppingsCounter()

    }

    private fun openFoodCounter() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.foodProducer
                .collect {
                    withContext(Dispatchers.Main) {
                        announceOrder(it)
                    }
                }
        }
    }

    /* To make sure that we serve only burgers in this counter
        we're using filter operator.
     */
    private fun openBurgerOnlyCounter() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.foodProducer
                .filter { it.foodItem is FoodItem.Burger }
                .collect {
                    withContext(Dispatchers.Main) {
                        announceOrder(it)
                    }
                }
        }
    }

    /* To make sure that we don't giveaway same items to 2 people
        consecutively, We're using distinctUntilChanged operator.
     */
    private fun openGiveAwayCounter() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.foodProducer
                .distinctUntilChanged { old, new -> old.foodItem.name == new.foodItem.name }
                .collect {
                    withContext(Dispatchers.Main) {
                        announceOrder(it)
                    }
                }
        }
    }

    /* We add-on free toppings to pizzas on this counter, we're
        using map operator to add free toppings to pizzas.
     */
    private fun openPizzaWithFreeToppingsCounter() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.pizzaProducer
                .map { order ->
                    addFreeToppings(order)
                }.collect {
                    withContext(Dispatchers.Main) {
                        announceOrder(it)
                    }
                }
        }
    }

    private fun openPizzaWithToppingsCounter() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.pizzaProducer.zip(viewModel.toppingProducer) { pizza, topping ->
                pizza.topping = topping
                pizza
            }.collect {
                withContext(Dispatchers.Main) {
                    announceOrder(it)
                }
            }
        }
    }


    private fun addFreeToppings(foodOrder: FoodOrder): FoodOrder {
        foodOrder.topping = getFreeTopping()
        return foodOrder
    }

    private fun getFreeTopping(): Topping {
        return when ((1..4).random()) {
            1 -> Topping.Onion()
            2 -> Topping.Tomato()
            3 -> Topping.Corn()
            else -> Topping.Paneer()
        }
    }

    private fun announceOrder(order: FoodOrder) {
        binding.title.text = getString(R.string.order_title, order.orderNumber.toString())

        if (order.topping.name == "None") {
            binding.announcementBoard.text =
                getString(R.string.order_notification, order.foodItem.name)
        } else {
            binding.announcementBoard.text = getString(
                R.string.pizza_order_notification,
                order.foodItem.name,
                order.topping.name
            )
        }

    }

}