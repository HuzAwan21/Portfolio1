# TODO: wait for keyboard input
# TODO: read keyboard input
# TODO: update cursor and display accordingly
# TODO: wait for next keyboard input
paint:
    # display is in memory, formula for pixel at(x,y) = 4 * x + 32 * 4 * y, weil als eindimensionaler array
    # -> dort steht value, letzte 3 bytes haben jeweils den R- bzw. G/B-wert des pixels
    # verändern dieser farb bytes durch ändern zu 0x00 oder 0xff führt zuz Änderung im display (Doku)
    
    # Save here temporaries. Think to adjust stack pointer
    sw ra, 0(x0)
    sw t0, 4(x0)            # keyboard_ready address
    sw t1, 8(x0)            # we load the ascii signs here for parsing      | load other immediates in moving
    sw t2, 12(x0)           # we load the data of the keyboard here
    sw t3, 16(x0)           # address of display
    sw t4, 20(x0)           # keyboard_data address
    sw t5, 24(x0)           # cursor position
    sw t6, 28(x0)           # cursor color
    sw a0, 32(x0)           # display + cursorposition. is only updated when we need it immediately afterwards
    sw a1, 36(x0)           # red
    sw a2, 40(x0)           # green
    sw a3, 44(x0)           # blue
    sw a4, 48(x0)           # saved color of the display under the cursor
    sw a5, 52(x0)           # base address of display
    sw a6, 56(x0)           # for calculation of coordinates to check for bounds
    sw a7, 60(x0)           # width (= 128) (32 * 4)

    # Init cursorposition and color at (15, 15) with no color selected -> white = 0x00FFFFFF
    li t5, 1980         # (15, 15) = 4 * 15 + 32 * 4 * 15
    li t6, 0x00FFFFFF

    la t0, keyboard_ready
    la t4, keyboard_data
    la t3, display
    mv a5, t3

    li a1, 0x00FF0000
    li a2, 0x0000FF00
    li a3, 0x000000FF
    li a7, 128


    add a0, t3, t5      # address of cursor to save

    lw a4, 0(a0)        # load the color of the display at cursor position to a4
    sw t6, 0(a0)

wait_for_input:
    lw t1, 0(t0)
    andi t1, t1, 1
    beqz t1, wait_for_input

read_input:
    lbu t2, 0(t4)

decide_where_to_jump:
    li t1, 119      # ascii for w
    beq t2, t1, input_is_w

    li t1, 97       # ascii for a
    beq t2, t1 input_is_a
    
    li t1, 115       # ascii for s
    beq t2, t1 input_is_s

    li t1, 100       # ascii for d
    beq t2, t1 input_is_d
    
    li t1, 114       # ascii for r
    beq t2, t1 input_is_r

    li t1, 103       # ascii for g
    beq t2, t1 input_is_g

    li t1, 98       # ascii for b
    beq t2, t1 input_is_b
    
    li t1, 32       # ascii for space
    beq t2, t1, input_is_space

    # Otherwise:
    j wait_for_input

# For cursor
input_is_w:
    # restore the old display:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_up:
    div a6, t5, a7
    beqz a6, update_display_or_cursor_color
    sub t5, t5, a7

    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)

    j update_display_cursor

input_is_a:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_left:
    rem a6, t5, a7
    beqz a6, update_display_or_cursor_color      # if the x-coordinate is 0 -> do not move
    addi t5, t5, -4
    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)
    j update_display_cursor

input_is_s:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_down:
    div a6, t5, a7
    li t1, 31
    bge a6, t1, update_display_or_cursor_color      # if a6 (here: y - coordinate) == 31 -> do not move
    add t5, t5, a7

    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)

    j update_display_cursor

input_is_d:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_right:
    rem a6, t5, a7
    li t1, 124      # 32 * 4
    beq a6, t1, update_display_or_cursor_color
    addi t5, t5, 4
    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)
    j update_display_cursor



# For colors simply adjust memory area of display
input_is_r:
    xor t6, t6, a1
    j update_display_or_cursor_color

input_is_g:
    xor t6, t6, a2
    j update_display_or_cursor_color

input_is_b:
    xor t6, t6, a3
    j update_display_or_cursor_color


input_is_space:
    mv a4, t6
    j update_display_or_cursor_color


update_display_cursor:
    sw t6, 0(a0)
    j wait_for_input


update_display_or_cursor_color:
    add a0, t3, t5
    sw t6, 0(a0)

    j wait_for_input


end:
    lw ra, 0(x0)
    lw t0, 4(x0)
    lw t1, 8(x0)
    lw t2, 12(x0)
    lw t3, 16(x0)
    lw t4, 20(x0)
    lw t5, 24(x0)
    lw t6, 28(x0)
    lw a0, 32(x0)
    lw a1, 36(x0)
    lw a2, 40(x0)
    lw a3, 44(x0)
    lw a4, 48(x0)
    lw a5, 52(x0)
    lw a6, 56(x0)
    lw a7, 60(x0)
