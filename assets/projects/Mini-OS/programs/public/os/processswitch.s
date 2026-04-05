# TODO: set up exception handler
# TODO: set up mepc to point to the first instruction of the fibonacci function
# TODO: enable and set up interrupts as needed
# TODO: set up data structures for process control blocks (here not needed, in task 4 needed)
# TODO: execute the fibonacci function until you get an interrupt

_start:
    la t0, exception_handler
    csrw mtvec, t0

    la t0, fibonacci
    csrw mepc, t0

    csrr t0, mie
    ori t0, t0, 128         # set bit 7 to 1 (enable timer interrupt)
    csrw mie, t0

    li t0, 0
    la t1, mtime
    sw t0, 0(t1)

    la t0, factorial
    sw t0, 40(x0)            # address of next instruction

    # use this code to update mtimecmp. It is from description.
    # New comparand is in a1:a0.
    li a0, 323
    li a1, 0
    li t0, -1
    la t1, mtimecmp
    sw t0, 0(t1)
    sw a1, 4(t1)
    sw a0, 0(t1)

    csrr t0, mstatus
    ori t0, t0, 16
    csrw mstatus, t0

    mret


exception_handler:
    # TODO: save some registers
    # TODO: set up new timer interrupt + implement process switch
    # TODO: return to user mode to continue with next process

# let fibonacci be process 1, and factorial 0 saved at 16(x0)
# save fibonacci registers beginning at 100(x0)
# save factorial beginning at 404(x0)

    # check for mip
    sw t0, 0(x0)
    sw t1, 4(x0)
    csrr t0, mip
    andi t0, t0, 128
    beqz t0, ende           # bit 7 is 0 -> no timer interrupt

    # check for mcause
    csrr t0, mcause
    srli t1, t0, 31
    beqz t1, ende

    addi t1, x0, 7
    andi t0, t0, 0x7fffffff # clear the sign bit
    beq t0, t1, ende

is_timer_interrupt:
    sw t1, 12(x0)

    # switch of mepc
    lw t0, 40(x0)

    csrr t1, mepc
    csrw mepc, t0

    sw t1, 40(x0)

    lw t0, 16(x0)
    beqz t0, next_process_is_factorial
    j next_process_is_fibonacci

next_process_is_factorial:
    # next process is fibonacci -> 1
    li t1, 1
    sw t1, 16(x0)

    lw t0, 0(x0)
    lw t1, 4(x0)
    # save registers of fibonacci
    sw x1, 100(x0)
    sw x2, 104(x0)
    sw x3, 108(x0)
    sw x4, 112(x0)
    sw x5, 116(x0)
    sw x6, 120(x0)
    sw x7, 124(x0)
    sw x8, 128(x0)
    sw x9, 132(x0)
    sw x10, 136(x0)
    sw x11, 140(x0)
    sw x12, 144(x0)
    sw x13, 148(x0)
    sw x14, 152(x0)
    sw x15, 156(x0)
    sw x16, 160(x0)
    sw x17, 164(x0)
    sw x18, 168(x0)
    sw x19, 172(x0)
    sw x20, 176(x0)
    sw x21, 180(x0)
    sw x22, 184(x0)
    sw x23, 188(x0)
    sw x24, 192(x0)
    sw x25, 196(x0)
    sw x26, 200(x0)
    sw x27, 204(x0)
    sw x28, 208(x0)
    sw x29, 212(x0)
    sw x30, 216(x0)
    sw x31, 220(x0)

    # load registers of factorial
    lw x1, 404(x0)
    lw x2, 408(x0)
    lw x3, 412(x0)
    lw x4, 416(x0)
    lw x5, 420(x0)
    lw x6, 424(x0)
    lw x7, 428(x0)
    lw x8, 432(x0)
    lw x9, 436(x0)
    lw x10, 440(x0)
    lw x11, 444(x0)
    lw x12, 448(x0)
    lw x13, 452(x0)
    lw x14, 456(x0)
    lw x15, 460(x0)
    lw x16, 464(x0)
    lw x17, 468(x0)
    lw x18, 472(x0)
    lw x19, 476(x0)
    lw x20, 480(x0)
    lw x21, 484(x0)
    lw x22, 488(x0)
    lw x23, 492(x0)
    lw x24, 496(x0)
    lw x25, 500(x0)
    lw x26, 504(x0)
    lw x27, 508(x0)
    lw x28, 512(x0)
    lw x29, 516(x0)
    lw x30, 520(x0)
    lw x31, 524(x0)
    j end_timer_interrupt

next_process_is_fibonacci:
    # next process is facotrial -> 0
    li t1, 0
    sw t1, 16(x0)

    lw t0, 0(x0)
    lw t1, 4(x0)

    # save registers of factorial
    sw x1, 404(x0)
    sw x2, 408(x0)
    sw x3, 412(x0)
    sw x4, 416(x0)
    sw x5, 420(x0)
    sw x6, 424(x0)
    sw x7, 428(x0)
    sw x8, 432(x0)
    sw x9, 436(x0)
    sw x10, 440(x0)
    sw x11, 444(x0)
    sw x12, 448(x0)
    sw x13, 452(x0)
    sw x14, 456(x0)
    sw x15, 460(x0)
    sw x16, 464(x0)
    sw x17, 468(x0)
    sw x18, 472(x0)
    sw x19, 476(x0)
    sw x20, 480(x0)
    sw x21, 484(x0)
    sw x22, 488(x0)
    sw x23, 492(x0)
    sw x24, 496(x0)
    sw x25, 500(x0)
    sw x26, 504(x0)
    sw x27, 508(x0)
    sw x28, 512(x0)
    sw x29, 516(x0)
    sw x30, 520(x0)
    sw x31, 524(x0)

    # load registers of fibonacci
    lw x1, 100(x0)
    lw x2, 104(x0)
    lw x3, 108(x0)
    lw x4, 112(x0)
    lw x5, 116(x0)
    lw x6, 120(x0)
    lw x7, 124(x0)
    lw x8, 128(x0)
    lw x9, 132(x0)
    lw x10, 136(x0)
    lw x11, 140(x0)
    lw x12, 144(x0)
    lw x13, 148(x0)
    lw x14, 152(x0)
    lw x15, 156(x0)
    lw x16, 160(x0)
    lw x17, 164(x0)
    lw x18, 168(x0)
    lw x19, 172(x0)
    lw x20, 176(x0)
    lw x21, 180(x0)
    lw x22, 184(x0)
    lw x23, 188(x0)
    lw x24, 192(x0)
    lw x25, 196(x0)
    lw x26, 200(x0)
    lw x27, 204(x0)
    lw x28, 208(x0)
    lw x29, 212(x0)
    lw x30, 216(x0)
    lw x31, 220(x0)


end_timer_interrupt:
    sw t0, 0(x0)
    sw t1, 4(x0)

    la t0, mtime
    lw t0, 0(t0)
    addi t0, t0, 310
    
    la t1, mtimecmp
    sw t0, 0(t1)

    lw t1, 4(x0)

ende:
    lw t0, 0(x0)
    mret
