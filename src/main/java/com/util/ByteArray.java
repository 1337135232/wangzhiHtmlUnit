package com.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ByteArray {

	private byte[] bb;
	
	public ByteArray(byte[] b) {
		bb=b;
	}
	
	public ByteArray() {
		bb=new byte[0];
	}
	
	public void append(byte byteArray[]) {
		byte[] btemp=new byte[bb.length+byteArray.length];
		System.arraycopy(bb, 0, btemp, 0, bb.length);
		System.arraycopy(byteArray, 0, btemp, bb.length, byteArray.length);
		bb=btemp;
	}
	
	public List<byte[]> splitArray(int size,byte[] b) {
		bb=b;
		List<byte[]> lsit=new ArrayList<byte[]>();
		int quotient=bb.length/size;
		int	remainder=bb.length%size;
		for(int i=0;i<quotient;i++)
		{
			byte[] btemp=new byte[size];
			System.arraycopy(bb,i*size, btemp, 0,size);
			lsit.add(btemp);
		}
		if(remainder>0)
		{
			byte[] btemp=new byte[size];
			System.arraycopy(bb,bb.length-remainder, btemp, 0,remainder);
			lsit.add(btemp);
		}
		return lsit;
	}
	
	public List<byte[]> splitArray(int size) {
		List<byte[]> lsit=new ArrayList<byte[]>();
		int quotient=bb.length/size;
		int	remainder=bb.length%size;
		for(int i=0;i<quotient;i++)
		{
			byte[] btemp=new byte[size];
			System.arraycopy(bb,i*size, btemp, 0,size);
			lsit.add(btemp);
		}
		if(remainder>0)
		{
			byte[] btemp=new byte[remainder];
			System.arraycopy(bb,bb.length-remainder, btemp, 0,remainder);
			lsit.add(btemp);
		}
		return lsit;
	}
	
	@Deprecated
	public void clearNull() {
		int sign=0;
		for(int i=(bb.length-1);i>-1;i--)
		{
			if(bb[i]!=0)
				break;
			sign++;
		}
		byte[] btemp=new byte[bb.length-sign];
		System.arraycopy(bb, 0, btemp, 0, bb.length-sign);
		bb=btemp;
	}
	
	
	public void clearNull(int sum) {
		int sign=sum;
		byte[] btemp=new byte[bb.length-sign];
		System.arraycopy(bb, 0, btemp, 0, bb.length-sign);
		bb=btemp;
	}
	
	
	@Override
	public String toString() {
		return new String(bb);
	}
	
	public String toString(String charset) throws UnsupportedEncodingException {
		if(charset.equalsIgnoreCase("gb2312"))
			charset="gbk";
		return new String(bb,charset);
	}
	
	public static void main(String[] args) {
		/*ByteArray ba=new ByteArray();
		ba.append("12345678901234567890123456789012345678901234567890123".getBytes());
		List<byte[]> bb=ba.splitArray(10);
		for(int i=0;i<bb.size();i++)
		System.out.println(new String(bb.get(i)));*/
		/*byte[] ab=new byte[10];
		ab[0]=2;
		ByteArray ba=new ByteArray(ab);
		ba.clearNull();
		System.out.println(ba.toString());*/
	}
}
