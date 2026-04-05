package bitmanipulation

import chisel3._
import chisel3.util._

abstract class AbstractShuffler(bitWidth: Int) extends Module {
  val io = IO(new Bundle {
    val input = Input(UInt(bitWidth.W))
    val pattern = Input(UInt((log2Ceil(bitWidth) - 1).W))
    val unshuffle = Input(UInt(1.W))
    val result = Output(UInt(bitWidth.W))
  })
}

class Shuffler(bitWidth: Int) extends AbstractShuffler(bitWidth) {
  val patternBitWidth = log2Ceil(bitWidth) - 1
  if (bitWidth == 4) {
    io.result := Mux(io.pattern(0), Cat(io.input(3), io.input(1), io.input(2), io.input(0)), io.input)
  } else {
    val msbRecursive = Module(new Shuffler(bitWidth / 2))
    val lsbRecursive = Module(new Shuffler(bitWidth / 2))

    msbRecursive.io.unshuffle := io.unshuffle
    lsbRecursive.io.unshuffle := io.unshuffle
    msbRecursive.io.pattern := io.pattern(patternBitWidth - 2, 0)
    lsbRecursive.io.pattern := io.pattern(patternBitWidth - 2, 0)
    when (io.unshuffle === 0.U) {
      val result = shuffle(io.input, bitWidth, io.pattern(patternBitWidth - 1))
      msbRecursive.io.input := result(bitWidth - 1, bitWidth / 2)
      lsbRecursive.io.input := result(bitWidth / 2 - 1, 0)
      io.result := Cat(msbRecursive.io.result, lsbRecursive.io.result)
    } .otherwise {
      msbRecursive.io.input := io.input(bitWidth - 1, bitWidth / 2)
      lsbRecursive.io.input := io.input(bitWidth / 2 - 1, 0)
      io.result := shuffle(Cat(msbRecursive.io.result, lsbRecursive.io.result), bitWidth, io.pattern(patternBitWidth - 1))
    }
  }

  def shuffle(toBeShuffled: UInt, bitWidthOftoBeShuffled: Int, patternBit: UInt) : UInt = {
    val msbQuarter = toBeShuffled(bitWidthOftoBeShuffled - 1, bitWidthOftoBeShuffled / 4 * 3)
    val midMsbQuarter = toBeShuffled(bitWidthOftoBeShuffled / 4 * 3 - 1, bitWidthOftoBeShuffled / 2)
    val midLsbQuarter = toBeShuffled(bitWidthOftoBeShuffled / 2 - 1, bitWidthOftoBeShuffled / 4)
    val lsbQuarter = toBeShuffled(bitWidthOftoBeShuffled / 4 - 1, 0)

    return Mux(patternBit.asBool, Cat(msbQuarter, midLsbQuarter, midMsbQuarter, lsbQuarter), toBeShuffled)
  }
}

