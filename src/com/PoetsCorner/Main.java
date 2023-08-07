package com.PoetsCorner;
import java.util.Scanner;

import static com.PoetsCorner.SEAGA.*;

public class Main {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.println("Hello, SEAGA EGY Console v1.0\n"
                +"1, 2 and 3 for red player\n"
                +"4, 5 and 6 for spaces\n"
                +"7, 8 and 9 for blue player.\n"
                +"Write for playing,   Example: 1-5 to switch between 1 and 5.\n"
                +"You can using q,w,e,a,s,d,z,x,c for playing with locations.\n"
                +"Write 'u' or 'uu' if you want to undo your moves.\n"
                +"Write d to enable/disable the playing with Diagonal.\n"
                +"Write 'color' to enable/disable the color board.\n");
        String s,c;
        System.out.print("Now, ");
        while(playsFirst!= PlaysFirst.CloseTheGame){

            // Computer
            System.out.println("write y for play with computer or any char to play with your friend.");
            s = x.next().toLowerCase();
            if (s.equals("y")){
                ComputerTurn();
                System.out.println("Write 0 to 7 to choose a computer intelligence.");
                s = x.next();
                try {ChangeLevel(Integer.parseInt(s));}
                catch (Exception ignored){
                    ChangeLevel(4);
                    System.out.print("Wrong entry! ");
                }
                System.out.println("The computer is " + getLevel());
            }
            else {ComputerTurn(false); s=null;}

            // choose who play first
            if (playsFirst==PlaysFirst.ByOurselves){
                System.out.println("write '1' if you want "+GetBluePlayerName()+" to play first");
                c = x.next();
                try{
                    if (Integer.parseInt(c)==1) start(1);
                    else start(0);
                } catch (Exception ignored) {start(0);}
            }else if (playsFirst==PlaysFirst.ByLot){
                System.out.println("write a or b to choose who play first"); start(x.next());
            }
            else {start();}

            // play
            while (getWhoWin()==2){
                System.out.print(Board());
                if (playerRole()==1 && s!=null) System.out.println("Press 'Enter' key for the computer to play");
                else System.out.println("Role of " + GetPlayerName(playerRole()));
                c = nextLine();
                if (c.equals("u")) undo();
                else if (c.equals("uu")) {undo(); undo();}
                else if (c.equals("d")) Diagonal();
                else if (c.equals("color")) color();
                else if (playerRole()==1 && s!=null){
                    resumeComputer(); pauseComputer();
                    System.out.println(computerStep);
                } else if (!input(c)) System.out.println("Wrong entry!");
                if (getWhoWin()!=2){
                    System.out.print(Board());
                    System.out.println("The winner is " + GetPlayerName(getWhoWin()));
                    if (getWhoWin()==1 && s!=null){
                        System.out.println("Do you think you made a stupid wrong move ?! 🤔\nWrite 'uu' if you want to undo your move\nor any character to end the game.");
                        if (nextLine().equals("uu")) {undo(); undo();}
                    }
                }
            }
            stop();

            System.out.println("Do you want to play another game ?! Y/N");
            if (!nextLine().equals("y")) playsFirst=PlaysFirst.CloseTheGame;
        }
        System.out.println("\nsee you later. \nDo not forget to pray for the Messenger of Allah, our master Muhammad.\n");
        System.out.println("This game developed by " + color("Mohamed Ashref Sayed", Color.GREEN));
        System.out.println(color("https://facebook.com/Mohamed3713317", Color.BLUE));
        System.out.println("\nTo download the windows version: \n"+color("https://www.mediafire.com/file/0wv97m4n96q5y0v", Color.BLUE));
        nextLine();
    }

    static Scanner xLine = new Scanner(System.in);
    static String nextLine() {return xLine.nextLine().toLowerCase();}
}
