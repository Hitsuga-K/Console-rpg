package Lesson8

open class GameObject(

    var x: Double, // x - позиционирование объекта по оси х (горизонтально)

    var speed: Double // скорость перемещения объекта(сколько единиц по х объёект пройдёт в секунду)

){
    open fun update(deltaTimeMillis: Double){
        // open fun - мето который можно переопределить в наследниках (override)

        x += speed * deltaTimeMillis
        // Считаем солько единиц по х мы должны пройти за deltaTimeMillis времени
        // Пример:
        // speed 2.0 (2 юнита/секунду), delta = 0.5 сек
        // d = 2.0 * 0.5 = 1.0 (1 юнит за пол секунды)
    }
}


