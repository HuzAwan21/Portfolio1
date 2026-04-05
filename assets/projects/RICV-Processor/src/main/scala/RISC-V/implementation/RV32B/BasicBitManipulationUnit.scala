package RISCV.implementation.RV32B

import chisel3._
import chisel3.util._

import RISCV.interfaces.generic.AbstractExecutionUnit
import RISCV.model._
import bitmanipulation.AbstractLeadingZerosCounter
import bitmanipulation.AbstractGeneralizedReverser
import bitmanipulation.GeneralizedReverser

class BasicBitManipulationUnit(
    genLeadingZerosCounter: () => AbstractLeadingZerosCounter
) extends AbstractExecutionUnit(InstructionSets.BasicBit) {
  io.misa := "b01__0000__0_00000_00000_00000_00000_00010".U

  val leadingZerosCounter = Module(genLeadingZerosCounter())

  io.stall := STALL_REASON.NO_STALL

  // io_data <> DontCare
  // io_reg <> DontCare
  // io_pc <> DontCare
  // io_reset <> DontCare
  // io_trap <> DontCare

  // leadingZerosCounter.io <> DontCare

  // Inits
  io_reg.reg_rd := io.instr(11, 7)
  io_reg.reg_rs1 := io.instr(19, 15)
  
  when (io.instr_type === RISCV_TYPE.clz || io.instr_type === RISCV_TYPE.ctz || io.instr_type === RISCV_TYPE.cpop) {
    io_reg.reg_rs2 := 0.U
  } .otherwise {
    io_reg.reg_rs2 := io.instr(24, 20)
  }

  io_pc.pc_wdata := io_pc.pc + 4.U
  io_pc.pc_we := true.B

  io_data <> DontCare

  io_trap <> DontCare

  // Defaults
  io_reg.reg_write_en := false.B
  io_reg.reg_write_data := 0.U

  leadingZerosCounter.io.input := 0.U


  // Implementing the instructions
  switch (io.instr_type) {
    is (RISCV_TYPE.clz) {
      io_reg.reg_write_en := true.B
      leadingZerosCounter.io.input := io_reg.reg_read_data1
      io_reg.reg_write_data := leadingZerosCounter.io.result
    }
    is (RISCV_TYPE.ctz) {
      io_reg.reg_write_en := true.B
      val reversed = Reverse(io_reg.reg_read_data1)
      leadingZerosCounter.io.input := reversed
      io_reg.reg_write_data := leadingZerosCounter.io.result
    }
    is (RISCV_TYPE.cpop) {
      io_reg.reg_write_en := true.B
      val popCount = PopCount(io_reg.reg_read_data1)
      io_reg.reg_write_data := popCount
    }
    is (RISCV_TYPE.min) {
      io_reg.reg_write_en := true.B
      val lhs = io_reg.reg_read_data1.asSInt
      val rhs = io_reg.reg_read_data2.asSInt
      io_reg.reg_write_data := Mux(lhs < rhs, lhs, rhs).asUInt
    }
    is (RISCV_TYPE.minu) {
      io_reg.reg_write_en := true.B
      val lhs = io_reg.reg_read_data1
      val rhs = io_reg.reg_read_data2
      io_reg.reg_write_data := Mux(lhs < rhs, lhs, rhs)
    }
    is (RISCV_TYPE.max) {
      io_reg.reg_write_en := true.B
      val lhs = io_reg.reg_read_data1.asSInt
      val rhs = io_reg.reg_read_data2.asSInt
      io_reg.reg_write_data := Mux(lhs > rhs, lhs, rhs).asUInt
    }
    is (RISCV_TYPE.maxu) {
      io_reg.reg_write_en := true.B
      val lhs = io_reg.reg_read_data1
      val rhs = io_reg.reg_read_data2
      io_reg.reg_write_data := Mux(lhs > rhs, lhs, rhs)
    }
  }
}
