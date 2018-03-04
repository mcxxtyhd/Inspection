
package com.inspect.util.excel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.jpedal.PdfDecoder;
import org.jpedal.color.ColorSpaces;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.constants.PageInfo;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.w3c.dom.Element;

import com.itextpdf.text.pdf.PdfReader;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

public class PdfToImage
{

	private static boolean debug = true;

	/** correct separator for OS */
	final static String separator = System.getProperty( "file.separator" );

	// only used if between 0 and 1
	private float JPEGcompression = -1f;

	private List<String> convertImageFiles = new ArrayList<String>( );

	/**
	 * main constructor to convert PDF to img
	 * 
	 * @param fileType
	 * @param pdfFile
	 * @throws Exception
	 */
	public PdfToImage( String fileType, String pdfFile, int dpi )
			throws Exception
	{
		convertImageFiles.clear( );

		String outputPath = pdfFile.substring( 0, pdfFile.toLowerCase( )
				.indexOf( ".pdf" ) )
				+ separator;
		File outputPathFile = new File( outputPath );
		if ( !outputPathFile.exists( ) || !outputPathFile.isDirectory( ) )
		{
			if ( !outputPathFile.mkdirs( ) )
			{
				if ( debug )
					System.err.println( "Can't create directory " + outputPath );
			}
		}

		// PdfDecoder object provides the conversion
		final PdfDecoder decoder = new PdfDecoder( true );

		// mappings for non-embedded fonts to use
		// FontMappings.setFontReplacements();

		decoder.openPdfFile( pdfFile );

		Map mapValues = new HashMap( );

		/** USEFUL OPTIONS */
		// do not scale above this figure
		mapValues.put( JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING, 2 );

		PdfReader reader = new PdfReader( pdfFile );

		com.itextpdf.text.Rectangle rect = reader.getPageSize( 1 );

		reader.close( );

		mapValues.put( JPedalSettings.EXTRACT_AT_PAGE_SIZE,
				new String[]{
						String.valueOf( rect.getWidth( ) * dpi / 72 ),
						String.valueOf( rect.getHeight( ) * dpi / 72 )
				} );

		// which takes priority (default is false)
		mapValues.put( JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE );

		PdfDecoder.modifyJPedalParameters( mapValues );

		if ( debug )
			System.out.println( "pdf : " + pdfFile );

		try
		{
			/**
			 * allow output to multiple images with different values on each
			 * 
			 * Note we REMOVE shapes as it is a new feature and we do not want
			 * to break existing functions
			 */
			String separation = System.getProperty( "org.jpedal.separation" );
			if ( separation != null )
			{

				Object[] sepValues = new Object[]{
						7, "", Boolean.FALSE
				}; // default of normal
				if ( separation.equals( "all" ) )
				{
					sepValues = new Object[]{
							PdfDecoder.RENDERIMAGES,
							"image_and_shapes",
							Boolean.FALSE,
							PdfDecoder.RENDERIMAGES
									+ PdfDecoder.REMOVE_RENDERSHAPES,
							"image_without_shapes",
							Boolean.FALSE,
							PdfDecoder.RENDERTEXT,
							"text_and_shapes",
							Boolean.TRUE,
							7,
							"all",
							Boolean.FALSE,
							PdfDecoder.RENDERTEXT
									+ PdfDecoder.REMOVE_RENDERSHAPES,
							"text_without_shapes",
							Boolean.TRUE
					};
				}

				int sepCount = sepValues.length;
				for ( int seps = 0; seps < sepCount; seps = seps + 3 )
				{

					decoder.setRenderMode( (Integer) sepValues[seps] );
					extractPageAsImage( fileType,
							outputPath,
							decoder,
							"_" + sepValues[seps + 1],
							(Boolean) sepValues[seps + 2] ); // boolean makes
																// last
																// transparent
																// so we can see
																// white text
				}

			}
			else
				// just get the page
				extractPageAsImage( fileType, outputPath, decoder, "", false );

		}
		finally
		{

			decoder.closePdfFile( );
		}

	}

	/**
	 * actual conversion of a PDF page into an image
	 */
	private void extractPageAsImage( String fileType, String outputPath,
			PdfDecoder decoder, String prefix, boolean isTransparent )
			throws PdfException, IOException
	{

		// page range
		int start = 1, end = decoder.getPageCount( );

		// container if the user is creating a multi-image tiff
		BufferedImage[] multiPages = new BufferedImage[1 + ( end - start )];

		/**
		 * set of JVM flags which allow user control on process
		 */

		// ////////////////TIFF OPTIONS/////////////////////////////////////////

		String multiPageFlag = System.getProperty( "org.jpedal.multipage_tiff" );
		boolean isSingleOutputFile = multiPageFlag != null
				&& multiPageFlag.toLowerCase( ).equals( "true" );

		String tiffFlag = System.getProperty( "org.jpedal.compress_tiff" );
		boolean compressTiffs = tiffFlag != null
				&& tiffFlag.toLowerCase( ).equals( "true" );

		// ////////////////JPEG OPTIONS/////////////////////////////////////////

		// allow user to specify value
		String rawJPEGComp = System.getProperty( "org.jpedal.compression_jpeg" );
		if ( rawJPEGComp != null )
		{
			try
			{
				JPEGcompression = Float.parseFloat( rawJPEGComp );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			if ( JPEGcompression < 0 || JPEGcompression > 1 )
				throw new RuntimeException( "Invalid value for JPEG compression - must be between 0 and 1" );

		}

		String jpgFlag = System.getProperty( "org.jpedal.jpeg_dpi" );

		// /////////////////////////////////////////////////////////////////////

		for ( int pageNo = start; pageNo < end + 1; pageNo++ )
		{

			if ( debug )
				System.out.println( "page : " + pageNo );

			BufferedImage imageToSave = decoder.getPageAsHiRes( pageNo,
					null,
					isTransparent );

			String imageFormat = System.getProperty( "org.jpedal.imageType" );
			if ( imageFormat != null )
			{
				if ( isNumber( imageFormat ) )
				{
					int iFormat = Integer.parseInt( imageFormat );
					if ( iFormat > -1 && iFormat < 14 )
					{
						BufferedImage tempImage = new BufferedImage( imageToSave.getWidth( ),
								imageToSave.getHeight( ),
								iFormat );
						Graphics2D g = tempImage.createGraphics( );
						g.drawImage( imageToSave, null, null );

						imageToSave = tempImage;
					}
					else
					{
						System.err.println( "Image Type is not valid. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables." );
					}
				}
				else
				{
					System.err.println( "Image Type provided is not an Integer. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables." );
				}
			}

			decoder.flushObjectValues( true );

			// image needs to be sRGB for JPEG
			if ( fileType.equals( "jpg" ) )
				imageToSave = ColorSpaceConvertor.convertToRGB( imageToSave );

			String outputFileName;
			if ( isSingleOutputFile )
				outputFileName = outputPath
						+ "allPages"
						+ prefix
						+ '.'
						+ fileType;
			else
			{
				/**
				 * create a name with zeros for if more than 9 pages appears in
				 * correct order
				 */
				String pageAsString = String.valueOf( pageNo );
				String maxPageSize = String.valueOf( end );
				int padding = maxPageSize.length( ) - pageAsString.length( );
				for ( int ii = 0; ii < padding; ii++ )
					pageAsString = '0' + pageAsString;

				outputFileName = outputPath
						+ "page"
						+ pageAsString
						+ prefix
						+ '.'
						+ fileType;
			}

			// if just gray we can reduce memory usage by converting image to
			// Grayscale

			/**
			 * see what Colorspaces used and reduce image if appropriate (only
			 * does Gray at present)
			 * 
			 * Can return null value if not sure
			 */
			Iterator colorspacesUsed = decoder.getPageInfo( PageInfo.COLORSPACES );

			int nextID;
			boolean isGrayOnly = colorspacesUsed != null; // assume true and
															// disprove

			while ( colorspacesUsed != null && colorspacesUsed.hasNext( ) )
			{
				nextID = (Integer) ( colorspacesUsed.next( ) );

				if ( nextID != ColorSpaces.DeviceGray
						&& nextID != ColorSpaces.CalGray )
					isGrayOnly = false;
			}

			// draw onto GRAY image to reduce colour depth
			// (converts ARGB to gray)
			if ( isGrayOnly )
			{
				BufferedImage image_to_save2 = new BufferedImage( imageToSave.getWidth( ),
						imageToSave.getHeight( ),
						BufferedImage.TYPE_BYTE_GRAY );
				image_to_save2.getGraphics( ).drawImage( imageToSave,
						0,
						0,
						null );
				imageToSave = image_to_save2;
			}

			// put image in array if multi-images (we save on last page in code
			// below)
			if ( isSingleOutputFile )
				multiPages[pageNo - start] = imageToSave;

			// we save the image out here
			if ( imageToSave != null )
			{

				/**
				 * BufferedImage does not support any dpi concept. A higher dpi
				 * can be created using JAI to convert to a higher dpi image
				 */

				// shrink the page to 50% with graphics2D transformation
				// - add your own parameters as needed
				// you may want to replace null with a hints object if you
				// want to fine tune quality.

				if ( JAIHelper.isJAIused( ) )
					JAIHelper.confirmJAIOnClasspath( );

				if ( JAIHelper.isJAIused( ) && fileType.startsWith( "tif" ) )
				{

					com.sun.media.jai.codec.TIFFEncodeParam params = new com.sun.media.jai.codec.TIFFEncodeParam( );

					if ( compressTiffs )
						params.setCompression( com.sun.media.jai.codec.TIFFEncodeParam.COMPRESSION_PACKBITS );

					if ( !isSingleOutputFile )
					{
						BufferedOutputStream os = new BufferedOutputStream( new FileOutputStream( outputFileName ) );

						javax.media.jai.JAI.create( "encode",
								imageToSave,
								os,
								"TIFF",
								params );
					}
					else if ( isSingleOutputFile && pageNo == end )
					{
						OutputStream out = new BufferedOutputStream( new FileOutputStream( outputFileName ) );
						com.sun.media.jai.codec.ImageEncoder encoder = com.sun.media.jai.codec.ImageCodec.createImageEncoder( "TIFF",
								out,
								params );
						List vector = new ArrayList( );
						vector.addAll( Arrays.asList( multiPages ).subList( 1,
								multiPages.length ) );

						params.setExtraImages( vector.iterator( ) );

						encoder.encode( multiPages[0] );
						out.close( );
					}
				}
				else if ( isSingleOutputFile )
				{
					// non-JAI
				}
				else if ( ( jpgFlag != null || rawJPEGComp != null )
						&& fileType.startsWith( "jp" )
						&& JAIHelper.isJAIused( ) )
				{

					saveAsJPEG( jpgFlag,
							imageToSave,
							JPEGcompression,
							new BufferedOutputStream( new FileOutputStream( outputFileName ) ) );
				}
				else
				{

					BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( new File( outputFileName ) ) );
					ImageIO.write( imageToSave, fileType, bos );
					bos.flush( );
					bos.close( );
				}

			}

			imageToSave.flush( );

			if ( debug )
			{
				System.out.println( "Created : " + outputFileName );

			}

			File file = new File( outputFileName );
			if ( file.exists( ) && file.length( ) > 0 )
			{
				convertImageFiles.add( outputFileName );
			}
		}
	}

	/** test to see if string or number */
	private static boolean isNumber( String value )
	{

		boolean isNumber = true;

		int charCount = value.length( );
		for ( int i = 0; i < charCount; i++ )
		{
			char c = value.charAt( i );
			if ( ( c < '0' ) | ( c > '9' ) )
			{
				isNumber = false;
				i = charCount;
			}
		}

		return isNumber;
	}

	private static void saveAsJPEG( String jpgFlag,
			BufferedImage image_to_save, float JPEGcompression,
			BufferedOutputStream fos ) throws IOException
	{
		JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix( "jpeg" )
				.next( );
		ImageOutputStream ios = ImageIO.createImageOutputStream( fos );
		imageWriter.setOutput( ios );

		IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata( new ImageTypeSpecifier( image_to_save ),
				null );

		if ( jpgFlag != null )
		{

			int dpi = 96;

			try
			{
				dpi = Integer.parseInt( jpgFlag );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}

			Element tree = (Element) imageMetaData.getAsTree( "javax_imageio_jpeg_image_1.0" );
			Element jfif = (Element) tree.getElementsByTagName( "app0JFIF" )
					.item( 0 );
			jfif.setAttribute( "Xdensity", Integer.toString( dpi ) );
			jfif.setAttribute( "Ydensity", Integer.toString( dpi ) );

		}

		if ( JPEGcompression >= 0 && JPEGcompression <= 1f )
		{

			JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam( );
			jpegParams.setCompressionMode( JPEGImageWriteParam.MODE_EXPLICIT );
			jpegParams.setCompressionQuality( JPEGcompression );
		}

		imageWriter.write( imageMetaData, new IIOImage( image_to_save,
				null,
				null ), null );
		ios.close( );
		imageWriter.dispose( );

	}

	public String[] getImageFiles( )
	{
		return convertImageFiles.toArray( new String[0] );
	}
	
	public static void main(String[] args) {

		 
	       String path="D:"+File.separator+"upload"+File.separator+"inspect2"+File.separator+"230_0.xls";	
	          try{
	             File file=new File(path);
	             
	             if(file.exists()){
	            	 System.out.println("ExcelToPdf et=new ExcelToPdf(path)");
	                ExcelToPdf et=new ExcelToPdf(path);
	            	 System.out.println("ExcelToPdf et=new ExcelToPdf(path)");

	                  et.listAllFile("230_0");
	                  
	             }else{
	            	 
	             System.out.println("Path Not Exist,Pls Comfirm: "+path);
	             
	             }
	       }catch(Exception ex){
	    	   
	             System.out.println("Pls Check Your Format,Format Must Be: java com/olive/util/RunTask Path(Exist Path) Frequency(Run Frequency,int)");
	            
	             ex.printStackTrace();
	       }
	 
	}
}