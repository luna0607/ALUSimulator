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

    }

    @Test
    public void fullAdder() throws Exception {
        assertEquals("10",alu.fullAdder('0','1','1'));
        assertEquals("11",alu.fullAdder('1','1','1'));
        assertEquals("01",alu.fullAdder('0','1','0'));
        assertEquals("00",alu.fullAdder('0','0','0'));
    }

    @Test
    public void claAdder() throws Exception {

    }

    @Test
    public void oneAdder() throws Exception {
        assertEquals("10000",alu.oneAdder("1111"));
        assertEquals("01010",alu.oneAdder("1001"));
    }
    @Test
    public void adder() throws Exception{

    }

    @Test
    public void integerAddition() throws Exception {

    }

    @Test
    public void integerSubtraction() throws Exception {

    }

    @Test
    public void integerMultiplication() throws Exception {

    }

    @Test
    public void integerDivision() throws Exception {

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