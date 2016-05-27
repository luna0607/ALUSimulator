import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by sarleon on 16-5-21.
 */
public class ALUTest  {


    ALU alu=new ALU();
    @Test
    public void integerReprsentation() throws Exception {
        assertTrue(alu.integerReprsentation("4",4).equals("0100"));
        assertTrue(alu.integerReprsentation("-4",4).equals("1100"));
        assertTrue(alu.integerReprsentation("4",8).equals("00000100"));
        assertTrue(alu.integerReprsentation("-4",8).equals("11111100"));
    }

    @Test
    public void floatRepresentation() throws Exception {
        assertEquals("00110111010011110001000100010000",alu.floatRepresentation("0.000012342134513215",8,23));
        assertEquals("11000101000100100110000111111010",alu.floatRepresentation("-2342.123523434",8,23));
        assertEquals("01000010111101100100100101010011",alu.floatRepresentation("123.14321",8,23));
        assertEquals("01010110111000001000010100100111",alu.floatRepresentation("123431241324235.12342135234234143",8,23));
        assertEquals("+Inf",alu.floatRepresentation("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",8,23));
        assertEquals("-Inf",alu.floatRepresentation("-1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",8,23));
        assertEquals("0",alu.floatRepresentation("-0.0000000000000000000000000000000000000000000000000000000000000000000000000001",8,23));
    }

    @Test
    public void ieee754() throws Exception {

    }

    @Test
    public void integerTrueValue() throws Exception {
        assertEquals(alu.integerTrueValue("1110"),"-2");
        assertEquals(alu.integerTrueValue("0100"),"4");
        assertEquals(alu.integerTrueValue("1110"),"-2");
        assertEquals(alu.integerTrueValue("1110"),"-2");
        assertEquals(alu.integerTrueValue("1110"),"-2");

    }

    @Test
    public void floatTrueValue() throws Exception {

    }

    @Test
    public void negation() throws Exception {
        assertEquals("10011001",alu.negation("01100110"));

    }

    @Test
    public void leftShift() throws Exception {
        assertEquals("00001100",alu.leftShift("00000011",2));
        assertEquals("00001100",alu.leftShift("10000011",2));
        assertEquals("01001100",alu.leftShift("10010011",2));

    }

    @Test
    public void logRightShift() throws Exception {
        assertEquals("00110000",alu.logRightShift("11000000",2));
    }

    @Test
    public void ariRightShift() throws Exception {
        assertEquals("11110000",alu.ariRightShift("11000000",2));
        assertEquals("11110000",alu.ariRightShift("11000000",2));

    }

    @Test
    public void fullAdder() throws Exception {
        assertEquals("10",alu.fullAdder('0','1','1'));
        assertEquals("10",alu.fullAdder('1','1','0'));
        assertEquals("11",alu.fullAdder('1','1','1'));
        assertEquals("01",alu.fullAdder('0','1','0'));
        assertEquals("00",alu.fullAdder('0','0','0'));
    }

    @Test
    public void claAdder() throws Exception {
        assertEquals("01110",alu.claAdder("0001","1101",'0'));
        assertEquals("10110",alu.claAdder("1001","1101",'0'));
        assertEquals("10010",alu.claAdder("1001","1001",'0'));
        assertEquals("10011",alu.claAdder("1001","1001",'1'));
        assertEquals("11111",alu.claAdder("1111","1111",'1'));
    }

    @Test
    public void oneAdder() throws Exception {
        assertEquals("10000",alu.oneAdder("1111"));
        assertEquals("01010",alu.oneAdder("1001"));
    }
    @Test
    public void adder() throws Exception{
        assertEquals("011111111",alu.adder("00000000","111111",'0',8));
        assertEquals("101111111",alu.adder("10000000","111111",'0',8));
        assertEquals("000000000",alu.adder("0000000","11111111",'1',8));
    }

    @Test
    public void integerAddition() throws Exception {
        assertEquals("01110",alu.integerAddition("1001","0101",4));
        assertEquals("00000",alu.integerAddition("1100","0100",4));
        assertEquals("10011",alu.integerAddition("1010","1001",4));
        assertEquals("11001",alu.integerAddition("0101","0100",4));
    }

    @Test
    public void integerSubtraction() throws Exception {
        assertEquals("01011",alu.integerSubtraction("0010","0111",4));
        assertEquals("00011",alu.integerSubtraction("0101","0010",4));
        assertEquals("01001",alu.integerSubtraction("1011","0010",4));
        assertEquals("10110",alu.integerSubtraction("1010","0100",4));
        assertEquals("00111",alu.integerSubtraction("0101","1110",4));
        assertEquals("11110",alu.integerSubtraction("0111","1001",4));

    }

    @Test
    public void integerMultiplication() throws Exception {
        assertEquals("000010101",alu.integerMultiplication("0111","0011",4));
        assertEquals("000010101",alu.integerMultiplication("1101","1001",4));
        assertEquals("011101011",alu.integerMultiplication("0111","1101",4));
        assertEquals("011101011",alu.integerMultiplication("0111","1101",4));
    }

    @Test
    public void integerDivision() throws Exception {
        assertEquals("011101111",alu.integerDivision("1001","0011",4));
        assertEquals("000100000",alu.integerDivision("0010","0001",4));
        assertEquals("000110000",alu.integerDivision("0110","0010",4));

        //assertEquals("0",alu.integerDivision("1100","0010",4));

    }

    @Test
    public void signedAddition() throws Exception {

    }

    @Test
    public void floatAddition() throws Exception {

    }

    @Test
    public void floatSubtraction() throws Exception {

    }

    @Test
    public void floatMultiplication() throws Exception {

    }

    @Test
    public void floatDivision() throws Exception {

    }




}