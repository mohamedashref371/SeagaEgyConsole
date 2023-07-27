package com.PoetsCorner;
import java.util.Scanner;
import java.util.Random;
public class OldMain2021XXX
{
    public static void main(String[] args)
    {
        String[] a = {"p1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "computer", "0", "0", "0", "0", "0", "0", "smart",""};
        Scanner t2 = new Scanner(System.in);
        p("Hello, SEAGA EGY 2021"+"\n"
                +"1♧, 2♧ and 3♧ for first player"+"\n"
                +"4, 5 and 6 for spaces"+"\n"
                +"7♡, 8♡ and 9♡ for second player."+"\n"
                +"Write for playing,   Example: 1-5 to switch between 1 and 5."+"\n"
                +"If you are playing with the computer, write the letter u if you want to undo your move."+"\n"
                +"Write y for play with computer or any char to play with your friend.");
        a[10] = t2.next();
        if (a[10].equals("y") || a[10].equals("Y")) {
            a[10]="1";
            p("Write a to play with a genius computer,"+"\n"
                    +"write b to play with a smart computer,"+"\n"
                    +"write other any char to play with a beginner computer.");
            a[17]=t2.next();
            if (a[17].equals("a")||a[17].equals("A")) {a[17] = "1";}
            else if (a[17].equals("b")||a[17].equals("B")) {a[17] = "2";}
            else {a[17] = "3";}
            p("If you want the computer to play first, write c");
            a[0] = t2.next();
            if (a[0].equals("c")||a[0].equals("C")) {a[0]="p2";} else {a[0]="p1";}
        } else {a[10]="0";}
        sega(a);
    }
    static void sega(String[] a) {
        Scanner t2 = new Scanner(System.in);
        Random rand = new Random();
        int w3;
        String[] a1 = {"  ",a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9],"☆", "♧", "♡","☘","❤"};
        String t1="", w1, w2 ;
        p("");
        for(int i=1;i<=9;i++){
            if (a[i].equals("1")&&a[11].equals("1") || a[i].equals("2")&&a[12].equals("1") || a[i].equals("3")&&a[13].equals("1")) {a1[i] = a[i] + a1[13];}
            else if (a[i].equals("1") || a[i].equals("2") || a[i].equals("3")) {a1[i] = a[i] + a1[11];}
            else if (a[i].equals("4") || a[i].equals("5") || a[i].equals("6")) {a1[i] = a[i] + a1[0];}
            else if (a[i].equals("7")&&a[14].equals("1") || a[i].equals("8")&&a[15].equals("1") || a[i].equals("9")&&a[16].equals("1")) {a1[i] = a[i] + a1[14];}
            else if (a[i].equals("7") || a[i].equals("8") || a[i].equals("9")) {a1[i] = a[i] + a1[12];}
        }
        p1(a1);
        if (a[0].equals("p1")){
            p("It's your turn to play, first player.");
        }
        else {
            if (a[10].equals("1")) {p("It's your turn to play, Computer.");} else {p("It's your turn to play, second player.");}
        }
        if (a[10].equals("1") && a[0].equals("p2")){
            w2="0";
            if (a[15].equals("1")&&a[16].equals("1")&&t(a,"8","9","4")) {t1="7 》 4";w2="";}
            else if (a[15].equals("1")&&a[16].equals("1")&&t(a,"8","9","5")) {t1="7 》 5";w2="";}
            else if (a[15].equals("1")&&a[16].equals("1")&&t(a,"8","9","6")) {t1="7 》 6";w2="";}
            else if (a[14].equals("1")&&a[16].equals("1")&&t(a,"7","9","4")) {t1="8 》 4";w2="";}
            else if (a[14].equals("1")&&a[16].equals("1")&&t(a,"7","9","5")) {t1="8 》 5";w2="";}
            else if (a[14].equals("1")&&a[16].equals("1")&&t(a,"7","9","6")) {t1="8 》 6";w2="";}
            else if (a[14].equals("1")&&a[15].equals("1")&&t(a,"7","8","4")) {t1="9 》 4";w2="";}
            else if (a[14].equals("1")&&a[15].equals("1")&&t(a,"7","8","5")) {t1="9 》 5";w2="";}
            else if (a[14].equals("1")&&a[15].equals("1")&&t(a,"7","8","6")) {t1="9 》 6";w2="";}
            w3=0;
            if (w2.equals("0") && (a[17].equals("1") || a[17].equals("2"))){
                for(int i=7;i<=9;i++) {
                    for(int j=4;j<=6;j++){
                        a = w(a,i+"",j+"");
                        if (f(a,1)) {w3++; a1[w3]=i+" 》 "+j;}
                        a = w(a,i+"",j+"");
                    }
                }
            }
            if (w2.equals("0")&&w3!=0) {
                t1=a1[rand.nextInt(w3)+1]; w2="";
            }
            if (w2.equals("0"))
            {
                w3 = rand.nextInt(3);
                if (w3==0) {t1="7 》 ";} else if (w3==1) {t1="8 》 ";} else {t1="9 》 ";}
                w3 = rand.nextInt(3);
                if (w3==0) {t1=t1+"4";} else if (w3==1) {t1=t1+"5";} else {t1=t1+"6";}
            }
            p(t1);
        } else{t1= t2.nextLine();}
        w1="0";w2="0";
        if (a[18].length()>8&&(t1.equals("u")||t1.equals("U"))&&a[10].equals("1")) {
            a = u(a);
            a[0]="p2";
        } else {
            if (!t1.equals("")){
                w1 = t1.substring(0,1);
                w3 = t1.length();
                w3 = w3 -1;
                w2= t1.substring(w3);}
            if (!w1.equals(w2)&&(w1.equals("4")||w1.equals("5")||w1.equals("6")||w2.equals("4")||w2.equals("5")||w2.equals("6"))&&(((w1.equals("1")||w1.equals("2")||w1.equals("3")||w2.equals("1")||w2.equals("2")||w2.equals("3"))&&a[0].equals("p1"))||((w1.equals("7")||w1.equals("8")||w1.equals("9")||w2.equals("7")||w2.equals("8")||w2.equals("9"))&&a[0].equals("p2"))))
            {
                a = w(a,w1,w2);
                if (a[0].equals("p1")) {a[18]+=w1+w2+a[11]+a[12]+a[13];}
                else {a[18]+=a[14]+a[15]+a[16]+w1+w2;}
            }

            else {
                p("Wrong entry! 🙁 .. Try again.");
                if (a[0].equals("p1")) {a[0]="p2";}
                else {a[0]="p1";}
            }}
        if (!a[1].equals("1")) {a[11] ="1";}
        if (!a[2].equals("2")) {a[12] ="1";}
        if (!a[3].equals("3")) {a[13] ="1";}
        if (!a[7].equals("7")) {a[14] ="1";}
        if (!a[8].equals("8")) {a[15] ="1";}
        if (!a[9].equals("9")) {a[16] ="1";}
        if ((a[11].equals("1")&&a[12].equals("1")&&a[13].equals("1")&&t(a,"1","2","3"))||(a[14].equals("1")&&a[15].equals("1")&&a[16].equals("1")&&t(a,"7","8","9"))) {
            if (a[0].equals("p1")){
                for(int i=1;i<=9;i++){
                    if (a[i].equals("1")||a[i].equals("2")||a[i].equals("3")) {a1[i] = a[i] +a1[10];} else {a1[i] = a[i] +a1[0];}
                }
                p("You won, first player.");
            }
            else{
                for(int i=1;i<=9;i++){
                    if (a[i].equals("7")||a[i].equals("8")||a[i].equals("9")) {a1[i] = a[i] +a1[10];} else {a1[i] = a[i] +a1[0];}
                }
                if (a[10].equals("1")) {p("computer won.");} else {p("You won, second player.");}
            }
            p1(a1);
            p("gg");
            if (a[10].equals("1")&&a[0].equals("p2")) {
                p("Do you think you made a stupid wrong move ?! 🤔"+"\n"
                        +"Write the letter u if you want to undo your move"+"\n"
                        +"or any character to end the game.");
                t1 = t2.next();
                if (t1.equals("u")||t1.equals("U")) {
                    a = u(a);
                    a[0]="p1";
                    sega(a);
                }else {p("see you later. Do not forget to pray for the Messenger of Allah, our master Muhammad.");}
            }else {p("see you later. Do not forget to pray for the Messenger of Allah, our master Muhammad.");}
        }
        else {
            if (a[0].equals("p1")) {a[0]="p2";}
            else {a[0]="p1";}
            sega(a);
        }
    }
    static void p(String a) {System.out.println(a);}
    static void p1(String[] a1){
        int w3=1;
        while(w3<=9){
            for(int i=1;i<=3;i++){
                System.out.print(a1[w3]+" "); w3++;
            }
            System.out.println();
        }
    }
    static String[] w(String[] a, String w1, String w2){
        for(int i=1; i<=8;i++){
            for(int j=i+1; j<=9;j++){
                if ((w1.equals(a[i])&& w2.equals(a[j])) || (w1.equals(a[j]) && w2.equals(a[i]))) {w1 = a[i]; a[i] = a[j]; a[j] = w1; w1=""; break;}
            }
            if (w1.equals("")) {break;}
        }
        return a;
    }
    static boolean t(String[] a, String x, String y, String z){
        boolean d = false;
        int i=1;
        while(i<=7){
            if ((a[i].equals(x)||a[i].equals(y)||a[i].equals(z))&&(a[i+1].equals(x)||a[i+1].equals(y)||a[i+1].equals(z))&&(a[i+2].equals(x)||a[i+2].equals(y)||a[i+2].equals(z))) {d = true; break;}
            i=i+3;
        }
        if (d==false){
            i=1;
            while(i<=3){
                if ((a[i].equals(x)||a[i].equals(y)||a[i].equals(z))&&(a[i+3].equals(x)||a[i+3].equals(y)||a[i+3].equals(z))&&(a[i+6].equals(x)||a[i+6].equals(y)||a[i+6].equals(z))) {d = true; break;}
                i++;
            }
        }
        return d;
    }
    static boolean f(String[] a, int m){
        boolean d=true;
        int n=0;
        if (a[11].equals("1")&&a[12].equals("1")&&(t(a,"1","2","4")||t(a,"1","2","5")||t(a,"1","2","6"))) {n++;}
        if (a[11].equals("1")&&a[13].equals("1")&&(t(a,"1","3","4")||t(a,"1","3","5")||t(a,"1","3","6"))) {n++;}
        if (a[12].equals("1")&&a[13].equals("1")&&(t(a,"2","3","4")||t(a,"2","3","5")||t(a,"2","3","6"))) {n++;}
        if (n>1) {d=false;}
        else if (m==1 && n==1) {d=false;}
        return d;
    }
    static String[] u(String[] a){
        int w3;
        String w1, w2;
        w3 = a[18].length();
        for (int i=1;i<=6;i++){
            a[i+10] =a[18].substring(i+w3-9,i+w3-8);
        }
        w1=a[18].substring(w3-2,w3-1);
        w2=a[18].substring(w3-1,w3);
        a = w(a,w1,w2);
        w1=a[18].substring(w3-10,w3-9);
        w2=a[18].substring(w3-9,w3-8);
        a = w(a,w1,w2);
        a[18]= a[18].substring(0,w3-10);
        return a;
    }
}
