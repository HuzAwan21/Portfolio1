# bootup
_start: 
    la t0, exception_handler        # Load exception handler address
    csrw mtvec, t0                  # Set mtvec CSR to exception handler

    # set mepc to user_systemcalls
    la t0, user_systemcalls         # Load user program start address
    csrw mepc, t0

    mret                            # return to user mode

exception_handler:
    # Save registers ra, t0, t1, t2, t3, t4, t5
    
    sw ra, 0(x0)
    sw t0, 4(x0)
    sw t1, 8(x0)
    sw t2, 12(x0)
    sw t3, 16(x0)
    sw t4, 20(x0)
    sw t5, 24(x0)

    # Check cause of exception
    csrr t0, mcause 
    li t1, 8                      # environment call from U-mode
    andi t2, t0, 0x7FFFFFFF       # mask interrupt bit
    bne t2, t1, exception_end     # if not ecall, return immediately

    # Handle system call
    li t1, 11
    beq a7, t1, sys_print_char
    li t1, 4
    beq a7, t1, sys_print_string

    # Other syscalls: just return
    j exception_end

sys_print_char:
    # Load terminal_ready address
    la t3, terminal_ready        # Load address of terminal_ready
    
wait_ready_char:
    lw t1, 0(t3)                  # load terminal_ready
    andi t1, t1, 1
    beqz t1, wait_ready_char

    # Load terminal_data address
    la t3, terminal_data          # Load address of terminal_data

    sw a0, 0(t3)                  # write char to terminal_data
    j exception_end

sys_print_string:
    mv t1, a0                     # t1 = pointer to string start

    # Load terminal_ready address
    la t4, terminal_ready

    # Load terminal_data address
    la t5, terminal_data

print_loop:
    lb t2, 0(t1)                  # load byte/next Char at t1
    beqz t2, exception_end        # if zero byte, end printing

wait_ready_str:
    lw t3, 0(t4)                  # load terminal_ready
    andi t3, t3, 1
    beqz t3, wait_ready_str       # wait while terminal not ready

    sb t2, 0(t5)                  # write byte to terminal_data
    addi t1, t1, 1
    j print_loop

exception_end:
    # Increment mepc to next instruction to avoid repeating ecall
    csrr t0, mepc
    addi t0, t0, 4
    csrw mepc, t0

    # Restore registers
    lw ra, 0(x0)
    lw t0, 4(x0)
    lw t1, 8(x0)
    lw t2, 12(x0)
    lw t3, 16(x0)
    lw t4, 20(x0)
    lw t5, 24(x0)
    
    mret
