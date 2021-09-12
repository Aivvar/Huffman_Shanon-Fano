import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Shenon_Fano {

    public class Symbol implements Comparable<Symbol>{//узел
        int sum;
        StringBuilder code=new StringBuilder();
        char symbol;
        public void buildCode(String zero_or_one) {
            code.append(zero_or_one);
        }
        public Symbol(char symbol,int sum){
            this.symbol=symbol;
            this.sum = sum;
        }
        public String getCode() {
            return this.code.toString() ;
        }
        @Override
        public int compareTo(Symbol o) {
            return(o.sum - sum);
        }
    }
    private void run() throws FileNotFoundException, UnsupportedEncodingException {
        Scanner input = new Scanner(new File("input.txt"));
        String s="";
        while(input.hasNext())
            s += input.nextLine() ;//+ "\r\n";
        input.close();
        Map<Character, Integer> char_count = new HashMap<>();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (char_count.containsKey(c)){
                char_count.put(c, char_count.get(c) + 1);
            } else {
                char_count.put(c, 1);
            }
        }
        ArrayList<Symbol> symbol_obj=new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : char_count.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            symbol_obj.add(new Symbol(entry.getKey(),entry.getValue()));

        }
        Collections.sort(symbol_obj);
        Procesing(symbol_obj,"");
        Results(symbol_obj,s);

    }
    public void Procesing(ArrayList<Symbol> symb,String code)
    {
        int sum=0,Commonsum=0,count=0;
        ArrayList<Symbol> left,right;
        left=new ArrayList<>();
        right=new ArrayList<>();
        for(Symbol smb:symb)
        {
            if(!code.equals(""))
            smb.buildCode(code);
            Commonsum+=smb.sum;
        }
        if(symb.size()>2) {
            while (sum < Commonsum/2) {
                sum += symb.get(count).sum;
                count++;
            }
            for(int i=0; i<=count-1; i++){
                left.add(symb.get(i));
            }

            for(int i=count; i<symb.size(); i++){
                right.add(symb.get(i));
            }
        }
        else {
         left.add(symb.get(0));
            right.add(symb.get(1));
        }
        if(left.size()>1)
            Procesing(left,"0");
        else left.get(0).buildCode("0");
        if(right.size()>1)
            Procesing(right,"1");
        else
            right.get(0).buildCode("1");

    }
public void Results(ArrayList<Symbol> symbols,String str)
{
    System.out.println("Исходная строка: "+str);
    System.out.println("Результаты");
    StringBuilder result=new StringBuilder();
    ArrayList<Character> symb=new ArrayList<>();
    ArrayList<String> codes=new ArrayList<>();
    for(Symbol smb:symbols)
    {
        System.out.print(smb.symbol+""+smb.code+" ");
         symb.add(smb.symbol);
         codes.add(smb.code.toString());
    }
    for(int count=0;count<str.length();count++)
        result.append(codes.get(symb.indexOf(str.toCharArray()[count])));
    System.out.println("");
    System.out.println(result);
    while (result.length()%8>0)
        result.append("0");
    byte [] data= new byte[result.length()/8];
    int bytecount=0;
    for(int count=0;count<result.length();count+=8)
    {
        data[bytecount]=(byte) Integer.parseInt(result.substring(count, count + 8), 2);
        bytecount++;
    }
    String codedS = new String(data, StandardCharsets.UTF_8);
    System.out.println("В UTF-8 результат: "+codedS);
    try {
        BufferedWriter out = new BufferedWriter( new FileWriter("coded"));
        out.write(codedS);
        out.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    String temp_decod="";
    StringBuilder decod=new StringBuilder();

    for(int char_count=0;char_count<result.length();char_count++)
    {
        temp_decod += (result.toString().toCharArray()[char_count]);
        for (int k=0;k<symbols.size();k++) {
            if (symbols.get(k).getCode().equals(temp_decod)) {
                decod.append(symbols.get(k).symbol);// нашли наше значение и возвращаем  ключ
                temp_decod="";
            }
        }
    }
    try {
        BufferedWriter out2 = new BufferedWriter( new FileWriter("decoded"));
        out2.write(decod.toString());
        out2.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    System.out.println(decod);
}
    public static void main(String[] args) throws FileNotFoundException {
        long startTIME = System.currentTimeMillis();
        try {
            new Shenon_Fano().run();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTIME + "ms");
    }
}