# vim:sw=2 syntax=asm
.data

.text
  .globl gen_byte, gen_bit

# Arguments:
#     $a0 : address of configuration in memory
#   0($a0): eca       (1 word)
#   4($a0): tape      (1 word)
#   8($a0): tape_len  (1 byte)
#   9($a0): rule      (1 byte)
#  10($a0): skip      (1 byte)
#  11($a0): column    (1 byte)
#
# Return value:
#  Compute the next valid byte (00, 01, 10) and put into $v0
#  If 11 would be returned, produce two new bits until valid
#
gen_byte:

  #Storing
  addiu $sp $sp -4
  sw $ra 0($sp)
  addiu $sp $sp -4
  sw $a2 0($sp)
  
  #Start
  li $a2 0  #empty reg a2
  
gen_byte_loop:
   addiu $sp $sp -4 #making space in stack
   sw $a2 0($sp)    #sroring first bit in stack before calling funtion
   jal gen_bit      #call gen_bit
   lw $a2 0($sp)    #loading first bit back into reg $a2
   addiu $sp $sp 4  #making space in stack
   beqz $v0 Eq_zero #jump to Eq_zero if reg v0 = 0
   move $a2 $v0     #reg a2 = 1
   addiu $sp $sp -4 #making space in stack
   sw $a2 0($sp)    #sroring first bit in stack before calling funtion
   jal gen_bit      #call gen_bit
   lw $a2 0($sp)    #loading first bit back into reg $a2
   addiu $sp $sp 4  #making space in stack
   beqz $v0 Exit    #Exit if 10 
   b gen_byte_loop  #loop if 11
Eq_zero:
   move $a2 $v0     # reg a2 first bit 0
   addiu $sp $sp -4 #making space in stack
   sw $a2 0($sp)    #sroring first bit in stack before calling funtion
   jal gen_bit      #call gen_bit
   lw $a2 0($sp)    #loading first bit back into reg $a2
   addiu $sp $sp 4  #making space in stack
   b Exit           #Exit
Exit:  
  sll $a2 $a2 1     #shift first bit in reg a2 one to left
  or $v0 $v0 $a2    #attach second bit to the first & store in reg v0   
  
  # Storing
  lw $a2 0($sp)
  addiu $sp $sp 4
  lw $ra 0($sp)
  addiu $sp $sp 4
  
  jr $ra           #return
     
# Arguments:
#     $a0 : address of configuration in memory
#   0($a0): eca       (1 word)
#   4($a0): tape      (1 word)
#   8($a0): tape_len  (1 byte)
#   9($a0): rule      (1 byte)
#  10($a0): skip      (1 byte)
#  11($a0): column    (1 byte)
#
# Return value:
#  Look at the field {eca} and use the associated random number generator to generate one bit.
#  Put the computed bit into $v0
#
gen_bit:
    
    #Storing
    addiu $sp $sp -4
    sw $ra 0($sp)
    addiu $sp $sp -4
    sw $a1 0($sp)
    addiu $sp $sp -4
    sw $a2 0($sp)
    addiu $sp $sp -4
    sw $a3 0($sp)   
    addiu $sp $sp -4
    sw $a0 0($sp)
    addiu $sp $sp -4
    sw $t1 0($sp)
    addiu $sp $sp -4
    sw $t2 0($sp)
    
  #Start      
  lw $a1 0($a0)    #load eca into reg a1
  
  #if
  bne $a1 $zero gen_bit_eca_none_zero  #branch to gen_bit_eca_none_zero if eca is non zero
  #else
    li $a0 0        #empty reg a0
    li $v0 41       #reg v0 = 0x29
    syscall         #syscall: reg a0 contains the next pseudorandom
    andi $v0 $a0 1  #computed bit in reg v0
    
    b finish        #branch to finish

  gen_bit_eca_none_zero:
  
    lb $a2 10($a0)  #load skip in reg a2
     
   calling_automaton:        #calling automaton skip times
    beqz $a2 done            #if skip = 0 branch to done
    addiu $sp $sp -4         #making space in stack
    sw $a2 0($sp)            #storing reg a2 before calling a function
    jal simulate_automaton   #call simulate_automaton
    lw $a2 0($sp)            #loading back reg a1
    addiu $sp $sp 4          #making space in stack
    addi $a2 $a1 -1          #skipped 1x
    b calling_automaton      #branch to calling_automaton  
  
   done:
   
   lb $t2 11($a0)    #load column in reg a2
   lw $a3 4($a0)     #load tape in reg a3
   lb $t1 8($a0)     #tape_len in reg t1
   sub $a2 $t1 $t2   #reg a2 = how often to shift
   sub $a2 $a2 1     #shifter minus 1
   srlv $a3 $a3 $a2  #shifthing tape and bringing columnnth bit to start
   andi $v0 $a3 1    #computed bit in reg v0
   b finish          #finish
    
finish:

    #Storing
    lw $t2 0($sp)
    addiu $sp $sp 4
    lw $t1 0($sp)
    addiu $sp $sp 4
    lw $a0 0($sp)
    addiu $sp $sp 4
    lw $a3 0($sp)
    addiu $sp $sp 4
    lw $a2 0($sp)
    addiu $sp $sp 4
    lw $a1 0($sp)
    addiu $sp $sp 4
    lw $ra 0($sp)
    addiu $sp $sp 4
    
    jr $ra          # return
