package RISCV.implementation.RV32B

import chisel3._
import chisel3.util._

import RISCV.interfaces.generic.AbstractExecutionUnit
import RISCV.model._
import bitmanipulation.AbstractGeneralizedReverser
import bitmanipulation.AbstractShuffler
import bitmanipulation.AbstractSequentialRotater

class BitPermutationUnit(
    genGeneralizedReverser: () => AbstractGeneralizedReverser,
    genShuffler: () => AbstractShuffler,
    genRotater: () => AbstractSequentialRotater
) extends AbstractExecutionUnit(InstructionSets.BitPerm) {

  io.misa := "b01__0000__0_00000_00000_00000_00000_00010".U

  val generalizedReverser = Module(genGeneralizedReverser())
  val shuffler = Module(genShuffler())
  val rotater = Module(genRotater())

  io.stall := STALL_REASON.NO_STALL

  // io_data <> DontCare
  // io_reg <> DontCare
  // io_pc <> DontCare
  // io_reset <> DontCare
  // io_trap <> DontCare

  // generalizedReverser.io <> DontCare
  // shuffler.io <> DontCare
  // rotater.io <> DontCare

  // Init
  rotater.io.input := 0.U

  io_reg.reg_rd := io.instr(11, 7)
  io_reg.reg_write_en := false.B
  io_reg.reg_write_data := 0.U

  io_data <> DontCare

  generalizedReverser.io.input := 0.U
  generalizedReverser.io.pattern := 0.U
  
  shuffler.io.input := 0.U
  shuffler.io.unshuffle := 0.U
  shuffler.io.pattern := 0.U

  rotater.io.input := 0.U
  rotater.io.start := false.B
  
  // TODO: Set correct rotater.io.shamt
  rotater.io.shamt := 0.U // TODO: SET CORRECT


  io_trap.trap_valid := false.B
  io_trap.trap_reason := TRAP_REASON.NONE

  // TODO: SET io_pc.pc_we correct (e.g. stall -> no write, others -> write)
  io_pc.pc_wdata := io_pc.pc + 4.U
  io_pc.pc_we := false.B

  io_reg.reg_rs1 := io.instr(19, 15)

  when (io.instr_type === RISCV_TYPE.grev || io.instr_type === RISCV_TYPE.shfl || io.instr_type === RISCV_TYPE.unshfl || io.instr_type === RISCV_TYPE.rol || io.instr_type === RISCV_TYPE.ror) {
    io_reg.reg_rs2 := io.instr(24, 20)
  } .otherwise {
    io_reg.reg_rs2 := 0.U
  }

  val immediate = Fill(20, io.instr(31)) ## io.instr(31, 20)

  val rotaterRotated = RegInit(false.B)

  // Implementing the instructions
  switch (io.instr_type) {
    is (RISCV_TYPE.grev) {
      generalizedReverser.io.input := io_reg.reg_read_data1
      generalizedReverser.io.pattern := io_reg.reg_read_data2
      io_reg.reg_write_data := generalizedReverser.io.result
      io_reg.reg_write_en := true.B
      io_pc.pc_we := true.B
    }
    is (RISCV_TYPE.grevi) {
      generalizedReverser.io.input := io_reg.reg_read_data1
      generalizedReverser.io.pattern := immediate(4, 0)
      io_reg.reg_write_data := generalizedReverser.io.result
      io_reg.reg_write_en := true.B
      io_pc.pc_we := true.B
    }
    is (RISCV_TYPE.shfl) {
      shuffler.io.input := io_reg.reg_read_data1
      shuffler.io.pattern := io_reg.reg_read_data2
      shuffler.io.unshuffle := 0.U
      io_reg.reg_write_data := shuffler.io.result
      io_reg.reg_write_en := true.B
      io_pc.pc_we := true.B
    }
    is (RISCV_TYPE.shfli) {
      shuffler.io.input := io_reg.reg_read_data1
      shuffler.io.pattern := immediate(4, 0)
      shuffler.io.unshuffle := 0.U
      io_reg.reg_write_data := shuffler.io.result
      io_reg.reg_write_en := true.B
      io_pc.pc_we := true.B
    }
    is (RISCV_TYPE.unshfl) {
      shuffler.io.input := io_reg.reg_read_data1
      shuffler.io.pattern := io_reg.reg_read_data2
      shuffler.io.unshuffle := 1.U
      io_reg.reg_write_data := shuffler.io.result
      io_reg.reg_write_en := true.B
      io_pc.pc_we := true.B
    }
    is (RISCV_TYPE.unshfli) {
      shuffler.io.input := io_reg.reg_read_data1
      shuffler.io.pattern := immediate(4, 0)
      shuffler.io.unshuffle := 1.U
      io_reg.reg_write_data := shuffler.io.result
      io_reg.reg_write_en := true.B
      io_pc.pc_we := true.B
    }

    is (RISCV_TYPE.rol) {
      val reversed = Reverse(io_reg.reg_read_data1)
      
      rotater.io.input := reversed

      rotater.io.shamt := io_reg.reg_read_data2(4,0)
      
      io.stall := STALL_REASON.EXECUTION_UNIT

      when (!rotaterRotated) {
        rotater.io.start := true.B
        rotaterRotated := true.B
      } .otherwise {
        rotater.io.start := false.B
      }

      // Stall until the rotation is done
      when (rotater.io.done && rotaterRotated) {
        io_reg.reg_write_data := Reverse(rotater.io.result)
        io_reg.reg_write_en := true.B
        io.stall := STALL_REASON.NO_STALL // Clear stall
        io_pc.pc_we := true.B
        rotaterRotated := false.B
      }
    }
    is (RISCV_TYPE.ror) {      
      rotater.io.input := io_reg.reg_read_data1

      rotater.io.shamt := io_reg.reg_read_data2(4,0)
      
      io.stall := STALL_REASON.EXECUTION_UNIT

      when (!rotaterRotated) {
        rotater.io.start := true.B
        rotaterRotated := true.B
      } .otherwise {
        rotater.io.start := false.B
      }

      // Stall until the rotation is done
      when (rotater.io.done && rotaterRotated) {
        io_reg.reg_write_data := rotater.io.result
        io_reg.reg_write_en := true.B
        io.stall := STALL_REASON.NO_STALL // Clear stall
        io_pc.pc_we := true.B
        rotaterRotated := false.B
      }
    }
    is (RISCV_TYPE.rori) {
      rotater.io.input := io_reg.reg_read_data1

      rotater.io.shamt := immediate(4, 0)
      
      io.stall := STALL_REASON.EXECUTION_UNIT

      when (!rotaterRotated) {
        rotater.io.start := true.B
        rotaterRotated := true.B
      } .otherwise {
        rotater.io.start := false.B
      }

      // Stall until the rotation is done
      when (rotater.io.done && rotaterRotated) {
        io_reg.reg_write_data := rotater.io.result
        io_reg.reg_write_en := true.B
        io.stall := STALL_REASON.NO_STALL // Clear stall
        io_pc.pc_we := true.B
        rotaterRotated := false.B
      }
    }
  }
}