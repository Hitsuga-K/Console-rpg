package Lesson8

class Inventory {
    private val items: MutableList<Item> = mutableListOf()

    fun addItem(item: Item) {
        items.add(item)
        println(" + ${item.name} добавлен в инвентарь")
    }

    fun removeItem(item: Item): Boolean {
        val removed = items.remove(item)
        if (removed) {
            println(" - ${item.name} удалён из инвентаря")
        } else {
            println(" ! Не удалось удалить ${item.name}")
        }
        return removed
    }

    fun printInventory() {
        if (items.isEmpty()) {
            println("Инвентарь пуст")
            return
        }

        println("=== ИНВЕНТАРЬ ===")

        for ((index, item) in items.withIndex()) {
            println("${index + 1}. ${item.name} [${item.type}]")
        }
    }

    fun findItemByName(name: String): Item? {
        for (item in items) {
            if (item.name == name) {
                return item
            }
        }
        return null
    }

    fun getAllItems(): List<Item> {
        return items.toList()
    }
}