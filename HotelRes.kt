package com.example.kotlinetest

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    var reserveProfile: Reservation
    var str: String
    var reserveListPath="./reserveList.txt"
    var reserveListFile: File = File(reserveListPath)
    var reserveList=reserveListFile.readLines().map { it.split(',') }.map { Reservation(it[0], it[1].toInt(), LocalDate.parse(it[2]), LocalDate.parse(it[3]), it[4].split(',').map{it.toInt()}.toIntArray()) }.toMutableList()
    var moneyHistoryMapPath="./moneyHistoryMap.txt"
    var moneyHistoryMapFile = File(moneyHistoryMapPath)
    var moneyHistoryMap = moneyHistoryMapFile.readLines().map { it.split(',') }.associate { Pair(it[0], it.drop(1).map { it.toInt() }) }.toMutableMap()
    var playflag = true

    while (playflag){
        while (true){
            println("호텔 예약 프로그램입니다.")
            println("[메뉴]")
            println("1. 방예약, 2. 예약목록 출력, 3. 예약목록 (정렬) 출력, 4. 시스템 종료, 5. 금액 입급-출금 내역 목록 출력 6. 예약 변경/취소")

            when (readln().trim().takeWhile { !it.isWhitespace() }){
                "1" -> {
                    reserveProfile= Reservation()
                    println("예약자분의 섬함을 입력해주세요.")
                    reserveProfile.name= readln().trim()
                    while (true){
                        try {
                            println("예약할 방번호를 입력해주세요.")
                            reserveProfile.roomNum= readln().trim().split("\\s+".toRegex())[0].toInt()
                            if(!(reserveProfile.roomNum in 100..999)){
                                println("올바르지 않은 방번호입니다. 방번호는 100~999 영역 이내입니다.")
                                continue
                            }
                            break
                        }
                        catch (e: java.lang.NumberFormatException){
                            println("숫자가 아닙니다.")
                        }
                    }
                    while (true){
                        try {
                            println("체크인 날짜를 입력해주세요. (표기형식: ${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)})")
                            reserveProfile.checkInDate= LocalDate.parse(readln().trim().split("\\s+".toRegex())[0], DateTimeFormatter.BASIC_ISO_DATE)
                            if(reserveProfile.checkInDate.compareTo(LocalDate.now())<0){
                                println("체크인은 지난날 선택할 수 없습니다.")
                                continue
                            }
                            else if(reserveList.any { it.roomNum==reserveProfile.roomNum }){
                                println("해당 날짜에 이미 방을 사용중입니다. 다른날짜를 입력해주세요.")
                                continue
                            }
                            break
                        }
                        catch (e: java.time.format.DateTimeParseException){
                            println("날짜가 아닙니다.")
                        }
                    }
                    while (true){
                        try {
                            println("체크아웃 날짜를 입력해주세요. (표기형식: ${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)})")
                            reserveProfile.checkOutDate= LocalDate.parse(readln().trim().split("\\s+".toRegex())[0], DateTimeFormatter.BASIC_ISO_DATE)
                            if(reserveProfile.checkOutDate.compareTo(reserveProfile.checkInDate)<=0){
                                println("체크인 날짜보다 이전이거나 같을 수 없습니다.")
                                continue
                            }
                            break
                        }
                        catch (e: java.time.format.DateTimeParseException){
                            println("날짜가 아닙니다.")
                        }
                    }
                    moneyHistoryMap.put(reserveProfile.name, listOf((5000..10000).random(), -(1000..5000).random()))
                    str=reserveProfile.giveLine()
                    reserveListFile.appendText(str)
                    reserveList.add(reserveProfile)
                    println("호텔 예약이 완료되었습니다.")
                }
                "2" -> {
                    println("호텔 예약자 목록입니다.")
                    reserveListFile.readLines().forEachIndexed { idx, it -> println("${idx+1}. 사용자: ${it.split(',')[0]}, 방번호: ${it.split(',')[1]}호, 체크인: ${it.split(',')[2]}, 체크아웃: ${it.split(',')[3]}") }
                }
                "3" -> {
                    println("호텔 예약자 목록입니다. (정렬완료)")
                    reserveListFile.readLines().sortedBy { it.split(',')[2] }.forEachIndexed { idx, it -> println("${idx+1}. 사용자: ${it.split(',')[0]}, 방번호: ${it.split(',')[1]}호, 체크인: ${it.split(',')[2]}, 체크아웃: ${it.split(',')[3]}") }
                }
                "4" -> {
                    playflag=false
                    break
                }
                "5" -> {
                    println("조회하실 사용자 이름을 입력하세요.")
                    str= readln().trim()
                    if(reserveList.filter { it.name==str }.isEmpty()){
                        println("예약된 사용자를 찾을 수 없습니다.")
                    }
                    else{
                        for ((idx, e) in moneyHistoryMap[str]!!.withIndex()){
                            print("${idx+1}. ")
                            when {
                                idx==0 -> println("초기 금액으로 ${e} 원 입급되었습니다.")
                                e<0 -> println("예약금으로 ${-e} 원 출금되었습니다.")
                                e>=0 -> println("예약금 환불로 ${e} 원 입급되었습니다.")
                                else -> println("error")
                            }
                        }
                    }
                }
                "6" -> {
                    println("예약을 변경할 사용자 이름을 입력하세요.")
                    str= readln().trim()
                    reserveProfile= Reservation()
                    reserveProfile.name=str
                    if(reserveList.filter { it.name==str }.isEmpty()){
                        println("사용자 이름으로 예약된 목록을 찾을 수 없습니다.")
                    }
                    else{
                        while(true){
                            println("${str} 님이 예약한 목록입니다. 변경하실 예약번호를 입력해주세요. (탈출은 exit입력)")
                            reserveList.filter { it.name == str }.forEachIndexed { idx, it -> println("${idx+1}. 방번호: ${it.roomNum}호, 체크인: ${it.checkInDate.format(DateTimeFormatter.ISO_DATE)}, 체크아웃: ${it.checkOutDate.format(DateTimeFormatter.ISO_DATE)}") }
                            str= readln().trim()
                            try{
                                if(str=="exit"){
                                    playflag=false
                                    break
                                }
                                else if(str.toInt() in 1..(reserveListFile.readLines().filter{it.split(',')[0]==str}.size)){
                                    reserveProfile=reserveList.filter { it.name == reserveProfile.name }[str.toInt()]
                                    reserveList.remove(reserveProfile)
                                    reserveListFile.delete()
                                    reserveListFile= File(reserveListPath)
                                    reserveList.forEach{ reserveListFile.appendText(it.giveLine()) }
                                    break
                                }
                                else{
                                    println("범위에 없는 예약번호 입니다.")
                                }
                            }
                            catch (e: java.lang.NumberFormatException){
                                println("숫자가 아닙니다.")
                            }
                        }
                        if(playflag==false){
                            playflag=true
                            break
                        }
                        while(true){
                            try {
                                println("해당 예약을 어떻게 하시겠어요? 1. 변경 2. 취소 / 이외 번호. 메뉴로 돌아가기")
                                str = readln().trim()
                                when (str.toInt()) {
                                    1 -> {
                                        while (true) {
                                            try {
                                                println("예약할 방번호를 입력해주세요.")
                                                reserveProfile.roomNum = readln().trim()
                                                    .split("\\s+".toRegex())[0].toInt()
                                                if (!(reserveProfile.roomNum in 100..999)) {
                                                    println("올바르지 않은 방번호입니다. 방번호는 100~999 영역 이내입니다.")
                                                    continue
                                                }
                                                break
                                            } catch (e: java.lang.NumberFormatException) {
                                                println("숫자가 아닙니다.")
                                            }
                                        }
                                        while (true) {
                                            try {
                                                println(
                                                    "체크인 날짜를 입력해주세요. (표기형식: ${
                                                        LocalDate.now()
                                                            .format(DateTimeFormatter.BASIC_ISO_DATE)
                                                    })"
                                                )
                                                reserveProfile.checkInDate = LocalDate.parse(
                                                    readln().trim().split("\\s+".toRegex())[0],
                                                    DateTimeFormatter.BASIC_ISO_DATE
                                                )
                                                if (reserveProfile.checkInDate.compareTo(LocalDate.now()) < 0) {
                                                    println("체크인은 지난날 선택할 수 없습니다.")
                                                    continue
                                                } else if (reserveList.any { it.roomNum == reserveProfile.roomNum }) {
                                                    println("해당 날짜에 이미 방을 사용중입니다. 다른날짜를 입력해주세요.")
                                                    continue
                                                }
                                                break
                                            } catch (e: java.time.format.DateTimeParseException) {
                                                println("날짜가 아닙니다.")
                                            }
                                        }
                                        while (true) {
                                            try {
                                                println(
                                                    "체크아웃 날짜를 입력해주세요. (표기형식: ${
                                                        LocalDate.now()
                                                            .format(DateTimeFormatter.BASIC_ISO_DATE)
                                                    })"
                                                )
                                                reserveProfile.checkOutDate = LocalDate.parse(
                                                    readln().trim().split("\\s+".toRegex())[0],
                                                    DateTimeFormatter.BASIC_ISO_DATE
                                                )
                                                if (reserveProfile.checkOutDate.compareTo(
                                                        reserveProfile.checkInDate
                                                    ) <= 0
                                                ) {
                                                    println("체크인 날짜보다 이전이거나 같을 수 없습니다.")
                                                    continue
                                                }
                                                break
                                            } catch (e: java.time.format.DateTimeParseException) {
                                                println("날짜가 아닙니다.")
                                            }
                                        }
                                        str = reserveProfile.giveLine()
                                        reserveListFile.appendText(str)
                                        reserveList.add(reserveProfile)
                                    }

                                    2 -> {
                                        println("취소 유의사항")
                                        println("체크인 3일 이전 취소 예약금 환불 불가")
                                        println("체크인 5일 이전 취소 예약금의 30% 환불")
                                        println("체크인 7일 이전 취소 예약금의 50% 환불")
                                        println("체크인 14일 이전 취소 예약금의 80% 환불")
                                        println("체크인 30일 이전 취소 예약금의 100% 환불")
                                        println("취소가 완료되었습니다.")
                                    }
                                }
                                break
                            } catch (e: java.lang.NumberFormatException) {
                                println("숫자가 아닙니다.")
                            }
                        }
                    }
                }
                else -> {
                    println("메뉴에 없는 일입니다.")
                    print("다시 ")
                }
            }
        }
    }

    println("호텔 예약 프로그램 종료합니다.")
}

class Reservation{
    var name: String
    var roomNum: Int
    var checkInDate: LocalDate
    var checkOutDate: LocalDate
    var initMoney=(1000..10000).random()
    var paymentHistory= intArrayOf(initMoney, -1*(0..initMoney).random())
    var strlist: List<String> = listOf("dummy list")

    constructor() : this("홍길동", 100, LocalDate.now(), LocalDate.now())

    constructor(_Line: String){
        this.strlist=_Line.split(',')
        this.name=this.strlist[0]
        this.roomNum=this.strlist[1].toInt()
        this.checkInDate=LocalDate.parse(this.strlist[2], DateTimeFormatter.BASIC_ISO_DATE)
        this.checkOutDate=LocalDate.parse(this.strlist[3], DateTimeFormatter.BASIC_ISO_DATE)
    }

    constructor(_name: String, _roomNum: Int, _arriveDate: LocalDate, _departDate: LocalDate){
        this.name=_name
        this.roomNum=_roomNum
        this.checkInDate=_arriveDate
        this.checkOutDate=_departDate
    }

    constructor(_name: String, _roomNum: Int, _arriveDate: LocalDate, _departDate: LocalDate, _paymentHistory: IntArray){
        this.name=_name
        this.roomNum=_roomNum
        this.checkInDate=_arriveDate
        this.checkOutDate=_departDate
        this.paymentHistory=_paymentHistory
    }

    fun giveLine(): String {
        return "${this.name},${this.roomNum},${this.checkInDate.format(DateTimeFormatter.ISO_DATE)},${this.checkOutDate.format((DateTimeFormatter.ISO_DATE))},${this.paymentHistory.joinToString(",")}\n"
    }
}