import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import A.*
import A.Function
import space.kscience.kmath.distributions.NormalDistribution
import kotlin.math.*
import kotlin.random.Random


class ATest {
    var EPS = 1e-3
    @Test fun fTest() {
        assertTrue( f() == 1, "!!!")
    }
    @Test fun integrateTest() {
        val x = {x: Double -> x}
        var value = x.integrate(0.0,1.0)
        println(value)
        assertEquals(0.5, value, EPS, "value=$value")

        value = x.integrate(0.0,10.0)
        println(value)
        assertEquals(50.0, value, 70 * EPS, "value=$value")

        value = x.integrate(-10.0,10.0)
        println(value)
        assertEquals(0.0, value, 15*EPS, "value=$value")

        val x2 = {x: Double -> x*x}
        value = x2.integrate(0.0,1.0)
        println(value)
        assertEquals(1.0 / 3, value, EPS)

        value = x2.integrate(-5.0,5.0)
        println(value)
        assertEquals(2 * 125.0 / 3, value, EPS * 500)
    }

    fun compareFuns(f: Function, g: Function, xs: List<Double>, eps: Double): Boolean {
        var k = xs.size / 20
        var mxdif = Double.NEGATIVE_INFINITY
        var i = 0
        xs.forEach {
            val dif = abs(f(it) - g(it))
            println("$it    ${f(it)}      ${g(it)}")
            assertTrue(dif <= eps || (dif / abs(f(it)) <= 0.1) || k-- > 0, "i=$i  dif=$dif, rel = ${(dif / abs(f(it)))} eps=$eps")
            mxdif = maxOf(mxdif, dif)
            ++i
        }
        println("MAXDIF=$mxdif, k=$k")
        return true
    }



    @Test fun convolutionCommutativityTest() {
        val rnd = Random(0)
        var (LB, UB) = listOf(-10.0, 10.0)
        repeat(3) {
//            var (from, to) = listOf(-5.0, 5.0 ).sorted()
            var (from, to) = List(2) { rnd.nextDouble(LB, UB) } .sorted()
            var (a, b, c) = List(3) { rnd.nextDouble(LB, UB) }
            println("$from, $to, $a, $b, $c")
            var f = { x: Double -> x * a }.interpolate(from, to, 100)
            var g = { x: Double -> x * x + c}.interpolate(from, to, 100)
            var h = { x: Double -> b * x + (a-c) * (x * x * x) }.interpolate(from, to, 100)

            val fun_ar = listOf(f,g,h)
            for (i in 0 until 3) {
                for (j in i+1 until 3) {
                    val f1 = fun_ar[i]
                    val f2 = fun_ar[j]
                    var ar1= ArrayList<ArrayList<Pair<Double,Double>>>()
                    var ary1= ArrayList<Double>()
                    var conv1 = my_convolve(f1, f2, from, to, from, to, numPointsInIntegral = 1000, ar=ar1, ary=ary1)
                    var ar2= ArrayList<ArrayList<Pair<Double,Double>>>()
                    var ary2= ArrayList<Double>()
                    var conv2 = my_convolve(f2, f1, from, to, from, to, numPointsInIntegral = 1000, ar=ar2, ary=ary2)
                    var step = 1.0
//                    step = ar1[0][1].first - ar1[0][0].first
//                    println(step)
                    var cmps = 0
//                    var diffar = List(ary1.size) { ary1.get(it) - ary2.get(it) }
//                    println("BAD = ${diffar.count { abs(it) > 2 }}, MX = ${diffar.map { abs(it) }.maxOrNull()!! }")
//                    var mxary = Double.NEGATIVE_INFINITY
//                    for(i in 0 until ary1.size) {
//                        var dif = abs(ary1.get(i) - ary2.get(i))
//                        assertEquals(ary1.get(i), ary2.get(i), 10000*EPS, "$i  $dif")
//                        mxary=maxOf(mxary, dif)
//                    }
//                    println(mxary)
                    var mx = Double.NEGATIVE_INFINITY
                    for (i in 0 until ar1.size) {
                        val ar = ar1.get(i)
                        for (j in 0 until ar.size) {
                            var (a,b) = ar.get(j)
                            if (a >= b) {
                                var k = j-1
                                while (k >= 0 && ar.get(k).first > b) {
                                    k--
                                }
                                if (k == -1) {
                                    assertTrue(b < from+step, "$b")
                                    continue
                                }
                                var ind = k+1
                                if ((-ar.get(k).first + b) < (-b + ar.get(ind).first)) {
                                    ind = k
                                }
                                var (ma,mb) = ar.get(ind)
                                assertEquals(ma, b, step, "$ma $b $EPS $cmps     $i  $j  $ind")
                                assertEquals(mb, a, step, "$mb $a $EPS $cmps")
                                cmps++
                                mx = maxOf(mx, abs(ma-b))
                                mx = maxOf(mx, abs(mb-a))
                            } else {
                                var k = j+1
                                while (k < ar.size && ar.get(k).first < b) {
                                    k++
                                }
                                if (k == ar.size) {
                                    assertTrue(b > to-step, "$b")
                                    continue
                                }
                                var ind = k-1
                                if ((ar.get(k).first - b) < (b - ar.get(ind).first)) {
                                    ind = k
                                }
                                var (ma,mb) = ar.get(ind)
                                assertEquals(ma, b, step, "$ma $b $EPS $cmps     $i  $j  $ind")
                                assertEquals(mb, a, step, "$mb $a $EPS $cmps")
                                cmps++
                                mx = maxOf(mx, abs(ma-b))
                                mx = maxOf(mx, abs(mb-a))
                            }
                        }
                    }
//                    println("$cmps $mx  ${ar1.size * ar1.get(0).size}")
                    compareFuns(conv1, conv2, pointsFromRange(2 * from, 2 * to), EPS * 30 * (to - from))
                }
            }
        }
    }

    @Test fun convolutionAssociativityTest() {
        val rnd = Random(0)
        var (LB, UB) = listOf(1.0, 5.0)
        repeat(3) {
            var (from, to) = List(2) { rnd.nextDouble(LB, UB) }.sorted()
            var (a, b, c) = List(3) { rnd.nextDouble(LB, UB) }
            println("$from, $to, $a, $b, $c")
            var f = { x: Double -> x * a }.interpolate(from, 2*to, 1000)
            var g = { x: Double -> x * x + c}.interpolate(from, 2*to, 1000)
            var h = { x: Double -> b * x + (a-c) * (x * x * x) }.interpolate(from, 2*to, 1000)

            var NP = 1000
            val fun_ar = listOf(f,g,h)
            for (l in 0 until 3) {
                for (r in 0 until 3) {
                    if (l == r)
                        continue
                    val c = (0 + 1 + 2) - (l + r)
                    val (f1, f2, f3) = listOf(fun_ar[l], fun_ar[c], fun_ar[r])
                    var conv1 = my_convolve(f1, f2, from, to, from, to, NP, numPointsInIntegral = NP)
                    conv1 = my_convolve(conv1, f3, from, 2*to, from, to, NP,numPointsInIntegral = NP)
                    var conv2 = my_convolve(f2, f3, from, to, from, to, NP,numPointsInIntegral = NP)
                    conv2 = my_convolve(f1, conv2, from, to, from, 2*to, NP,numPointsInIntegral = NP)
                    compareFuns(conv1, conv2, pointsFromRange(3*from, 3 * to), EPS * 30 * (to - from))
                }
            }
        }

    }

    fun myNormDist(mu: Double, sigma: Double): Function {
        return { x ->
            (1.0 / (sigma * sqrt(2* PI))) * exp(-0.5 * (((x-mu)/sigma).pow(2)))
        }
    }

    @Test fun NormDistTest() {
        var ndist = NormalDistribution(0.0,1.0)
        var myndist = myNormDist(0.0,1.0)
        var from = -3.0
        var to = 3.0
        var range = pointsFromRange(from,to, 20)
        for (x in range) {
            var y = ndist.probability(x)
            println("$x $y  my = ${myndist(x)}")
        }
    }

    @Test fun BernoulliTest() {
        var p = 0.999
        var d = 0.1
        var dd = 0.00000001
        var xs = listOf(0.0-dd, 0.0, d, d+dd, 1.0-d-dd, 1.0-d, 1.0, 1+dd)
        var ys = listOf(0.0, (1-p)/d, (1-p)/d, 0.0, 0.0, p/d, p/d, 0.0)
        var pdf = interpolate(xs, ys)
        val (ifrom, ito) = listOf(0.0-dd, 1.0+dd)
        var (from, to) = listOf(ifrom, ito)
        var mu = p
        var sigma = p * (1-p)
        var n = 30
        var f = pdf
        for (i in 0 until n-1) {
            f = my_convolve(pdf, f, ifrom, ito, from, to, numPointsToCalc = 100)
            from+=ifrom
            to+=ito
            println("i = $i")
        }
        val expectedMean = n * mu
        val expectedStd = sqrt(n.toDouble()) * sigma
        val range = pointsFromRange(from, to, 100)
        println("exp mean = $expectedMean  exp sigma = $expectedStd")
        println("calc mean = ${calc_mean(range,f)}  calc sigma = ${calc_std(range,f)}")
        val expectedDist = NormalDistribution(expectedMean, expectedStd)
        println("$from .. $to")
        var maxY=Double.NEGATIVE_INFINITY
        var maxX=Double.NEGATIVE_INFINITY
        for (x in range) {
            val y = expectedDist.probability(x)
            val me = f(x)
            println("$x  $y   $me")
            if (me > maxY) {
                maxY=me
                maxX=x
            }
        }
        println("maxY=$maxY in x = $maxX")
    }


    @Test fun UniformTest() {
        var dd = 0.00000001
        var a = 0.0
        var b = 1.0
        val (ifrom, ito) = listOf(a-dd, b+dd)
        var p = 1 / (b-a)
        var xs = listOf(a-dd, a, b, b+dd)
        var ys = listOf(0.0, p, p, 0.0)
        var pdf = interpolate(xs, ys)
        var (from, to) = listOf(ifrom, ito)
        var mu = (a + b) / 2
        var sigma = (b-a) / (sqrt(12.0))
        var n = 10
        var f = pdf
        for (i in 0 until n-1) {
            f = my_convolve(pdf, f, ifrom, ito, from, to, numPointsToCalc = 100)
            from+=ifrom
            to+=ito
            println("i = $i")
        }
        val expectedMean = n * mu
        val expectedStd = sqrt(n.toDouble()) * sigma
        val range = pointsFromRange(from, to, 100)
        println("exp mean = $expectedMean  exp sigma = $expectedStd")
        println("calc mean = ${calc_mean(range,f)}  calc sigma = ${calc_std(range,f)}")
        val expectedDist = NormalDistribution(expectedMean, expectedStd)
        println("$from .. $to")
        var maxY=Double.NEGATIVE_INFINITY
        var maxX=Double.NEGATIVE_INFINITY
        for (x in range) {
            val y = expectedDist.probability(x)
            val me = f(x)
            println("$x  $y   $me")
            if (me > maxY) {
                maxY=me
                maxX=x
            }
        }
        println("maxY=$maxY in x = $maxX")
    }


    fun calc_mean(range: List<Double>, f: Function): Double {
        var mean = 0.0
        val step = range.get(1) - range.get(0)
        for (x in range) {
            mean += x * f(x) * step
        }
        return mean
    }
    fun calc_std(range: List<Double>, f: Function): Double {
        val mean = calc_mean(range, f)
        var D = 0.0
        val step = range.get(1) - range.get(0)
        for (x in range) {
            D += (x - mean).pow(2) * f(x) * step
        }
        return sqrt(D)
    }

    @Test fun cheatingTest() {
        val (xfrom, xto) = listOf(-1000.0, 1000.0)
        val (yfrom, yto) = listOf(-10.0, 10.0)
        val x = pointsFromRange(xfrom, xto)
        val y = pointsFromRange(yfrom, yto)
        val a = y.minOrNull()!!
        val b = y.maxOrNull()!!
        val mu = (a + b) / 2
        val sigma = (b-a) / (sqrt(12.0))
        val BIG_N = 1000
        val expectedMean = BIG_N * mu
        val expectedStd = sqrt(BIG_N.toDouble()) * sigma
        val ndist = myNormDist(expectedMean, expectedStd)
        val expectedAns = x.map { ndist(it) }
        for (n in 1..100 step 10) {
            val ans = cheating(n,x,y)
            myCmp(expectedAns, ans)
        }
    }

    private fun myCmp(expectedAns: List<Double>, ans: List<Double>) {
        var ms = expectedAns.withIndex().fold(0.0, {acc, iv -> acc + abs(iv.value - ans.get(iv.index)) })
        ms /= expectedAns.size
        println("ABS = $ms")
    }

    @Test fun stupidTest() {
        val (xfrom, xto) = listOf(-5.0, 5.0)
        val (yfrom, yto) = listOf(1.0, 1.0)
        val d = 0.01
        val x = pointsFromRange(xfrom , xto, 10)
        val y = pointsFromRange(yfrom-d, yto+d, 10)
        var f = interpolate(x, y)
        var mu = calc_mean(x, f)
        var sigma = calc_std(x, f)
        println("mu = $mu   sigma = $sigma")

        for (n in 1..5 step 1) {
            val st_f = stupid(n,x,y)
            val range = pointsFromRange(xfrom*n, xto*n)
            val expectedMean = n * mu
            val expectedStd = sqrt(n.toDouble()) * sigma
            println("exp mean = $expectedMean  exp sigma = $expectedStd")
            val ndist = myNormDist(expectedMean, expectedStd)
            val expectedAns = range.map { ndist(it) }
            val ans = range.map {st_f(it)}
            for (i in 0 until range.size) {
                println("x = ${range.get(i)}, me = ${ans.get(i)},  expected = ${expectedAns.get(i)}")
            }
            myCmp(expectedAns, ans)
        }
    }
}