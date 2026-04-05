package bitmanipulation

import chisel3._
import chisel3.util._

abstract class AbstractLeadingZerosCounter(bitWidth: Int) extends Module {
  val io = IO(new Bundle {
    val input = Input(UInt(bitWidth.W))
    val result = Output(UInt(log2Ceil(bitWidth + 1).W))
  })
}

// You may expect bitWidth to be a power of two.
class LeadingZerosCounter(bitWidth: Int)
    extends AbstractLeadingZerosCounter(bitWidth) {

  if(bitWidth==1){ 
    //base case
    io.result:= ~(io.input(0)).asUInt //if input= 0: count=1 else count=0

  } else{ 
   //recusive case
    val newBitwidth = bitWidth / 2
    val leftCounter = Module(new LeadingZerosCounter(newBitwidth)) //create a subcircuit with input=input/2
    val rightCounter = Module(new LeadingZerosCounter(newBitwidth)) //create a subcircuit with input=input/2
   
    leftCounter.io.input := io.input(bitWidth-1, newBitwidth) //upper half bits
    rightCounter.io.input := io.input(newBitwidth-1,0)      //lower half bits
   
    val newOutputBitwidth: Int = log2Ceil(newBitwidth + 1) //otput bitwidth of the subcircuits

    val leftMsbBit = leftCounter.io.result(newOutputBitwidth-1) //MSb of upper half
    val rightMsbBit = rightCounter.io.result(newOutputBitwidth - 1) //MSB of lower half

    val outputIfZero: UInt = Cat(0.U, leftCounter.io.result) //upper half=0 then output= lower half countZero+ bitWidth/2
    val outputIfOne = Cat(rightMsbBit, ~rightMsbBit, rightCounter.io.result(newOutputBitwidth - 2, 0)) //upper half !=0 then output = upper half countZero
    io.result := Mux(leftMsbBit, outputIfOne, outputIfZero)
  }
}
