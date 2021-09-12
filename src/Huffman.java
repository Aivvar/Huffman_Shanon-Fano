import javax.sound.sampled.AudioFormat;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Huffman {

    class Node implements Comparable<Node>{//узел
        int sum;
        String code;
        void buildCode(String code) {
            this.code = code;
        }
        public Node(int sum){
            this.sum = sum;
        }
        @Override
        public int compareTo(Node o) {
            return Integer.compare(sum, o.sum);
        }
    }
    class InternalNode extends Node {//внутренний узел
        Node left;
        Node right;
        @Override
        void buildCode(String code) {
            super.buildCode(code);
            left.buildCode(code + "0");
            right.buildCode(code + "1");
        }
        public InternalNode (Node left, Node right) {
            super(left.sum+ right.sum);
            this.left = left;
            this.right = right;
        }
    }
    class LeafNode extends Node {//лист
        char symbol;
        @Override
        void buildCode(String code) {
            super.buildCode(code);
            System.out.println(symbol + ": " + code);
        }
        private LeafNode(char symbol, int count) {
            super(count);
            this.symbol = symbol;
        }
    }
    private void run() throws FileNotFoundException, UnsupportedEncodingException {
        Scanner input = new Scanner(new File("input.txt"));
        String s="";
        while(input.hasNext())
            s += input.nextLine();// + "\r\n";
        input.close();
        Map<Character, Integer> charcount = new HashMap<>();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (charcount.containsKey(c)){
                charcount.put(c, charcount.get(c) + 1);
            } else {
                charcount.put(c, 1);
            }
        }
        System.out.println(s.length());
        for (Map.Entry<Character, Integer> entry : charcount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        Map<Character, Node> charNodes = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(); //создаём приоритетную очередь для узлов
        for (Map.Entry<Character, Integer> entry : charcount.entrySet()) {
            LeafNode node = (new LeafNode(entry.getKey(), entry.getValue()));
            charNodes.put(entry.getKey(), node);
            priorityQueue.add(node);
        }
        while(priorityQueue.size() > 1) {
            Node first = priorityQueue.poll();
            System.out.println("Первый "+first.sum);
            Node second = priorityQueue.poll();
            System.out.println("Второй "+second.sum);
            InternalNode node = new InternalNode(first, second);
            priorityQueue.add(node);
            System.out.println("Новый "+node.sum);
        }
        System.out.println(charcount.size());
        Node root = priorityQueue.poll();
        assert root != null;
        root.buildCode("");
        StringBuilder result = new StringBuilder();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            result.append(charNodes.get(c).code);
        }
        New_coder(result);

        String temp_decod="";
        StringBuilder decod=new StringBuilder();
        Set<Map.Entry<Character,Node>> entrySet=charNodes.entrySet();
        for(int char_count=0;char_count<result.length();char_count++)
        {
            temp_decod += (result.toString().toCharArray()[char_count]);
            for (Map.Entry<Character,Node> pair : entrySet) {
                if (temp_decod.equals(pair.getValue().code)) {
                    decod.append(pair.getKey());// нашли наше значение и возвращаем  ключ
                    temp_decod="";
                }
            }
        }
        System.out.println(decod);
        try {
            BufferedWriter out2 = new BufferedWriter( new FileWriter("decoded"));
            out2.write(decod.toString());
            out2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
public void New_coder(StringBuilder result) throws UnsupportedEncodingException
{

    System.out.println("Кодированное "+result);
    while (result.length()%8>0)
       result.append("0");
    byte [] data= new byte[result.length()/8];
    int bytecount=0;
    for(int count=0;count<result.length();count+=8)
    {
        data[bytecount]=(byte) Integer.parseInt(result.substring(count, count + 8), 2);
        bytecount++;
    }
    String codedH = new String(data, StandardCharsets.UTF_8);
    System.out.println("В UTF-8 результат: "+codedH);
    try {
        BufferedWriter out1 = new BufferedWriter( new FileWriter("coded"));
        out1.write(codedH);
        out1.close();
    } catch (IOException e) {
        e.printStackTrace();
    }


}
    public static void main(String[] args) throws FileNotFoundException {
        long startTIME = System.currentTimeMillis();
        try {
            new Huffman().run();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTIME + "ms");
    }
}