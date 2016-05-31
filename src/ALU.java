import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author ["151250081_李思聪"]
 *
 */

public class ALU {


	public static void main(String[] args) {
		ALU alu=new ALU();
		System.out.println(alu.signedAddition("01000","000001",8));
	}

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerReprsentation (String number, int length) {
		boolean positive=true;
		String  temp="";
		String  result="";
		StringBuilder stringBuffer=new StringBuilder();
		if(number.charAt(0)=='-') {
			positive = false;
		}
		if(!positive) {
			temp = hex2Bin(number.substring(1,number.length()));
		} else {
			temp=hex2Bin(number);
		}
		int a=1;
		stringBuffer.append(temp);
		for (int i = 0; i < length-temp.length(); i++) {
			stringBuffer.insert(0,'0');
		}
		if (!positive){
			for (int i = 0; i < stringBuffer.length(); i++) {
				if(stringBuffer.charAt(i)=='0') {
					stringBuffer.replace(i, i+1, "1");
				} else {
					stringBuffer.replace(i, i+1, "0");
				}
			}
			for (int i = stringBuffer.length()-1; i >=0 ; i--) {
				if(stringBuffer.charAt(i)=='0'){
					stringBuffer.replace(i,i+1,"1");
					break;
				} else {
					stringBuffer.replace(i,i+1,"0");

				}
			}

		}
		result=stringBuffer.toString();

		return result;
	}



	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {

		double maxValue=(2-Math.pow(2,-sLength))*Math.pow(2,Math.pow(2,eLength-1)-1);
		double minRegValue=Math.pow(2,(-Math.pow(2,eLength-1)+2));
		double minValue=minRegValue*Math.pow(2,-sLength);



		StringBuilder stringBuilder=new StringBuilder();
		String  result;
		boolean positive=true;
		String num="";
		String sign="0";
		String exponent="";
		String fragtion="";
		double num1=0;



		if(number.charAt(0)=='-'){
			positive=false;
			sign="1";
			num1=Double.parseDouble(number.substring(1));
		} else {
			num1=Double.parseDouble(number);
		}
		StringBuilder exponentBuiler=new StringBuilder();
		StringBuilder fragtionBuiler=new StringBuilder();

		if(num1>maxValue){
			for (int i = 0; i < eLength; i++) {
				exponentBuiler.append('1');
			}
			for (int i = 0; i < sLength; i++) {
				exponentBuiler.append('0');
			}
			result=exponentBuiler.toString()+fragtionBuiler.toString();
				if (positive){

				return "0"+result;
			} else {
				return  "1"+result;
			}
		}

		if(num1<minValue){
			for (int i = 0; i < eLength; i++) {
				exponentBuiler.append('0');
			}
			for (int i = 0; i < sLength; i++) {
				exponentBuiler.append('0');
			}
			result=exponentBuiler.toString()+fragtionBuiler.toString();
			if (positive) {
				return "0"+result;
			} else {
				return  "1"+result;
			}
		}

		if(num1<minRegValue){
			new ALU().leftShift(number,1);
		}
		if(!positive){
			num=number.substring(1,number.length());
		} else {
			num=number;
		}
		String[] nums=num.split("\\.");

		StringBuilder decimalStringBuilder=new StringBuilder();
		String integralPart=hex2Bin(nums[0]);
		String tempNumber="0."+nums[1];
		double decimalPartNum=Double.parseDouble(tempNumber);


		/*小数部分的二进制表示*/
		for (int i = 0; i <=sLength+Math.pow(2,eLength-1)-1 ; i++) {
			decimalPartNum=decimalPartNum*2;
			if(decimalPartNum>1){
				decimalPartNum--;
				decimalStringBuilder.append("1");
			} else {
					decimalStringBuilder.append("0");
			}
			if(decimalPartNum==1.0){
				break;
			}
		}

		String decimalPart=decimalStringBuilder.toString() ;
		StringBuilder exponentStringBuilder=new StringBuilder();
		StringBuilder fragtionStringBuilder=new StringBuilder();
		int exponentNum=0;
		if(integralPart.length()>1) {
			exponentNum = (int) (integralPart.length()-1 +Math.pow(2,eLength-1)-1);
			exponentStringBuilder.append(hex2Bin(String.valueOf(exponentNum)));
			for (int i = 0; exponentStringBuilder.length()<eLength; i++) {
				exponentStringBuilder.insert(0,0);
			}
			exponent=exponentStringBuilder.toString();
			if(integralPart.length()<sLength){

				fragtionStringBuilder.append(integralPart.substring(1,integralPart.length())+decimalPart.substring(0,sLength-integralPart.length()+1));
				if(decimalPart.substring(sLength-integralPart.length()+1,sLength-integralPart.length()+2).equals("1")){
					fragtionStringBuilder.replace(0,fragtionStringBuilder.length(),addOne(fragtionStringBuilder.toString()));
				}
				fragtion=fragtionStringBuilder.toString();

			} else {
				fragtionStringBuilder.append(integralPart.substring(1,1+sLength));
				if (integralPart.length()>sLength+1) {
					if (integralPart.substring(1+sLength,2+sLength).equals("1")){
						fragtionStringBuilder.replace(0,fragtionStringBuilder.length(),addOne(fragtionStringBuilder.toString()));
					}
				} else {
				}
				fragtion=fragtionStringBuilder.toString();
			}

		} else {
			for (int i = 0; i < decimalPart.length(); i++) {
				if(decimalPart.charAt(i)=='1'){
					exponentNum=-(i+1);
					exponentNum=exponentNum+(int)(Math.pow(2,eLength-1)-1);
					exponentStringBuilder.append(hex2Bin(String.valueOf(exponentNum)));
					for (int j = 0; exponentStringBuilder.length()<eLength; j++) {
						exponentStringBuilder.insert(0,0);
					}
					exponent=exponentStringBuilder.toString();
					if(decimalPart.length()>i+sLength+1) {
						fragtionStringBuilder.append(decimalPart.substring(i + 1, i + sLength + 1));
					} else {
						fragtionStringBuilder.append(decimalPart.substring(i+1));
					}
					while (fragtionStringBuilder.length()<sLength){
						fragtionStringBuilder.append("0");
					}
					fragtion=fragtionStringBuilder.toString();

					break;
				}
			}
		}
		exponent=exponentStringBuilder.toString();
		result=sign+exponent+fragtion;
		return result;
	}
	
	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {
		if (length == 32) {
			return floatRepresentation(number,8,23);
		} else if(length==64){
			return floatRepresentation(number,11,52);
		} else  return "";

	}
	
	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		int res=0;
		String result;
		int len=operand.length();
		for (int i = 0; i < len-1; i++) {
			res+=(operand.charAt(len-i-1)-'0')*Math.pow(2,i);
		}

		res+=-(operand.charAt(0)-'0')*Math.pow(2,len-1);
		result=String.valueOf(res);
		return result;
	}



	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		double maxValue=(2-Math.pow(2,-sLength))*Math.pow(2,Math.pow(2,eLength-1)-1);
		double minRegValue=Math.pow(2,(-Math.pow(2,eLength-1)+2));
		double minValue=minRegValue*Math.pow(2,-sLength);
		String sign=operand.substring(0,1);
		String exponent=operand.substring(1,1+eLength);
		String fragtion=operand.substring(1+eLength,1+eLength+sLength);
		boolean positive=true;
		if(sign.equals("1")){
			positive=false;
		}

		boolean exponentAllZero=true;
		boolean exponentAllOne=true;
		boolean fragtionAllOne=true;
		boolean fragtionAllZero=true;
		for (int i = 0; i < exponent.length(); i++) {
			if(exponent.charAt(i)=='0'){
				exponentAllOne=false;
			}
			if(exponent.charAt(i)=='1'){
				exponentAllZero=false;
			}
		}
		for (int i = 0; i < fragtion.length(); i++) {
			if(fragtion.charAt(i)=='0'){
				fragtionAllOne=false;
			}
			if (fragtion.charAt(i)=='1'){
				fragtionAllZero=false;
			}
		}
		if (exponentAllZero&&fragtionAllZero){
			return "0";
		}
		if(exponentAllOne&&fragtionAllZero){
			if(positive){
				return "+Inf";
			} else {
				return "-Inf";
			}
		}
		if(exponentAllOne&&(!fragtionAllZero)){
				return "NaN";
		}
		String  tempTrueValue=integerTrueValue("01"+fragtion);
		String  exponentTrueValue=integerTrueValue("0"+exponent);
		BigInteger exponentValue=new BigInteger(exponentTrueValue);
		exponentValue=exponentValue.subtract(new BigInteger(String.valueOf((int)Math.pow(2,(eLength-1))+sLength)));
		BigDecimal  bd=new BigDecimal(tempTrueValue);
		bd=bd.multiply(new BigDecimal(Math.pow(2,Double.parseDouble(exponentValue.toString()))));
		if(exponentAllZero);{
			bd=bd.multiply(new BigDecimal(2));
		}








		return bd.toString();
	}
	
	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < operand.length(); i++) {
			if(operand.charAt(i)=='0'){
				sb.append('1');

			} else {
				sb.append('0');
			}
		}
		String  result=sb.toString();
		return  result;
	}
	
	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift (String operand, int n) {
		String result;
		StringBuilder stringBuilder=new StringBuilder(operand.substring(n,operand.length()));

		for (int i = 0; i <n ; i++) {
			stringBuilder.append('0');

		}
		result=stringBuilder.toString();
		return result;
	}
	
	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift (String operand, int n) {
		String result;
		StringBuilder stringBuilder=new StringBuilder(operand.substring(0,operand.length()-n));

		for (int i = 0; i <n ; i++) {
			stringBuilder.insert(0,'0');

		}
		result=stringBuilder.toString();
		return result;
	}
	
	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift (String operand, int n) {
		String result;
		StringBuilder stringBuilder=new StringBuilder(operand.substring(0,operand.length()-n));

		for (int i = 0; i <n ; i++) {
			if(operand.charAt(0)=='0') {
				stringBuilder.insert(0, '0');
			} else {
				stringBuilder.insert(0, '1');

			}
		}
		result=stringBuilder.toString();
		return result;
	}
	
	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder (char x, char y, char c) {
		char s=xor(xor(x,y),c);
		char c1;
		if((x+y+c)>=('0'+'0'+'0'+2)) {
			c1='1';
		} else {
			c1='0';
		}
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(c1);
		stringBuilder.append(s);
		String  result=stringBuilder.toString();
		return result;
	}
	
	/**
	 * 4位先行进位加法器。<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {
		char[] o1=operand1.toCharArray();
		char[] o2=operand2.toCharArray();
		int[] a=new int[4];
		int[] b=new int[4];
		for (int i = 0; i < 4; i++) {
			a[3-i]=o1[i]-'0';
			b[3-i]=o2[i]-'0';
		}
		int[] p=new int[4];
		int[] g=new int[4];
		int[] s=new int[4];
		int[] carry=new int[5];
		carry[0]=c-'0';
		for (int i = 0; i < 4; i++) {
			p[i]=(int)xor((char)a[i],(char)b[i])-'0';
			g[i]=a[i]*b[i];
		}
		carry[1]=g[0]+(p[0]*carry[0]);
		carry[2]=g[1]+(p[1]*g[0])+(p[1]*p[0]*carry[0]);
		carry[3]=g[2]+(p[2]*g[1])+(p[2]*p[1]*g[0])+(p[0]*p[2]*p[1]*carry[0]);
		carry[4]=g[3]+(p[3]*g[2])+(p[3]*p[2]*g[1])+(p[3]*p[2]*p[1]*g[0])+((p[3]*p[2]*p[1]*p[0]*carry[0]));
		for (int i = 0; i < 4; i++) {
			char temp=xor((char)a[i],(char)b[i]);
			s[i]=(int)xor((char)(carry[i]),(char)(temp-'0'))-'0';
		//	s[i]=(int)xor(xor((char)a[i],(char)b[i]),(char)carry[i])-'0';
		}
		StringBuilder resStringBuilder=new StringBuilder();
		resStringBuilder.append(String.valueOf(carry[4]));
		for (int i = 0; i < 4; i++) {
			resStringBuilder.append(String.valueOf(s[3-i]));
		}
		String  result=resStringBuilder.toString();




		return result;
	}
	
	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要模拟{@link #fullAdder(char, char, char) fullAdder}来实现，但不可以调用{@link #fullAdder(char, char, char) fullAdder}。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		StringBuilder stringBuilder=new StringBuilder();
		String result;;
		stringBuilder.append(0);
		stringBuilder.append(operand);
		for (int i =stringBuilder.length()-1; i>=0; i--) {
			if(stringBuilder.charAt(i)=='0'){
				stringBuilder.replace(i,i+1,"1");
				break;
			} else {

				stringBuilder.replace(i,i+1,"0");
			}
		}
		result=stringBuilder.toString();
		return  result;

	}
	public static  String addOne(String in){
		StringBuilder stringBuilder=new StringBuilder();
		String result;;
		stringBuilder.append(in);
		for (int i =stringBuilder.length()-1; i>=0; i--) {
			if(stringBuilder.charAt(i)=='0'){
				stringBuilder.replace(i,i+1,"1");
				break;
			} else {

				stringBuilder.replace(i,i+1,"0");
			}
		}
		result=stringBuilder.toString();
		return  result;
	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		assert length%4==0;
	//	assert length>=operand1.length();
		//assert length>=operand2.length();

		StringBuilder stringBuilderO1=new StringBuilder(operand1);
		StringBuilder stringBuilderO2=new StringBuilder(operand2);

		int maxLength=Math.max((int)Math.ceil(stringBuilderO1.length()/4)*4,(int)Math.ceil(stringBuilderO2.length()/4)*4);
		while (stringBuilderO1.length()!=maxLength){
			stringBuilderO1.insert(0,stringBuilderO1.charAt(0));
		}
		while (stringBuilderO2.length()!=maxLength){
			stringBuilderO2.insert(0,stringBuilderO2.charAt(0));
		}

		int caNum=Math.max(stringBuilderO1.length()/4,stringBuilderO2.length()/4);
		String[] o1Strings=new String[caNum];
		String[] o2Strings=new String[caNum];
		try {

			for (int i = caNum-1; i >=0; i--) {
				o1Strings[caNum-1-i]=stringBuilderO1.substring(4*i,4*i+4);
				o2Strings[caNum-1-i]=stringBuilderO2.substring(4*i,4*i+4);

			}

		} catch (Exception e){
			System.out.println(o1Strings.length);
			System.out.println(o2Strings.length);
			System.out.println("单元数"+caNum);;
			System.out.println(stringBuilderO1.toString());;
			System.out.println(stringBuilderO2.toString());;

			e.printStackTrace();
		}
		String[] parts=new String[caNum];
		char[] carry=new char[caNum+1];
		carry[0]=c;
		for (int i = 0; i < caNum; i++) {
			parts[i]=claAdder(o1Strings[i],o2Strings[i],carry[i]).substring(1,5);
			carry[i+1]=claAdder(o1Strings[i],o2Strings[i],carry[i]).charAt(0);
		}

		StringBuilder resStringBuilder=new StringBuilder();
		for (int i = 0; i < caNum; i++) {
			resStringBuilder.append(parts[caNum-1-i]);
		}
		char temp=xor(stringBuilderO1.charAt(0),stringBuilderO2.charAt(0));
		if(temp=='0'){
			if(stringBuilderO1.charAt(0)==resStringBuilder.charAt(0)){
				carry[caNum]='0';
			}
			else {
				carry[caNum]='1';
			}
		} else {
			carry[caNum]='0';
		}
		resStringBuilder.insert(0,carry[caNum]);
		return resStringBuilder.toString();
	}

	/**
	 * 整数加法，要求调用{@link #claAdder(String, String, char) claAdder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数2
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1,operand2,'0',length);
	}
	
	/**
	 * 整数减法，可调用{@link #integerAddition(String, String, char, int) integerAddition}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String
	integerSubtraction (String operand1, String operand2, int length) {

		return integerAddition(operand1,toOpposite(operand2),length);
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #integerAddition(String, String, char, int) integerAddition}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符	串表示的相乘结果，其中第1位指	示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		assert length%4==0;
		assert length>=operand1.length();
		assert length>=operand2.length();
		length/=2;

		StringBuilder stringBuilderO1=new StringBuilder(operand1);
		StringBuilder stringBuilderO2=new StringBuilder(operand2);

		while (stringBuilderO1.length()%4!=0){
			stringBuilderO1.insert(0,stringBuilderO1.charAt(0));
		}
		while (stringBuilderO2.length()%4!=0){
			stringBuilderO2.insert(0,stringBuilderO2.charAt(0));
		}
		String fixOperand1=stringBuilderO1.toString();
		String fixOperand2=stringBuilderO2.toString();
		StringBuilder resStringBuilder=new StringBuilder();
		resStringBuilder.append(fixOperand2);
		resStringBuilder.append('0');

		for (int i = 0; i < length; i++) {
			resStringBuilder.insert(0,'0');
		}
		String  a=resStringBuilder.substring(0,length);
		String  q=fixOperand2+"0";
		String result=resStringBuilder.toString();
		int temp=0;
		for (int i = 0; i < length; i++) {
			temp=result.charAt(2*length)-result.charAt(2*length-1);
			if(temp==-1){
				a=integerSubtraction(a,fixOperand1,length).substring(1);
			} else if(temp==1){
				a=integerAddition(a,fixOperand1,length).substring(1);
			}
			result=a+q;

			result=ariRightShift(result,1);
			a=result.substring(0,length);
			q=result.substring(length);




			resStringBuilder.replace(0,resStringBuilder.length(),result);
		}
		String overflow;
		if(xor(operand1.charAt(0),operand2.charAt(0))==result.charAt(0)){
			overflow="0";
		} else {
			overflow="1";
		}


		result=overflow+result.substring(0,result.length()-1);
		return result;
	}
	
	/**
	 * 整数的不恢复余数除法，可调用{@link #integerAddition(String, String, char, int) integerAddition}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {

		assert length%4==0;
		assert length>=operand1.length();
		assert length>=operand2.length();

		/*被除数*/
		StringBuilder stringBuilderO1=new StringBuilder(operand1);

		/*除数*/
		StringBuilder stringBuilderO2=new StringBuilder(operand2);

		while (stringBuilderO1.length()%4!=0){
			stringBuilderO1.insert(0,stringBuilderO1.charAt(0));
		}
		while (stringBuilderO2.length()%4!=0){
			stringBuilderO2.insert(0,stringBuilderO2.charAt(0));
		}
		String fixOperand1=stringBuilderO1.toString();
		String divisor=stringBuilderO2.toString();
		StringBuilder resStringBuilder=new StringBuilder();

		resStringBuilder.append(fixOperand1);
		for (int i = 0; i <= length; i++) {
			resStringBuilder.insert(0,resStringBuilder.charAt(0));
		}
		String  remainer= resStringBuilder.substring(0,length);
		String  quotient=resStringBuilder.substring(length,2*length);
		if (divisor.charAt(0) == remainer.charAt(0)) {
			addOne(quotient);
		}
		quotient=fixOperand1;
		String  result=remainer+fixOperand1;



		for (int i = 0; i <length; i++) {
			result=leftShift(result,1);
			remainer= result.substring(0,length);
			quotient=result.substring(length);
			if (divisor.charAt(0) == remainer.charAt(0)) {
				remainer=integerSubtraction(remainer,divisor,length).substring(1);
			} else {
				remainer=integerAddition(remainer,divisor,length).substring(1);
			}
			result=remainer+quotient;



			if (divisor.charAt(0) == remainer.charAt(0)) {
				quotient=addOne(quotient);
			}else {

			}
			result=remainer+quotient;

		}
		if(remainer.charAt(0)!=fixOperand1.charAt(0)){
			if(quotient.charAt(length-1)=='1') {
				remainer = integerSubtraction(remainer, divisor, length).substring(1);
				quotient = addOne(quotient);
			} else {
				remainer= integerAddition(remainer,divisor,length).substring(1);
			}
		}
		result=quotient+remainer;

		result="0"+result;

		return result;


	}
	
	/**
	 * 带符号整数加法，要求调用{@link #integerAddition(String, String, int) integerAddition}、{@link #integerSubtraction(String, String, int) integerSubtraction}等方法实现。
	 * 但符号的确定、结果是否修正等需要按照相关算法进行，不能直接转为补码表示后运算再转回来<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		boolean o1Positive=true;
		boolean o2Positive=true;
		boolean resultPositive=true;
		String o1Value;
		String o2Value;
		if (operand1.charAt(0) == '0') {
			o1Positive=true;
		} else {
			o1Positive=false;
		}

		if (operand2.charAt(0) == '0') {
			o2Positive=true;
		} else {
			o2Positive=false;
		}
		o1Value=operand1.substring(1);
		o2Value=operand2.substring(1);
		String value;
		String result;
		String overflow;
		String sign="0";
		if(o1Positive&&o2Positive){
			value=integerAddition(o1Value,o2Value,(int)Math.ceil((double)length/4)*4).substring(1);
			overflow=integerAddition(o1Value,o2Value,(int)Math.ceil(length/4)*4).substring(0,1);
			return overflow+"0"+value;
		} else if((!o1Positive)&&(!o2Positive)){
			value=integerAddition(o1Value,o2Value,(int)Math.ceil((double)length/4)*4).substring(1);
			overflow=integerAddition(o1Value,o2Value,(int)Math.ceil(length/4)*4).substring(0,1);
			return overflow+"1"+value;
		} else if ((!o1Positive) && o2Positive) {
			value=integerSubtraction(o2Value,o1Value,(int)Math.ceil((double)length/4)*4).substring(1);
			overflow=integerSubtraction(o2Value,o1Value,(int)Math.ceil((double)length/4)*4).substring(0,1);
			if(value.charAt(0)=='1'){
				value=toOpposite(value);
				sign="1";
			}
			return overflow+sign+value;

		} else if ((o1Positive) && (!o2Positive)) {
			value=integerSubtraction(o1Value,o2Value,(int)Math.ceil((double)length/4)*4).substring(1);
			overflow=integerSubtraction(o1Value,o2Value,(int)Math.ceil((double)length/4)*4).substring(0,1);
			if(value.charAt(0)=='1'){
				value=toOpposite(value);
				sign="1";
			}
			return overflow+sign+value;
		}


		return null;
	}
	public String NBCDsignedAddition (String operand1, String operand2, int length) {
		assert  length%4==0;
		int caNum=length/4;
		String[] binOf ={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"};

		char sign;
		char overflow;
		boolean o1Positive=true;
		boolean o2Positive=true;
		boolean resultPositive=true;
		StringBuilder stringBuilder1=new StringBuilder();
		StringBuilder stringBuilder2=new StringBuilder();
		if(operand1.charAt(0)=='1'){
			o1Positive=false;
			stringBuilder1.append(operand1.substring(1));
		} else {
			stringBuilder1.append(operand1.substring(1));
		}

		if(operand2.charAt(0)=='1') {
			o2Positive = false;
			stringBuilder2.append(operand2.substring(1));
		} else {
			stringBuilder2.append(operand2.substring(1));
		}

		while (stringBuilder1.length()!=length){
			stringBuilder1.insert(0,stringBuilder1.charAt(0));
		}

		while (stringBuilder2.length()!=length){
			stringBuilder2.insert(0,stringBuilder2.charAt(0));
		}


		String[] o1Strings=new String[caNum];
		String[] o2Strings=new String[caNum];
		String[] resultStrings=new String[caNum];
		String  result;
		StringBuilder stringBuilderRes=new StringBuilder();
		char carry='0';
		char carry2='0';
		for (int i = caNum-1; i >=0; i--) {
			o1Strings[caNum-1-i]=stringBuilder1.substring(4*i,4*i+4);
			o2Strings[caNum-1-i]=stringBuilder2.substring(4*i,4*i+4);
		}


		if(o1Positive==o2Positive){
			if(o1Positive){
				resultPositive=true;
				sign='0';
			} else {
				resultPositive=false;
				sign='1';
			}
			for (int i = 0; i < caNum; i++) {
							resultStrings[i]=integerAddition(o1Strings[i],o2Strings[i],4).substring(1);
							if(carry=='1'){
								resultStrings[i]=addOne(resultStrings[i]);
								carry='0';
							}
							carry=integerAddition(o1Strings[i],o2Strings[i],length).charAt(0);
							if(carry=='1'){
								resultStrings[i]=integerAddition(resultStrings[i],"0110",4).substring(1);

							}
						}
						carry2='0';
						for (int i = 0; i < caNum; i++) {

							if(carry2=='1'){
								resultStrings[i]=addOne(resultStrings[i]);
								carry2='0';
							}
							for (int j = 10; j <16 ; j++) {
								if(binOf[j].equals(resultStrings[i])){
									resultStrings[i]=integerAddition(resultStrings[i],"0110",4).substring(1);
									carry2='1';
					}
				}
			}
			if(carry=='1'||carry2=='1'){
				overflow='1';
			} else {
				overflow='0';
			}






		}else {
			overflow='0';
			if(!o1Positive){
				for (int i = 0; i < caNum; i++) {
					o1Strings[i]=integerSubtraction("1001",o1Strings[i],4).substring(1);
				}
				o1Strings[0]=addOne(o1Strings[0]);
			} else if(!o2Positive){
				for (int i = 0; i <caNum ; i++) {
					o2Strings[i]=integerSubtraction("1001",o2Strings[i],4).substring(1);
				}
				o2Strings[0]=addOne(o2Strings[0]);
			}
			for (int i = 0; i < caNum; i++) {
				resultStrings[i]=integerAddition(o1Strings[i],o2Strings[i],4).substring(1);
				if(carry=='1'){
					resultStrings[i]=addOne(resultStrings[i]);
					carry='0';
				}
				carry=integerAddition(o1Strings[i],o2Strings[i],length).charAt(0);
				if(carry=='1'){
					resultStrings[i]=integerAddition(resultStrings[i],"0110",4).substring(1);

				}
			}
			carry2='0';
			for (int i = 0; i < caNum; i++) {

				if(carry2=='1'){
					resultStrings[i]=addOne(resultStrings[i]);
					carry2='0';
				}
				for (int j = 10; j <16 ; j++) {
					if(binOf[j].equals(resultStrings[i])){
						resultStrings[i]=integerAddition(resultStrings[i],"0110",4).substring(1);
						carry2='1';
					}
				}
			}
			if(carry=='1'||carry2=='1'){
				sign='0';
			} else {
				for (int i = 0; i < caNum; i++) {
					resultStrings[i]=integerSubtraction("1001",resultStrings[i],4);
				}
				sign='1';
			}


		}
		stringBuilderRes.append(overflow);
		stringBuilderRes.append(sign);
		for (int i = caNum-1; i >=0 ; i--) {
			stringBuilderRes.append(resultStrings[i]);
		}
		result=stringBuilderRes.toString();





		return result;
	}
	
	/**
	 * 浮点数加法，可调用{@link #integerAddition(String, String, char, int) intergerAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		double maxValue=(2-Math.pow(2,-sLength))*Math.pow(2,Math.pow(2,eLength-1)-1);
		double minRegValue=Math.pow(2,(-Math.pow(2,eLength-1)+2));
		double minValue=minRegValue*Math.pow(2,-sLength);
		StringBuilder exponentStringBuilder=new StringBuilder();
		StringBuilder fragtionStringBuilder=new StringBuilder();

		/*构造0*/
		for (int i = 0; i < eLength; i++) {
			exponentStringBuilder.append("0");
		}
		for (int i = 0; i < sLength; i++) {
			fragtionStringBuilder.append("0");
		}
		String postiveZero="0"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		String negativeZero="1"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		exponentStringBuilder.delete(0,exponentStringBuilder.length());
		fragtionStringBuilder.delete(0,fragtionStringBuilder.length());


		/*构造无穷大*/
		for (int i = 0; i < eLength; i++) {
			exponentStringBuilder.append("1");
		}
		for (int i = 0; i < sLength; i++) {
			fragtionStringBuilder.append("0");
		}
		String positiveInfinite="0"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		String negativeInfinite="1"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		String NaN="0"+exponentStringBuilder.toString()+"0"+fragtionStringBuilder.substring(1);

		exponentStringBuilder.delete(0,exponentStringBuilder.length());
		fragtionStringBuilder.delete(0,fragtionStringBuilder.length());

		char sign;
		String exponent1;
		String exponent2;
		String sign1;
		String sign2;
		int exponentValue1;
		int exponentValue2;
		String fragtion1;
		String fragtion2;
		String protect1;
		String protect2;
		StringBuilder protect1StringBuilder=new StringBuilder();
		StringBuilder protect2StringBuilder=new StringBuilder();
		String resultFragtion;
		char overFlow;
		for (int i = 0; i < gLength; i++) {
			protect1StringBuilder.append("0");
			protect2StringBuilder.append("0");
		}
		int exponentMaxValue=(int)Math.pow(2,(int)eLength)-1;
		String result1;
		String result2;
		boolean o1Positive;
		boolean o2Positive;
		if(operand1.charAt(0)=='0'){
			o1Positive=true;
		} else {
			o1Positive=false;
		}

		if(operand2.charAt(0)=='0'){
			o2Positive=true;
		} else {
			o2Positive=false;
		}
		exponent1=operand1.substring(1,1+eLength);
		exponent2=operand2.substring(1,1+eLength);
		fragtion1=operand1.substring(1+eLength,1+eLength+sLength);
		fragtion2=operand2.substring(1+eLength,1+eLength+sLength);
		exponentValue1=Integer.valueOf(integerTrueValue("0"+exponent1));
		exponentValue2=Integer.valueOf(integerTrueValue("0"+exponent2));
		sign1=operand1.substring(0,1);
		sign2=operand2.substring(0,1);
		if(operand1.equals(positiveInfinite)) {
			if (operand2.equals(negativeInfinite)) {
				return NaN;
			}
			if (operand2.equals(positiveInfinite)) {
				return positiveInfinite;
			}
		}

		if(operand1.equals(negativeInfinite)) {
			if (operand2.equals(positiveInfinite)) {
				return NaN;
			}
			if (operand2.equals(negativeInfinite)) {
				return negativeInfinite;
			}
		}
		if(operand2.equals(positiveInfinite)) {
			if (operand1.equals(negativeInfinite)) {
				return NaN;
			}
			if (operand1.equals(positiveInfinite)) {
				return positiveInfinite;
			}
		}

		if(operand2.equals(negativeInfinite)) {
			if (operand1.equals(positiveInfinite)) {
				return NaN;
			}
			if (operand1.equals(negativeInfinite)) {
				return negativeInfinite;
			}
		}
		if(operand1.equals(postiveZero)||operand1.equals(negativeZero)){
			return "0"+operand2;
		} else if(operand2.equals(postiveZero)||operand2.equals(negativeZero)){
			return  "0"+operand1;
		} else{
			boolean firstShift=true;
			if(!exponent1.equals(exponent2)){
				if(exponentValue1>exponentValue2){
					while (exponentValue1!=exponentValue2){
						exponentValue2++;
						fragtion2=logRightShift(fragtion2,1);
						if(firstShift){
							fragtion2="1"+fragtion2.substring(1);
							firstShift=false;
						}
						if (fragtion2.equals(postiveZero.substring(1+eLength,1+eLength+sLength))){
							return "0"+operand1;
						}
					}
				} else {
					while (exponentValue1!=exponentValue2){
						/**/
						System.out.println("指数统一,右移并加指数，此时指数为"+exponentValue1);

						exponentValue1++;
						fragtion1=logRightShift(fragtion1,1);
						if(firstShift){
							fragtion1="1"+fragtion1.substring(1);
							firstShift=false;
						}

						if (fragtion1.equals(postiveZero.substring(1+eLength,1+eLength+sLength))){
							return "0"+operand2.substring(1);
						}
					}
				}

			}
			int length;
			if(sLength%4==0){
				length=sLength;

			}else {
				length=(sLength/4+1)*4;
			}
			String temp=signedAddition(sign1+fragtion1,sign2+fragtion2,length);
			resultFragtion=temp.substring(temp.length()-sLength,temp.length());
			sign=signedAddition(sign1+fragtion1,sign2+fragtion2,length).charAt(1);
			overFlow=signedAddition(sign1+fragtion1,sign2+fragtion2,length).charAt(0);
			if (overFlow == '1') {

				resultFragtion="1"+ariRightShift(resultFragtion,1).substring(1);
				exponentValue1++;
				if(exponentValue1>=exponentMaxValue){
					if (sign == '1') {
						return negativeInfinite;
					} else {
						return positiveInfinite;
					}
				}

			}

		/*	while (resultFragtion.charAt(0) == '0') {
				System.out.println("规格化,左移并减指数，此时指数为"+exponentValue1);
				resultFragtion=leftShift(resultFragtion,1);
				exponentValue1--;
				if (exponentValue1 < 0) {

				}
				if(exponentValue1==0){
					resultFragtion=ariRightShift(resultFragtion,1);
				}
			}*/









		}
		String resultExponent=integerReprsentation(String.valueOf("0"+exponentValue1),eLength);
		System.out.println(sign);
		System.out.println(resultExponent);
		System.out.println(resultFragtion);
		String result=String.valueOf(sign)+resultExponent+resultFragtion;


		return result;
	}
	
	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		char sign;
		if (operand2.charAt(0) == '0') {
			sign='1';
		}else {
			sign='0';
		}
		String  realOperand2=String.valueOf(sign)+operand2.substring(1);
		return floatAddition(operand1,realOperand2,eLength,sLength,gLength);
	}
	
	/**
	 * 浮点数乘法，可调用{@link #integerAddition(String, String, char, int) integerAddition}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		double maxValue=(2-Math.pow(2,-sLength))*Math.pow(2,Math.pow(2,eLength-1)-1);
		double minRegValue=Math.pow(2,(-Math.pow(2,eLength-1)+2));
		double minValue=minRegValue*Math.pow(2,-sLength);
		StringBuilder exponentStringBuilder=new StringBuilder();
		StringBuilder fragtionStringBuilder=new StringBuilder();

		/*构造0*/
		for (int i = 0; i < eLength; i++) {
			exponentStringBuilder.append("0");
		}
		for (int i = 0; i < sLength; i++) {
			fragtionStringBuilder.append("0");
		}
		String postiveZero="0"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		String negativeZero="1"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		exponentStringBuilder.delete(0,exponentStringBuilder.length());
		fragtionStringBuilder.delete(0,fragtionStringBuilder.length());


		/*构造无穷大*/
		for (int i = 0; i < eLength; i++) {
			exponentStringBuilder.append("1");
		}
		for (int i = 0; i < sLength; i++) {
			fragtionStringBuilder.append("0");
		}
		String positiveInfinite="0"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		String negativeInfinite="1"+exponentStringBuilder.toString()+fragtionStringBuilder.toString();
		String NaN="0"+exponentStringBuilder.toString()+"0"+fragtionStringBuilder.substring(1);

		exponentStringBuilder.delete(0,exponentStringBuilder.length());
		fragtionStringBuilder.delete(0,fragtionStringBuilder.length());

		char sign;
		String exponent1;
		String exponent2;
		String sign1;
		String sign2;
		int exponentValue1;
		int exponentValue2;
		String fragtion1;
		String fragtion2;
		String protect1;
		String protect2;
		StringBuilder protect1StringBuilder=new StringBuilder();
		StringBuilder protect2StringBuilder=new StringBuilder();
		String resultFragtion;
		char overFlow;
		int exponentMaxValue=(int)Math.pow(2,(int)eLength)-1;
		String result1;
		String result2;
		boolean o1Positive;
		boolean o2Positive;
		if(operand1.charAt(0)=='0'){
			o1Positive=true;
		} else {
			o1Positive=false;
		}

		if(operand2.charAt(0)=='0'){
			o2Positive=true;
		} else {
			o2Positive=false;
		}
		exponent1=operand1.substring(1,1+eLength);
		exponent2=operand2.substring(1,1+eLength);
		fragtion1=operand1.substring(1+eLength,1+eLength+sLength);
		fragtion2=operand2.substring(1+eLength,1+eLength+sLength);
		exponentValue1=Integer.valueOf(integerTrueValue("0"+exponent1));
		exponentValue2=Integer.valueOf(integerTrueValue("0"+exponent2));
		sign1=operand1.substring(0,1);
		sign2=operand2.substring(0,1);


		if (operand1.equals(positiveInfinite)) {
			if (operand2.equals(negativeInfinite)) {
				return negativeInfinite;
			}
			if (operand2.equals(positiveInfinite)) {
				return positiveInfinite;
			}
			if (operand2.equals(postiveZero)) {
				return NaN;

			}
		}

		if (operand2.equals(positiveInfinite)) {
			if (operand1.equals(negativeInfinite)) {
				return negativeInfinite;
			}
			if (operand1.equals(positiveInfinite)) {
				return positiveInfinite;
			}
			if (operand1.equals(postiveZero)) {
				return NaN;

			}
		}
		if(operand1.equals(postiveZero)||operand1.equals(negativeZero)){
			return postiveZero;
		} else if(operand2.equals(postiveZero)||operand2.equals(negativeZero)){
			return postiveZero;
		}
		int exponentSum=exponentValue1+exponentValue2-(int)(Math.pow(2,eLength-1)-1);
		if (exponentSum >= exponentMaxValue) {
			if ((o1Positive&&o2Positive)||((!o1Positive)&&(!o2Positive))) {
				return positiveInfinite;
			} else {
				return negativeInfinite;
			}
		}
		if(exponentSum<=0){
			return postiveZero;
		}
		String mutiOperand1;
		String mutiOperand2;
		mutiOperand1="01"+fragtion1;
		mutiOperand2="01"+fragtion2;
		int operandLength=(int)Math.ceil((mutiOperand1.length())/4)*4;
		String mutiplyResult=integerMultiplication(mutiOperand1,mutiOperand2,2*operandLength);
		int firstIndexOfOne=mutiplyResult.indexOf('1');
		int resultLength=mutiplyResult.substring(firstIndexOfOne).length();
		if (resultLength == 2 * sLength + 2) {
			exponentSum++;
		}
		resultFragtion=mutiplyResult.substring(firstIndexOfOne+1,firstIndexOfOne+1+sLength);
		String result=mutiplyResult.substring(firstIndexOfOne+1,firstIndexOfOne+1+sLength);
		String exponentResult=integerReprsentation(String.valueOf(exponentSum),eLength);
		if (o1Positive && o2Positive) {
			sign='0';
		} else if ((!o1Positive) && (!o2Positive)) {
			sign='0';
		} else {
			sign= '1';
		}

		return String.valueOf(sign)+exponentResult+resultFragtion;
	}
	
	/**
	 * 浮点数除法，可调用{@link #integerAddition(String, String, char, int) integerAddition}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}







	public static String hex2Bin(String in) {
		if (in.equals("0"))
			return "0";
		BigInteger number=new BigInteger(in);

		StringBuilder sb=new StringBuilder();
		while(!number.equals(new BigInteger("1"))){
			sb.insert(0, number.mod(new BigInteger("2")));
			number=number.divide(new BigInteger("2"));


		}
		sb.insert(0, 1);
		String reString=sb.toString();
		return reString;
	}


	public static char xor(char a,char b){
		if(a==b){
			return '0';
		} else {
			return '1';

		}
	}
	/*按位取反*/
	public static String invert(String  s){
		StringBuilder stringBuilder=new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i)=='0'){
				stringBuilder.append('1');
			} else  if(s.charAt(i)=='1'){
				stringBuilder.append('0');
			} else  {
				throw  new IllegalStateException();
			}
		}

		String  result=stringBuilder.toString();
		return result;
	}


	/*取反加一*/
	public static String toOpposite(String  s){
		String temp=invert(s);
		StringBuilder stringBuilder=new StringBuilder();
		String result;;
		stringBuilder.append(temp);
		for (int i =stringBuilder.length()-1; i>=0; i--) {
			if(stringBuilder.charAt(i)=='0'){
				stringBuilder.replace(i,i+1,"1");
				break;
			} else {

				stringBuilder.replace(i,i+1,"0");
			}
		}
		result=stringBuilder.toString();
		return  result;
	}

}
