
package com.inspect.util.excel;

import java.io.File;

import java.util.Arrays;

public class ExcelToImage
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{	String  d1="D:"+File.separator+"upload"+File.separator+"inspect1"+File.separator+"室内详情.xls";
	String  d2="D:"+File.separator+"upload"+File.separator+"inspect2"+File.separator+"室内详情.pdf";
		try
		{
			boolean result = Excel2Pdf.excel2Pdf( new File( d1 ),
					new File( d2 ) );
			if ( result )
			{
				PdfToImage test = new PdfToImage( "jpg", d2, 150 );
				String[] images = test.getImageFiles( );
				//	System.out.println( Arrays.toString( images ) );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
