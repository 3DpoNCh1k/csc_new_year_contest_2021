fun main() {
    println("Hello!")
    A().method(1)
    B().method(1)
    A().method_2(3,4)
}


open class A {
	open public fun method(x: Int) {
		var y = x;
		println(y)
		y++
		println(y)
	}
	
	fun method_1() {
		val Fun = 123
		println("method_1")
		if (Fun == 1) {
			
		}
	}
	
	
	open public fun method_2
				(x: Int,
				 y: Int,
				 )
				  {
		
		
		
		val Fun = x + 1
		
		println("method_2 $x, $y, $Fun")
		
		
		
		
	}
	
}


class B:A() {
	public override fun method(x: Int) {
		var y = x + 10;
		println(y)
		y++
		println(y)
	}
}
