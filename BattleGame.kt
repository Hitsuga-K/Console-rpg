package Lesson8

import kotlin.random.Random

class BattleGame {

    val player = Player("Юра", 100, 15)

    private val enemies = mutableListOf<Enemy>()
    private val itemsOnMap = mutableListOf<Item>()
    private val finishX = 110.0

    fun generateEnemies() {
        val howManyEnemies = Random.nextInt(2, 5)
        println("Появилось $howManyEnemies врагов")

        for (i in 1..howManyEnemies) {
            val x = Random.nextDouble(25.0, 100.0)
            val hp = Random.nextInt(40, 91)
            val attack = Random.nextInt(5, 16)

            enemies.add(Enemy(i, x, hp, attack))
            println("Враг $i на позиции x = ${"%.1f".format(x)}, HP: $hp")
        }

        enemies.sortBy { it.x }
    }

    fun generateItems() {
        val howManyItems = Random.nextInt(1, 5)

        for (i in 1..howManyItems) {
            if (Random.nextDouble() < 0.3) {
                val x = Random.nextDouble(0.0, finishX)
                val item = makeRandomItem(i, x)
                itemsOnMap.add(item)
                println("Предмет ${item.name} на позиции x = ${"%.1f".format(x)}")
            }
        }
    }

    private fun makeRandomItem(id: Int, x: Double): Item {
        val randomNumber = Random.nextInt(1, 4)

        return when (randomNumber) {
            1 -> Item(
                id = id,
                name = "HERRSHER OF HUMANITY",
                description = "Был украден у Элизии",
                price = 50,
                type = ItemType.WEAPON,
                damageBonus = id * 5,
                defenseBonus = 0,
                healAmount = 0
            )
            2 -> Item(
                id = id,
                name = "Броня темми",
                description = "Вот что можно делать с высшим образованием!",
                price = 40,
                type = ItemType.ARMOR,
                damageBonus = 0,
                defenseBonus = id * 3,
                healAmount = 0
            )
            else -> Item(
                id = id,
                name = "Кока кола (без сахара)",
                description = "Восстанавливает +20HP",
                price = 30,
                type = ItemType.CONSUMABLE,
                damageBonus = 0,
                defenseBonus = 0,
                healAmount = 20
            )
        }
    }

    private fun checkForItems() {
        val nearbyItems = itemsOnMap.filter { Math.abs(it.id.toDouble() - player.x) < 0.5 }

        for (item in nearbyItems) {
            player.inventory.addItem(item)
            itemsOnMap.remove(item)
            println("${player.name} подобрал: ${item.name}")

            if (item.type == ItemType.WEAPON && player.equippedWeapon == null) {
                player.equipItem(item)
            } else if (item.type == ItemType.ARMOR && player.equippedArmor == null) {
                player.equipItem(item)
            }
        }
    }

    private fun turnBasedBattle(enemy: Enemy): Boolean {
        println("\n=== БОЙ С ВРАГОМ ${enemy.id} ===")

        val potion = player.inventory.getAllItems().find { it.type == ItemType.CONSUMABLE }

        while (true) {
            println("\n--- ТВОЙ ХОД ---")
            println("Твоё HP: ${player.currentHealth}/${player.maxHealth}")
            println("HP врага ${enemy.id}: ${enemy.currentHealth}/${enemy.maxHealth}")
            println("\nЧто делаем?")
            println("1. Бить врага")

            if (potion != null) {
                println("2. Выпить колу (+${potion.healAmount} HP)")
            } else {
                println("2. Нет коле :(")
            }

            println("3. Посмотреть свои статы")
            print("Выбирай (1-3): ")

            val choice = readlnOrNull()

            when (choice) {
                "1" -> {
                    val damage = player.attack()
                    val enemyDead = enemy.takeDamage(damage)

                    if (enemyDead) {
                        println("Ты победил врага ${enemy.id}!")
                        enemies.remove(enemy)
                        return true
                    }
                }
                "2" -> {
                    if (potion != null) {
                        val used = player.useHealingPotion()
                        if (!used) {
                            println("Нет коле!")
                        }
                    } else {
                        println("Нет коле!")
                    }
                    continue
                }
                "3" -> {
                    player.printStatus()
                    continue
                }
                else -> {
                    println("Не понял... Пропускаю ход")
                }
            }

            if (choice == "1" || (choice != "2" && choice != "3")) {
                println("\n--- ХОД ВРАГА ---")
                val enemyDamage = enemy.attack()
                val playerDead = player.takeDamage(enemyDamage)

                if (playerDead) {
                    println("Ты проиграл врагу ${enemy.id}!")
                    return false
                }
            }

            if (enemy.currentHealth <= 0) {
                println("Ты победил врага ${enemy.id}!")
                enemies.remove(enemy)
                return true
            }
        }
    }

    fun startGame() {
        println("=== НАЧАЛО ИГРЫ ===")
        println("Цель: дойти до x = $finishX")
        println("Игра сама двигается, при встрече с врагом - бой")

        generateEnemies()
        generateItems()

        val time = GameTime()
        var gameIsRunning = true

        val sword = Item(
            id = 11,
            name = "НУ палка?",
            description = "отссылка на андертейл?",
            price = 25,
            type = ItemType.WEAPON,
            damageBonus = 5,
            defenseBonus = 0,
            healAmount = 0
        )

        val armor = Item(
            id = 12,
            name = "Шлем в виде орешка Биг Боб",
            description = "я лучше ничего не придумал сорри",
            price = 50,
            type = ItemType.ARMOR,
            damageBonus = 0,
            defenseBonus = 3,
            healAmount = 0
        )

        val smallPotion = Item(
            id = 13,
            name = "Кока кола (без сахара)",
            description = "ВОсстанавливает 20 HP",
            price = 100,
            type = ItemType.CONSUMABLE,
            damageBonus = 0,
            defenseBonus = 0,
            healAmount = 20
        )

        println("\n=== ДАЛИ СТАРТОВЫЕ ПРЕДМЕТЫ ===")
        player.inventory.addItem(sword)
        player.inventory.addItem(armor)
        player.inventory.addItem(smallPotion)

        player.equipItem(sword)
        player.equipItem(armor)

        while (gameIsRunning && player.x < finishX) {
            val oldPosition = player.x

            time.update()
            val dt = time.deltaTimeSeconds

            player.move(dt)

            checkForItems()

            val enemyNear = enemies.firstOrNull { Math.abs(it.x - player.x) < 0.5 }

            if (enemyNear != null) {
                println("\nВстретил врага ${enemyNear.id} на x = ${"%.1f".format(player.x)}")

                player.x = oldPosition
                println("Остановился для боя на x = ${"%.1f".format(player.x)}")

                val won = turnBasedBattle(enemyNear)

                if (!won) {
                    println("=== ПРОИГРАЛ ===")
                    gameIsRunning = false
                    break
                }

                time.update()
            }

            println("\n--- Время: ${"%.1f".format(time.totalTimeSeconds)} сек ---")
            player.printStatus()
            println("Врагов осталось: ${enemies.size}")
            println("До финиша: ${"%.1f".format(finishX - player.x)}")

            if (player.x >= finishX) {
                println("\n=== ПОБЕДА! ===")
                println("Дошел до финиша!")
                println("Здоровье: ${player.currentHealth}/${player.maxHealth}")
                println("Убил врагов: осталось ${enemies.size}")
                gameIsRunning = false
            }

            Thread.sleep(1000)
        }

        if (enemies.isNotEmpty() && player.currentHealth > 0 && player.x < finishX) {
            println("\n=== КОНЕЦ ===")
            println("Выжил, но не дошел")
            println("Осталось пройти: ${"%.1f".format(finishX - player.x)}")
        }
    }
}