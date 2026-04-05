.data
Win:
 .asciiz "W"
Lose:
 .asciiz "L"
Tie: 
 .asciiz "T"
.text
  .globl play_game_once

# Play the game once, that is
# (1) compute two moves (RPS) for the two computer players
# (2) Print (W)in (L)oss or (T)ie, whether the first player wins, looses or ties.
#
# Arguments:
#     $a0 : address of configuration in memory
#   0($a0): eca       (1 word)
#   4($a0): tape      (1 word)
#   8($a0): tape_len  (1 byte)
#   9($a0): rule      (1 byte)
#  10($a0): skip      (1 byte)
#  11($a0): column    (1 byte)
#
# Returns: Nothing, only print either character 'W', 'L', or 'T' to stdout

play_game_once:

  #Storing
  addiu $sp $sp -4
  sw $ra 0($sp) 
  addiu $sp $sp -4
  sw $a0 0($sp)  
  addiu $sp $sp -4
  sw $s1 0($sp) 
  addiu $sp $sp -4
  sw $s2 0($sp)
  addiu $sp $sp -4
  sw $v0 0($sp) 
  
play_game_one_loop:  
  jal gen_byte       #call gen_byte
  addiu $sp $sp -4   #making space in stack
  sw $v0 0($sp)      #plyer one's move is stored in stack before calling function
  jal gen_byte       #call gen_byte
  move $s2 $v0       #player two's move is stored in reg s2
  lw $s1 0($sp)      #loading plyer one's move in reg s1
  addiu $sp $sp 4    #making space in stack
   
  beq $s1 $s2 tie    #tie if both moves are the same
  beq $s1 0 rock     #player one has rock - jump to rock
  beq $s1 1 paper    #player one has paper - jump to paper
  beq $s1 2 scissors #player one has scissors - jump to scissors
  
   
rock:                 #player one has rock  
      beq $s2 1 lose  #player two has paper - jump to lose
      beq $s2 2 win   #player two has scissors - jump to win
      
paper:                #player one has paper  
       beq $s2 0 win  #player two has rock - jump to win
       beq $s2 2 lose #player two has scissors - jump to lose
       
scissors:                 #player one has scissors  
          beq $s2 0 lose  #player two has rock - jump to lose
          beq $s2 1 win   #player two has paper - jump to win
          
tie:  
      la $a0 Tie  #reg a0 has adress of "T"
      j end       #jump to end
      
lose: 
      la $a0 Lose #reg a0 has adress of "L" 
      j end       #jump to end
      
win: 
      la $a0 Win  #reg a0 has adress of "W" 
      j end       #jump to end 
      
end:
  li $v0 4       #reg v0 = 4
  syscall        # syscall print result from reg a0
  
  #Storing
  lw $v0 0($sp)
  addiu $sp $sp 4
  lw $s2 0($sp)
  addiu $sp $sp 4
  lw $s1 0($sp)
  addiu $sp $sp 4
  lw $a0 0($sp)
  addiu $sp $sp 4
  lw $ra 0($sp)
  addiu $sp $sp 4
  
  jr $ra         # return
