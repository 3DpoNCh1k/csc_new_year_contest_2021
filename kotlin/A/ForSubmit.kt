package SUBMIT

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

typealias Function = (Double) -> Double

/**
 * Интерполируем функцию по набору значений [xs] и [ys]. Длины наборов должны быть одинаковыми
 */
fun interpolate(xs: List<Double>, ys: List<Double>): Function {
    require(xs.size == ys.size) { "Длина набора xs - ${xs.size}, а набора ys - ${ys.size}" }
    // return an anonymous function
    return { x ->
        // check interpolation region. Zero outside the region
        if (x < xs.first() || x > xs.last()) 0.0 else {
            // find section number
            val num: Int = xs.indexOfFirst { it > x }
            // num >=1
            if (num < 0) {
                if (x == xs.last()) ys.last() else 0.0
            }
            else if (num == 0) {
                if (xs[0] == x) ys[0] else 0.0
            } else {
                //return the result as last expression
                ys[num - 1] + (ys[num] - ys[num - 1]) / (xs[num] - xs[num - 1]) * (x - xs[num - 1])
            }
        }
    }
}
/**
 * Генерируем точки по диапазону
 */
fun pointsFromRange(from: Double, to: Double, numPoints: Int = 100): List<Double> {
    require(to > from)
    require(numPoints > 2)
    val step = (to - from) / (numPoints - 1)
    require(step > 0)
    return List(numPoints) { i -> from + step * i }
}

/**
 * Интерполируем значения функций в диапазоне от [from] до [to] с количеством узлов [numPoints
 */
fun Function.interpolate(from: Double, to: Double, numPoints: Int = 100): Function {
    //compute xs
    val xs = pointsFromRange(from, to, numPoints)
    //compute function values
    val ys = xs.map { invoke(it) }
    return interpolate(xs, ys)
}


fun main() {
    val n = readLine()!!.trim().toInt()
    val x: List<Double> = readLine()!!.split(" ").map { it.toDouble() }
    val y: List<Double> = readLine()!!.split(" ").map { it.toDouble() }

//    val res: List<Double> = cheating(n, x, y)
    val f = stupid(n, x, y)
    val res: List<Double> = x.map { f(it) }
    println(res.joinToString(separator = " "))
}

fun myNormDist(mu: Double, sigma: Double): Function {
    return { x ->
        (1.0 / (sigma * sqrt(2* PI))) * exp(-0.5 * (((x-mu)/sigma).pow(2)))
    }
}

fun cheating(n: Int, xs: List<Double>, ys: List<Double>): List<Double> {
    // make uniform dist
    val a = ys.minOrNull()!!
    val b = ys.maxOrNull()!!
    val mu = (a + b) / 2
    val sigma = (b-a) / (sqrt(12.0))
    val expectedMean = n * mu
    val expectedStd = sqrt(n.toDouble()) * sigma
    val ndist = myNormDist(expectedMean, expectedStd)
    val ans = xs.map { ndist(it) }
    return ans
}

fun stupid(n: Int, xs: List<Double>, ys: List<Double>): Function {
    assert(xs == xs.sorted())
    var f = interpolate(xs, ys)
//    val s = stupidIntegrate(xs, f)
//    val newys = ys.map { it / s}
//    f = interpolate(xs, newys)
    val g = f
    val gFrom = xs.minOrNull()!! //- 0.01
    val gTo = xs.maxOrNull()!! //+ 0.01
    var (fFrom, fTo) = listOf(gFrom, gTo)
    for (i in 1..n-1) {
//        f = convolve(f, g, fFrom, fTo, gFrom, gTo, numPointsToCalc = 1000)
        f = stupidConvolve(f,g,fFrom, fTo, gFrom, gTo, numPointsToCalc = 100,numPointsInIntegral = 1000)
        fFrom += gFrom
        fTo += gTo
    }
    return f
}

fun stupidIntegrate(xs: List<Double>, f: Function): Double {
    require(xs.size > 1)
    val step = xs.get(1) - xs.get(0)
    var s = 0.0
    for (i in 0 until xs.size - 1) {
        s += f(xs.get(i)) * step
    }
    return s
}


fun convolve(f: Function, g: Function,
             fFrom: Double, fTo: Double,
             gFrom: Double, gTo: Double,
             numPointsToCalc: Int = 100, numPointsInIntegral: Int = 100): Function {
    val gxs = pointsFromRange(gFrom, gTo, numPointsToCalc)
    val sz = (((fTo-fFrom) / (gTo-gFrom)) * numPointsToCalc).toInt()
    val fxs = pointsFromRange(fFrom, fTo, sz)
//    println("sz = $sz")
    require(fxs.size > 1)
    val step = fxs.get(1) - fxs.get(0)
    val nFrom = fFrom + gFrom
    val nTo = fTo + gTo
    val xs = pointsFromRange(nFrom, nTo, sz + numPointsToCalc)
    val n = xs.size
    val ys = List(n) {
        var res = 0.0
        for (i in 0 until numPointsToCalc) {
            val j = it - i
            if (j >= 0 && j < sz) {
                res += g(gxs.get(i)) * f(fxs.get(j)) * step
            }
        }
        res
    }
    return interpolate(xs, ys)
}


fun stupidConvolve(f: Function, g: Function,
                   fFrom: Double, fTo: Double,
                   gFrom: Double, gTo: Double,
                   numPointsToCalc: Int = 100, numPointsInIntegral: Int = 100): Function {
    val f_xs = pointsFromRange(fFrom, fTo, numPointsInIntegral)
    require(f_xs.size > 1)
    val step = f_xs[1] - f_xs[0]
    val nFrom = fFrom + gFrom
    val nTo = fTo + gTo
    val xs = pointsFromRange(nFrom, nTo, numPointsToCalc)
    val n = xs.size
    val ys = List(n) {
        var res = 0.0
        for (i in 0 until numPointsInIntegral) {
            val t = f_xs[i]
            res += f(t) * g(xs[it] - t) * step
        }
        res
    }
    return interpolate(xs, ys)
}