package com.sptdi.formsbyid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 从客户端 获得下载文件名称
		String fileName = request.getParameter("fileName");
		// 处理乱码
		fileName = new String(fileName.getBytes("ISO-8859-1"), "utf-8");

		// 设置MIME 类型
		response.setContentType(getServletContext().getMimeType(fileName));

		// 设置以附件形式打开
		response.setHeader("Content-Disposition", "attachment;filename="
				+ MimeUtility.encodeText(fileName, "utf-8", "B"));

		// 获得文件完整路径
		// fileName = getServletContext().getRealPath("/" + fileName);
		// 下载WEB-INF/upload下文件
		fileName = getServletContext().getRealPath(
				"/WEB-INF/upload" + "/" + fileName);

		// 读取文件
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				fileName));

		// 将输入流拷贝到response的输出流中
		BufferedOutputStream out = new BufferedOutputStream(response
				.getOutputStream());

		int temp;
		while ((temp = in.read()) != -1) {
			out.write(temp);
		}
		in.close();
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
