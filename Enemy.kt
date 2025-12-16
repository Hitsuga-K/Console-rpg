package Lesson8

import kotlin.random.Random

class Enemy(
    val id: Int,
    x: Double,
    val maxHealth: Int,
    val baseAttack: Int
) {
    var currentHealth: Int = maxHealth
    var x: Double = x

    fun attack(): Int {
        return Random.nextInt(5, 21)
    }

    fun takeDamage(damage: Int): Boolean {
        currentHealth -= damage
        println("Враг $id получил $damage урона. Осталось HP: $currentHealth/$maxHealth")
        return currentHealth <= 0
    }

    fun printStatus() {
        println("=== Враг $id ===")
        println("Позиция: x = ${"%.1f".format(x)}")
        println("HP: $currentHealth / $maxHealth")
        println("Урон: $baseAttack")
    }
}