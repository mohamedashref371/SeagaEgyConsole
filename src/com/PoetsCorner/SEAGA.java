package com.PoetsCorner;

import java.util.Random;
import static com.PoetsCorner.SEAGA.PlaysFirst.*;

public class SEAGA {

    public static final String version = "2023.08.01";

    private static int temp;

    private static final String[] playersName = {"RedPlayer", "BluePlayer"};

    public static boolean SetPlayerName(int i, String s){
        if (i!=0 && i!=1 || s==null || s.trim().equals("")) return false;
        playersName[i] = s; return true;
    }
    public static boolean SetRedPlayerName(String s){
        if (s!=null && !s.trim().equals("")) {playersName[0] = s; return true;}
        return false;
    }
    public static boolean SetBluePlayerName(String s){
        if (s!=null && !s.trim().equals("") && theGame[16]==0) {playersName[1] = s; return true;}
        return false;
    }

    public static String GetPlayerName(int i){
        if(i==0 || i==1) return playersName[i];
        return null;
    }
    public static String GetRedPlayerName(){return playersName[0];}
    public static String GetBluePlayerName(){return playersName[1];}

    public static int PlayerSteps(int i){
        if (i==0){return theGame[11]+theGame[12]+theGame[13];}
        if (i==1){return theGame[17]+theGame[18]+theGame[19];}
        return -1;
    }
    public static int RedPlayerSteps(){return theGame[11]+theGame[12]+theGame[13];}
    public static int BluePlayerSteps(){return theGame[17]+theGame[18]+theGame[19];}

    private static final int[] theGame =
                   {2 /* player role 0 or 1 */,
                    1,2,3,4,5,6,7,8,9, /* The array fields represent the pieces and the numbers represent their locations
                    [1], [2] and [3] are the pieces of RedPlayer
                    [4], [5] and [6] are empty boxes
                    [7], [8] and [9] are the pieces of BluePlayer */
         /* [10] */ 0 /* selected piece */,
                    0,0,0, /* movementOfPieces */
         /* [14] */ 0 /* 0: Vertical and Horizontal only, 1: V & H and Diagonal */,
         /* [15] */ 2 /* who started playing first, 0: RedPlayer, 1: BluePlayer */,
         /* [16] */ 0 /* 0: player vs. player, 1: player vs. computer */,
                    0,0,0, /* movementOfPieces */
         /* [20] */ 4 /* level */ };
    private static final int[] fastGame = {0/* never used */, 1,2,3,4,5,6,7,8,9}; /* The array fields represent the locations of the pieces, and the numbers represent the pieces */

    static int PieceLocation(int piece) {return theGame[piece];} // return the box of piece.
    static int PieceInBox(int box) {return fastGame[box];} // select the box and return the piece.

    private static final Level[] lvl = Level.values();
    public static String getLevel(){return lvl[theGame[20]].name();}
    public static boolean ChangeLevel(int lvl){
        if (lvl>=0 && lvl <=7) { theGame[20]=lvl; return true;}
        return false;
    }
    public static boolean ChangeLevel(Level lvl){
        if (lvl==null) return false;
        theGame[20]=lvl.getValue();
        return true;
    }

    public static int playerRole() {return theGame[0];}

    private static int win =2;
    public static int getWhoWin() {return win;}
    private static void win() {
        if (theGame[0]==2) return;
        if (aRLine(1,2,3)) win=0;
        else if (aBLine(7,8,9)) win=1;
        else win=2;
    }

    //region - undo & redo
    private static final StringBuilder undo = new StringBuilder(), redo = new StringBuilder();

    public static boolean undo(){
        int l = undo.length();
        if (l==0) return false;
        String s = undo.substring(l-2);
        undo.delete(l-2,l);
        redo.append(s);
        moving(s.substring(0,1),s.substring(1),false);
        theGame[0] = 1- theGame[0];
        theGame[10]=0;
        if (theGame[0]==1 && theGame[16]==1 && !pauseComputer) ComputerIntelligence();
        win();
        return true;
    }

    public static boolean redo(){
        int l = redo.length();
        if (l==0) return false;
        String s = redo.substring(l-2);
        redo.delete(l-2,l);
        undo.append(s);
        moving(s.substring(0,1),s.substring(1),true);
        theGame[0] = 1- theGame[0];
        theGame[10]=0;
        if (theGame[0]==1 && theGame[16]==1 && !pauseComputer) ComputerIntelligence();
        win();
        return true;
    }
    //endregion

    //region - save and load
    public static String save(){
        if (theGame[0]==2) return null;
        StringBuilder s = new StringBuilder();
        for (int i=0; i<20; i++){s.append(theGame[i]).append('/');}
        s.append(theGame[20]);
        return s.toString();
    }

    public static boolean load(String s){
        if (s==null) return false;
         String[] S = s.split("/");
         if (S.length!=21) return false;
         try{
             for (int i=0; i<21; i++) theGame[i] = Integer.parseInt(S[i]);
         } catch(Exception ignored){reset(); return false;}
         fastGameUpdate();
         return true;
    }
    //endregion

    //region - Start and Stop
    public static PlaysFirst playsFirst = ByOurselves;

    public static boolean start(int i){
        if (playsFirst != ByOurselves && i!=0 && i!=1) return start();
        if (theGame[0]!=2) return false;
        theGame[15] = i;
        reset();
        return true;
    }

    public static boolean start(String s){
        if (playsFirst != ByLot){
            if (theGame[0]!=2) return false;
            int a = new Random().nextInt(2);
            if (a==0 && s.equals("a") || a==1 && !s.equals("a")) theGame[15] =0;
            else theGame[15] =1;
            reset();
            return true;
        }
        else return start();
    }

    public static boolean start() {
        if (theGame[0]!=2) return false;
        if (playsFirst == TheWinner && win==0 || playsFirst == TheLoser && win==1 || playsFirst == WhoPlayedSecondLastTime && theGame[15]==1) theGame[15] =0;
        else if (playsFirst == TheWinner && win==1 || playsFirst == TheLoser && win==0 || playsFirst == WhoPlayedSecondLastTime && theGame[15]==0) theGame[15] =1;
        else theGame[15] = new Random().nextInt(2);
        reset();
        return true;
    }

    public static void reset() {
        theGame[0] = theGame[15];
        theGame[10] = 0;
        for(int i=1; i<=9; i++) theGame[i] = i;
        for(int i=11; i<=13; i++) theGame[i] = 0;
        for(int i=17; i<=19; i++) theGame[i] = 0;
        fastGameUpdate();
        undo.setLength(0); redo.setLength(0);
    }

    public static void stop() { theGame[0]=2;}
    //endregion


    public static boolean input(String s) {
        s=s.trim();
        int x, y;
        try{x = Integer.parseInt(s.substring(0,1));} catch (Exception ex) { return false;}
        try{y = Integer.parseInt(s.substring(s.length()-1));} catch (Exception ex) { return false;}
        if (x==4 || x==5 || x==6) { select(y); return select(x);}
        else if (y==4 || y==5 || y==6) { select(x); return select(y);}
        return false;
    }

    private static boolean select(int i){
        if (theGame[0]==0 && (i==1 || i==2 || i==3) || theGame[0]==1 && (i==7 || i==8 || i==9) && theGame[16]==0) theGame[10]=i;
        else if (theGame[10]>0 && (i==4 || i==5 || i==6)) {
            moving(i,theGame[10],true);
            theGame[0] = 1 - theGame[0];
            undo.append(i).append(theGame[10]);
            theGame[10]=0;
            redo.setLength(0);
            win();
            if (theGame[0]==1 && theGame[16]==1 && !pauseComputer) ComputerIntelligence();
        } else return false;
        return true;
    }

    private static void moving(String block, boolean plus){moving(block.substring(0,1),block.substring(1),plus);}
    private static void moving(String blank, String piece, boolean plus) {try {moving(Integer.parseInt(blank),Integer.parseInt(piece),plus);} catch(Exception ignored){}}
    private static void moving(int blank, int piece, boolean plus) {
        temp = theGame[blank]; theGame[blank] = theGame[piece]; theGame[piece] = temp;
        temp = fastGame[theGame[blank]]; fastGame[theGame[blank]] = fastGame[theGame[piece]]; fastGame[theGame[piece]] = temp;
        if (plus) theGame[piece + 10]+=1; else theGame[piece + 10]-=1;
    }

    private static void fastGameUpdate(){ for(int i=1; i<=9; i++) fastGame[theGame[i]] = i;}

    //region - Computer
    public static String computerStep="";

    private static boolean  pauseComputer = true;
    public static void pauseComputer(){ pauseComputer=true;}
    public static void resumeComputer(){
        pauseComputer=false;
        if (theGame[0]==1) ComputerIntelligence();
    }

    public static void ComputerTurn() {ComputerTurn(true);}
    public static void ComputerTurn(boolean On) {
        if (On) {
            theGame[16]=1; playersName[1]="Computer";
            if (theGame[0]==1 && !pauseComputer) ComputerIntelligence();
        }
        else {theGame[16]=0; playersName[1]="Player2";}
    }

    private static boolean functions(int f, String block) {
        try{
            int blank = Integer.parseInt(block.substring(0,1));
            int blue = Integer.parseInt(block.substring(1));
            if (f==1) return ComputerWin(blank,blue);
            if (f==2) return OpponentNotWin(blank,blue);
            if (f==3) return ComputerDouWin(blank,blue);
            if (f==4) return OpponentNotDouWin(blank,blue);
            if (f==5) return ComputerSpecialCases(blank,blue);
            if (f==6) return DiagonalCatchingSleeperComputerPiece(blank,blue);
            if (f==7) return OpponentSpecialCases(blank,blue);
            if (f==8) return SleeperPiece(blue);
            if (f==9) return PieceInMiddle(blank,blue);
        } catch (Exception ignored){}
        return false;
    }

    private static void ComputerIntelligence() {
        int num = new Random().nextInt(100);
        // ArrayList<String> game = new ArrayList<String>(); game.add("47");game.add("48");game.add("48");game.add("49");game.add("57");game.add("58");game.add("59");game.add("67");game.add("68");game.add("69"); ArrayList<String> temp = (ArrayList<String>) game.clone();
        String[] game = {"47","48","49","57","58","59","67","68","69"}; int l = 9;
        String[] temp = game.clone(); int t = l;
        String tmp;
        boolean[] bool = {
              /* [0] */ theGame[20]==0,
              /* [1] */ theGame[20]>=1,
              /* [2] */ theGame[20]>=2,
              /* [3] */ theGame[20]>=3,
              /* [4] */ theGame[20] == 7 || theGame[20] == 6 && num < 75 || theGame[20] == 5 && num < 50 || theGame[20] == 4 && num < 25,
              /* [5] */ (theGame[17] == 0 || theGame[18] == 0 || theGame[19] == 0) && (theGame[14]==1 && theGame[20] >= 6),
              /* [6] */ (theGame[17] == 0 || theGame[18] == 0 || theGame[19] == 0) && (theGame[14]==1 && theGame[20] >= 6),
              /* [7] */ (theGame[17] == 0 || theGame[18] == 0 || theGame[19] == 0) && (theGame[14]==1 && theGame[20] >= 6),
              /* [8] */ theGame[17] == 0 || theGame[18] == 0 || theGame[19] == 0,
              /* [9] */ (theGame[17] == 0 || theGame[18] == 0 || theGame[19] == 0) && theGame[20]==7 && theGame[14]==1
                };
        int i;
        if (!bool[0])
        for(int j=1; j<bool.length; j++){
            if (bool[j]){
                i=0;
                while(i<l){
                    if (!functions(j,game[i])){
                        tmp = game[i]; game[i] = game[l-1]; game[l-1] = tmp;
                        l--; i--;
                    }
                    i++;
                }
                if (l==0){game=temp.clone(); l=t;}
                else {temp=game.clone(); t=l;}
            }
        }
        Choosing(game,l);
    }

    private static void Choosing(String[] game, int l){
        int num = new Random().nextInt(l);
        computerStep = game[num].charAt(0) + " -> " + game[num].charAt(1);
        theGame[10] = Integer.parseInt(game[num].substring(1));
        select(Integer.parseInt(game[num].substring(0,1)));
    }
    //endregion

    //region - Computer Intelligence Functions
    private static void TempMove(int space, int blue, boolean plus){
        temp = theGame[space]; theGame[space] = theGame[blue]; theGame[blue] = temp;
        if (plus) theGame[blue + 10]+=1; else theGame[blue + 10]-=1;
    }

    private static boolean SleeperPiece(int blue){ return theGame[blue + 10] == 0;}

    private static boolean ComputerWin(int space, int blue){
        boolean result = false;
        TempMove(space, blue, true);
        if (aBLine(7, 8, 9)) result = true;
        TempMove(space, blue, false);
        return result;
    }

    private static boolean OpponentNotWin(int space, int blue){
        boolean result = false;
        TempMove(space, blue, true);
        for(int k=4; k<=6; k++){
            if (aRLine(1, 2, k) || aRLine(1, 3, k) || aRLine(2, 3, k)) break;
            if (k==6) result = true;
        }
        TempMove(space, blue, false);
        return result;
    }

    private static boolean ComputerDouWin(int space, int blue){
        int counter=0;
        TempMove(space, blue, true);
        if (((theGame[17] > 0) && (theGame[18] > 0) && (hLine(7, 8, 4) || hLine(7, 8, 5) || hLine(7, 8, 6))) || ((theGame[17] > 0) && (theGame[19] > 0) && (hLine(7, 9, 4) || hLine(7, 9, 5) || hLine(7, 9, 6))) || ((theGame[18] > 0) && (theGame[19] > 0) && (hLine(8, 9, 4) || hLine(8, 9, 5) || hLine(8, 9, 6)))) counter++;
        if (((theGame[17] > 0) && (theGame[18] > 0) && (vLine(7, 8, 4) || vLine(7, 8, 5) || vLine(7, 8, 6))) || ((theGame[17] > 0) && (theGame[19] > 0) && (vLine(7, 9, 4) || vLine(7, 9, 5) || vLine(7, 9, 6))) || ((theGame[18] > 0) && (theGame[19] > 0) && (vLine(8, 9, 4) || vLine(8, 9, 5) || vLine(8, 9, 6)))) counter++;
        if (theGame[14]==1){
            if (((theGame[17] > 0) && (theGame[18] > 0) && (mDiagonal(7, 8, 4) || mDiagonal(7, 8, 5) || mDiagonal(7, 8, 6))) || ((theGame[17] > 0) && (theGame[19] > 0) && (mDiagonal(7, 9, 4) || mDiagonal(7, 9, 5) || mDiagonal(7, 9, 6))) || ((theGame[18] > 0) && (theGame[19] > 0) && (mDiagonal(8, 9, 4) || mDiagonal(8, 9, 5) || mDiagonal(8, 9, 6)))) counter++;
            if (((theGame[17] > 0) && (theGame[18] > 0) && (sDiagonal(7, 8, 4) || sDiagonal(7, 8, 5) || sDiagonal(7, 8, 6))) || ((theGame[17] > 0) && (theGame[19] > 0) && (sDiagonal(7, 9, 4) || sDiagonal(7, 9, 5) || sDiagonal(7, 9, 6))) || ((theGame[18] > 0) && (theGame[19] > 0) && (sDiagonal(8, 9, 4) || sDiagonal(8, 9, 5) || sDiagonal(8, 9, 6)))) counter++;
        }
        TempMove(space, blue, false);
        return counter > 1;
    }

    private static boolean OpponentNotDouWin(int space, int blue){
        byte counter = 0;
        TempMove(space, blue, true);
        outerLoop: for(int i=4; i<=6; i++){
            for(int j=1; j<=3; j++){
                counter = 0;
                TempMove(i, j, true);
                if (!(aBLine(7, 8, 4) || aBLine(7, 8, 5) || aBLine(7, 8, 6) || aBLine(7, 9, 4) || aBLine(7, 9, 5) || aBLine(7, 9, 6) || aBLine(8, 9, 4) || aBLine(8, 9, 5) || aBLine(8, 9, 6)))
                {
                    if (((theGame[11] > 0) && (theGame[12] > 0) && (hLine(1, 2, 4) || hLine(1, 2, 5) || hLine(1, 2, 6))) || ((theGame[11] > 0) && (theGame[13] > 0) && (hLine(1, 3, 4) || hLine(1, 3, 5) || hLine(1, 3, 6))) || ((theGame[12] > 0) && (theGame[13] > 0) && (hLine(2, 3, 4) || hLine(2, 3, 5) || hLine(2, 3, 6)))) counter++;
                    if (((theGame[11] > 0) && (theGame[12] > 0) && (vLine(1, 2, 4) || vLine(1, 2, 5) || vLine(1, 2, 6))) || ((theGame[11] > 0) && (theGame[13] > 0) && (vLine(1, 3, 4) || vLine(1, 3, 5) || vLine(1, 3, 6))) || ((theGame[12] > 0) && (theGame[13] > 0) && (vLine(2, 3, 4) || vLine(2, 3, 5) || vLine(2, 3, 6)))) counter++;
                    if (theGame[14]==1){
                        if (((theGame[11] > 0) && (theGame[12] > 0) && (mDiagonal(1, 2, 4) || mDiagonal(1, 2, 5) || mDiagonal(1, 2, 6))) || ((theGame[11] > 0) && (theGame[13] > 0) && (mDiagonal(1, 3, 4) || mDiagonal(1, 3, 5) || mDiagonal(1, 3, 6))) || ((theGame[12] > 0) && (theGame[13] > 0) && (mDiagonal(2, 3, 4) || mDiagonal(2, 3, 5) || mDiagonal(2, 3, 6)))) counter++;
                        if (((theGame[11] > 0) && (theGame[12] > 0) && (sDiagonal(1, 2, 4) || sDiagonal(1, 2, 5) || sDiagonal(1, 2, 6))) || ((theGame[11] > 0) && (theGame[13] > 0) && (sDiagonal(1, 3, 4) || sDiagonal(1, 3, 5) || sDiagonal(1, 3, 6))) || ((theGame[12] > 0) && (theGame[13] > 0) && (sDiagonal(2, 3, 4) || sDiagonal(2, 3, 5) || sDiagonal(2, 3, 6)))) counter++;
                    }
                }
                TempMove(i, j, false);
                if (counter > 1) break outerLoop;
            }
        }
        TempMove(space, blue, false);
        return counter <= 1;
    }

    private static boolean PieceInMiddle(int space, int blue){
        boolean result = false;
        TempMove(space, blue, true);
        if (aBlueLoc(5)) result = true;
        TempMove(space, blue, false);
        return result;
    }

    private static boolean ComputerSpecialCases(int space, int blue) /* very bad */ {
        boolean result = false;
        TempMove(space, blue, true);
        outerLoop: for(int i=4; i<=6; i++){
            for(int j=1; j<=3; j++){
                TempMove(i, j, true);
                Loop: for (int i1=4; i1<=6; i1++){
                    for (int j1=7; j1<=9; j1++){
                        if (ComputerWin(i1, j1) || ComputerDouWin(i1, j1)) {result=true; break Loop;}
                        else if (i1==4 && j1==9) {result=false;}
                    }
                }
                TempMove(i, j, false);
                if (!result) break outerLoop;
            }
        }
        TempMove(space, blue, false);
        return result;
    }

    private static boolean OpponentSpecialCases(int space, int blue) /* very bad */ {
        boolean result = false;
        TempMove(space, blue, true);
        outerLoop: for(int i=4; i<=6; i++){
            for(int j=1; j<=3; j++){
                TempMove(i, j, true);
                Loop: for (int i1=4; i1<=6; i1++){
                    for (int j1=7; j1<=9; j1++){
                        if (OpponentNotWin(i1, j1) && DiagonalCatchingSleeperComputerPiece(i1, j1) && OpponentNotDouWin(i1, j1)) {result=true; break Loop;}
                        else if (i1==4 && j1==9) {result=false;}
                    }
                }
                TempMove(i, j, false);
                if (!result) break outerLoop;
            }
        }
        TempMove(space, blue, false);
        return result;
    }

    private static boolean DiagonalCatchingSleeperOpponentPiece(int space, int blue){
        boolean result = false;
        TempMove(space, blue, true);
        if (aBlueLoc(5) && ((aBlueLoc(9) && RedLoc(1)) || (aBlueLoc(7) && RedLoc(3)) || (aBlueLoc(3) && RedLoc(7)) || (aBlueLoc(1) && RedLoc(9)))) result = true;
        TempMove(space, blue, false);
        return result;
    }

    private static boolean DiagonalCatchingSleeperComputerPiece(int space, int blue){
        boolean result = true;
        TempMove(space, blue, true);
        outerLoop: for(int i=4; i<=6; i++){
            for(int j=1; j<=3; j++){
                TempMove(i, j, true);
                if (aRedLoc(5) && ((aRedLoc(9) && BlueLoc(1)) || (aRedLoc(7) && BlueLoc(3)) || (aRedLoc(3) && BlueLoc(7)) || (aRedLoc(1) && BlueLoc(9)))) result=false;
                TempMove(i, j, false);
                if (!result) break outerLoop;
            }
        }
        TempMove(space, blue, false);
        return result;
    }

    private static boolean redIdle(int piece) {return (piece <= 3) && (theGame[10 + piece] == 0);}

    private static boolean blueIdle(int piece){return (piece >= 7) && (theGame[10 + piece] == 0);}

    private static boolean aRLine(int piece1, int piece2, int piece3){
        if (redIdle(piece1) || redIdle(piece2) || redIdle(piece3)) return false;
        return line(piece1, piece2, piece3);
    }

    private static boolean aBLine(int piece1, int piece2, int piece3){
        if (blueIdle(piece1) || blueIdle(piece2) || blueIdle(piece3)) return false;
        return line(piece1, piece2, piece3);
    }

    private static boolean hLine(int piece1, int piece2, int piece3){
        for(int i=1; i<=7; i+=3){
            if (((theGame[piece1] == i) || (theGame[piece2] == i) || (theGame[piece3] == i)) && ((theGame[piece1] == i + 1) || (theGame[piece2] == i + 1) || (theGame[piece3] == i + 1)) && ((theGame[piece1] == i + 2) || (theGame[piece2] == i + 2) || (theGame[piece3] == i + 2))) return true;
        }
        return false;
    }

    private static boolean vLine(int piece1, int piece2, int piece3){
        for(int i=1; i<=3; i++){
            if (((theGame[piece1] == i) || (theGame[piece2] == i) || (theGame[piece3] == i)) && ((theGame[piece1] == i + 3) || (theGame[piece2] == i + 3) || (theGame[piece3] == i + 3)) && ((theGame[piece1] == i + 6) || (theGame[piece2] == i + 6) || (theGame[piece3] == i + 6))) return true;
        }
        return false;
    }

    /* To distinguish the difference between theGame array and fastGame array
    private static boolean hLine(int loc1, int loc2, int loc3){
        for(int i=1; i<=7; i+=3){
            if (((fastGame[i] == loc1) || (fastGame[i] == loc2) || (fastGame[i] == loc3)) && ((fastGame[i+1] == loc1) || (fastGame[i+1] == loc2) || (fastGame[i+1] == loc3)) && ((fastGame[i+2] == loc1) || (fastGame[i+2] == loc2) || (fastGame[i+2] == loc3))) return true;
        }
        return false;
    }

    private static boolean vLine(int loc1, int loc2, int loc3){
        for(int i=1; i<=3; i++){
            if (((fastGame[i] == loc1) || (fastGame[i] == loc2) || (fastGame[i] == loc3)) && ((fastGame[i+3] == loc1) || (fastGame[i+3] == loc2) || (fastGame[i+3] == loc3)) && ((fastGame[i+6] == loc1) || (fastGame[i+6] == loc2) || (fastGame[i+6] == loc3))) return true;
        }
        return false;
    } */

    private static boolean mDiagonal(int piece1, int piece2, int piece3){
        return ((theGame[piece1] == 1) || (theGame[piece2] == 1) || (theGame[piece3] == 1)) && ((theGame[piece1] == 5) || (theGame[piece2] == 5) || (theGame[piece3] == 5)) && ((theGame[piece1] == 9) || (theGame[piece2] == 9) || (theGame[piece3] == 9));
    }

    private static boolean sDiagonal(int piece1, int piece2, int piece3){
        return ((theGame[piece1] == 3) || (theGame[piece2] == 3) || (theGame[piece3] == 3)) && ((theGame[piece1] == 5) || (theGame[piece2] == 5) || (theGame[piece3] == 5)) && ((theGame[piece1] == 7) || (theGame[piece2] == 7) || (theGame[piece3] == 7));
    }

    private static boolean line(int piece1, int piece2, int piece3){
        if (hLine(piece1, piece2, piece3)) return true;
        if (vLine(piece1, piece2, piece3)) return true;
        return theGame[14]==1 && (mDiagonal(piece1, piece2, piece3) || sDiagonal(piece1, piece2, piece3));
    }

    private static boolean RedLoc(int loc){
        return ((theGame[1] == loc) && (theGame[11] == 0)) || ((theGame[2] == loc) && (theGame[12] == 0)) || ((theGame[3] == loc) && (theGame[13] == 0));
    }

    private static boolean aRedLoc(int loc){
        return ((theGame[1] == loc) && (theGame[11] > 0)) || ((theGame[2] == loc) && (theGame[12] > 0)) || ((theGame[3] == loc) && (theGame[13] > 0));
    }

    private static boolean BlueLoc(int loc){
        return ((theGame[7] == loc) && (theGame[17] == 0)) || ((theGame[8] == loc) && (theGame[18] == 0)) || ((theGame[9] == loc) && (theGame[19] == 0));
    }

    private static boolean aBlueLoc(int loc){
        return ((theGame[7] == loc) && (theGame[17] > 0)) || ((theGame[8] == loc) && (theGame[18] > 0)) || ((theGame[9] == loc) && (theGame[19] > 0));
    }
    //endregion

    //region - Rolling
    public static void RollLeft(){
        theGame[fastGame[3]] = 1;
        theGame[fastGame[6]] = 2;
        theGame[fastGame[9]] = 3;
        theGame[fastGame[8]] = 6;
        theGame[fastGame[7]] = 9;
        theGame[fastGame[4]] = 8;
        theGame[fastGame[1]] = 7;
        theGame[fastGame[2]] = 4;
        fastGameUpdate();
    }

    public static void RollRight(){
        theGame[fastGame[1]] = 3;
        theGame[fastGame[2]] = 6;
        theGame[fastGame[3]] = 9;
        theGame[fastGame[6]] = 8;
        theGame[fastGame[9]] = 7;
        theGame[fastGame[8]] = 4;
        theGame[fastGame[7]] = 1;
        theGame[fastGame[4]] = 2;
        fastGameUpdate();
    }

    public static void RollHorizontal(){
        theGame[fastGame[1]] = 3;
        theGame[fastGame[4]] = 6;
        theGame[fastGame[7]] = 9;
        theGame[fastGame[3]] = 1;
        theGame[fastGame[6]] = 4;
        theGame[fastGame[9]] = 7;
        fastGameUpdate();
    }

    public static void RollVertical(){
        theGame[fastGame[1]] = 7;
        theGame[fastGame[2]] = 8;
        theGame[fastGame[3]] = 9;
        theGame[fastGame[7]] = 1;
        theGame[fastGame[8]] = 2;
        theGame[fastGame[9]] = 3;
        fastGameUpdate();
    }
    //endregion

    public static boolean color = false;
    public static String color(String s, Color c){
        if (c==null || !color || s==null) return s;
        return c.code + s + Color.RESET;
    }
    public static String color(int i, Color c){
        String s = i+"";
        if (c==null || !color) return s;
        return c.code + s + Color.RESET;
    }

    private static StringBuilder s = new StringBuilder();
    public static String Board() {
        s.setLength(0);
        int tmp;
        for (int i=1; i<=9; i++){
            tmp = fastGame[i];

            if (tmp==1 || tmp==2 || tmp==3) s.append(color(tmp, Color.RED));
            else if (tmp==7 || tmp==8 || tmp==9) s.append(color(tmp, Color.BLUE));
            else s.append(tmp);

            if ((tmp==1 || tmp==2 || tmp==3) && theGame[10+tmp]==0) s.append(color("x",Color.MAGENTA));
            else if ((tmp==7 || tmp==8 || tmp==9) && theGame[10+tmp]==0) s.append(color("x",Color.CYAN));
            else if (win==0 && (tmp==1 || tmp==2 || tmp==3)) s.append(color("$",Color.YELLOW));
            else if (win==1 && (tmp==7 || tmp==8 || tmp==9)) s.append(color("$",Color.GREEN));

            if(i==3 || i==6 || i==9) {s.append("\n");}
            else s.append("\t");
        }
        return s.toString();
    }

    public enum Level {
        Stupid(0),
        Beginner(1),
        Medium(2),
        Good(3),
        Advanced(4),
        VeryGood(5),
        Excellent(6),
        Professional(7);

        private final int value;
        Level(int value) {this.value = value;}
        public int getValue() {return value;}
    }

    public enum PlaysFirst {
        TheWinner(0),
        TheLoser(1),
        WhoPlayedSecondLastTime(2),
        ByLot(3),
        Automatically(4),
        ByOurselves(5),
        CloseTheGame(6);

        private final int value;
        PlaysFirst(int value) {this.value = value;}
        public int getValue() {return value;}
    }

    public enum Color {
        RESET("[0m"),
        BLACK("[0;30m"),
        RED("[0;31m"),
        GREEN("[0;32m"),
        YELLOW("[0;33m"),
        BLUE("[0;34m"),
        MAGENTA("[0;35m"),
        CYAN("[0;36m"),
        WHITE("[0;37m");

        private final String code;
        Color(String code) {this.code = code;}

        @Override
        public String toString() {return code;}
    }
}
