package com.hrishi.minesweeper

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hrishi.minesweeper.databinding.ActivityGameBinding

const val FLAG = 2
const val MINE = 1

class Game : AppCompatActivity() {

    private var choice = MINE
    lateinit var binding : ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val width = intent.getIntExtra("width", 8)
        val length = intent.getIntExtra("length", 8)
        val mines = intent.getIntExtra("mines", 10)

        val game = Minesweeper(length, width)

        binding.ivChoice.setOnClickListener {
            if(choice == FLAG){
                choice = MINE
                //it.setBackgroundResource(R.drawable.mine)
            }else if(choice == MINE){
                choice = FLAG
                //it.setBackgroundResource(R.drawable.flag)
            }
        }

        val timer = object : CountDownTimer(1000000, 1000) {
            override fun onTick(p0: Long) {
                binding.tvTimer.setText("${(1000000-p0)/1000}")
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }
        timer.start()

        setBoard(length, width, game)
        setMine(length, width, mines, game)

        updateBoard(game)
        checkStatus(game)

    }

    private fun setBoard(length: Int, width: Int, game :Minesweeper) {

        var counter = 1
        val board = findViewById<LinearLayout>(R.id.board)

        val param1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        )

        val param2 = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        for (i in 1..length) {

            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams = param1
            param1.weight = 1.0F

            for (j in 1..width) {

                val button = Button(this)
                button.id = counter
                button.setBackgroundResource(R.drawable.button_style)

                button.layoutParams = param2
                param2.weight = 1.0F
                button.isLongClickable = true

                button.setOnClickListener {
                    recordMove(it, game, length, width)
                }

                button.setOnLongClickListener {
                    recordMove(it, game, length, width, true)
                    true
                }


                linearLayout.addView(button)
                counter++
            }
            board.addView(linearLayout)
        }
    }

    private fun recordMove(view: View, game: Minesweeper,
    length: Int, width: Int, longClick: Boolean = false) {
        val id = view.id
        var x = id%width - 1
        var y = id/width
        var choiceCopy = choice

        if(longClick){
            if(choiceCopy == FLAG){
                choiceCopy = MINE
            }else{
                choiceCopy = FLAG
            }
        }

        if(id%width == 0){
            x = (id-1)%width
            y--
        }

        game.move(choiceCopy, x, y)
        updateBoard(game)
        checkStatus(game)
    }


    //sets the mine randomly
    private fun setMine(length: Int, width: Int, count: Int, game: Minesweeper) {

        for (i in 1..count) {
            var set = false
            while (!set) {
                if (game.setMine(
                        (0 until length).random(),
                        (0 until width).random()
                    )
                ){
                    set = true
                }
            }
        }
    }

    //checks status, declare win loose
    private fun checkStatus(game: Minesweeper){
        val dialog = AlertDialog.Builder(this)
            .setIcon(R.drawable.mine_angry)
            .setPositiveButton("OK"){_, _ ->}

        if(game.status == Status.LOST){
            dialog
                .setTitle("LOST")
                .setMessage("You Lost the game")
                .show()
        }else if(game.status == Status.WON){
            dialog
                .setTitle("LOST")
                .setMessage("You Lost the game")
                .show()
        }
    }

    //updates the board as per array. If game lost, shows all the mine.
    private fun updateBoard(game: Minesweeper){

        var id = 1
        for (i in 0..game.board.size-1){
            for (j in 0..game.board[0].size-1){
                val btn = findViewById<Button>(id)
                val value = game.board[i][j].value

                if(game.status == Status.LOST){
                    if(value == Minesweeper.MINE){
                        btn.background = getDrawable(R.drawable.mine)
                        btn.setBackgroundColor(Color.RED)
                    }else{
                        btn.setText("$value")
                    }

                }else{
                    if(game.board[i][j].isRevealed){
                        btn.isEnabled = false

                        if(value == Minesweeper.MINE){
                            btn.background = getDrawable(R.drawable.mine)
                        }else{
                            btn.setText("$value")
                            btn.setBackgroundColor(Color.YELLOW)
                        }
                    }else{
                        if (game.board[i][j].isMarked){
                            btn.background = getDrawable(R.drawable.flag)
                            btn.setBackgroundColor(Color.YELLOW)
                        }
                    }
                }
                id++
            }
        }
    }


}