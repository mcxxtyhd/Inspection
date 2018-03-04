
package com.inspect.util.excel;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Excel2Pdf
{

	public static boolean excel2Pdf( final File excelFile, final File pdfFile )
	{
		synchronized ( ExcelToPdf.class )
		{
			if ( !excelFile.exists( ) )
				return false;
			if ( pdfFile.exists( ) )
				pdfFile.delete( );
			if ( pdfFile.exists( ) )
				return false;

			final boolean[] results = new boolean[]{
				false
			};

			Display.getDefault( ).syncExec( new Runnable( ) {

				public void run( )
				{
					Shell shell = new Shell( );
					OleFrame frame = new OleFrame( shell, SWT.NONE );
					Variant result = null;
					try
					{
						OleControlSite ocs = new OleControlSite( frame,
								SWT.NONE,
								"Excel.Application" );
						OleAutomation application = new OleAutomation( ocs );

						application.setProperty( getOleMethodId( application,
								"Visible" ),
								new Variant( false ) );

						OleAutomation excels = application.getProperty( getOleMethodId( application,
								"Workbooks" ) )
								.getAutomation( );

						OleAutomation excel = excels.invoke( getOleMethodId( excels,
								"Open" ),
								new Variant[]{
										new Variant( excelFile.getAbsolutePath( ) ),
										new Variant( false ),
										new Variant( false )
								} )
								.getAutomation( );

						//	System.out.println("before saveas");
						result = excel.invoke( getOleMethodId( excel, "SaveAs" ),
								new Variant[]{
										new Variant( pdfFile.getAbsolutePath( ) ),
										new Variant( 57 ),
										new Variant( false ),
										new Variant( 57 ),
										new Variant( 57 ),
										new Variant( false ),
										new Variant( true ),
										new Variant( 57 ),
										new Variant( false ),
										new Variant( true ),
										new Variant( false )
								} );
						//System.out.println("after saveas");

						excel.invoke( getOleMethodId( excel, "Close" ),
								new Variant[]{
									new Variant( false )
								} );

						application.invoke( getOleMethodId( application, "Quit" ) );

						excel.dispose( );
						application.dispose( );
						ocs.dispose( );
					}
					catch ( Exception e )
					{
						e.printStackTrace( );
					}

					if ( result == null )
						results[0] = false;
					else
						results[0] = result.getBoolean( );

					frame.dispose( );
					shell.dispose( );

				}
			} );
			
			Display.getDefault( ).dispose( );

			return results[0];
		}
	}

	private static int getOleMethodId( OleAutomation excel, String method )
	{
		return excel.getIDsOfNames( new String[]{
			method
		} )[0];
	}
}
