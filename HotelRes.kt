package com.example.kotlinetest

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    var reserveProfile: Reservation
    var str: String
    var reserveListFile: File
    var reserveListPath="./reserveList.txt"
    var reserveList: List<Reservation>
    var playflag = true

    while (playflag){
        while (true){
            println("호텔 예약 프로그램입니다.")
            println("[메뉴]")
            println("1. 방예약, 2. 예약목록 출력, 3. 예약목록 (정렬) 출력, 4. 시스템 종료, 5. 금액 입급-출금 내역 목록 출력 6. 예약 변경/취소")

            when (readln().trim().takeWhile { !it.isWhitespace() }){
                "1" -> {
                    reserveProfile= Reservation()
                    reserveListFile= File(reserveListPath)
                    reserveList=reserveListFile.readLines().map { it.split(',') }.map { Reservation(it[0], it[1].toInt(), LocalDate.parse(it[2]), LocalDate.parse(it[3])) }
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
                            reserveProfile.arriveDate= LocalDate.parse(readln().trim().split("\\s+".toRegex())[0], DateTimeFormatter.BASIC_ISO_DATE)
                            if(reserveProfile.arriveDate.compareTo(LocalDate.now())<0){
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
                            reserveProfile.departDate= LocalDate.parse(readln().trim().split("\\s+".toRegex())[0], DateTimeFormatter.BASIC_ISO_DATE)
                            if(reserveProfile.departDate.compareTo(reserveProfile.arriveDate)<=0){
                                println("체크인 날짜보다 이전이거나 같을 수 없습니다.")
                                continue
                            }
                            break
                        }
                        catch (e: java.time.format.DateTimeParseException){
                            println("날짜가 아닙니다.")
                        }
                    }
                    str="${reserveProfile.name},${reserveProfile.roomNum},${reserveProfile.arriveDate.format(DateTimeFormatter.ISO_DATE)},${reserveProfile.departDate.format((DateTimeFormatter.ISO_DATE))}\n"
                    reserveListFile.appendText(str)
                    println("호텔 예약이 완료되었습니다.")
                }
                "2" -> {
                    println("호텔 예약자 목록입니다.")
                    reserveListFile= File(reserveListPath)
                    reserveListFile.readLines().forEachIndexed { idx, it -> println("${idx+1}. 사용자: ${it.split(',')[0]}, 방번호: ${it.split(',')[1]}호, 체크인: ${it.split(',')[2]}, 체크아웃: ${it.split(',')[3]}") }
                }
                "3" -> {
                    println("호텔 예약자 목록입니다. (정렬완료)")
                    reserveListFile= File(reserveListPath)
                    reserveListFile.readLines().sortedBy { it.split(',')[2] }.forEachIndexed { idx, it -> println("${idx+1}. 사용자: ${it.split(',')[0]}, 방번호: ${it.split(',')[1]}호, 체크인: ${it.split(',')[2]}, 체크아웃: ${it.split(',')[3]}") }
                }
                "4" -> {
                    playflag=false
                    break
                }
                "5" -> {
                }
                "6" -> {
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
    var name: String = "홍길동"
    var roomNum: Int = 100
    var arriveDate= LocalDate.now()
    var departDate= LocalDate.now()

    constructor() : this("홍길동", 100, LocalDate.now(), LocalDate.now())

    constructor(_name: String, _roomNum: Int, _arriveDate: LocalDate, _departDate: LocalDate){
        this.name=_name
        this.roomNum=_roomNum
        this.arriveDate=_arriveDate
        this.departDate=_departDate
    }
}