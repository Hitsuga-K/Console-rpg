package Lesson8

import kotlin.random.Random

class Player(
    val name: String,
    val maxHealth: Int,
    val baseAttack: Int
){
    var currentHealth: Int = maxHealth
    var x: Double = 0.0
    var speed: Double = 2.0

    val inventory: Inventory = Inventory()
    var equippedWeapon: Item? = null
    var equippedArmor: Item? = null

    fun calculateDamage(): Int {
        var damage = baseAttack
        if (equippedWeapon != null) {
            damage += equippedWeapon!!.damageBonus
        }
        return damage
    }

    fun calculateDefense(): Int {
        var defense = 0
        if (equippedArmor != null) {
            defense += equippedArmor!!.defenseBonus
        }
        return defense
    }

    fun attack(): Int {
        val damage = calculateDamage()
        // Добавляем случайность урона от 10 до 40 (по заданию)
        val randomDamage = Random.nextInt(10, 41)
        return randomDamage
    }

    fun takeDamage(incomingDamage: Int): Boolean {
        val defense = calculateDefense()
        val actualDamage = maxOf(0, incomingDamage - defense) // урон не может быть отрицательным
        currentHealth -= actualDamage

        println("$name получил $actualDamage урона (броня заблокировала ${incomingDamage - actualDamage}). Осталось HP: $currentHealth/$maxHealth")

        return currentHealth <= 0
    }

    fun move(deltaTime: Double) {
        x += speed * deltaTime
    }

    fun useHealingPotion(): Boolean {
        val potion = inventory.getAllItems().find { it.type == ItemType.CONSUMABLE }
        if (potion != null) {
            val healAmount = potion.healAmount
            currentHealth = minOf(maxHealth, currentHealth + healAmount)
            inventory.removeItem(potion)
            println("$name использовал ${potion.name} и восстановил $healAmount HP. Теперь HP: $currentHealth/$maxHealth")
            return true
        }
        return false
    }

    fun equipItem(item: Item): Boolean {
        return when (item.type) {
            ItemType.WEAPON -> {
                equippedWeapon = item
                println("$name экипировал оружие: ${item.name} (+${item.damageBonus} урона)")
                true
            }
            ItemType.ARMOR -> {
                equippedArmor = item
                println("$name экипировал броню: ${item.name} (+${item.defenseBonus} защиты)")
                true
            }
            else -> {
                println("${item.name} нельзя экипировать")
                false
            }
        }
    }

    fun printStatus(){
        println("=== Статус игрока: $name ===")
        println("Позиция: x = ${"%.1f".format(x)}")
        println("HP: $currentHealth / $maxHealth")
        println("Базовый урон: $baseAttack")

        val totalDamage = calculateDamage()
        println("Общий урон: $totalDamage (с учетом оружия)")
        println("Защита: ${calculateDefense()}")

        if(equippedWeapon != null){
            println("Оружие: ${equippedWeapon!!.name} (+${equippedWeapon!!.damageBonus} урона)")
        } else {
            println("Оружие: нет")
        }

        if(equippedArmor != null){
            println("Броня: ${equippedArmor!!.name} (+${equippedArmor!!.defenseBonus} защиты)")
        } else {
            println("Броня: нет")
        }

        println("Предметов в инвентаре: ${inventory.getAllItems().size}")
    }
}