# vim:sw=2 syntax=asm
.data 
Live:
 .asciiz "X"
Dead: 
 .asciiz "_"
 
 
.text
  .globl simulate_automaton, print_tape

# Simulate one step of the cellular automaton
# Arguments:
#     $a0 : address of configuration in memory
#   0($a0): eca       (1 word)
#   4($a0): tape      (1 word)
#   8($a0): tape_len  (1 byte)
#   9($a0): rule      (1 byte)
#  10($a0): skip      (1 byte)
#  11($a0): column    (1 byte)
#
# Returns: Nothing, but updates the tape in memory location 4($a0)
simulate_automaton:

  #Storing
  addiu $sp $sp -4
  sw $ra 0($sp) 
  addiu $sp $sp -4  
  sw $a1 0($sp) 
  addiu $sp $sp -4  
  sw $a0 0($sp)  
  addiu $sp $sp -4
  sw $v0 0($sp)
  addiu $sp $sp -4
  sw $t0 0($sp)
  addiu $sp $sp -4
  sw $t1 0($sp)
  addiu $sp $sp -4
  sw $t2 0($sp)
  addiu $sp $sp -4
  sw $t3 0($sp)
  addiu $sp $sp -4
  sw $t4 0($sp)
  addiu $sp $sp -4
  sw $t5 0($sp)
  addiu $sp $sp -4
  sw $t6 0($sp)
  addiu $sp $sp -4
  sw $s3 0($sp)
  addiu $sp $sp -4
  sw $v1 0($sp)
  addiu $sp $sp -4
  sw $a2 0($sp)
  addiu $sp $sp -4
  sw $a3 0($sp)
  


  #Start
  lw $a1 4($a0)   #load tape in reg a1
  lb $a2 8($a0)   #load tape_len in reg a2
  lb $a3 9($a0)   #load rule in reg a3
  
  move $v0 $a1      #copy tape into reg v0
  subi $v1 $a2 1    #set reg v1 to tape_len minus 1 
  srlv $v0 $v0 $v1  #shift the tape to get the last cell
  andi $v0 $v0 1    #left most cell in reg v0
  
  move $v1 $a1      #copy tape into reg v1
  andi $v1 $v1 1    #right most cell reg v1
  
  move $t0 $a1      #copy tape into reg  t0
  subi $t1 $a2 2    #set reg t1 to tape_len minus 2
  srlv $t1 $t0 $t1  #shift the tape to get the second last cell
  andi $t1 $t1 1    #second last cell in reg t1
  
   move $t2 $v1    #copy right most into reg t2
   sll  $t2 $t2 1  #shift reg t2 1 to left  
   or $t2 $t2 $v0  #right most and left most in reg t2
   sll $t2 $t2 1   #shift reg t2 1 to left  
   or $t2 $t2 $t1  #pair of the first three cells in reg t2
   
   lb $t5 8($a0)      #setiing reg t5 to tape_len
   subi $t5 $t5 3     #shifter starts at tape_len -3
   li $t4 0           #load 0 into reg t4
   
   b loop          #branch to loop

loop:
  beq $a2 1 end     #loop counter set to tape_len
  subi $a2 $a2 1    #loop counter minuis 1
  jal rule          #branch to rule
  lw  $t6 4($a0)    #copy tape into reg t6
  sll $t2 $t2 1     #shift pointer on to left
  andi $t2 $t2 7    #deleting the left most neighbor
  srlv $t6 $t6 $t5  #shifting tape to bring the next right neighbor to lsd
  subi $t5 $t5 1    #shift one less to right next time
  andi $t6 $t6 1    #next right neighbor in reg t6
  or   $t2 $t2 $t6  #next neighborhood in reg t2
  b loop            #loop 
  
  
  
rule:
  lb   $s3 9($a0)   #copy rule into reg s3
  move $t3 $t2      #set reg t3 to how often to shift rule
  srlv $s3 $s3 $t3  #lsd in reg s3 in the respective rule
  andi $s3 $s3 1    #respective rule in reg s3
  or   $t4 $t4 $s3  #storing the next gen in reg t4
  sll  $t4 $t4 1    #shift reg t4 1 to left
  jr $ra            #back to loop
   
end:
  or   $t2 $t2 $v0  #creating last neigborhood-left most is right most neighbor
  lb   $s3 9($a0)   #copy rule into reg s3
  move $t3 $t2      #set reg t3 to how often to shift rule
  srlv $s3 $s3 $t3  #lsd in reg s3 in the respective rule
  andi $s3 $s3 1    #respective rule in reg s3
  or   $t4 $t4 $s3  #storing the next gen in reg t4
  sw   $t4 4($a0)   #storing next Generation in reg t4
  
  #Storing
  lw $a3 0($sp)
  addiu $sp $sp 4
  lw $a2 0($sp)
  addiu $sp $sp 4
  lw $v1 0($sp)
  addiu $sp $sp 4
  lw $s3 0($sp)
  addiu $sp $sp 4
  lw $t6 0($sp)
  addiu $sp $sp 4
  lw $t5 0($sp)
  addiu $sp $sp 4
  lw $t4 0($sp)
  addiu $sp $sp 4
  lw $t3 0($sp)
  addiu $sp $sp 4
  lw $t2 0($sp)
  addiu $sp $sp 4
  lw $t1 0($sp)
  addiu $sp $sp 4
  lw $t0 0($sp)
  addiu $sp $sp 4
  lw $v0 0($sp)
  addiu $sp $sp 4
  lw $a0 0($sp)
  addiu $sp $sp 4
  lw $a1 0($sp)
  addiu $sp $sp 4
  lw $ra 0($sp)
  addiu $sp $sp 4
  
  jr $ra           #return
  
# Print the tape of the cellular automaton
# Arguments:
#     $a0 : address of configuration in memory
#   0($a0): eca       (1 word)
#   4($a0): tape      (1 word)
#   8($a0): tape_len  (1 byte)
#   9($a0): rule      (1 byte)
#  10($a0): skip      (1 byte)
#  11($a0): column    (1 byte)
#
# Return nothing, print the tape as follows:
#   Example:
#       tape: 42 (0b00101010)
#       tape_len: 8
#   Print:  
#       __X_X_X_

print_tape:
  #Storing
  addiu $sp $sp -4
  sw $ra 0($sp) 
  addiu $sp $sp -4
  sw $a0 0($sp)  
  addiu $sp $sp -4
  sw $a2 0($sp)  
  addiu $sp $sp -4
  sw $t1 0($sp) 
  addiu $sp $sp -4
  sw $t2 0($sp)  
  addiu $sp $sp -4
  sw $a1 0($sp)  
  addiu $sp $sp -4
  sw $v0 0($sp) 
  
  #print_tape start
  lw $a1 4($a0) #load tape into reg a1
  lb $t1 8($a0) #load tape_len into reg t1
  move $t2 $t1  #reg t2 tells how often to shift
  
Loop:
  beq  $t1 $zero End  #branch to End if tape_len = 0
  addi $t1 $t1 -1     #tape_len minus 1
  addi $t2 $t2 -1     #shifter minus 1
  srlv $a2 $a1 $t2    #right shift the tape to the value in reg t2
  andi $a2 $a2 1      #lsd of the tape in reg a2
  beqz $a2 dead       #if dead cell branch to dead
  bgt  $a2 $zero live #if live cell branch to live
  b Loop              #loop until tape_len = 0
  
dead:
   li $v0 4     #reg v0 = 4
   la $a0 Dead  #reg a0 "_"
   syscall      #syscall printing "_"
   b Loop       #branch to loop
   

live: 
  li $v0 4     #reg v0 = 4
  la $a0 Live  #reg a0 "X"
  syscall      #syscall printing "X"
  b Loop       #branch to loop

End:  
  li $a0 10          #load charachter '\n' into reg a0
  li $v0 11          # reg v0 = 11
  syscall            # syscall printing a new line
  
  #Storing
  lw $v0 0($sp)
  addiu $sp $sp 4
  lw $a1 0($sp)
  addiu $sp $sp 4
  lw $t2 0($sp)
  addiu $sp $sp 4
  lw $t1 0($sp)
  addiu $sp $sp 4
  lw $a2 0($sp)
  addiu $sp $sp 4
  lw $a0 0($sp)
  addiu $sp $sp 4
  lw $ra 0($sp)
  addiu $sp $sp 4
  
  
  jr $ra   #return
