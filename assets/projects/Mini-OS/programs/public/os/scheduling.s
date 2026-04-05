_start:
    la t0, exception_handler
    csrw mtvec, t0

    la t0, startup
    csrw mepc, t0

    # setup interrupts as needed

    csrr t0, mstatus
    ori t0, t0, 16
    csrw mstatus, t0

    # set up data structures: e.g. number of processes, current process, ...
    # we are allowed tu use memory between 0x0 and 0xffff.
    # Save of all Registers (x1 - x31): Process 0/Startup: 1000(x0), Process 1: 2000(x0), Process 2: 3000(x0), ...
    # Save of all estimated times to completion: Process 0/Startup: 100(x0), Process 1: 104(x0), Process 2: 108(x0), ...
    # Save of all addresses of processes: Process 0/Startup: 44(x0), Process 1: 48(x0), Process 2: 52(x0), ...
    # number of processes (inclusive to startup): 36(x0)
    # STARTUP IS ACTIVE: 32(x0)
    # which process is currently running: 40(x0) - startup: 1, process 1: 2, process 2: 3, ...
    # which processes are active: startup: 200(x0), process 1: 201(x0), process 2: 202(x0), ... (lb)
    # save temporary registers beginning at 0(x0)
    # save last mtime at 300(x0) for calculating the difference and updating estimated time to completion

    li t0, 1            # 8 ADDITIONAL processes
    sw t0, 36(x0)

    li t0, 1
    sw t0, 32(x0)

    sw t0, 40(x0)  # current active process is startup

    li t0, 1
    sb t0, 200(x0)  # startup is active

    la t0, mtime
    lw t0, 0(t0)
    sw t0, 300(x0)

    mret


shutdown:
    j shutdown # infinite loop


exception_handler:
    sw t0, 0(x0)

    li t0, 221
    beq t0, a7, process_create

    li t0, 93
    beq t0, a7, kill

    mret


process_create:
# update estimated time to completion
    sw t1, 4(x0)
    sw t2, 8(x0)

    la t0, mtime
    lw t0, 0(t0)
    lw t1, 300(x0)
    sub t2, t0, t1   # calculate difference of mtime

    lw t1, 40(x0)
    li t0, 4
    mul t0, t0, t1
    addi t0, t0, 96
    lw t1, 0(t0)
    sub t1, t1, t2  # subtract the (pre) calculated difference
    sw t1, 0(t0)

    lw t1, 4(x0)
    lw t2, 8(x0)

    lw t0, 32(x0)  # check if startup is active
    beqz t0, startup_not_active

check_9:
    # check if there are already 8 processes running (PLUS startup) => 9
    lw t0, 36(x0)
    li t1, 9            # 8 processes + startup
    beq t0, t1, number_of_processes_too_high

    addi t0, t0, 1
    sw t0, 36(x0)
    j determine_inactive_process

startup_not_active:
    # Here, we check for 8 processes (since startup is not active anymore)
    lw t0, 36(x0)
    li t1, 8
    beq t0, t1, number_of_processes_too_high
    addi t0, t0, 1
    sw t0, 36(x0)

    # here begins the actual process_create

# determine memory location - choose first one that is not active
determine_inactive_process:         # determine free memory slot (beginning at 1)
    li t1, 1        # counter
    sw t2, 8(x0)
    li t2, 200
loop:
    lbu t0, 0(t2)
    beqz t0, mark_active
    addi t1, t1, 1
    addi t2, t2, 1
    j loop

mark_active:
    addi t2, t1, 199
    li t0, 1
    sb t0, 0(t2)

save_mepc:
    # save mepc of old process
    lw t2, 40(x0)  # get current active process
    li t0, 4
    mul t0, t2, t0
    addi t0, t0, 40

    csrr t2, mepc
    addi t2, t2, 4
    sw t2, 0(t0)

    # save mepc of new process
    # set new instruction address (t1 * 4 + 40) (t1 >= 1)
    li t2, 4
    mul t2, t2, t1  # calculate address of new process
    addi t2, t2, 40  # add offset to mepc
    sw a0, 0(t2)  # save mepc of new process

set_estimated_time:
    # save estimated time to completion
    li t0, 4
    mul t0, t1, t0
    addi t0, t0, 96

    sw a1, 0(t0)  # save estimated time to completion

calculate_memory:
    # calculate memory location for new process
    li t0, 1000
    mul t0, t1, t0

    # save registers at new location aka copy
    sw x1, 0(t0)

    
    mv x1, t0       # we saved x1 at 0(t0), -> we can use it now as base address
    lw t0, 0(x0)
    lw t1, 4(x0)
    lw t2, 8(x0)

    sw x2, 4(x1)
    sw x3, 8(x1)
    sw x4, 12(x1)
    sw x5, 16(x1)
    sw x6, 20(x1)
    sw x7, 24(x1)
    sw x8, 28(x1)
    sw x9, 32(x1)
    sw x10, 36(x1)
    sw x11, 40(x1)
    sw x12, 44(x1)
    sw x13, 48(x1)
    sw x14, 52(x1)
    sw x15, 56(x1)
    sw x16, 60(x1)
    sw x17, 64(x1)
    sw x18, 68(x1)
    sw x19, 72(x1)
    sw x20, 76(x1)
    sw x21, 80(x1)
    sw x22, 84(x1)
    sw x23, 88(x1)
    sw x24, 92(x1)
    sw x25, 96(x1)
    sw x26, 100(x1)
    sw x27, 104(x1)
    sw x28, 108(x1)
    sw x29, 112(x1)
    sw x30, 116(x1)
    sw x31, 120(x1)


save_registers_of_active_process:
    # currently running process:
    sw t0, 0(x0)
    sw t1, 4(x0)
    lw t0, 40(x0)

    li t1, 1000

    mul t0, t0, t1

    sw x1, 0(t0)

    mv x1, t0

    lw t0, 0(x0)
    lw t1, 4(x0)

    sw x2, 4(x1)
    sw x3, 8(x1)
    sw x4, 12(x1)
    sw x5, 16(x1)
    sw x6, 20(x1)
    sw x7, 24(x1)
    sw x8, 28(x1)
    sw x9, 32(x1)
    sw x10, 36(x1)
    sw x11, 40(x1)
    sw x12, 44(x1)
    sw x13, 48(x1)
    sw x14, 52(x1)
    sw x15, 56(x1)
    sw x16, 60(x1)
    sw x17, 64(x1)
    sw x18, 68(x1)
    sw x19, 72(x1)
    sw x20, 76(x1)
    sw x21, 80(x1)
    sw x22, 84(x1)
    sw x23, 88(x1)
    sw x24, 92(x1)
    sw x25, 96(x1)
    sw x26, 100(x1)
    sw x27, 104(x1)
    sw x28, 108(x1)
    sw x29, 112(x1)
    sw x30, 116(x1)
    sw x31, 120(x1)

determine_what_next:
    lw t0, 32(x0)  # check if startup is active
    beqz t0, next_is_not_startup
    j next_is_startup


next_is_not_startup:
    li t0, 1    # Counter
    li t1, 2147483647  # maximum estimated time to completion (2^31 - 1)
    li t2, 0    # argmin
    li t3, 100  # address of estimated time to completion
    li t4, 200  # address of active processes
    li t6, 9    # number of processes (including startup) (maximal number)

loop_what_next:
    beq t0, t6, set_mepc  # if counter equals number of processes, set mepc (you are finished with loop)
    lbu t5, 0(t4)
    beqz t5, next

    lw t5, 0(t3)    # load estimated time to completion
    blt t5, t1, is_smaller

    # else (not smaller)
    j next

is_smaller:
    mv t1, t5  # save new minimum estimated time to completion
    mv t2, t0  # save argmin

next:
    addi t0, t0, 1  # increment counter
    addi t3, t3, 4  # increment address of estimated time to completion
    addi t4, t4, 1  # increment address of active processes
    j loop_what_next


set_mepc:
    # t2 holds argmin
    li t0, 4
    mul t0, t0, t2  # calculate address of next process
    addi t0, t0, 40  # add offset to mepc

    lw t1, 0(t0)  # load mepc of next process
    csrw mepc, t1

    # set current active process
    sw t2, 40(x0)  # save current active process


load_registers:
    li t0, 1000
    mul t0, t2, t0
    lw x1, 0(t0)  # load x1
    lw x2, 4(t0)
    lw x3, 8(t0)
    lw x4, 12(t0)
    # lw x5, 16(t0)         erst später (x5 = t0) (problem since base address usage of t0)
    lw x6, 20(t0)
    lw x7, 24(t0)
    lw x8, 28(t0)
    lw x9, 32(t0)
    lw x10, 36(t0)
    lw x11, 40(t0)
    lw x12, 44(t0)
    lw x13, 48(t0)
    lw x14, 52(t0)
    lw x15, 56(t0)
    lw x16, 60(t0)
    lw x17, 64(t0)
    lw x18, 68(t0)
    lw x19, 72(t0)
    lw x20, 76(t0)
    lw x21, 80(t0)
    lw x22, 84(t0)
    lw x23, 88(t0)
    lw x24, 92(t0)
    lw x25, 96(t0)
    lw x26, 100(t0)
    lw x27, 104(t0)
    lw x28, 108(t0)
    lw x29, 112(t0)
    lw x30, 116(t0)
    lw x31, 120(t0)

    lw x5, 16(t0)       # x5 = t0

    # update mtime
    sw t0, 0(x0)
    la t0, mtime
    lw t0, 0(t0)
    sw t0, 300(x0)  # save current mtime
    lw t0, 0(x0)

    mret


next_is_startup:
    lw t0, 44(x0)
    csrw mepc, t0

    lw x1, 1000(x0)
    lw x2, 1004(x0)
    lw x3, 1008(x0)
    lw x4, 1012(x0)
    lw x5, 1016(x0)
    lw x6, 1020(x0)
    lw x7, 1024(x0)
    lw x8, 1028(x0)
    lw x9, 1032(x0)
    lw x10, 1036(x0)
    lw x11, 1040(x0)
    lw x12, 1044(x0)
    lw x13, 1048(x0)
    lw x14, 1052(x0)
    lw x15, 1056(x0)
    lw x16, 1060(x0)
    lw x17, 1064(x0)
    lw x18, 1068(x0)
    lw x19, 1072(x0)
    lw x20, 1076(x0)
    lw x21, 1080(x0)
    lw x22, 1084(x0)
    lw x23, 1088(x0)
    lw x24, 1092(x0)
    lw x25, 1096(x0)
    lw x26, 1100(x0)
    lw x27, 1104(x0)
    lw x28, 1108(x0)
    lw x29, 1112(x0)
    lw x30, 1116(x0)
    lw x31, 1120(x0)

    # update mtime
    sw t0, 0(x0)
    la t0, mtime
    lw t0, 0(t0)
    sw t0, 300(x0)  # save current mtime
    lw t0, 0(x0)

    mret
    


number_of_processes_too_high:
    li a0, -1

    csrr t0, mepc
    addi t0, t0, 4
    csrw mepc, t0
    
    lw t1, 4(x0)
    lw t0, 0(x0)

    mret


kill:
    # set active process to 0
    lw t0, 40(x0)  # get current active process
    addi t1, t0, 199  # calculate address of active process
    li t0, 0
    sb t0, 0(t1)  # set active process to 0

    lw t0, 32(x0)  # check if startup is active (aka is this startup?) (If you call this the first time, startup is always the caller)
    beqz t0, determine_what_after_kill

is_startup:
    # set startup active to 0
    li t0, 0
    sw t0, 32(x0)

determine_what_after_kill:
    # check if there is only one process running, if so -> shutdown
    li t0, 1
    lw t1, 36(x0)
    beq t0, t1, shutdown

    lw t0, 36(x0)
    addi t0, t0, -1
    sw t0, 36(x0)

end_kill:
    lw t0, 0(x0)
    # There are active processes -> decide where to continue
    j next_is_not_startup
