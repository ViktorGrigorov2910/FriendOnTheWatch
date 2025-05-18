package com.example.qloq.presentation

class EPet(
    var name: String,
    var hunger: Int,
    var thirst: Int,
    var happiness: Int
) {

    private var decrementMultiplier = 0.0001
    fun incHunger() {
        this.hunger++
    }

    fun incThirst() {
        this.thirst++
    }

    fun incHappiness() {
        this.happiness++
    }

    fun updateStats(timeDelta: Long) {
        this.hunger -= (timeDelta * decrementMultiplier).toInt()
        this.thirst -= (timeDelta * decrementMultiplier).toInt()
        this.happiness -= (timeDelta * decrementMultiplier).toInt()

        checkStats()
    }

    fun checkStats() {
        //Sets and verifies upper and lower bounds
        if (hunger <= 0) {
            hunger = 0
        }
        if (thirst <= 0) {
            thirst = 0
        }
        if (happiness <= 0) {
            happiness = 0
        }
        if (happiness >= 100) {
            happiness = 100
        }
        if (thirst >= 100) {
            thirst = 100
        }
        if (hunger >= 100) {
            hunger = 100
        }
    }

    fun getAverage(): Int {
        return ((happiness + thirst + hunger) / 3)
    }
}