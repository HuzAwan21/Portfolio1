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
    sw s1, 64(x0)           # immediate 2 for bonusformula in m
    sw s2, 68(x0)           # immediate result for clearing for specific color for bonusformula in m from display
    sw s3, 72(x0)           # immediate result for bonusformula in m
    sw s4, 76(x0)           # immediate finished result
    sw s5, 80(x0)           # immediate result for clearing for specific color for bonusformula in m from cursor
    sw s6, 84(x0)           # address of 1 (bonus task)
    sw s7, 88(x0)

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
    li s1, 2

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

    li t1, 112      # ascii for p
    beq t2, t1, input_is_p

    li t1, 109      # ascii for m
    beq t2, t1, input_is_m

    li t1, 49       # ascii for 1
    beq t2, t1, input_is_1

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
    

# BONUS:
input_is_p:
    mv t6, a4
    j update_display_or_cursor_color


input_is_m:
    li s4, 0

calculate_red:
    # clear for red
    srli s2, a4, 16
    andi s2, s2, 0xFF
    srli s5, t6, 16
    andi s5, s5, 0xFF

    # red_cursor + red_display
    add s3, s2, s5
    # div by 2
    div s3, s3, s1
    # set immediate result
    add s4, s4, s3
    slli s4, s4, 8

calculate_green:
    # clear for green
    srli s2, a4, 8
    andi s2, s2, 0xFF
    srli s5, t6, 8
    andi s5, s5, 0xFF

    # green_cursor + green_display
    add s3, s2, s5
    # div by 2
    div s3, s3, s1
    # set immediate result
    add s4, s4, s3
    slli s4, s4, 8

calculate_blue:
    # clear for blue
    andi s2, a4, 0xFF
    andi s5, t6, 0xFF

    # blue_cursor + blue_display
    add s3, s2, s5
    # div by 2
    div s3, s3, s1
    # set immediate result
    add s4, s4, s3

    # setze farbe von cursor auf immediate result
    mv t6, s4

    j update_display_or_cursor_color


input_is_1:
    li s6, 0x00010000

loop:
    lbu s7, 0(s6)
    beqz s7, wait_for_input         # if byte is 0 -> end
    addi s6, s6, 1

parsing:
    li t1, 119      # ascii for w
    beq s7, t1, input_is_w_bonus

    li t1, 97       # ascii for a
    beq s7, t1 input_is_a_bonus
    
    li t1, 115       # ascii for s
    beq s7, t1 input_is_s_bonus

    li t1, 100       # ascii for d
    beq s7, t1 input_is_d_bonus
    
    li t1, 114       # ascii for r
    beq s7, t1 input_is_r_bonus

    li t1, 103       # ascii for g
    beq s7, t1 input_is_g_bonus

    li t1, 98       # ascii for b
    beq s7, t1 input_is_b_bonus
    
    li t1, 32       # ascii for space
    beq s7, t1, input_is_space_bonus

    li t1, 112      # ascii for p
    beq s7, t1, input_is_p_bonus

    li t1, 109      # ascii for m
    beq s7, t1, input_is_m_bonus

    li t1, 49       # ascii for 1
    beq s7, t1, input_is_1

    # Otherwise: to be on the safe side:
    j wait_for_input

input_is_w_bonus:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_up_bonus:
    div a6, t5, a7
    beqz a6, update_display_or_cursor_color_bonus
    sub t5, t5, a7

    add a0, t5, a5
    lw a4, 0(a0)

    j update_display_or_cursor_color_bonus

input_is_a_bonus:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_left_bonus:
    rem a6, t5, a7
    beqz a6, update_display_or_cursor_color_bonus      # if the x-coordinate is 0 -> do not move
    addi t5, t5, -4
    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)
    j update_display_cursor_bonus

input_is_s_bonus:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_down_bonus:
    div a6, t5, a7
    li t1, 31
    bge a6, t1, update_display_or_cursor_color_bonus      # if a6 (here: y - coordinate) == 31 -> do not move
    add t5, t5, a7

    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)

    j update_display_cursor_bonus

input_is_d_bonus:
    add a0, a5, t5
    sw a4, 0(a0)

move_one_right_bonus:
    rem a6, t5, a7
    li t1, 124      # 32 * 4
    beq a6, t1, update_display_or_cursor_color_bonus
    addi t5, t5, 4
    # Save of display color
    add a0, t5, a5
    lw a4, 0(a0)
    j update_display_cursor_bonus

input_is_r_bonus:
    xor t6, t6, a1
    j update_display_or_cursor_color_bonus

input_is_g_bonus:
    xor t6, t6, a2
    j update_display_or_cursor_color_bonus

input_is_b_bonus:
    xor t6, t6, a3
    j update_display_or_cursor_color_bonus


input_is_space_bonus:
    mv a4, t6
    j update_display_or_cursor_color_bonus

input_is_p_bonus:
    mv t6, a4
    j update_display_or_cursor_color_bonus


input_is_m_bonus:
    li s4, 0

calculate_red_bonus:
    # clear for red
    srli s2, a4, 16
    andi s2, s2, 0xFF
    srli s5, t6, 16
    andi s5, s5, 0xFF

    # red_cursor + red_display
    add s3, s2, s5
    # div by 2
    div s3, s3, s1
    # set immediate result
    add s4, s4, s3
    slli s4, s4, 8

calculate_green_bonus:
    # clear for green
    srli s2, a4, 8
    andi s2, s2, 0xFF
    srli s5, t6, 8
    andi s5, s5, 0xFF

    # green_cursor + green_display
    add s3, s2, s5
    # div by 2
    div s3, s3, s1
    # set immediate result
    add s4, s4, s3
    slli s4, s4, 8

calculate_blue_bonus:
    # clear for blue
    andi s2, a4, 0xFF
    andi s5, t6, 0xFF

    # blue_cursor + blue_display
    add s3, s2, s5
    # div by 2
    div s3, s3, s1
    # set immediate result
    add s4, s4, s3

    # setze farbe von cursor auf immediate result
    mv t6, s4

    j update_display_or_cursor_color_bonus


update_display_cursor:
    sw t6, 0(a0)
    j wait_for_input


update_display_or_cursor_color:
    add a0, t3, t5
    sw t6, 0(a0)

    j wait_for_input

update_display_cursor_bonus:
    sw t6, 0(a0)
    j loop

update_display_or_cursor_color_bonus:
    add a0, t3, t5
    sw t6, 0(a0)

    j loop




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
    lw s1, 64(x0)
    lw s2, 68(x0)
    lw s3, 72(x0)
    lw s4, 76(x0)
    lw s5, 80(x0)
    lw s6, 84(x0)