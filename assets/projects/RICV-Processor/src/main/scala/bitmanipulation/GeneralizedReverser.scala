package bitmanipulation

import chisel3._
import chisel3.util._

abstract class AbstractGeneralizedReverser(bitWidth: Int) extends Module {
  val io = IO(new Bundle {
    val input = Input(UInt(bitWidth.W))
    val pattern = Input(UInt(log2Ceil(bitWidth).W))
    val result = Output(UInt(bitWidth.W))
  })
}

class GeneralizedReverser(bitWidth: Int)
    extends AbstractGeneralizedReverser(bitWidth) {

  val patternBitwidth = log2Ceil(bitWidth)
  if (bitWidth == 1) {       // 1 bit -> nothing to do with it -> give it back
    io.result := io.input
  } else if (bitWidth == 2) {   //2 bits ->swap if necessary, else give just back
    io.result := Mux(io.pattern(0), Cat(io.input(0), io.input(1)), io.input)
  } else {
    val newBitwidth = bitWidth / 2        // recursive case -> generate new subcircuits with half of the bits
    val lsbRev = Module(new GeneralizedReverser(newBitwidth))   //one for left part of bitstring
    val msbRev = Module(new GeneralizedReverser(newBitwidth))   //one for right part of bitstring

    val msbBits = io.input(bitWidth - 1, newBitwidth)
    val lsbBits = io.input(newBitwidth - 1, 0)

    // dependend on the msb of the pattern, decide what to give the recursive circuits
    val muxResultForLsb = Mux(io.pattern(patternBitwidth - 1), msbBits, lsbBits)
    val muxResultForMsb = Mux(io.pattern(patternBitwidth - 1), lsbBits, msbBits)

    lsbRev.io.input := muxResultForLsb
    msbRev.io.input := muxResultForMsb

    lsbRev.io.pattern := io.pattern(patternBitwidth - 2, 0) //new patternbitwidth is now reduced by two,
    msbRev.io.pattern := io.pattern(patternBitwidth - 2, 0) // since indexing starts with one, most significant one is "left behind"

    //Decide in which order inputs are being concatenated
    io.result := Cat(msbRev.io.result, lsbRev.io.result)
  }
}
