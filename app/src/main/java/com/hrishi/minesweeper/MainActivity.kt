package com.hrishi.minesweeper

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hrishi.minesweeper.databinding.ActivityMainBinding
import java.io.Serializable

val EASY = level(8, 8, 10)
val NORMAL = level(16, 16, 40)
val HARD = level(30, 16, 99)


class MainActivity : AppCompatActivity(), Serializable{

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        disablePicker()
        var thread = Thread(object: Runnable{
            private var count = 0
            override fun run() {
                count++
                runOnUiThread {
                    Log.d("Answer", "run: $count")
                }
            }
        })
            thread.start()



        binding.swiCustom.setOnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked){
                Log.d("Main", "onCreate: Activated")
                binding.lengthPicker.isEnabled = true
                binding.widthPicker.isEnabled = true
                binding.minesPicker.isEnabled = true
            }

            if(!isChecked){
                binding.lengthPicker.isEnabled = false
                binding.widthPicker.isEnabled = false
                binding.minesPicker.isEnabled = false
            }
        }

        binding.btnStart.setOnClickListener{

            if(binding.swiCustom.isChecked){
                goto_game(level(binding.lengthPicker.value, binding.widthPicker.value,
                binding.minesPicker.value))

            }else{
                show_options()
            }

        }


    }

    private fun show_options(){

        val options = mutableListOf("Easy", "Normal", "Hard")
        var level = "Easy"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Select Difficulty Level")
            .setSingleChoiceItems(options.toTypedArray(), 0){_, position ->
                level = options[position]
            }
            .setIcon(R.drawable.mine_angry)
            .setPositiveButton("Start"){_,_ ->

                if(level.equals(options[0])){
                    goto_game(EASY)
                }else if(level.equals(options[1])){
                    goto_game(NORMAL)
                }else if(level.equals(options[2])){
                    goto_game(HARD)
                }
            }

        dialog.show()
    }

    private fun goto_game(level : level){
        val intent = Intent(this, Game::class.java)
        intent.putExtra("length", level.length)
        intent.putExtra("width", level.width)
        intent.putExtra("mines", level.mines)

        startActivity(intent)
    }

    private fun disablePicker(){
        binding.lengthPicker.isEnabled = false
        binding.widthPicker.isEnabled = false
        binding.minesPicker.isEnabled = false

        binding.lengthPicker.minValue = 8
        binding.widthPicker.minValue = 8
        binding.minesPicker.minValue = 10
        binding.lengthPicker.maxValue = 30
        binding.widthPicker.maxValue = 16
        binding.minesPicker.maxValue = 99
    }
}

