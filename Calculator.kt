package com.example.kotlinetest

fun main() {
    var calc: Calculator
    var str: String
    var n1: Int? = null
    var n2: Int? = null
    var ans : Int
    var playflag = true

    while (playflag){
        println("계산할 두 숫자를 입력하거나, q를 눌러 계산기를 종료하세요.")
        while (true){
            try {
                str = readln().trim()
                if(str.split("\\s+".toRegex())[0] == "q" || str.split("\\s+".toRegex())[0] == "Q"){
                    playflag=false
                    break
                }
                if(n1==null){
                    n1 = n1 ?: str.split("\\s+".toRegex())[0].toInt()
                    n2 = n2 ?: str.split("\\s+".toRegex())[1].toInt()
                }
                else{
                    n2 = n2 ?: str.split("\\s+".toRegex())[0].toInt()
                }
                break
            }
            catch (e: java.lang.IndexOutOfBoundsException) {
                println("입력이 올바르지 않습니다. 두번째 숫자를 입력하세요.")
            }
            catch (e: java.lang.NumberFormatException){
                if(n1!=null)
                    println("입력이 올바르지 않습니다. 두번째 숫자를 입력하세요.")
                else
                    println("입력이 올바르지 않습니다. 숫자를 입력하세요.")
            }
        }
        if(!playflag){
            break
        }
        println("첫번째 숫자: ${n1}, 두번째 숫자: ${n2}")
        calc= Calculator(n1!!, n2!!)
        while (true){
            println("메뉴에 하고 싶은 일을 선택하세요.")
            println("1. 더하기  2. 빼기  3. 나누기  4.곱하기  5: 나머지 구하기  6: 숫자 다시 선택하기  q: 나가기")

            when (readln().trim().takeWhile { !it.isWhitespace() }){
                "1" -> {
                    ans=calc.add()
                    println("${n1} + ${n2} = ${ans}")
                }
                "2" -> {
                    ans=calc.sub()
                    println("${n1} - ${n2} = ${ans}")
                }
                "3" -> {
                    ans=calc.div()
                    println("${n1} / ${n2} = ${ans}")
                }
                "4" -> {
                    ans=calc.mul()
                    println("${n1} * ${n2} = ${ans}")
                }
                "5" -> {
                    ans=calc.rem()
                    println("${n1} % ${n2} = ${ans}")
                }
                "6" -> {
                    n1=null
                    n2=null
                    break
                }
                "Q", "q" -> {
                    playflag=false
                    break
                }
                else -> {
                    println("메뉴에 없는 일입니다.")
                    print("다시 ")
                }
            }
        }
    }

    println("계산기가 종료합니다.")
}

class Calculator(_a: Int, _b: Int){
    var a: Int =0
    var b: Int =0

    init {
        this.a=_a
        this.b=_b
    }
    fun add() : Int{
        return AddOperation().operation(a, b)
    }
    fun sub() : Int{
        return SubtractOperation().operation(a, b)
    }
    fun div() : Int{
        return MultiplyOperation().operation(a, b)
    }
    fun mul() : Int{
        return DivideOperation().operation(a, b)
    }
    fun rem() : Int{
        return a%b
    }
}

abstract class AbstractClass{
    abstract fun operation(a:Int, b:Int) : Int
}

class AddOperation :AbstractClass(){
    override fun operation(a: Int, b: Int) : Int{
        return a+b
    }
}

class SubtractOperation :AbstractClass(){
    override fun operation(a: Int, b: Int) : Int{
        return a-b
    }
}

class DivideOperation :AbstractClass(){
    override fun operation(a: Int, b: Int) : Int{
        return a/b
    }
}

class MultiplyOperation :AbstractClass(){
    override fun operation(a: Int, b: Int) : Int{
        return a*b
    }
}