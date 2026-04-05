package RISCV.utils

import chisel3.Bool
import cats.instances.list
import scala.collection.mutable

object PermBuilder {

  /** This function takes a mapping for the permutation and returns the list of
    * necessary instructions to implement the permutation.
    *
    * You may assume that the map encodes a valid permutation, i.e., that every
    * destination bit is associated with a unique source bit.
    *
    * You may only write to the register rd.
    *
    * @param rd
    *   The destination register
    * @param rs1
    *   The source register
    * @param perm
    *   A map from representing the permutation, mapping destination bit
    *   positions to source bit positions.
    * @return
    *   A list of strings representing the instructions to implement the
    *   permutation e.g. List("grevi x1, x2, 0x01", "grevi x1, x2, 0x02", ...)
    */
  def buildPermutation(rd: Int, rs1: Int, perm: Map[Int, Int]): List[String] = {
    def isFinished(map: Map[Int, Int]) : Boolean = {
      for (i <- 0 until 32) {
        if (map(i) != i) {
          return false
        }
      }
      return true
    }

    def isFinishedList(liste: List[Int]) : Boolean = {
      for (i <- 0 until 32) {
        if (liste(i) != i) {
          return false
        }
      }
      return true
    }

    def isSingleRotation(liste: List[Int]) : Boolean = {
      for (i <- 1 until 32) {
        if ((liste(i - 1) + 1) % 32 != liste(i)) {
          return false
        }
      }
      return true
    }

    def isSingleGrevi15(liste: List[Int]) : Boolean = {
      var counter = 31
      for (i <- 0 until 32) {
        if ((liste(i) != counter)) {
          return false
        }
        counter -= 1
      }
      return true
    }

    def performRotation(
        permutation: List[Int],
        immediate: Int
    ): List[Int] = {
        val n = immediate & 31
        permutation.drop(n) ++ permutation.take(n)
    }

    def performGeneralizedReverse(
        permutation: List[Int],
        immediate: Int
    ): List[Int] = {
        var new_permutation = permutation
        for (i <- 0 until 32) yield {
            var old_index = i
            if ((immediate & (1 << 4)) != 0) old_index = if (old_index >= 16) old_index - 16 else old_index + 16
            if ((immediate & (1 << 3)) != 0) old_index = if (old_index % 16 >= 8) old_index - 8 else old_index + 8
            if ((immediate & (1 << 2)) != 0) old_index = if (old_index % 8 >= 4) old_index - 4 else old_index + 4
            if ((immediate & (1 << 1)) != 0) old_index = if (old_index % 4 >= 2) old_index - 2 else old_index + 2
            if ((immediate & (1 << 0)) != 0) old_index = if (old_index % 2 == 1) old_index - 1 else old_index + 1
        
            new_permutation = new_permutation.updated(
                i,
                permutation(old_index)
            )
        }
        new_permutation
    }

    def performShuffle(
        permutation: List[Int],
        immediate: Int
    ): List[Int] = {
        var new_permutation = permutation
        for (i <- 0 until 32) yield {
            var old_index = i
            if ((immediate & (1 << 0)) != 0) old_index = old_index % 4 match {
                case 0 => old_index
                case 1 => old_index + 1
                case 2 => old_index - 1
                case 3 => old_index 
            }
            if ((immediate & (1 << 1)) != 0) old_index = (old_index % 8 / 2) match {
                case 0 => old_index
                case 1 => old_index + 2
                case 2 => old_index - 2
                case 3 => old_index 
            }
            if ((immediate & (1 << 2)) != 0) old_index = (old_index % 16 / 4) match {
                case 0 => old_index
                case 1 => old_index + 4
                case 2 => old_index - 4
                case 3 => old_index 
            }
            if ((immediate & (1 << 3)) != 0) old_index = (old_index % 32 / 8) match {
                case 0 => old_index
                case 1 => old_index + 8
                case 2 => old_index - 8
                case 3 => old_index 
            }
            new_permutation = new_permutation.updated(
                i,
                permutation(old_index)
            )
        }
        new_permutation
    }

    def performUnshuffle(
        permutation: List[Int],
        immediate: Int
    ): List[Int] = {
        var new_permutation = permutation
        for (i <- 0 until 32) yield {
            var old_index = i
            if ((immediate & (1 << 3)) != 0) old_index = (old_index % 32 / 8) match {
                case 0 => old_index
                case 1 => old_index + 8
                case 2 => old_index - 8
                case 3 => old_index 
            }
            if ((immediate & (1 << 2)) != 0) old_index = (old_index % 16 / 4) match {
                case 0 => old_index
                case 1 => old_index + 4
                case 2 => old_index - 4
                case 3 => old_index 
            }
            if ((immediate & (1 << 1)) != 0) old_index = (old_index % 8 / 2) match {
                case 0 => old_index
                case 1 => old_index + 2
                case 2 => old_index - 2
                case 3 => old_index 
            }
            if ((immediate & (1 << 0)) != 0) old_index = old_index % 4 match {
                case 0 => old_index
                case 1 => old_index + 1
                case 2 => old_index - 1
                case 3 => old_index 
            }
            new_permutation = new_permutation.updated(
                i,
                permutation(old_index)
            )
        }
        new_permutation
        
    }

    def checkOfDeath(workingMap: List[Int], currentCycle: Int, lastOperation : String = "", recursionDepth : Int = 10, lastPattern : Int = -1) : Tuple2[Boolean, List[String]] = {
      if (isFinishedList(workingMap)) {
        return (true, Nil)
      } else if (currentCycle == recursionDepth) {
        return (false, Nil)
      }

      if (isSingleRotation(workingMap)) {
        return (true, List(f"rori x$rd, x$rs1, ${32 - workingMap(0)}"))
      }
      if (isSingleGrevi15(workingMap)) {
        return (true, List(f"grevi x$rd, $rs1, 15"))
      }


      if (lastOperation != "rori") {
        for (shamt <- 1 to 31) {
          val roli = performRotation(workingMap, shamt)
          
          val resultRoli = checkOfDeath(roli, currentCycle + 1, lastOperation = "rori")
          if (resultRoli._1) {
            return (true, f"rori x$rd, x$rs1, $shamt" :: resultRoli._2)
          }
          
        }
      }


      for (shamt <- 1 to 31) {
        if (!(lastOperation == "grevi" && lastPattern == shamt)) {
          val grevi = performGeneralizedReverse(workingMap, shamt)
          
          val resultGrevi = checkOfDeath(grevi, currentCycle + 1, lastOperation = "grevi", lastPattern = shamt)
          if (resultGrevi._1) {
            return (true, f"grevi x$rd, x$rs1, $shamt" :: resultGrevi._2)
          }
        }
      }
      

      for (shamt <- 1 to 15) {
        if (!((lastOperation == "shfli" || lastOperation == "unshfli") && lastPattern == shamt)) {
          val shfli = performShuffle(workingMap, shamt)
        
          val resultShfli = checkOfDeath(shfli, currentCycle + 1, lastOperation = "shfli", lastPattern = shamt)
          if (resultShfli._1) {
            return (true, f"shfli x$rd, x$rs1, $shamt" :: resultShfli._2)
          }
        }
      }

      for (shamt <- 1 to 15) {
        if (!((lastOperation == "unshfli" || lastOperation == "shfli") && lastPattern == shamt)) {
          val unshfli = performUnshuffle(workingMap, shamt)

          val resultUnshfli = checkOfDeath(unshfli, currentCycle + 1, lastOperation = "unshfli", lastPattern = shamt)
          if (resultUnshfli._1) {
            return (true, f"unshfli x$rd, x$rs1, $shamt" :: resultUnshfli._2)
          }
        }
      }
      return (false, Nil)
    }
    var permList : List[Int] = List()

    for (i <- 0 until 32) {
      permList = permList.appended(perm(i))
    }

    val result = checkOfDeath(permList, 0)
    if (result._1) {
      return result._2
    } else {
      return Nil
    }
  }

}




    // // As inverse to rori
    // def performRoli(performMap : Map[Int, Int], shamt : Int) : Map[Int, Int] = {

    //   var resultRoli : Map[Int, Int] = Map.empty
    //   for (i <- 0 until 32) {
    //     val newIndex = (i - shamt + 32) % 32
    //     resultRoli = resultRoli.updated(i, performMap(newIndex))
    //   }
    //   return resultRoli
    // }

    // def performGrevi(performMap: Map[Int, Int], pattern : Int) : Map[Int, Int] = {
    //   var workingMap = performMap
    //   if ((pattern & 16) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 32) {
    //       newMap = newMap.updated(i, workingMap((i + 16) % 32))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 8) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 16) {
    //       newMap = newMap.updated(i, workingMap((i + 8) % 16))
    //       newMap = newMap.updated(i + 16, workingMap(16 + (i + 8) % 16))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 4) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 8) {
    //       newMap = newMap.updated(i, workingMap((i + 4) % 8))
    //       newMap = newMap.updated(i + 8, workingMap(8 + (i + 4) % 8))
    //       newMap = newMap.updated(i + 2 * 8, workingMap(2 * 8 + (i + 4) % 8))
    //       newMap = newMap.updated(i + 3 * 8, workingMap(3 * 8 + (i + 4) % 8))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 2) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 4) {
    //       newMap = newMap.updated(i, workingMap((i + 2) % 4))
    //       newMap = newMap.updated(i + 4, workingMap(8 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 2 * 4, workingMap(2 * 2 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 3 * 4, workingMap(3 * 2 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 4 * 4, workingMap(8 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 5  * 4, workingMap(2 * 2 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 6 * 4, workingMap(3 * 2 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 7  * 4, workingMap(2 * 2 + (i + 2) % 4))
    //       newMap = newMap.updated(i + 8 * 4, workingMap(3 * 2 + (i + 2) % 4))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 1) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 2) {
    //       newMap = newMap.updated(i, workingMap((i + 2) % 2))
    //       newMap = newMap.updated(i + 2, workingMap(2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 2 * 2, workingMap(2 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 3 * 2, workingMap(3 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 4 * 2, workingMap(4 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 5  * 2, workingMap(5 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 6 * 2, workingMap(6 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 7  * 2, workingMap(7 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 8 * 2, workingMap(8 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 9 * 2, workingMap(9 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 10 * 2, workingMap(10 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 11 * 2, workingMap(11 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 12  * 2, workingMap(12 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 13 * 2, workingMap(13 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 14  * 2, workingMap(14 * 2 + (i + 2) % 2))
    //       newMap = newMap.updated(i + 15 * 2, workingMap(15 * 2 + (i + 2) % 2))
    //     }
    //     workingMap = newMap
    //   }
    //   return workingMap
    // }

    
    // def performShfli(performMap: Map[Int, Int], pattern: Int) : Map[Int, Int] = {
    //   var workingMap = performMap
      
    //   if ((pattern & 8) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 8) {
    //       newMap = newMap.updated(i, workingMap(8))
    //     }
    //     for (i <- 24 until 32) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 8 until 16) {
    //       newMap = newMap.updated(i, workingMap(i + 8))
    //       newMap = newMap.updated(i + 8, workingMap(i))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 4) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 4) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 4 until 8) {
    //       newMap = newMap.updated(i, workingMap(i + 4))
    //       newMap = newMap.updated(i + 4, workingMap(i))
    //     }
    //     for (i <- 8 until 20) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 20 until 24) {
    //       newMap = newMap.updated(i, workingMap(i + 4))
    //       newMap = newMap.updated(i + 4, workingMap(i))
    //     }
    //     for (i <- 28 until 32) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 2) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 2) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 2 until 4) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 6 until 10) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 10 until 12) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 14 until 18) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 18 until 20) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 22 until 24) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 24 until 26) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 30 until 32) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     workingMap = newMap
    //   }
    //   if ((pattern & 1) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     newMap = newMap.updated(0, workingMap(0))
    //     newMap = newMap.updated(1, workingMap(2))
    //     newMap = newMap.updated(2, workingMap(1))
    //     newMap = newMap.updated(3, workingMap(3))

    //     newMap = newMap.updated(4, workingMap(4))
    //     newMap = newMap.updated(5, workingMap(6))
    //     newMap = newMap.updated(6, workingMap(5))
    //     newMap = newMap.updated(7, workingMap(7))

    //     newMap = newMap.updated(8, workingMap(8))
    //     newMap = newMap.updated(9, workingMap(10))
    //     newMap = newMap.updated(10, workingMap(9))
    //     newMap = newMap.updated(11, workingMap(11))

    //     newMap = newMap.updated(12, workingMap(12))
    //     newMap = newMap.updated(13, workingMap(14))
    //     newMap = newMap.updated(14, workingMap(13))
    //     newMap = newMap.updated(15, workingMap(15))

    //     newMap = newMap.updated(16, workingMap(16))
    //     newMap = newMap.updated(17, workingMap(18))
    //     newMap = newMap.updated(18, workingMap(17))
    //     newMap = newMap.updated(19, workingMap(19))

    //     newMap = newMap.updated(20, workingMap(21))
    //     newMap = newMap.updated(21, workingMap(22))
    //     newMap = newMap.updated(22, workingMap(21))
    //     newMap = newMap.updated(23, workingMap(23))

    //     newMap = newMap.updated(24, workingMap(24))
    //     newMap = newMap.updated(25, workingMap(26))
    //     newMap = newMap.updated(26, workingMap(25))
    //     newMap = newMap.updated(27, workingMap(27))

    //     newMap = newMap.updated(28, workingMap(28))
    //     newMap = newMap.updated(29, workingMap(30))
    //     newMap = newMap.updated(30, workingMap(29))
    //     newMap = newMap.updated(31, workingMap(31))

    //     workingMap = newMap
    //   }

    //   return workingMap
    // }

    // def performUnShfli(performMap: Map[Int, Int], pattern: Int) : Map[Int, Int] = {
    //   var workingMap = performMap
    
    //   if ((pattern & 1) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     newMap = newMap.updated(0, workingMap(0))
    //     newMap = newMap.updated(1, workingMap(2))
    //     newMap = newMap.updated(2, workingMap(1))
    //     newMap = newMap.updated(3, workingMap(3))

    //     newMap = newMap.updated(4, workingMap(4))
    //     newMap = newMap.updated(5, workingMap(6))
    //     newMap = newMap.updated(6, workingMap(5))
    //     newMap = newMap.updated(7, workingMap(7))

    //     newMap = newMap.updated(8, workingMap(8))
    //     newMap = newMap.updated(9, workingMap(10))
    //     newMap = newMap.updated(10, workingMap(9))
    //     newMap = newMap.updated(11, workingMap(11))

    //     newMap = newMap.updated(12, workingMap(12))
    //     newMap = newMap.updated(13, workingMap(14))
    //     newMap = newMap.updated(14, workingMap(13))
    //     newMap = newMap.updated(15, workingMap(15))

    //     newMap = newMap.updated(16, workingMap(16))
    //     newMap = newMap.updated(17, workingMap(18))
    //     newMap = newMap.updated(18, workingMap(17))
    //     newMap = newMap.updated(19, workingMap(19))

    //     newMap = newMap.updated(20, workingMap(21))
    //     newMap = newMap.updated(21, workingMap(22))
    //     newMap = newMap.updated(22, workingMap(21))
    //     newMap = newMap.updated(23, workingMap(23))

    //     newMap = newMap.updated(24, workingMap(24))
    //     newMap = newMap.updated(25, workingMap(26))
    //     newMap = newMap.updated(26, workingMap(25))
    //     newMap = newMap.updated(27, workingMap(27))

    //     newMap = newMap.updated(28, workingMap(28))
    //     newMap = newMap.updated(29, workingMap(30))
    //     newMap = newMap.updated(30, workingMap(29))
    //     newMap = newMap.updated(31, workingMap(31))

    //     workingMap = newMap
    //   }
    //   if ((pattern & 2) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 2) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 2 until 4) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 6 until 10) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 10 until 12) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 14 until 18) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 18 until 20) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 22 until 24) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 24 until 26) {
    //       newMap = newMap.updated(i, workingMap(i + 2))
    //       newMap = newMap.updated(i + 2, workingMap(i))
    //     }
    //     for (i <- 30 until 32) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     workingMap = newMap
    //   }

    //   if ((pattern & 4) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 4) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 4 until 8) {
    //       newMap = newMap.updated(i, workingMap(i + 4))
    //       newMap = newMap.updated(i + 4, workingMap(i))
    //     }
    //     for (i <- 8 until 20) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 20 until 24) {
    //       newMap = newMap.updated(i, workingMap(i + 4))
    //       newMap = newMap.updated(i + 4, workingMap(i))
    //     }
    //     for (i <- 28 until 32) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     workingMap = newMap
    //   }

    //   if ((pattern & 8) != 0) {
    //     var newMap : Map[Int, Int] = Map.empty
    //     for (i <- 0 until 8) {
    //       newMap = newMap.updated(i, workingMap(8))
    //     }
    //     for (i <- 24 until 32) {
    //       newMap = newMap.updated(i, workingMap(i))
    //     }
    //     for (i <- 8 until 16) {
    //       newMap = newMap.updated(i, workingMap(i + 8))
    //       newMap = newMap.updated(i + 8, workingMap(i))
    //     }
    //     workingMap = newMap
    //   }

    //   return workingMap
    // }