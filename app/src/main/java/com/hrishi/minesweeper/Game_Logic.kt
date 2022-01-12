package com.hrishi.minesweeper
import java.util.*
//
//fun main(args: Array<String>){
//    val read = Scanner(System.`in`)
//    val size = read.nextInt()
//    val count = read.nextInt()
//
//
//
//    //iterates input ans prints output
//    val n = read.nextInt()
//    for(i in 1..n) {
//        if (game.move(read.nextInt(), read.nextInt(), read.nextInt())) {
//            println("true")
//            game.displayBoard()
//        } else {
//            println("false")
//        }
//        if(game.status!= Status.ONGOINING)
//            break
//    }
//    println(game.status)
//}

//board class
class Minesweeper(private val length:Int, private val width : Int){
    val board = Array(length) { Array(width) { MineCell() }}
    var status = Status.ONGOINING
        private set

    //sets up mines
    fun setMine(row: Int, column: Int) : Boolean{
        if(board[row][column].value != MINE) {
            board[row][column].value = MINE
            updateNeighbours(row,column)
            return true
        }
        return false
    }

    //updates the values os the cells neighbouring to the mines
    private fun updateNeighbours(row: Int, column: Int) {
        for (i in movement) {
            for (j in movement) {
                if(((row+i) in 0 until length) && ((column+j) in 0 until width) && board[row+i][column+j].value != MINE)
                    board[row+i][column+j].value++
            }
        }
    }

    // Handles when board[x][y]==0
    private val xDir = intArrayOf(-1, -1, 0, 1, 1, 1, 0, -1)
    private val yDir = intArrayOf(0, 1, 1, 1, 0, -1, -1, -1)
    fun handleZero(x:Int ,y:Int){

        board[x][y].isRevealed = true
        for(i in 0..7){
            var xstep = x+xDir[i]
            var ystep = y+yDir[i]
            if((xstep<0 || xstep>=width) || (ystep<0 || ystep>=length)){
                continue;
            }
            if(board[xstep][ystep].value>0 && !board[xstep][ystep].isMarked){
                board[xstep][ystep].isRevealed = true
            }else if( !board[xstep][ystep].isRevealed && !board[xstep][ystep].isMarked && board[xstep][ystep].value==0){
                handleZero(xstep,ystep)

            }
        }

    }

    // To update status (ongoing/won)
    fun checkStatus(){
        var flag1=0
        var flag2=0
        for(i in 0..length-1){
            for(j in 0..width-1){
                if(board[i][j].value==MINE && !board[i][j].isMarked){
                    flag1=1
                }
                if(board[i][j].value!=MINE && !board[i][j].isRevealed){
                    flag2=1
                }
            }
        }
        if(flag1==0 || flag2==0) status = Status.WON
        else status = Status.ONGOINING
    }

    fun move(choice: Int, x: Int, y:Int): Boolean{
        // Complete this function
        /* Don't write main().
         * Don't read input, it is passed as function argument.
         * Return output and don't print it.
         * Taking input and printing output is handled automatically.
         */

        if(choice==1){
            if(board[x][y].isMarked || board[x][y].isRevealed){
                return false
            }
            if(board[x][y].value == MINE){
                status = Status.LOST;
                return true
            }
            else if(board[x][y].value >0){
                // status = Status.ONGOINING
                board[x][y].isRevealed = true
                checkStatus();
                return true
            }
            else if(board[x][y].value==0){
                handleZero(x,y)
                checkStatus();
                return true
            }
            // checkStatus();

        }
        if(choice == 2){
            if(board[x][y].isRevealed || board[x][y].isMarked){
                return false;
            }
            board[x][y].isMarked = true;
            checkStatus()

            return true;
        }



        return false
    }

    //displays the board
    fun displayBoard() {
        board.forEach { row ->
            row.forEach {
                if(it.isRevealed)
                    print("| ${it.value} |")
                else if (it.isMarked)
                    print("| X |")
                else if (status == Status.LOST && it.value == MINE)
                    print("| * |")
                else if (status == Status.WON && it.value == MINE)
                    print("| X |")
                else
                    print("|   |")
            }
            println()
        }
    }

    companion object{
        const val MINE = -1
        val movement = intArrayOf(-1, 0, 1)
    }
}

//mineCell Data Class
data class MineCell(var value:Int = 0 , var isRevealed: Boolean = false, var isMarked: Boolean = false)

//game status
enum class Status{
    WON,
    ONGOINING,
    LOST
}