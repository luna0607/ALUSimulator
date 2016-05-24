import java.math.BigInteger;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author [请将此处修改为“学号_姓名”]
 *
 */

public class ALU {


	public static void main(String[] args) {
		ALU alu=new ALU();
		System.out.println(alu.floatRepresentation("123.321",8,23));
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

		if(num1>maxValue){
			if (positive){
				return "+Inf";
			} else {
				return  "-Inf";
			}
		}

		if(num1<minValue){
			return "0";
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
		System.out.println("整数部分"+integralPart);
		System.out.println("小数部分"+decimalPart);
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




		return null;
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
			a[4-i-1]=o1[i]-'0';
			b[4-i-1]=o2[i]-'0';
		}

		return null;
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
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数加法，要求调用{@link #claAdder(String, String, char) claAdder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数减法，可调用{@link #integerAddition(String, String, char, int) integerAddition}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #integerAddition(String, String, char, int) integerAddition}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.

		return null;
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
		// TODO YOUR CODE HERE.
		return null;
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
		// TODO YOUR CODE HERE.
		return null;
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
		// TODO YOUR CODE HERE.
		return null;
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
		// TODO YOUR CODE HERE.
		return null;
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
		// TODO YOUR CODE HERE.
		return null;
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
