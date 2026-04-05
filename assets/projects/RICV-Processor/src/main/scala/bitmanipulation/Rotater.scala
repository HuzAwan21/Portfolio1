package bitmanipulation

import chisel3._
import chisel3.util._

abstract class AbstractFixedRotater(bitWidth: Int) extends Module {
  val io = IO(new Bundle {
    val input = Input(UInt(bitWidth.W))
    val result = Output(UInt(bitWidth.W))
  })
}

class FixedRotater(bitWidth: Int, shamt: Int)
    extends AbstractFixedRotater(bitWidth) {
    if (bitWidth == 1 || shamt == 0) {
      io.result := io.input
    } else {
      val leastSignificantBits = io.input(shamt - 1, 0)
      val mostSignificantBits = io.input(bitWidth - 1 , shamt)
      io.result := Cat(leastSignificantBits, mostSignificantBits)
    }
}

abstract class AbstractSequentialRotater(bitWidth: Int) extends Module {
  val io = IO(new Bundle {
    val input = Input(UInt(bitWidth.W))
    val shamt = Input(UInt(log2Ceil(bitWidth).W))
    val start = Input(Bool())
    val done = Output(Bool())
    val result = Output(UInt(bitWidth.W))
  })
}

class SequentialRotater(bitWidth: Int, generator: () => AbstractFixedRotater)
    extends AbstractSequentialRotater(bitWidth) {

  val Rotater = Module(generator())   // Rotater wird erstellt, können davon ausgehen, dass FixedRotater um 1 generiert wird (Aufgabenstellung)

  // Rotater.io <> DontCare

  val intermediateResult = RegInit(0.U(bitWidth.W))             // create register that keeps value trough clockcycles
  val counterOfCycles = RegInit(1.U(log2Ceil(bitWidth + 1).W))  // create register to count passed cycles (may be smaller than input)


  when (io.start && io.shamt > 0.U) {
    Rotater.io.input := io.input                // link the register that stores the first input of the SequentialRotater to the input of the rotater
    counterOfCycles := 1.U                      // reset counterOfCycles to 1, since we start new and have passed exactly one cycle
    intermediateResult := Rotater.io.result     // update the register with the output of our rotation by 1
  }.elsewhen (counterOfCycles < io.shamt) {
    Rotater.io.input := intermediateResult
    counterOfCycles := counterOfCycles + 1.U    // Counter goes up, as one cycle has passed
    intermediateResult := Rotater.io.result     // update the register with the output of our rotation by 1
  } .otherwise {
    Rotater.io.input := 0.U
  }

  when (io.start && io.shamt === 0.U) {
    io.result := io.input
    io.done := true.B
  } .otherwise {
    //Return last iteration of bitShift
    io.result := intermediateResult
    io.done := counterOfCycles === io.shamt   // we should have completed exactly io.shamt rotations by 1 -> ideally this is true
  }

}
